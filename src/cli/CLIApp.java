package cli;

import exceptions.NoSuchLengthException;
import exceptions.WrongRequestedLengthException;
import logic.Keys;
import logic.LetterGrade;
import logic.Round;
import java.util.Scanner;

public class CLIApp {
	private static Round currentRound = null;
	private static final String MENU_MESSAGE = "Press q to quit, b to begin a game, r to freeze the application for 8 seconds.";

	public static void main(String[] args) {
		System.out.println(MENU_MESSAGE);
		try(Scanner scanner = new Scanner(System.in)) {
			while (true) {
				String input = scanner.next();
				//menu
				if (currentRound == null) {
					switch (input) {
						case "q":
							System.out.println("Quitting...");
							return;
						case "r":
							currentRound = new Round();
							System.out.println(getLetterGridString());
							System.out.println(getKeyboardString());
							System.out.println("Beginning a new round with a random word length of " + currentRound.getLength());
							break;
						case "b":
							try {
								currentRound = new Round(5);
							} catch (WrongRequestedLengthException e) {
								e.printStackTrace();
							} catch (NoSuchLengthException e) {
								System.out.println(e.getMessage());
							}
							System.out.println(getLetterGridString());
							System.out.println(getKeyboardString());
							System.out.println("Beginning a new round with a default word length of 5.");
							break;
						default:
							System.out.println("Invalid input.");
							System.out.println(MENU_MESSAGE);
					}
				}
				//in a round
				else {
					try {
						currentRound.grade(input);
						System.out.println(getLetterGridString());
						System.out.println(getKeyboardString());
					}
					catch (Exception e) {
						System.out.println(getLetterGridString());
						System.out.println(getKeyboardString());
						System.out.println(e.getMessage());
					}

					if (currentRound.getGameOver()) {
						if (currentRound.getGameWon()) {
							System.out.println("Won in " + currentRound.getCurrentAttempts());
						}
						else {
							System.out.println("Oops! The correct answer was " + currentRound.getCurrentWord());
						}
						currentRound = null;
						System.out.println(MENU_MESSAGE);
					}
				}
			}
		}
	}

	private static String getKeyboardString() {
		StringBuilder builder = new StringBuilder();
		for (char c : Keys.topRow) {
			builder.append(getKeyAppearance(c)).append(" ");
		}
		builder.append("\n");
		builder.append("   ");
		for (char c : Keys.midRow) {
			builder.append(getKeyAppearance(c)).append(" ");
		}
		builder.append("\n");
		builder.append("      ");
		for (char c : Keys.bottomRow) {
			builder.append(getKeyAppearance(c)).append(" ");
		}
		builder.append("\n");
		return builder.toString();
	}

	private static String getKeyAppearance(char c) {
		//[A] for present letters
		//    for absent letters
		// a  for unknown letters
		switch (currentRound.letterGradeMap.get(c)) {
			case WRONG:
				return "   ";
			case CORRECT:
				return "[" + (char)(c - 32) + "]";
			case RIGHT_LETTER:
				return "." + (char)(c - 32) + ".";
			default:
				return " " + c + " ";
		}

	}

	private static String getLetterGridString() {
		//[A] for correct
		//.A. for right letter
		// a for wrong
		// _ for blank
		StringBuilder builder = new StringBuilder();
		for (int row = 0; row < currentRound.attemptLetters.length; row++) {
			for (int col = 0; col < currentRound.getLength(); col++) {
				Character c = currentRound.attemptLetters[row][col];
				LetterGrade grade = currentRound.grades[row][col];
				String prefix = " ";
				String postfix = " ";
				if (c != null) {
					switch (grade) {
						case CORRECT:
							prefix = "[";
							postfix = "]";
							c = (char) (c - 32);	//transform to uppercase
							break;
						case RIGHT_LETTER:
							prefix = ".";
							postfix = ".";
							c = (char) (c - 32);
							break;
						default:		//WRONG is here
							prefix = " ";
							postfix = " ";
					}
				}
				else {
					c = '_';	//x_x
				}
				builder.append(prefix)
						.append(c)
						.append(postfix);
			}
			builder.append("\n");
		}
		return builder.toString();
	}
}
