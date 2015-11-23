package pt.inescid.l2f.connection.exception;

public class DatabaseException extends Exception{

    /** the table where the problem occurred */
	private String table;

    /** what was being done when the problem occurred*/
	private String function;


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    public DatabaseException(String table, String function) {
        this.table = table;
        this.function = function;
    }

    public String getTable() {
        return table;
    }

    public String getFunction() {
        return function;
    }

    public String getMessage(){
        return "The problem occurred in the database's Table "+table+ " in the method "+ function;
    }
}
