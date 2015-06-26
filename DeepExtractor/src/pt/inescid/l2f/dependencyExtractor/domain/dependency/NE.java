package pt.inescid.l2f.dependencyExtractor.domain.dependency;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import pt.inescid.l2f.xipapi.domain.Dependency;
import pt.inescid.l2f.xipapi.domain.Feature;
import pt.inescid.l2f.xipapi.domain.XIPNode;

public class NE extends DependencyType{
	private HashMap<String, String> _namedEnteties;
	private HashMap<String, String> _entetiesTable; 
	
	public NE() {
	  super();
	  _namedEnteties = new HashMap<String, String>();	
	  _entetiesTable = getEntetiesTable();
	}
	
	public void getDepedencyInformation(Dependency dep){
		for (XIPNode node : dep.getNodes()) {
			
			String features = ""; 
			for (Feature feature : dep.getFeatures()) {
				if(!features.isEmpty()){
					features += " " + feature.getName();
				}
				else{
					features += feature.getName();
				}
			}
			if(_entetiesTable.containsKey(features)){
				_namedEnteties.put(node.getNodeNumber(), _entetiesTable.get(features));
			}
			else{
				_namedEnteties.put(node.getNodeNumber(), features);
				//escrever num ficheiro as novas features
			}
		}
	}
	
	private HashMap<String, String> getEntetiesTable() {
		HashMap<String, String> entetiesTable = new HashMap<String, String>();
		
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
