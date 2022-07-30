// Anna F, Ulee K, Iris T, Meira C
// CS 3 Summer 2022-2023
// Final Project: Language Chatbot
// 7/25/22
// A class to represent a noun
// Noun

public class Noun {
	
	public String word = "";
	
	/**
	 * A constructor for a noun object.
	 * 
	 * @param word a word that is a noun
	 */
	public Noun(String word) {
		this.word = word;
	}
	
	/**
	 * Gets the noun.
	 * 
	 * @return the noun
	 */
	public String getWord() {
		return this.word;
	}
	
	/**
	 * Sets the noun.
	 * 
	 * @param word the noun to set
	 */
	public void setWord(String word) {
		this.word = word;
	}
	
}