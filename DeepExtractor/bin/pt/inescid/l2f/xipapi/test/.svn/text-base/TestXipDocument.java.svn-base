package pt.inescid.l2f.xipapi.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import junit.framework.TestCase;

import org.junit.Before;
import org.xml.sax.SAXException;

import pt.inescid.l2f.xipapi.XipDocumentFactory;
import pt.inescid.l2f.xipapi.domain.Dependency;
import pt.inescid.l2f.xipapi.domain.Token;
import pt.inescid.l2f.xipapi.domain.XIPNode;
import pt.inescid.l2f.xipapi.domain.XipDocument;
import pt.inescid.l2f.xipapi.exception.NodeHasNoNextSiblingException;
import pt.inescid.l2f.xipapi.exception.NodeHasNoPreviousSiblingException;
import pt.inescid.l2f.xipapi.exception.NodeHasNoSiblingsException;

public class TestXipDocument extends TestCase {

	private final String filepath = "PATH";//"C:\\Users\\nuno\\Desktop\\desktop\\txt\\avalia��o\\all\\xip\\teste40XIP.xml";

	private XipDocumentFactory documentFactory = XipDocumentFactory
			.getInstance();
	private static XipDocument document;

	
	public void testInstance () {
		System.out.println("new instance");
		documentFactory = XipDocumentFactory.getInstance();

		System.out.println(documentFactory.toString());
		System.out.println(documentFactory.toString());
	}
	
	@Before
	public void testinitialize() throws ParserConfigurationException,
			SAXException, IOException {

		System.out.println("***Inicializing tests***");
		System.out.println("creating document...");
		System.out.println();
		documentFactory = XipDocumentFactory.getInstance();

		BufferedReader buffer = new BufferedReader(new FileReader(filepath));

		document = documentFactory.getXipResult(buffer);
		System.out.println("Tokens:");
		for (XIPNode sentence : document.getSentences()) {
			for (Token token : sentence.getTokens()) {
				System.out.println(token + ", id: " + token.getId());
			}
		}

		System.out.println();
	}

	public void testCreatedDocument() throws Exception {
		System.out.println("testCreatedDocument:");
		System.out.println();

		assertNotNull(document.getDependencies());
		assertNotNull(document.getSentences());
		assertNotNull(document.getName());
		assertNotNull(document.getText());

		System.out.println("testCreateDocument - document sentences:");
		for (XIPNode node : document.getSentences()) {
			System.out.println(node.getSentence());
		}

		System.out.println("testCreatedDocument - dependencies:");
		System.out.println(document.getDependencies());
		assertFalse(document.getDependencies().isEmpty());

		System.out.println();
	}

	public void testAddNodeToDocument() throws Exception {

		System.out.println("testAddNodeToDocument:");

		XIPNode parent = null;
		String nodeName = "test";
		String nodeStart = "0";
		String nodeEnd = "10";
		String nodeNumber = "0";
		int sentenceNumber = 0;

		XIPNode node = new XIPNode(parent, nodeName, nodeStart, nodeEnd,
				nodeNumber, sentenceNumber, 0);

		document.addSentence(node);
		assertEquals(node, document.getNodeById(node.getId()));

	}

	public void testDependencies() {
		System.out.println("----------");
		System.out.println("testDependencies");

		for (Dependency dependency : document.getDependencies()) {
			System.out.println(dependency.getName());

			if (dependency.getName().equals("ACANDIDATE")) {
				Object[] nodes = dependency.getNodes().toArray();

				if (nodes.length < 2) {
					System.out.println("ACANDIDATE with less than two nodes");
				}
			}

			for (XIPNode node : dependency.getNodes()) {
				System.out.println(node + ", id: " + node.getId());
			}
		}
	}

	public void testNodeHasNoSiblingsException()
			throws NodeHasNoPreviousSiblingException {

		System.out.println("----------");
		System.out.println("testNodeHasNoSiblingsException");

		XIPNode parent = null;
		String nodeName = "test";
		String nodeStart = "0";
		String nodeEnd = "10";
		String nodeNumber = "0";
		int sentenceNumber = 0;

		XIPNode node = new XIPNode(parent, nodeName, nodeStart, nodeEnd,
				nodeNumber, sentenceNumber, 0);

		try {
			node.getPreviousSibling();
		} catch (NodeHasNoSiblingsException e) {
			System.out.println("caught " + e.getMessage());
		}

	}

