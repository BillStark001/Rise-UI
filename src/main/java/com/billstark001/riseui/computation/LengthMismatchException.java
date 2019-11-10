package com.billstark001.riseui.computation;

public class LengthMismatchException extends Exception {

	private static final long serialVersionUID = 1;
	protected static final String MESSAGE_DEFAULT = "Length(Dimension) Mismatch.";
	protected static final String MESSAGE_NORMAL = "Length(Dimension) Mismatch: %s and %s.";
	protected static final String MESSAGE_EXPECT = "Length(Dimension) Mismatch: expected %s, got %s.";
	
	public LengthMismatchException() {
		super("MESSAGE_DEFAULT");
	}
	
	public LengthMismatchException(int p1, int p2) {
		super(String.format(MESSAGE_NORMAL, p1, p2));
	}
	
	public LengthMismatchException(Vector m1, Vector m2) {
		super(String.format(MESSAGE_NORMAL, m1 == null ? null : m1.getDimension(), m2 == null ? null : m2.getDimension()));
	}
	
	public LengthMismatchException Expect(int expected, int actual) {
		String message = String.format(MESSAGE_EXPECT, expected, actual);
		return new LengthMismatchException(message);
	}
	
	public LengthMismatchException Expect(Vector expected, Vector actual) {
		return Expect(expected == null ? null : expected.getDimension(), actual == null ? null : actual.getDimension());
	}
	
	public LengthMismatchException Expect(int expected, Vector actual) {
		return Expect(expected, actual == null ? null : actual.getDimension());
	}
	
	public LengthMismatchException Expect(Vector expected, int actual) {
		return Expect(expected == null ? null : expected.getDimension(), actual);
	}

	public LengthMismatchException(String arg0) {
		super(arg0);
	}

	protected LengthMismatchException(Throwable cause) {
		super(MESSAGE_DEFAULT, cause);
	}

	protected LengthMismatchException(String message, Throwable cause) {
		super(message, cause);
	}

	protected LengthMismatchException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
