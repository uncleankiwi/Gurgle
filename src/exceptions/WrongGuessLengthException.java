package exceptions;

public class WrongGuessLengthException extends Exception {
	public WrongGuessLengthException() {
		super("Not enough letters");
	}
}
