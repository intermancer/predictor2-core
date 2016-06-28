package com.intermancer.predictor.mutation;

public class MutationException extends RuntimeException {

	private static final long serialVersionUID = -7057938162863768849L;

	public MutationException() {
		super();
	}

	public MutationException(String message) {
		super(message);
	}

	public MutationException(Throwable cause) {
		super(cause);
	}

	public MutationException(String message, Throwable cause) {
		super(message, cause);
	}

}
