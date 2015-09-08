package pt.inescid.l2f.xipapi;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import pt.inescid.l2f.xipapi.domain.*;
import pt.inescid.l2f.xipapi.exception.NodeDoesNotExistException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

//import java.io.FileInputStream;
//import java.util.Scanner;

public class XipDocumentFactory {

//##############################################################################
//   ###########################################################################
//      ATTRIBUTES
//   ###########################################################################
//##############################################################################
   
   /** ??????????????????????? */
	private static XipDocumentFactory INSTANCE;

   /** ??????????????????????? */
	private static String xipPath;

	//private static String FILENAME = "xipAddress.txt";


//##############################################################################
//   ###########################################################################
//      CONSTUCTORS
//   ###########################################################################
//##############################################################################
   
  /**
   * Constructor.
   * 
   */
	private XipDocumentFactory() { }


//##############################################################################
//   ###########################################################################
//      GETTERS AND SETTERS
//   ###########################################################################
//##############################################################################
   
	/**
	 * 
	 * @return Instance of XipDocumentFactory
	 */
	public static XipDocumentFactory getInstance() {
		if (INSTANCE == null) {
				INSTANCE = new XipDocumentFactory();
		}
		return INSTANCE;
	}


	public String getXipPath() {
		return XipDocumentFactory.xipPath;
	}
	
	/**
	 * @param input
	 *            string to be evaluated by XIP
	 * @return {@link BufferedReader} with XIP's result
	 * @throws Exception
	 */
	public BufferedReader getXipBufferFromString(String input) throws Exception {

		final String[] xip_cmd = { "/bin/bash", "-c",
				"echo " + input + " | " + xipPath + " -inutf8 -oututf8 -xml -f" };

		BufferedReader result = null;

		Runtime rt = Runtime.getRuntime();
		Process pr = rt.exec(xip_cmd);
		result = new BufferedReader(new InputStreamReader(pr.getInputStream(),
				"UTF-8"));

		int exitVal = pr.waitFor();
		if (exitVal != 0)
			throw new Exception();

		return result;
	}

	/**
	 * @param filename
	 *            to be evaluated by XIP
	 * @return {@link BufferedReader} with XIP's result
	 * @throws Exception
	 */
	public BufferedReader getXipBufferFromFile(String filename)
			throws Exception {

		final String[] xip_cmd = {
				"/bin/bash",
				"-c",
				"cat " + filename + " | " + xipPath
						+ " -inutf8 -oututf8 -xml -f" };

		BufferedReader result = null;

		Runtime rt = Runtime.getRuntime();
		Process pr = rt.exec(xip_cmd);
		result = new BufferedReader(new InputStreamReader(pr.getInputStream(),
				"UTF-8"));

		int exitVal = pr.waitFor();
		if (exitVal != 0)
			throw new Exception();

		return result;
	}

