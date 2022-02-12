package exceptions;

public class NoSuchWordException extends Exception {
	public NoSuchWordException() {
		super("No such word exists in the dictionary.");
	}
}
