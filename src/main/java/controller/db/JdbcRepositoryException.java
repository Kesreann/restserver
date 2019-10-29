package controller.db;

public class JdbcRepositoryException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public JdbcRepositoryException(String message) {
		super(message);
	}

	public JdbcRepositoryException(String message, Throwable cause) {
		super(message, cause);
	}
}
