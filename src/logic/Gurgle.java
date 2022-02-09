package logic;

import java.io.*;
import java.util.*;

public class Gurgle {
	public static final List<String> allWords = new ArrayList<>();
	public static final Map<Integer, List<String>> wordsByLength = new HashMap<>();
	public static final int SHORTEST_LENGTH = 4;
	public static final int LONGEST_LENGTH = 31;

	public static void loadAllWords() {
		for (int i = SHORTEST_LENGTH; i <= LONGEST_LENGTH; i++) {
			loadWordsOfLength(i);
		}
	}

	@SuppressWarnings("unchecked")
	public static void loadWordsOfLength(int length) {
		//words of this length already loaded. skip.
		if (wordsByLength.containsKey(length)) {
			return;
		}

		//no such words of this length.
		File inFile = new File("resources/words" + length + ".dat");
		if (!inFile.exists()) {
			return;
		}

		//load from file
		try(FileInputStream fileInputStream = new FileInputStream(inFile);
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
			List<String> list = (List<String>) objectInputStream.readObject();
			wordsByLength.put(length, list);
			allWords.addAll(list);
			System.out.println("Loaded " + list.size() + " words of length " + length);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static LetterGrade[] grade(char[] attempt, String answer, Map<Character, KeyState> keyStateMap) {
		LetterGrade[] grades = new LetterGrade[attempt.length];
		char[] answerArr = answer.toCharArray();
		Set<Character> answerLetterSet = new HashSet<>();
		//looking at characters present in answer
		for (char c : answerArr) {
			answerLetterSet.add(c);
		}
		//marking correct letters
		for (int i = 0; i < attempt.length; i++) {
			if (answerArr[i] == attempt[i]) {
				grades[i] = LetterGrade.CORRECT;
				keyStateMap.replace(attempt[i], KeyState.LIT);
			}
			else if (answerLetterSet.contains(attempt[i])) {
				grades[i] = LetterGrade.RIGHT_LETTER;
				keyStateMap.replace(attempt[i], KeyState.LIT);
			}
			else {
				grades[i] = LetterGrade.WRONG;
				keyStateMap.replace(attempt[i], KeyState.DARK);
			}
		}
		return grades;
	}

	public static String getWord() {
		if (allWords.size() == 0) {
			loadAllWords();
		}
		Random random = new Random();
		return allWords.get((int) (random.nextDouble() * (allWords.size() + 1)));
	}

	public static String getWord (int length) {
		//load from file if length is within range
		if (length < SHORTEST_LENGTH || length > LONGEST_LENGTH) {
			throw new RuntimeException("Word length must be between " + SHORTEST_LENGTH + " and " + LONGEST_LENGTH);
		}

		loadWordsOfLength(length);

		//another check, since certain lengths don't have any words - i.e. 26 and 30
		if (wordsByLength.containsKey(length)) {
			List<String> wordList = wordsByLength.get(length);
			if (wordList.size() > 0) {
				Random random = new Random();
				return wordList.get((int) (random.nextDouble() * (wordList.size() + 1)));
			}
		}
		throw new RuntimeException("No words in dictionary of length " + length);
	}
}
