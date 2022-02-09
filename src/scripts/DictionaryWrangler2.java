package scripts;

import java.io.*;
import java.util.*;

/*
Raw file: a list containing
	SomeWord FrequencyNumber
Things to do:
1. for every line, split the input
2. take the first word, convert to lowercase
3. ignore it if it contains non-alphabetical characters
4. ignore if length is less than 4
5. write to file
 */
public class DictionaryWrangler2 {
	public static final List<String> allWords = new ArrayList<>();

	public static void main(String[] args) {
		freqToList();
		listToCSV();
	}

	@SuppressWarnings({"unused", "SpellCheckingInspection"})
	private static void freqToList() {
		long startTime = System.currentTimeMillis();
		File dictionary = new File("resources/freqmap.txt");
		if (!dictionary.exists()) {
			throw new RuntimeException("Dictionary does not exist");
		}
		try(Scanner scanner = new Scanner(dictionary)) {
			int droppedWords = 0;
			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				String[] words = line.split(" ");		//split
				words[0] = words[0].toLowerCase();			//lowercase

				boolean containsNonAlpha = false;
				for (char c : words[0].toCharArray()) {
					if (c < 97 || c > 122) {
						containsNonAlpha = true;			//ignore non-alphabetical words
						break;
					}
				}

				if (words[0].length() >= 4 && !containsNonAlpha) {	//ignore <4 length
					allWords.add(words[0]);
				}
				else {
					droppedWords++;
				}
			}
			System.out.println(allWords.size() + " words loaded to dictionary.");
			System.out.println(droppedWords + " words excluded.");
		} catch (
				FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("Run time for loading from freq list: " + (System.currentTimeMillis() - startTime) + " ms");
	}

	//DataOutputStream's writeUTF(string) gives funny output
	@SuppressWarnings("unused")
	private static void listToCSV() {
		File csv = new File("resources/dictionary.csv");

		try {
			try(PrintWriter output = new PrintWriter(csv)){
				System.out.println("Saving to csv...");
				for (String s : allWords) {
					output.println(s);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("Saved list to csv.");
	}
}
