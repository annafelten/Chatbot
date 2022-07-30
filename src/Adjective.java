// Anna F, Ulee K, Iris T, Meira C
// CS 3 Summer 2022-2023
// Final Project: Language Chatbot
// 7/25/22
// A class to represent an adjective
// Adjective

public class Adjective {
	
	private String word = "";
	
	/**
	 * A constructor for an adjective.
	 * 
	 * @param word the word that is an adjective
	 */
	public Adjective(String word) {
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
	 * @param word the word to set
	 */
	public void setWord(String word) {
		this.word = word;
	}
	
}