package logic;

import exceptions.*;

import java.util.HashMap;
import java.util.Map;

public class Round {
	public static final int MAX_ATTEMPTS = 6;

	private final String currentWord;
	private int currentAttempts = 0;
	private boolean gameOver = false;
	private Boolean gameWon = null;
	public LetterGrade[][] grades;
	public Character[][] attemptLetters;
	public Map<Character, LetterGrade> letterGradeMap;

	public Round(int length) throws WrongRequestedLengthException, NoSuchLengthException {
		currentWord = Gurgle.getWord(length);
		init(length);
	}

	public Round() {
		currentWord = Gurgle.getWord();
		init(currentWord.length());
	}

	private void init(int length) {
		this.grades = new LetterGrade[MAX_ATTEMPTS][length];
		this.attemptLetters = new Character[MAX_ATTEMPTS][length];
		this.letterGradeMap = new HashMap<>();
		for (int i = 97; i <= 122; i++) {
			this.letterGradeMap.put((char) i, LetterGrade.DEFAULT);
		}
	}

	public boolean getGameOver() {
		return this.gameOver;
	}

	public int getLength() {
		return this.currentWord.length();
	}

	public void grade(String input) throws GameOverException, NoSuchWordException, WrongGuessLengthException {
		if (this.gameOver) {
			throw new GameOverException();
		}
		else if (input.length() != currentWord.length()) {
			throw new WrongGuessLengthException();
		}
		else if (!Gurgle.allWords.contains(input)) {
			throw new NoSuchWordException();
		}
		input = input.toLowerCase();

		//grading, then updating letter/grade trackers, plus watching for a win
		boolean allCorrect = true;
		LetterGrade[] grade = Gurgle.grade(input.toCharArray(), currentWord, letterGradeMap);
		for (int i = 0; i < currentWord.length(); i++) {
			attemptLetters[currentAttempts][i] = input.toCharArray()[i];
			grades[currentAttempts][i] = grade[i];
			if (grade[i] != LetterGrade.CORRECT) {
				allCorrect = false;
			}
		}
		currentAttempts++;

		//see if the round is over
		if (allCorrect) {
			this.gameOver = true;
			this.gameWon = true;
		}
		else if (this.currentAttempts >= MAX_ATTEMPTS) {
			this.gameOver = true;
			this.gameWon = false;
		}
	}

	public String getCurrentWord() {
		if (this.gameOver) {
			return this.currentWord;
		}
		else {
			throw new RuntimeException("Can't reveal word unless game has been won");
		}
	}

	public Boolean getGameWon() {
		return this.gameWon;
	}

	public int getCurrentAttempts() {
		return this.currentAttempts;
	}
}
