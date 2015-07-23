package pt.inescid.l2f;

import pt.inescid.l2f.connection.database.RelationalFactory;
import pt.inescid.l2f.dependencyExtractor.DependencyExtractor;
import pt.inescid.l2f.xipapi.XipDocumentFactory;
import pt.inescid.l2f.xipapi.domain.XipDocument;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.*;


public class DeepMeasures {
	/**
	 * Main function.
	 *
	 * @param args path/filename of the file that contains the XML output from XIP.
	 */
	public static void main(String[] args) {
		System.out.println("Inicio");
	
		String corpusName = "CETEMPúblico";
		
		RelationalFactory rf = new RelationalFactory(corpusName, args[0]);
		DependencyExtractor de = new DependencyExtractor();

		//calcula as medidas
		de.CalculateAssociationMeasures();
			
		rf.closeConnection();
		System.out.println("FIM");		
	}
}