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
		Connection c_mysql = ConnectionMySQL.getConnectionMySQL();
		DependencyExtractor de = new DependencyExtractor(c_mysql);
		
		XipDocument document = null;
		try{
			System.out.println("Inicio");
			XipDocumentFactory xipDocumentFactory = XipDocumentFactory.getInstance();
			BufferedReader buffer = new BufferedReader(new FileReader(args[0]));
			document = xipDocumentFactory.getXipResult(buffer);
			

			System.out.println(ConnectionMySQL.getStatusConnection());
			
			
			de.CorpusInformation();
			de.Extract(document.getDependencies());
			
			
			
			ConnectionMySQL.CloseConnection(c_mysql);
			System.out.println("FIM");
			
		}catch(Exception e1){
			System.err.println("DeepExplorer: input error");
			e1.printStackTrace();
			return;
		}
	}
}