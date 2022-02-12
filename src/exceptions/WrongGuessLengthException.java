package exceptions;

public class WrongGuessLengthException extends Exception {
	public WrongGuessLengthException() {
		super("Guess must be of same length as answer");
	}
}
