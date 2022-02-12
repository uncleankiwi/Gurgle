package exceptions;

import static logic.Gurgle.LONGEST_LENGTH;
import static logic.Gurgle.SHORTEST_LENGTH;

public class WrongRequestedLengthException extends Exception {
	public WrongRequestedLengthException() {
		super("Word length must be between " + SHORTEST_LENGTH + " and " + LONGEST_LENGTH);
	}
}
