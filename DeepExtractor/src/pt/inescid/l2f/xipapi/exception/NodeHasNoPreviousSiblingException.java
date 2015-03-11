package pt.inescid.l2f.xipapi.exception;

public class NodeHasNoPreviousSiblingException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NodeHasNoPreviousSiblingException() {
	}

	public NodeHasNoPreviousSiblingException(String message) {
		super(message);
	}

	public NodeHasNoPreviousSiblingException(Throwable cause) {
		super(cause);
	}

	public NodeHasNoPreviousSiblingException(String message, Throwable cause) {
		super(message, cause);
	}

}
