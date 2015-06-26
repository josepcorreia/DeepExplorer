package pt.inescid.l2f;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
		
		System.out.println(ConnectionMySQL.getStatusConnection());

		XipDocumentFactory xipDocumentFactory = XipDocumentFactory.getInstance();
					
		Path dir = Paths.get(args[0]);
		
		de.CorpusInformation();
			
		inspectDirectory(dir, xipDocumentFactory, de);
			
		de.CalculateAssociationMeasures();
			
		ConnectionMySQL.CloseConnection(c_mysql);
		System.out.println("FIM");		
	}
	
	public static void inspectDirectory(Path path, XipDocumentFactory xipdc, DependencyExtractor de){
		XipDocument document = null;
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
			for (Path file: stream) {
				if(file.toFile().isDirectory()){
					System.out.println("DIRECTORY " +file.getFileName());
					inspectDirectory(file, xipdc, de);

				}
				if(file.toFile().isFile() && file.toString().endsWith(".xml")){
					
					System.out.println("FILE " +file.getFileName());
					BufferedReader buffer = new BufferedReader(new InputStreamReader(new FileInputStream(file.toString()), "UTF-8"));
					document = xipdc.getXipResult(buffer);
										
					de.Extract(document);
				}

			}
		} catch (IOException | DirectoryIteratorException x) {
			// IOException can never be thrown by the iteration.
			// In this snippet, it can only be thrown by newDirectoryStream.
			System.err.println(x);
		}catch(Exception e1){
			System.err.println("DeepExplorer: input error");
			e1.printStackTrace();
		return;
		}
	} 
	
}