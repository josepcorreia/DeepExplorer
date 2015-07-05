package pt.inescid.l2f.dependencyExtractor.domain.dependency;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import pt.inescid.l2f.xipapi.domain.Dependency;
import pt.inescid.l2f.xipapi.domain.Feature;
import pt.inescid.l2f.xipapi.domain.XIPNode;

public class NE extends DependencyType{
	private HashMap<String, String> _namedEnteties;
	private LinkedHashMap<String, String> _entetiesTable; 
	
	public NE(){
		super();
	  _namedEnteties = new HashMap<String, String>();	
	  _entetiesTable = getEntetiesTable();
	
	}
	
	public void getDepedencyInformation(Dependency dep){
		Boolean flagBreak;
		
		depLoop:
		for (XIPNode node : dep.getNodes()) {
			flagBreak = false;
			
			String features = ""; 
			
			featuresloop:
			for (Feature feature : dep.getFeatures()) {
	
				if(!features.isEmpty()){
					features += " " + feature.getName();
				}
				else{
					features += feature.getName();
				}
				
				if(_entetiesTable.containsKey(feature.getName())){
					_namedEnteties.put(node.getNodeNumber(), _entetiesTable.get(feature.getName()));

					flagBreak = true;
					break featuresloop;
				}
			}
			if(flagBreak){
				continue depLoop;
			}
			
			if(_entetiesTable.containsKey(features)){
				_namedEnteties.put(node.getNodeNumber(), _entetiesTable.get(features));
			}
			else{
				//escrever num ficheiro as novas features
				String path = new File("src/out/named_enteties.txt").getAbsolutePath();
				
				BufferedWriter writer = null;
		        try {
		            writer = new BufferedWriter(new FileWriter(path));
		            writer.write(features + "\n");

		        } catch (Exception e) {
		            e.printStackTrace();
		        } finally {
		            try {
		                writer.close();
		            } catch (Exception e) {
		            }
		        }
			}
		}
	
	}
	
	private LinkedHashMap<String, String> getEntetiesTable() {
		LinkedHashMap<String, String> entetiesTable = new LinkedHashMap<String, String>();
		
		try {
			String path = new File("src/resources/named_enteties.txt").getAbsolutePath();
			BufferedReader buffer = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
			
			String line;
		    while ((line = buffer.readLine()) != null) {
		    	String aux[] = line.split(",");
		    
		        entetiesTable.put(aux[0], aux[1]);
		    }

		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return entetiesTable;
		
	}
	
	public void ClearNamedEnteties(){
		_namedEnteties = new HashMap<String, String>();
	}
	
	public HashMap<String, String> getNamedEnteties(){
		return _namedEnteties;
	}
 
}
