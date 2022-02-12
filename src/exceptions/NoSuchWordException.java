package exceptions;

public class NoSuchWordException extends Exception {
	public NoSuchWordException() {
		super("Not in word list");
	}
}
