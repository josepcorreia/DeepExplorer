package pt.inescid.l2f;

import java.io.BufferedReader;
import java.io.FileReader;

import pt.inescid.l2f.xipapi.XipDocumentFactory;
import pt.inescid.l2f.xipapi.domain.XipDocument;

public class DeepExplorer {
	/**
	 * Main function.
	 *
	 * @param args path/filename of the file that contains the XML output from XIP.
	 */
	public static void main(String[] args) {

		XipDocument document = null;
		try{
			XipDocumentFactory xipDocumentFactory = XipDocumentFactory.getInstance();
			BufferedReader buffer = new BufferedReader(new FileReader(args[0]));
			document = xipDocumentFactory.getXipResult(buffer);

		}catch(Exception e1){
			System.err.println("AfterXIP: input error");
			e1.printStackTrace();
			return;
		}
	}
}