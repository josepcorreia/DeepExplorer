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


public class DeepExtractor {
	/**
	 * Main function.
	 *
	 * @param args path/filename of the file that contains the XML output from XIP.
	 */
	public static void main(String[] args) {
		System.out.println("Inicio");
	
		String corpusName = "CETEMPúblico";
		System.out.println(args[0]);
		RelationalFactory rf = new RelationalFactory(corpusName, args[0]);
		DependencyExtractor de = new DependencyExtractor();
		
		XipDocumentFactory xipDocumentFactory = XipDocumentFactory.getInstance();
					
		Path dir = Paths.get(args[1]);
		
		RelationalFactory.getCorpus().insertNew(corpusName, "Público", "2000", "Noticíario", false);
			
		inspectDirectory(dir, xipDocumentFactory, de);


		//calcula as medidas
		//de.CalculateAssociationMeasures();
			
		rf.closeConnection();
		System.out.println("FIM");		
	}
	
	public static void inspectDirectory(Path path, XipDocumentFactory xipdc, DependencyExtractor de){
		XipDocument document = null;
		try{
			DirectoryStream<Path> stream = Files.newDirectoryStream(path);
			for (Path file: stream) {
				if(file.toFile().isDirectory()){
					System.out.println("DIRECTORY " +file.getFileName());
					inspectDirectory(file, xipdc, de);

				}
				if(file.toFile().isFile() && file.toString().endsWith(".xml")){
					String filename = file.getFileName().toString();

                    if(!RelationalFactory.getFile().fileExists(filename)) {

                        System.out.println("FILE " + filename);
                        BufferedReader buffer = new BufferedReader(new InputStreamReader(new FileInputStream(file.toString()), "UTF-8"));
                        document = xipdc.getXipResult(buffer);

                        de.Extract(document, filename);

                        RelationalFactory.getFile().insertNewFile(filename);
                    }

				}

			}
		} catch (IOException x) {
			// IOException can never be thrown by the iteration.
			// In this snippet, it can only be thrown by newDirectoryStream.
			System.err.println(x);
		} catch (DirectoryIteratorException x) {
			// IOException can never be thrown by the iteration.
			// In this snippet, it can only be thrown by newDirectoryStream.
			System.err.println(x);
		} catch(Exception e1){
			System.err.println("DeepExplorer: input error");
			e1.printStackTrace();
		return;
		}
	} 
	
}