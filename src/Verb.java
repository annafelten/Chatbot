// Anna F, Ulee K, Iris T, Meira C
// CS 3 Summer 2022-2023
// Final Project: Language Chatbot
// 7/25/22
// A class to represent a verb
// Verb

public class Verb {
	
	private String word = "";
	
	/**
	 * A constructor for a singular verb.
	 * 
	 * @param word a word that is a verb
	 */
	public Verb(String word) {
		this.word = word;
	}
	
	/**
	 * Gets the word.
	 * 
	 * @return the word
	 */
	public String getWord() {
		return this.word;
	}
	
	/**
	 * Sets the word.
	 * 
	 * @param word the word to set
	 */
	public void setWord(String word) {
		this.word = word;
	}
}
