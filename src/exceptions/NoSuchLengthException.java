package exceptions;

public class NoSuchLengthException extends Exception {
	public NoSuchLengthException(int length) {
		super("No words in dictionary of length " + length);
	}
}
