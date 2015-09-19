package pt.inescid.l2f;

import pt.inescid.l2f.connection.database.RelationalFactory;
import pt.inescid.l2f.dependencyExtractor.DependencyExtractor;
import pt.inescid.l2f.xipapi.XipDocumentFactory;
import pt.inescid.l2f.xipapi.domain.XipDocument;

import java.io.*;
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

		RelationalFactory rf = new RelationalFactory(corpusName, args[0]);
        rf.getCorpus().insertNew(corpusName, "Público", "2000", "Noticíario", false);

        DependencyExtractor de = new DependencyExtractor();
		
		XipDocumentFactory xipDocumentFactory = XipDocumentFactory.getInstance();
					
		//Path dir = Paths.get(args[1]);
        File[] dir = new File(args[1]).listFiles();

        inspectDirectory(dir, xipDocumentFactory, de);

			
		rf.closeConnection();
		System.out.println("FIM");		
	}
	
	public static void inspectDirectory(File[] files, XipDocumentFactory xipdc, DependencyExtractor de){
		XipDocument document = null;
		try{
            for (File file : files) {
				if(file.isDirectory()){
					System.out.println("DIRECTORY " +file.getName());
					inspectDirectory(file.listFiles(), xipdc, de);

				}
				if(file.isFile() && file.getName().endsWith(".xml")){
					String filename = file.getName();

                    if(!RelationalFactory.getFile().fileExists(filename)) {

                        System.out.println("FILE " + filename);
                        BufferedReader buffer = new BufferedReader(new InputStreamReader(new FileInputStream(file.toString()), "UTF-8"));
                        document = xipdc.getXipResult(buffer);

                        de.Extract(document, filename);
                    }

				}

			}
		} catch (IOException x) {
			// IOException can never be thrown by the iteration.
			// In this snippet, it can only be thrown by newDirectoryStream.
            RelationalFactory.closeConnection();
            System.err.println(x);
		} catch (DirectoryIteratorException x) {
			// IOException can never be thrown by the iteration.
			// In this snippet, it can only be thrown by newDirectoryStream.
            RelationalFactory.closeConnection();
            System.err.println(x);
		} catch(Exception e1){
			System.err.println("DeepExplorer: input error");
			e1.printStackTrace();
			RelationalFactory.closeConnection();
            return;
		}
	} 
	
}