package logic;

public class Round {
	private static final int MAX_ATTEMPTS = 6;

	private final String currentWord;
	private int currentAttempts = 0;
	private boolean gameOver = false;
	public LetterGrade[][] grades;

	public Round(int length) {
		currentWord = Gurgle.getWord(length);
		init(length);
	}

	public Round() {
		currentWord = Gurgle.getWord();
		init(currentWord.length());
	}

	private void init(int length) {
		this.grades = new LetterGrade[length][MAX_ATTEMPTS];
	}

	public boolean getGameOver() {
		return this.gameOver;
	}

	public int getLength() {
		return this.currentWord.length();
	}

	public LetterGrade[] grade(String input) {
		if (input.length() != currentWord.length()) {
			throw new RuntimeException("Guess must be of same length as answer");
		}
		else if (!Gurgle.allWords.contains(input)) {
			throw new RuntimeException("Dictionary does not contain this word");
		}
		currentAttempts++;
		LetterGrade[] grade = Gurgle.grade(input.toCharArray(), currentWord);
		boolean allCorrect = true;
		for (LetterGrade letterGrade : grade) {
			if (letterGrade != LetterGrade.CORRECT) {
				allCorrect = false;
				break;
			}
		}
		this.gameOver = allCorrect;
		return grade;
	}

	public int getCurrentAttempts() {
		return this.currentAttempts;
	}
}
