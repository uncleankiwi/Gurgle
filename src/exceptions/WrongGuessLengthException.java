package exceptions;

public class WrongGuessLengthException extends Exception {
	public WrongGuessLengthException(int given, int expected) {
		super(given > expected ? "Guess has too many letters" : "Guess has too few letters");
	}
}
