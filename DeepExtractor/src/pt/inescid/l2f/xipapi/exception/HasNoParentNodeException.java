package pt.inescid.l2f.xipapi.exception;

public class HasNoParentNodeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public HasNoParentNodeException() {
	}

	public HasNoParentNodeException(String message) {
		super(message);
	}

	public HasNoParentNodeException(Throwable cause) {
		super(cause);
	}

	public HasNoParentNodeException(String message, Throwable cause) {
		super(message, cause);
	}

}
