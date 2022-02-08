package scripts;

import java.io.*;
import java.util.*;

/*
full dictionary: 367,520 words loaded in 1,003 ms
word length: 4, list size 7186
word length: 5, list size 15918
word length: 6, list size 29874
word length: 7, list size 41998
word length: 8, list size 51627
word length: 9, list size 53402
word length: 10, list size 45872
word length: 11, list size 37539
word length: 12, list size 29125
word length: 13, list size 20944
word length: 14, list size 14149
word length: 15, list size 8846
word length: 16, list size 5182
word length: 17, list size 2967
word length: 18, list size 1471
word length: 19, list size 760
word length: 20, list size 359
word length: 21, list size 168
word length: 22, list size 74
word length: 23, list size 31
word length: 24, list size 12
word length: 25, list size 8
word length: 27, list size 3
word length: 28, list size 2
word length: 29, list size 2
word length: 31, list size 1
 */
public class DictionaryWrangler {
	public static final List<String> allWords = new ArrayList<>();
	public static final Map<Integer, List<String>> wordsByLength = new HashMap<>();

	public static void main(String[] args) {
	}

	@SuppressWarnings({"unused", "SpellCheckingInspection"})
	private static void loadAllToMap() {
		long startTime = System.currentTimeMillis();
		File dictionary = new File("resources/fulldictionary.txt");
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
		} catch (
				FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("Run time for loading from text: " + (System.currentTimeMillis() - startTime) + " ms");

		for (Map.Entry<Integer, List<String>> entry : wordsByLength.entrySet()) {
			System.out.println("word length: " + entry.getKey() + ", list size " + entry.getValue().size());
		}
	}

	@SuppressWarnings("unused")
	private static void listsToFiles() {
		for (Map.Entry<Integer, List<String>> entry : wordsByLength.entrySet()) {
			File outfile = new File("resources/words" + entry.getKey() + ".dat");
			try(FileOutputStream fileOutputStream = new FileOutputStream(outfile, false);
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
				objectOutputStream.writeObject(entry.getValue());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Saved lists to disk.");
	}

	@SuppressWarnings("unused")
	private static void filesToLists() {

	}
}
