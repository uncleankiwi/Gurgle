package logic;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Gurgle {
	public static final List<String> allWords = new ArrayList<>();
	public static final Map<Integer, List<String>> wordsByLength = new HashMap<>();

	//load only words longer than 4 characters
	public static void loadWords() {
		File dictionary = new File("resources/dictionary.csv");
		if (!dictionary.exists()) {
			throw new RuntimeException("Dictionary does not exist");
		}
		try(Scanner scanner = new Scanner(dictionary)) {
			while (scanner.hasNext()) {
				String word = scanner.nextLine();
				if (word.length() >= 4) {
					allWords.add(word);
					if (!wordsByLength.containsKey(word.length())) {
						wordsByLength.put(word.length(), new ArrayList<>());
					}
					wordsByLength.get(word.length()).add(word);
				}
			}
			System.out.println(allWords.size() + " words loaded to dictionary.");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static LetterGrade[] grade(char[] attempt, String answer) {
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
			}
			else if (answerLetterSet.contains(attempt[i])) {
				grades[i] = LetterGrade.RIGHT_LETTER;
			}
			else {
				grades[i] = LetterGrade.WRONG;
			}
		}
		return grades;
	}

	public static String getWord() {
		Random random = new Random();
		return allWords.get((int) (random.nextDouble() * (allWords.size() + 1)));
	}

	public static String getWord (int length) {
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
