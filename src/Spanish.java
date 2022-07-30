// Anna F, Ulee K, Iris T, Meira C
// CS 3 Summer 2022-2023
// Final Project: Language Chatbot
// 7/25/22
// An intelligent chatbot that converses with a Spanish learner
// Language processing API: Apache OpenNPL - https://opennlp.apache.org/
// Spanish Parts of Speech ML Model - https://cavorite.com/labs/nlp/opennlp-models-es/
// Spanish

import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;

import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.lemmatizer.LemmatizerME;
import opennlp.tools.lemmatizer.LemmatizerModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.util.InvalidFormatException;

public class Spanish {

	/**
	 * The difficulty of the conversation on a scale of 0-1.
	 */
	protected static double chatDifficulty;

	/**
	 * The current category of the bot's response.
	 */
	private static String responseCateg;

	/**
	 * A TreeMap containing all words in the bot's dataset with their corresponding
	 * difficulty value (0-1).
	 */
	protected static TreeMap<String, Double> wordsRanked;

	/**
	 * The recognized user inputs. If a user inputs one of these strings, the
	 * difficulty will not be increased.
	 */
	protected static TreeMap<String, String> recognizedUserInput;

	/**
	 * A boolean stating whether a current conversation is occuring. The
	 * conversation ends if the user enters a phrase such as "adios".
	 */
	private static boolean currentConvo;

	/**
	 * The model of the bot that recognizes conversation topics in Spanish based on
	 * user input.
	 */
	private DoccatModel model;

	/**
	 * A constructor for a Spanish bot.
	 *
	 * @param answerCategories all possible categories for user input and the bot's
	 *                         response
	 * @param model            the pre-trained model of the chatbot
	 * @throws IOException           if input or output exception occurs
	 * @throws FileNotFoundException if file is not found
	 * @throws InterruptedException  if timer is interrupted
	 */
	public Spanish(ArrayList<String> answerCategories, DoccatModel model)
			throws FileNotFoundException, IOException, InterruptedException {
		wordsRanked = new TreeMap<>();
		chatDifficulty = 0;
		responseCateg = "";
		recognizedUserInput = new TreeMap<>();
		currentConvo = true;
		this.model = model;
	}

	/**
	 * Runs the language bot by starting a conversation and responding appropriately
	 * to the user.
	 *
	 * @throws FileNotFoundException if file is not found
	 * @throws IOException           if an input or output exception occurs
	 * @throws InterruptedException  if the timer that counts seconds until the
	 *                               program quits is interrupted
	 */
	public void runBot() throws FileNotFoundException, IOException, InterruptedException {

		Scanner masterListReader = new Scanner(new File("masterwordlist"));
		Scanner userPhrasesReader = new Scanner(new File("categorizer.txt"));
		wordsRanked = Difficulty.readFileData(masterListReader);
		recognizedUserInput = Difficulty.readRecognizedInput(userPhrasesReader);
		System.out.println("SpanishGui created");

		// getting user input
		while (currentConvo) {

			if (SpanishGui.isMessageSent()) {
				SpanishGui.messageSent = false;

				String[] sentences = detectSentences(SpanishGui.getUserInput());

				String response = "";

				SpanishGui.updateStatus("Pablo está escribiendo un mensaje...");
				System.out.println("Pablo está escribiendo un mensaje...");

				for (String s : sentences) {
					response += getResponse(s, model);

					// currentConvo is updated through the method call getResponse()
					if (!currentConvo) {
						// ends the conversation immediately after printing Pablo's closing words
						SpanishGui.pabloAnswer(response);
						SpanishGui.updateStatus("");

						// waits 5 seconds before quitting the program
						TimeUnit.SECONDS.sleep(5);
						System.exit(0);

					}

				}

				SpanishGui.pabloAnswer(response);

			}

			SpanishGui.updateStatus("");

		}

	}

