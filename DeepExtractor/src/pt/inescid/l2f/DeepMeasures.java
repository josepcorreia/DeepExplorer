package pt.inescid.l2f;

import pt.inescid.l2f.connection.database.RelationalFactory;
import pt.inescid.l2f.connection.exception.DatabaseException;


public class DeepMeasures {
	/**
	 * Main function.
	 *
	 * @param args the path to teh database.
	 */
	public static void main(String[] args) {
		System.out.println("Inicio");
	
		String corpusName = "CETEMPúblico";
		
		RelationalFactory rf = new RelationalFactory(corpusName, args[0]);

		//calcula as medidas
		try {
			rf.getCoocorrence().updateMeasures();
		} catch (DatabaseException e) {
			System.err.println(e.getMessage());

			//if a database's problem was occurred, the connection is closed and the program exits
			RelationalFactory.closeConnection();
			System.exit(0);
		}

		rf.commit();

		rf.closeConnection();
		System.out.println("FIM");		
	}
}