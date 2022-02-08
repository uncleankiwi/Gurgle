package cli;

import logic.Gurgle;
import logic.LetterGrade;
import logic.Round;

import java.util.Scanner;

public class cliApp {
	private static Round currentRound = null;

	public static void main(String[] args) {
		Gurgle.loadWords();
		try(Scanner scanner = new Scanner(System.in)) {
			while (true) {
				String input = scanner.next();
				//menu
				if (currentRound == null) {
					switch (input) {
						case "q":
							System.out.println("Quitting...");
							return;
						case "b":
							System.out.println("Beginning a new round with a random word length.");
							currentRound = new Round();
							System.out.println("Word length: " + currentRound.getLength());
							break;
						case "5":
							System.out.println("Beginning a new round.");
							currentRound = new Round(5);
							System.out.println("Word length: " + currentRound.getLength());
							break;
						default:
							System.out.println("Press q to quit, b to begin a game, 5 to start a 5-letter round.");
					}
				}
				//in a round
				else {
					try {
						printGrade(currentRound.grade(input));
					}
					catch (Exception e) {
						System.out.println(e.getMessage());
					}

					if (currentRound.getGameOver()) {
						System.out.println("Won in " + currentRound.getCurrentAttempts());
						currentRound = null;
					}
				}
			}
		}
	}

	private static void printGrade(LetterGrade[] grades) {
		StringBuilder builder = new StringBuilder();
		for (LetterGrade grade : grades) {
			switch (grade) {
				case CORRECT:
					builder.append("v ");
					break;
				case WRONG:
					builder.append("  ");
					break;
				case RIGHT_LETTER:
					builder.append("? ");
					break;
			}
		}
		System.out.println(builder.toString());
	}
}
