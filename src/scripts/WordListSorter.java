package scripts;

import java.io.*;
import java.util.*;

/*
dictionary.csv:
	- contains a lot of non-english words but it ordered
wordsN.dat:
	- contains pretty much the words that we want to use, but it's unordered
Things to do:
	1. for every word in dictionary.csv, create a map of word, index
	2. use a Comparator that sorts the words in our wordsN.dat by that index
		- if a word isn't in the index, the other word goes first
		- if both words aren't in the index, both words are sorted alphabetically
	3. overwrite wordsN.dat
 */
public class WordListSorter {
	private static final Map<String, Integer> wordMap = new HashMap<>();
	public static final Map<Integer, List<String>> wordsByLength = new HashMap<>();

	public static void main(String[] args) {
		dictionaryToIndexMap();
		filesToSortedLists();
		listsToFiles();
	}

	@SuppressWarnings("unused")
	private static void dictionaryToIndexMap() {
		long startTime = System.currentTimeMillis();
		File dictionary = new File("resources/dictionary.csv");
		if (!dictionary.exists()) {
			throw new RuntimeException("Dictionary does not exist");
		}
		int index = 0;
		try(Scanner scanner = new Scanner(dictionary)) {
			while (scanner.hasNext()) {
				String word = scanner.nextLine();
				wordMap.put(word, index);
				index++;
			}
			System.out.println(wordMap.size() + " words loaded to word-index map.");
		} catch (
				FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("Run time for creating word-index map: " + (System.currentTimeMillis() - startTime) + " ms");
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

	//open wordsN.dat, load to file, then sort in respective Lists
	@SuppressWarnings({"unused", "unchecked"})
	private static void filesToSortedLists() {
		long startTime = System.currentTimeMillis();

		for (int i = 4; i <= 31; i++) {
			File inFile = new File("resources/words" + i + ".dat");
			if (!inFile.exists()) {
				continue;
			}
			try(FileInputStream fileInputStream = new FileInputStream(inFile);
				ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
				wordsByLength.put(i, (List<String>) objectInputStream.readObject());
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		System.out.println("Run time for loading from text: " + (System.currentTimeMillis() - startTime) + " ms");

		for (Map.Entry<Integer, List<String>> entry : wordsByLength.entrySet()) {
			System.out.println("word length: " + entry.getKey() + ", list size " + entry.getValue().size());
			System.out.println("Unsorted:");
			printHeadAndTail(entry.getValue());
			entry.getValue().sort(new WordComparator(wordMap));
			System.out.println("Sorted:");
			printHeadAndTail(entry.getValue());
		}
	}

	private static void printHeadAndTail(List<String> list) {
		for (int i = 0; i < 5 && i < list.size(); i++) {
			System.out.println(i + ":" + list.get(i));
		}

		for (int i = list.size() - 5; i < list.size(); i++) {
			if (i >= 0) System.out.println(i + ":" + list.get(i));
		}
	}

	private static class WordComparator implements Comparator<String> {
		private final Map<String, Integer> wordMap;

		WordComparator(Map<String, Integer> map) {
			this.wordMap = map;
		}

		@Override
		public int compare(String o1, String o2) {
			if (!wordMap.containsKey(o1) && !wordMap.containsKey(o2)) {
				return o1.compareTo(o2);
			}
			else if (!wordMap.containsKey(o1)) {
				return 1;
			}
			else if (!wordMap.containsKey(o2)) {
				return -1;
			}
			else {
				return Integer.compare(wordMap.get(o1), wordMap.get(o2));
			}
		}
	}
}
