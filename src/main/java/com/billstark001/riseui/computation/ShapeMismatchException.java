package com.billstark001.riseui.computation;

public class ShapeMismatchException extends Exception {

	private static final long serialVersionUID = 1;
	protected static final String MESSAGE_DEFAULT = "Shape Mismatch.";
	protected static final String MESSAGE_NORMAL = "Shape Mismatch: %s and %s.";
	protected static final String MESSAGE_EXPECT = "Shape Mismatch: expected %s, got %s.";
	
	public ShapeMismatchException() {
		super("MESSAGE_DEFAULT");
	}
	
	public ShapeMismatchException(Pair p1, Pair p2) {
		super(String.format(MESSAGE_NORMAL, p1, p2));
	}
	
	public ShapeMismatchException(Matrix m1, Matrix m2) {
		super(String.format(MESSAGE_NORMAL, m1 == null ? null : m1.getShape(), m2 == null ? null : m2.getShape()));
	}
	
	public static ShapeMismatchException Expect(Pair expected, Pair actual) {
		String message = String.format(MESSAGE_EXPECT, expected, actual);
		return new ShapeMismatchException(message);
	}
	
	public static ShapeMismatchException Expect(Matrix expected, Matrix actual) {
		return Expect(expected == null ? null : expected.getShape(), actual == null ? null : actual.getShape());
	}
	
	public static ShapeMismatchException Expect(Pair expected, Matrix actual) {
		return Expect(expected, actual == null ? null : actual.getShape());
	}
	
	public static ShapeMismatchException Expect(Matrix expected, Pair actual) {
		return Expect(expected == null ? null : expected.getShape(), actual);
	}

	public ShapeMismatchException(String arg0) {
		super(arg0);
	}

	protected ShapeMismatchException(Throwable cause) {
		super(MESSAGE_DEFAULT, cause);
	}

	protected ShapeMismatchException(String message, Throwable cause) {
		super(message, cause);
	}

	protected ShapeMismatchException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
