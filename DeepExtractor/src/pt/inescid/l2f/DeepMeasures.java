package pt.inescid.l2f;

import pt.inescid.l2f.connection.database.RelationalFactory;


public class DeepMeasures {
	/**
	 * Main function.
	 *
	 * @param args path/filename of the file that contains the XML output from XIP.
	 */
	public static void main(String[] args) {
		System.out.println("Inicio");
	
		String corpusName = "CETEMPúblico";
		
		RelationalFactory rf = new RelationalFactory(corpusName, args[0]);

		//calcula as medidas
		rf.getCoocorrence().updateMeasures();
		rf.commit();

		rf.closeConnection();
		System.out.println("FIM");		
	}
}