	/**
	 * Tokenizes a user inputted-sentence, analyzes the parts of speech, finds the
	 * lemma for each token, and detects the category of the user's input. Finds an
	 * appropriate response based on the category.
	 *
	 * @param s     one sentence that the user inputted
	 * @param model the machine learning model being used
	 * @return an appropriate response to the user
	 * @throws FileNotFoundException if file is not found
	 * @throws IOException           if input or output exception occured
	 */
	public static String getResponse(String s, DoccatModel model)
			throws FileNotFoundException, IOException {

		String[] tokens = s.toLowerCase().split(" ");

		// Splits a sentence into tokens (words and punctuation)
		String[] tokenizedSentence = tokenizeSentence(s);

		// Detects the part of speech of each token
		String[] partsOfSpeech = detectPOSTags(tokenizedSentence);

		// Finds the lemma of each token (for example, "break" is the lemma of broke,
		// break, broken...)
		String[] lemmas = lemmatizeTokens(tokenizedSentence, partsOfSpeech);

		// Determines which category its response should fall under
		responseCateg = detectCategory(model, lemmas);

		// analyzes the complexity of the user's input using the master word list
		// to see if the difficulty of the conversation should be increased
		Difficulty.updateDifficulty(tokens, responseCateg);

		// Returns an appropriate response
		return " " + createAnswers(responseCateg);

	}

	/**
	 * Define all possible answers for each phrase category.
	 *
	 * @param category an appropriate response to the user depending on the
	 *                 difficulty of the conversation
	 * @return an appropriate answer
	 * @throws IOException if input or output exception occurs
	 */
	public static String createAnswers(String category) throws IOException {
		File categoryFile = new File("rankedcategories");

		Scanner categoryReader = new Scanner(categoryFile);

		TreeMap<String, Double> possibleAnswerList = new TreeMap<>();

		boolean keepSearching = true;
		while (keepSearching && categoryReader.hasNextLine()) {

			// Creating the file that contains the possible answers for a given category
			// File names are in the format "category.txt"

			String line = categoryReader.nextLine();
			String[] lineArray = line.split(";");

			if (lineArray[0].contains(category)) {
				keepSearching = false;

				for (int j = 1; j < lineArray.length; j++) {
					String[] valAndRanking = lineArray[j].split("#");

					String phrase = valAndRanking[0];
					if (phrase != "]" && phrase != "[" && phrase != ",") {
						double difficulty = Double.valueOf(valAndRanking[1]);
						possibleAnswerList.put(phrase, difficulty);
					}

				}

			}

		}

		categoryReader.close();
		String goodAnswer = "";

		// the possible answer list is usually generated without problems (depends on
		// user input)
		if (!possibleAnswerList.isEmpty()) {
			goodAnswer = generateBestAnswer(possibleAnswerList);
		}

		// if the user is trying to say goodbye
		else if (category.contains("response")) {
			String[] goodbyes = { "Adios", "Hasta luego, entonces.", "Chao.",
					"Buenas noches, descansa.", "Hasta pronto.",
					"Gracias y que tengas un buen día.", "¡Besos!" };
			int indexAnswer = new Random().nextInt(goodbyes.length);
			currentConvo = false;
			goodAnswer = goodbyes[indexAnswer];
		}

		// if the category is not recognized, then Pablo chooses a random answer
		// expressing uncertainty
		else {
			String[] unsureAnswers = { "No sé", "No lo comprendo", "¿Qué?", "¿Por qué?" };
			int indexAnswer = new Random().nextInt(unsureAnswers.length);
			goodAnswer = unsureAnswers[indexAnswer];

		}
		return goodAnswer;
	}

	/**
	 * Gets a random answer for each possible response category based on the bot's
	 * current vocabulary.
	 *
	 * @param possibleAnswerList the TreeMap of possible answers for a given
	 *                           category
	 * @return a randomly chosen response from the file
	 * @throws IOException           if input or output exception occurs
	 * @throws FileNotFoundException if file is not found
	 */
	public static String generateBestAnswer(TreeMap<String, Double> possibleAnswerList)
			throws FileNotFoundException, IOException {

		// the difference between the chat difficulty and the phrase difficulty can be
		// no greater than 1
		// because both are ranked on a scale of 0-1
		double difference = 1;

		Entry<String, Double> closestMatch = null;

		for (Entry<String, Double> entry : possibleAnswerList.entrySet()) {

			// finds the entry with the closest difficulty to the current difficulty of the
			// conversation
			if (entry.getValue() - chatDifficulty <= difference) {
				difference = entry.getValue() - chatDifficulty;
				closestMatch = entry;
			}

		}

		ArrayList<String> feasibleAnswers = new ArrayList<>();
		feasibleAnswers.add(closestMatch.getKey());
		String term = closestMatch.getKey();
		possibleAnswerList.remove(closestMatch.getKey()); // removes entry so that the floor and
															// ceiling keys are unique

		Entry<String, Double> oneLower = null, oneHigher = null;

		// the response with the greatest difficulty ranking less than or equal to the
		// best match
		if (possibleAnswerList.firstKey() != closestMatch.getKey()) { // avoiding null pointer
																		// exception
			oneLower = (possibleAnswerList.floorEntry(term));
		}
		// the response with the lowest difficulty ranking greater than or equal to the
		// best match
		if (possibleAnswerList.lastKey() != closestMatch.getKey()) { // avoiding null pointer
																		// exception
			oneHigher = (possibleAnswerList.ceilingEntry(term));
		}

		if (oneLower != null) {
			feasibleAnswers.add(oneLower.getKey());
		}
		if (oneHigher != null) {
			feasibleAnswers.add(oneHigher.getKey());
		}

		int indexAnswer = new Random().nextInt(feasibleAnswers.size());

		possibleAnswerList.put(closestMatch.getKey(), closestMatch.getValue());

		return feasibleAnswers.get(indexAnswer);
	}

