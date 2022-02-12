package exceptions;

public class GameOverException extends Exception {
	public GameOverException() {
		super("This round is no more. It has ceased to be. It has expired and gone to meet its maker.");
	}
}
