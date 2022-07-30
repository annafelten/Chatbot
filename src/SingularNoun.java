
public class SingularNoun extends Noun{

	/**
	 * A constructor for a singular noun.
	 * 
	 * @param word a word that is a singular noun
	 */
	public SingularNoun(String word) {
		super(word);
	}
	
	/**
	 * Gets the singular noun.
	 * 
	 * @return the singular noun
	 */
	public String getWord() {
		return "the " + this.word;
	}

}