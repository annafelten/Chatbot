// Anna F, Ulee K, Iris T, Meira C
// CS 3 Summer 2022-2023
// Final Project: Language Chatbot
// 7/25/22
// A class to represent plural nouns
// PluralNouns

public class PluralNouns extends Noun {

	/**
	 * A constructor for a plural noun.
	 * 
	 * @param word a word that is a plural noun
	 */
	public PluralNouns(String word) {
		super(word);
	}

	/**
	 * Gets a word.
	 * 
	 * @return the word
	 */
	public String getWord() {
		return "the " + this.word;
	}
	
}