package pt.inescid.l2f;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;

import pt.inescid.l2f.connection.ConnectionMySQL;
import pt.inescid.l2f.dependencyExtractor.DependencyExtractor;
import pt.inescid.l2f.xipapi.XipDocumentFactory;
import pt.inescid.l2f.xipapi.domain.XipDocument;


public class DeepExtractor {
	/**
	 * Main function.
	 *
	 * @param args path/filename of the file that contains the XML output from XIP.
	 */
	public static void main(String[] args) {
		DependencyExtractor de = new DependencyExtractor();
		ConnectionMySQL c_mysql = new ConnectionMySQL();
		
		XipDocument document = null;
		try{
			System.out.println("Inicio");
			XipDocumentFactory xipDocumentFactory = XipDocumentFactory.getInstance();
			BufferedReader buffer = new BufferedReader(new FileReader(args[0]));
			document = xipDocumentFactory.getXipResult(buffer);
			
			c_mysql.makeConnectionMySQL();
			System.out.println(c_mysql.getStatusConnection());
			
			
			de.CorpusInformation(c_mysql);
			de.Extract(document.getDependencies(), c_mysql);
			
			
			
			c_mysql.CloseConnection();
			System.out.println("FIM");
			
		}catch(Exception e1){
			System.err.println("DeepExplorer: input error");
			e1.printStackTrace();
			return;
		}
	}
}