	/**
	 * 
	 * @param buffer
	 * @return XipDocument containing the xml file structure
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public XipDocument getXipResult(BufferedReader buffer)
			throws ParserConfigurationException, SAXException, IOException {

		Document doc = getDocument(buffer);
		XipDocument xipdoc = new XipDocument("XipDocument", doc);

		buildXipDocument(doc, xipdoc);
		extractDependencies(doc, xipdoc);

		return xipdoc;
	}

	private static Document getDocument(BufferedReader buffer)
			throws ParserConfigurationException, SAXException, IOException {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		DocumentBuilder db = dbf.newDocumentBuilder();

		InputSource is = new InputSource(buffer);
		return db.parse(is);
	}

	private void buildXipDocument(Document doc, XipDocument document) {
		NodeList lunits = doc.getElementsByTagName("LUNIT");
		int sentenceNumber = 0;
		int nodeNum = 0;
		for (int i = 0; i < lunits.getLength(); i++) {
			nodeNum++;
			Node lunit = lunits.item(i);

			Element topElement = (Element) lunit.getFirstChild();

			String nodeName = topElement.getAttribute("tag");
			String nodeNumber = topElement.getAttribute("num");
			String start = topElement.getAttribute("start");
			String end = topElement.getAttribute("end");

			XIPNode top = new XIPNode(document, nodeName, start, end, nodeNumber, sentenceNumber, nodeNum);

			document.addSentence(top);

			NodeList sentenceNodes = topElement.getChildNodes();

			for (int index = 0; index < sentenceNodes.getLength(); index++) {
				nodeNum++;
				Element child = (Element) sentenceNodes.item(index);
				if (child.getNodeName().equals("FEATURE")) {// add a feature
					top.addFeature(new Feature(child.getAttribute("attribute"), child.getAttribute("value")));
				} else
					nodeNum = nodeCrawler(top, child, sentenceNumber, nodeNum); 
				// crawls
				// into xml
				// structure
				// and builds XipDocument
			}
			sentenceNumber++;
		}

	}


	private int nodeCrawler(XIPNode parent, Element element, int sentenceNumber, int nodeNum) {

		nodeNum++;

		String nodeName = element.getAttribute("tag");
		String nodeNumber = element.getAttribute("num");
		String start = element.getAttribute("start");
		String end = element.getAttribute("end");

		XIPNode child = new XIPNode(parent, nodeName, start, end, nodeNumber, sentenceNumber, nodeNum);

		parent.addNode(child);
		NodeList children = element.getChildNodes();
		
		for (int index = 0; index < children.getLength(); index++) {
			nodeNum++;

			Element childElement = (Element) children.item(index);

			if (childElement.getNodeName().equals("FEATURE"))
				child.addFeature(new Feature(childElement.getAttribute("attribute"), childElement.getAttribute("value")));
			else if (childElement.getNodeName().equals("TOKEN")) {
				String name = childElement.getNodeName();
				String pos = childElement.getAttribute("pos");
				String startToken = childElement.getAttribute("start");
				String endToken = childElement.getAttribute("end");
				String word = childElement.getTextContent();
				Token token = new Token(child, name, pos, startToken, endToken, word, sentenceNumber, nodeNum);

				NodeList readings = childElement.getElementsByTagName("READING");
				for (int j = 0; j < readings.getLength(); j++) {
					Element reading = (Element) readings.item(j);
					String lemma = reading.getAttribute("lemma");
					token.addLemma(lemma);
				}
				child.addNode(token);
			} else if (childElement.getNodeName().equals("NODE")) {
				nodeNum = nodeCrawler(child, childElement, sentenceNumber, nodeNum);
			}
		}

		return nodeNum;
	}

	private void extractDependencies(Document document, XipDocument xipDocument) {

		NodeList lunits = document.getElementsByTagName("LUNIT");
		int sentenceNumber = 0;
		for (int i = 0; i < lunits.getLength(); i++) {
			Element lunit = (Element) lunits.item(i);
			NodeList dependencies = lunit.getElementsByTagName("DEPENDENCY");

			for (int j = 0; j < dependencies.getLength(); j++) {
				Element dep = (Element) dependencies.item(j);
				Dependency dependency = new Dependency(dep.getAttribute("name"),sentenceNumber);

				for (int K = 0; K < dep.getChildNodes().getLength(); K++) {

					Element child = (Element) dep.getChildNodes().item(K);

					if (child.getNodeName().equals("PARAMETER")) {
						String num = child.getAttribute("num");
						// String word = child.getAttribute("word");
						try {
							XIPNode node = xipDocument.getNode(sentenceNumber, num);
							
							dependency.addNode(node);
						} catch (NodeDoesNotExistException e) {
							//System.out.println(e.toString() + ": " + num);
														
							//if the node is the "top" one, it's necessary to get the sentence instead 
							//José Correia 06/2015
							XIPNode top =(XIPNode) xipDocument.getSentences().toArray()[sentenceNumber];
							if(top.getNodeNumber().equals(num)){
								dependency.addNode(top);
							}
							
						}
					} else if (child.getNodeName().equals("FEATURE")) {
						String name = child.getAttribute("attribute");
						String value = child.getAttribute("value");
						Feature feature = new Feature(name, value);
						dependency.addFeatures(feature);
					}
				}
				xipDocument.addDependency(dependency);
			}
			sentenceNumber++;
		}
	}
}
