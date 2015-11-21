package pt.inescid.l2f.dependencyExtractor.dependency;

import pt.inescid.l2f.xipapi.domain.Dependency;
import pt.inescid.l2f.xipapi.domain.Feature;
import pt.inescid.l2f.xipapi.domain.XIPNode;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class NE extends DependencyType{

    /** list with the named entities of a certain sentence */
	private HashMap<String, String> _namedEntetiesNodes;

	/** list with the named entities present in this system. The NEs was present in the file named_enteties.txt*/
	private LinkedHashMap<String, String> _namedEntetiesTable; 

	public NE(){
		super();
		_namedEntetiesNodes = new HashMap<String, String>();	
		_namedEntetiesTable = getNamedEntetiesTable();

	}

    /**
     * In the case of the XIPDependency NE (named entity) be present in the list with with the system's NE,
     * it is added to the map _namedEntetiesNodes the XIPNode which this NE applies and its category.
     *
     * @param  dep - Xip Dependency - NE (XIPAPI)
     * */
	public void getDependencyInformation(Dependency dep){
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

						if(_namedEntetiesTable.containsKey(feature.getName())){
							_namedEntetiesNodes.put(node.getNodeNumber(), _namedEntetiesTable.get(feature.getName()));

							flagBreak = true;
							break featuresloop;
						}
					}
				if(flagBreak){
					continue depLoop;
				}

				if(_namedEntetiesTable.containsKey(features)){
					_namedEntetiesNodes.put(node.getNodeNumber(), _namedEntetiesTable.get(features));
				}
				else{

					for (Entry<String, String> entry : _namedEntetiesTable.entrySet()) {
						if(features.contains(entry.getKey())){
							_namedEntetiesNodes.put(node.getNodeNumber(), _namedEntetiesTable.get(entry.getKey()));
							continue depLoop;
						}
					}	
					//caso se deseje fazer alguma coisa com as features não detectadas, será aqui
					
				}
			}

	}

    /**
     * Get the list with the named entities present in this system.
     * The NEs was present in the file named_enteties.txt
     *
     * @return List with the named entities present in this system.
     * */
	private LinkedHashMap<String, String> getNamedEntetiesTable() {
		LinkedHashMap<String, String> namedEntetiesTable = new LinkedHashMap<String, String>();

		try {
			String path = new File("src/resources/named_enteties.txt").getAbsolutePath();
			BufferedReader buffer = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));

			String line;
			while ((line = buffer.readLine()) != null) {
				String aux[] = line.split(",");

				namedEntetiesTable.put(aux[0], aux[1]);
			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return namedEntetiesTable;

	}

    /**
     * Clear the list that has the named entities of a certain sentence
     *
     * */
	public void ClearNamedEntetiesNodes(){
		_namedEntetiesNodes = new HashMap<String, String>();
	}

    /**
     * Get the list that has the named entities of a certain sentence
     *
     * @return list with the named entities of a certain sentence
     * */
	public HashMap<String, String> getnamedEntetiesNodes(){
		return _namedEntetiesNodes;
	}

}
