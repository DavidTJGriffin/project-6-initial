package com.example.aggregator.service;

import com.example.aggregator.client.AggregatorRestClient;
import com.example.aggregator.model.Entry;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class AggregatorService {

    private AggregatorRestClient restClient;

    public AggregatorService(AggregatorRestClient restClient) {
        this.restClient = restClient;
    }

    public Entry getDefinitionFor(String word) {
        return restClient.getDefinitionFor(word);
    }

    public List<Entry> getAllPalindromes() {

        List<Entry> candidates = new ArrayList<>();

        // Iterate from a to z using a char array
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();

        for (char letter : alphabet) {

            String c = Character.toString(letter);

            // get words starting and ending with character
            List<Entry> startsWith = restClient.getWordsStartingWith(c);
            List<Entry> endsWith = restClient.getWordsEndingWith(c);

            // keep entries that exist in both lists
            List<Entry> startsAndEndsWith = new ArrayList<>(startsWith);
            startsAndEndsWith.retainAll(endsWith);

            // store list with existing entries
            candidates.addAll(startsAndEndsWith);
        }

        // test each entry for palindrome
        List<Entry> palindromes = new ArrayList<>();

        for (Entry entry : candidates) {
            String word = entry.getWord();
            String reverse = new StringBuilder(word).reverse().toString();
            if (word.equals(reverse)) {
                palindromes.add(entry);
            }
        }

        // sort and return
        Collections.sort(palindromes);

        return palindromes;
    }

    public List<Entry> getWordsThatContainSuccessiveLettersAndStartsWith(String chars) {

        List<Entry> wordsThatStartWith = restClient.getWordsStartingWith(chars);
        List<Entry> wordsThatContainSuccessiveLetters = restClient.getWordsThatContainConsecutiveLetters();

        List<Entry> common = new ArrayList<>(wordsThatStartWith);
        common.retainAll(wordsThatContainSuccessiveLetters);

        return common;
    }

    public List<Entry> getWordsThatContainSuccessiveLettersAndContains(String chars) {

        List<Entry> wordsThatContain = restClient.getWordsThatContain(chars);
        List<Entry> wordsThatContainSuccessiveLetters = restClient.getWordsThatContainConsecutiveLetters();

        List<Entry> common = new ArrayList<>(wordsThatContain);
        common.retainAll(wordsThatContainSuccessiveLetters);

        return common;
    }
}