	/**
	 * Splits the user input into sentences.
	 *
	 * @param userInput the user input to the bot
	 * @return an array of sentences that the user inputted
	 * @throws IOException           if input or output exception occurs
	 * @throws FileNotFoundException if file is not found
	 */
	public static String[] detectSentences(String userInput)
			throws IOException, FileNotFoundException {

		InputStream modelIn = new FileInputStream("en-sent.bin");
		SentenceDetectorME myCategorizer = new SentenceDetectorME(new SentenceModel(modelIn));
		String[] sentences = myCategorizer.sentDetect(userInput);
		return sentences;
	}

	/**
	 * Splits a sentence into tokens, such as individual words and punctuation
	 * marks.
	 *
	 * @param sentence the sentence to be split
	 * @return a String array of the tokens in the sentence
	 * @throws FileNotFoundException if file is not found
	 * @throws IOException           if input or output exception occurs
	 */
	public static String[] tokenizeSentence(String sentence)
			throws FileNotFoundException, IOException {

		// Using basic tokenizer, but a trained model could also be implemented
		SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;

		String[] tokens = tokenizer.tokenize(sentence);

		return tokens;

	}

	/**
	 * Detects the parts of speech (noun, adjective, etc.) of each token using a
	 * Spanish POS model.
	 *
	 * @param tokens the String array of tokens that make up a sentence
	 * @return the tokens categorized as parts of speech
	 * @throws IOException if input or output exception occurs
	 */
	public static String[] detectPOSTags(String[] tokens) throws IOException {
		InputStream modelIn = new FileInputStream("opennlp-es-pos-perceptron-pos-es.model");

		POSTaggerME myCategorizer = new POSTaggerME(new POSModel(modelIn));

		String[] posTokens = myCategorizer.tag(tokens);
		
		return posTokens;

	}

	/**
	 * Finds the lemma of each token. For example, the lemma of "broken" and "broke"
	 * in English is "break".
	 *
	 * @param tokens  the array of tokens in the sentence
	 * @param posTags the array of parts of speech for each token in the sentence
	 * @return an array of lemmatized tokens
	 * @throws InvalidFormatException if format is invalid
	 * @throws IOException            if input or output exception occurs
	 */
	public static String[] lemmatizeTokens(String[] tokens, String[] posTags)
			throws InvalidFormatException, IOException {

		InputStream modelIn = new FileInputStream("en-lemmatizer.bin");
		LemmatizerME myCategorizer = new LemmatizerME(new LemmatizerModel(modelIn));
		String[] lemmaTokens = myCategorizer.lemmatize(tokens, posTags);

		return lemmaTokens;

	}

	/**
	 * Detects which category the user's input is most likely to fall under, such as
	 * a greeting or a conversation closer.
	 *
	 * @param model       the machine learning model being user
	 * @param finalTokens the fully analyzed, lemmatized tokens
	 * @throws IOException if input or output exception occurs
	 * @return the category of the user's input that the model will use to determine
	 *         a response
	 */
	private static String detectCategory(DoccatModel model, String[] finalTokens)
			throws IOException {

		DocumentCategorizerME myCategorizer = new DocumentCategorizerME(model);

		// find most likely category
		double[] probabilitiesOfOutcomes = myCategorizer.categorize(finalTokens);
		String category = myCategorizer.getBestCategory(probabilitiesOfOutcomes);

		return category;

	}

	/**
	 * @return the chatDifficulty
	 */
	public static double getChatDifficulty() {
		return chatDifficulty;
	}

	/**
	 * @param chatDifficulty the chatDifficulty to set
	 */
	public static void setChatDifficulty(double chatDifficulty) {
		Spanish.chatDifficulty = chatDifficulty;
	}

}