import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

/**
 * A basic word-level translator program implemented in Java.
 * This class uses a static dictionary (HashMap) to translate words 
 * from one language (e.g., English) to another (e.g., Spanish).
 * * It is platform-independent as it relies solely on standard Java libraries.
 */
public class WordLevelTranslator {

    // The core dictionary mapping English words (keys) to Spanish translations (values).
    private final Map<String, String> dictionary;

    /**
     * Constructor initializes the translator and populates the dictionary.
     */
    public WordLevelTranslator() {
        dictionary = new HashMap<>();
        // Populate the dictionary with a few example word pairs (English -> Spanish)
        initializeDictionary();
    }

    /**
     * Fills the internal dictionary with key-value pairs.
     * All keys are stored in lowercase for easier lookup.
     */
    private void initializeDictionary() {
        // Basic words
        dictionary.put("hello", "hola");
        dictionary.put("world", "mundo");
        dictionary.put("i", "yo");
        dictionary.put("am", "soy");
        dictionary.put("a", "un");
        dictionary.put("cat", "gato");
        dictionary.put("dog", "perro");
        dictionary.put("house", "casa");
        dictionary.put("the", "el");
        dictionary.put("my", "mi");
        dictionary.put("name", "nombre");
        dictionary.put("is", "es");
        dictionary.put("beautiful", "hermoso");
        dictionary.put("food", "comida");
        // Verb examples
        dictionary.put("eat", "comer");
        dictionary.put("love", "amar");
        dictionary.put("run", "correr");
        
        System.out.println("Dictionary loaded with " + dictionary.size() + " entries.");
    }

    /**
     * Translates an entire sentence word by word using the dictionary.
     * It attempts to preserve capitalization of the first letter and any trailing punctuation.
     *
     * @param sentence The sentence to be translated.
     * @return The translated sentence string.
     */
    public String translateSentence(String sentence) {
        if (sentence == null || sentence.trim().isEmpty()) {
            return "";
        }

        // Split the sentence by spaces. We use a regex that splits but keeps non-word characters for manual handling.
        // A simpler approach is to split by space and then process each "token".
        String[] words = sentence.trim().split("\\s+");
        StringBuilder translatedSentence = new StringBuilder();

        for (String token : words) {
            // Trim and clean the token to find the base word
            String cleanedWord = token.toLowerCase(Locale.ENGLISH);
            String punctuation = "";
            boolean wasCapitalized = false;

            // 1. Separate Punctuation (Handles single trailing punctuation like . , ! ?)
            int lastCharIndex = cleanedWord.length() - 1;
            if (lastCharIndex >= 0 && !Character.isLetterOrDigit(cleanedWord.charAt(lastCharIndex))) {
                punctuation = cleanedWord.substring(lastCharIndex);
                cleanedWord = cleanedWord.substring(0, lastCharIndex);
            }

            // 2. Check for initial capitalization on the original token
            if (token.length() > 0 && Character.isUpperCase(token.charAt(0))) {
                wasCapitalized = true;
            }
            
            // 3. Look up the translation
            // Use the cleaned, lowercase word for the lookup
            String translation = dictionary.get(cleanedWord);

            if (translation == null) {
                // If the word is not in the dictionary, keep the original word
                translation = cleanedWord; 
                // Restore original capitalization if it was capitalized and untranslated
                if (wasCapitalized) {
                    translation = capitalize(translation);
                }
                System.out.println("-> Warning: Word '" + cleanedWord + "' not found in dictionary.");
            } else {
                // 4. Apply capitalization if the original word had it (for titles/start of sentence)
                if (wasCapitalized) {
                    translation = capitalize(translation);
                }
            }

            // Append the translated word, the punctuation, and a space
            translatedSentence.append(translation).append(punctuation).append(" ");
        }

        // Return the final string, removing the trailing space
        return translatedSentence.toString().trim();
    }
    
    /**
     * Helper method to capitalize the first letter of a string.
     * @param word The string to capitalize.
     * @return The capitalized string.
     */
    private String capitalize(String word) {
        if (word == null || word.isEmpty()) {
            return word;
        }
        return word.substring(0, 1).toUpperCase(Locale.ENGLISH) + word.substring(1);
    }

    /**
     * Main method to run the translator application.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        // Create an instance of the translator
        WordLevelTranslator translator = new WordLevelTranslator();

        // Use Scanner to get user input from the console
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("\n--- Simple Word-Level Translator (English -> Spanish) ---");
            System.out.println("Type 'exit' to quit the program.");
            
            // Main application loop
            while (true) {
                System.out.print("\nEnter English sentence: ");
                String input = scanner.nextLine();

                if (input.equalsIgnoreCase("exit")) {
                    System.out.println("Exiting translator. Goodbye!");
                    break;
                }

                // Translate the input sentence
                String translatedText = translator.translateSentence(input);

                // Display the result
                System.out.println("Translated Spanish sentence: " + translatedText);
            }
        } catch (Exception e) {
            // Catch any unexpected exceptions and print them
            System.err.println("An error occurred during execution: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
