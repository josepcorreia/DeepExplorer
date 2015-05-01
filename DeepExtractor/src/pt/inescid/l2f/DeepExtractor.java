package pt.inescid.l2f;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
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
		System.out.println("Inicio");
		Connection c_mysql = ConnectionMySQL.getConnectionMySQL();
		DependencyExtractor de = new DependencyExtractor(c_mysql, "CETEMPúblico");
		
		XipDocument document = null;
		try{
			XipDocumentFactory xipDocumentFactory = XipDocumentFactory.getInstance();
			//BufferedReader buffer = new BufferedReader(new FileReader(args[0]));
			BufferedReader buffer = new BufferedReader(new InputStreamReader(new FileInputStream(args[0]), "UTF-8"));
			document = xipDocumentFactory.getXipResult(buffer);
			

			System.out.println(ConnectionMySQL.getStatusConnection());
						
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