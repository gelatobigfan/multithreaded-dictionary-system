/**
 * [Author:Junyi Pan] [Student ID:1242599] 
 */

package server;

import java.util.HashMap;
import java.util.Map;

public class DictionaryLogic {
    private static final Map<String, String> dictionary = new HashMap<>();

    public static String addWord(String word, String meaning) {
        if (dictionary.containsKey(word)) {
            return "❌ Word already exists, try again";
        }
        dictionary.put(word, meaning);
        return word + " added successfully!🎉";
    }

    public static String queryWord(String word) {
        if (!dictionary.containsKey(word)) {
            return "❌ Word not found.";
        }
        return word + " → " + dictionary.get(word);
    }

    public static String deleteWord(String word) {
        if (!dictionary.containsKey(word)) {
            return "❌ Word not found.";
        }
        dictionary.remove(word);
        return "️ Word deleted.";
    }

    public static String updateWord(String word, String newMeaning, String oldMeaning) {
        if (!dictionary.containsKey(word)) {
            return "❌ Word not found";
        }

        String[] meanings = dictionary.get(word).split(";");
        boolean found = false;
        for (int i = 0; i < meanings.length; i++) {
            if (meanings[i].trim().equals(oldMeaning)) {
                meanings[i] = newMeaning;
                found = true;
                break;
            }
        }

        if (!found) {
            return "❌ Old meaning not found";
        }

        dictionary.put(word, String.join(";", meanings));
        return "️ Meaning updated";
    }

    public static String addMeaning(String word, String newMeaning) {
        if (!dictionary.containsKey(word)) {
            return "❌ Word not found";
        }
        String meanings = dictionary.get(word);
        if (meanings.contains(newMeaning)) {
            return "❌ Meaning already exists";
        }
        dictionary.put(word, meanings + ";" + newMeaning);
        return "️ Meaning added successfully!🎉";
    }

    public static Map<String, String> getAll() {
        return new HashMap<>(dictionary);
    }
}