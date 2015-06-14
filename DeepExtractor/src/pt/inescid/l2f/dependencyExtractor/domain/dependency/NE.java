package pt.inescid.l2f.dependencyExtractor.domain.dependency;

import java.util.HashMap;

import pt.inescid.l2f.xipapi.domain.Dependency;
import pt.inescid.l2f.xipapi.domain.Feature;
import pt.inescid.l2f.xipapi.domain.XIPNode;

public class NE extends DependencyType{
	private HashMap<String, String> _namedEnteties;
	
	public NE() {
	  super();
	  _namedEnteties = new HashMap<String, String>();	
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
			//System.out.println(node.getNodeNumber());
			_namedEnteties.put(node.getNodeNumber(), features);
				
		}
	}
	
	public void ClearNamedEnteties(){
		_namedEnteties = new HashMap<String, String>();
	}
	
	public HashMap<String, String> getNamedEnteties(){
		return _namedEnteties;
	}
}