	public void testNodeHasNoPreviousSiblingException()
			throws NodeHasNoSiblingsException {

		System.out.println("----------");
		System.out.println("NodeHasNoPreviousSiblingException");
		String nodeName = "test";
		String nodeStart = "0";
		String nodeEnd = "10";
		String nodeNumber = "0";
		int sentenceNumber = 0;

		XIPNode parent = new XIPNode(document, nodeName, nodeStart, nodeEnd,
				nodeNumber, sentenceNumber, 0);
		XIPNode child1 = new XIPNode(parent, nodeName + 1, nodeStart + 1,
				nodeEnd + 1, nodeNumber + 1, sentenceNumber, 0);
		XIPNode child2 = new XIPNode(parent, nodeName + 2, nodeStart + 2,
				nodeEnd + 2, nodeNumber + 2, sentenceNumber, 0);

		parent.addNode(child1);
		parent.addNode(child2);

		try {
			child1.getPreviousSibling();
		} catch (NodeHasNoPreviousSiblingException e) {
			System.out.println("caught " + e.getMessage());
		}

	}

	public void testNodeHasNoNextSiblingException()
			throws NodeHasNoSiblingsException {

		System.out.println("----------");
		System.out.println("NodeHasNoPreviousSiblingException");
		String nodeName = "test";
		String nodeStart = "0";
		String nodeEnd = "10";
		String nodeNumber = "0";
		int sentenceNumber = 0;

		XIPNode parent = new XIPNode(document, nodeName, nodeStart, nodeEnd,
				nodeNumber, sentenceNumber, 0);
		XIPNode child1 = new XIPNode(parent, nodeName + 1, nodeStart + 1,
				nodeEnd + 1, nodeNumber + 1, sentenceNumber, 0);
		XIPNode child2 = new XIPNode(parent, nodeName + 2, nodeStart + 2,
				nodeEnd + 2, nodeNumber + 2, sentenceNumber, 0);

		parent.addNode(child1);
		parent.addNode(child2);

		try {
			child2.getNextSibling();
		} catch (NodeHasNoNextSiblingException e) {
			System.out.println("caught " + e.getMessage());
		}

	}

	public void testNodeSiblings() throws NodeHasNoSiblingsException,
			NodeHasNoPreviousSiblingException, NodeHasNoNextSiblingException {

		System.out.println("----------");
		System.out.println("testNodeSiblings");

		String nodeName = "test";
		String nodeStart = "0";
		String nodeEnd = "10";
		String nodeNumber = "0";
		int sentenceNumber = 0;

		XIPNode parent = new XIPNode(document, nodeName, nodeStart, nodeEnd,
				nodeNumber, sentenceNumber, 0);
		XIPNode child1 = new XIPNode(parent, nodeName + 1, nodeStart + 1,
				nodeEnd + 1, nodeNumber + 1, sentenceNumber, 0);
		XIPNode child2 = new XIPNode(parent, nodeName + 2, nodeStart + 2,
				nodeEnd + 2, nodeNumber + 2, sentenceNumber, 0);

		XIPNode child11 = new XIPNode(child1, nodeName + 11, nodeStart + 11,
				nodeEnd + 11, nodeNumber + 11, sentenceNumber, 0);
		XIPNode child12 = new XIPNode(child1, nodeName + 12, nodeStart + 12,
				nodeEnd + 12, nodeNumber + 12, sentenceNumber, 0);

		XIPNode child21 = new XIPNode(child2, nodeName + 21, nodeStart + 21,
				nodeEnd + 21, nodeNumber + 21, sentenceNumber, 0);
		XIPNode child22 = new XIPNode(child2, nodeName + 22, nodeStart + 22,
				nodeEnd + 22, nodeNumber + 22, sentenceNumber, 0);

		parent.addNode(child1);
		parent.addNode(child2);

		child1.addNode(child11);
		child1.addNode(child12);

		child2.addNode(child21);
		child2.addNode(child22);

		assertEquals(child1, child2.getPreviousSibling());
		assertEquals(child2, child1.getNextSibling());

		assertEquals(child11, child12.getPreviousSibling());
		assertEquals(child12, child11.getNextSibling());

		assertEquals(child21, child22.getPreviousSibling());
		assertEquals(child22, child21.getNextSibling());

	}

}
