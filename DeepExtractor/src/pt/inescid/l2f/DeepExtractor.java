package pt.inescid.l2f;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;

import pt.inescid.l2f.connection.ConnectionSQLite;
import pt.inescid.l2f.dependencyExtractor.DependencyExtractor;
import pt.inescid.l2f.dependencyExtractor.domain.database.RelationalFactory;
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
		//Connection connection = ConnectionMySQL.getConnectionMySQL();
		Connection connection = ConnectionSQLite.getConnectionSQLite(args[1]);
		
		String corpusName = "CETEMPúblico";
		
		RelationalFactory relationalFactory  = new RelationalFactory(connection, corpusName);//alterar e tornar a classe compeltamente estatica
		DependencyExtractor de = new DependencyExtractor(corpusName);
		
		System.out.println(ConnectionSQLite.getStatusConnection());

		XipDocumentFactory xipDocumentFactory = XipDocumentFactory.getInstance();
					
		Path dir = Paths.get(args[0]);
		
		de.CorpusInformation();
			
		inspectDirectory(dir, xipDocumentFactory, de);
			
		de.CalculateAssociationMeasures();
			
		//ConnectionMySQL.CloseConnection(connection);
		ConnectionSQLite.CloseConnection(connection);
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