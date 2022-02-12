package exceptions;

public class InputNotAllowedException extends Exception {
	public InputNotAllowedException() {
		super("Input isn't possible right now.");
	}
}
