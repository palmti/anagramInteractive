package org.example.service

import org.springframework.stereotype.Service
import java.util.*


private const val ASCII_CHAR_NUMBER = 256

/**
 * Service to handle anagrams.
 *
 * English wiki definition: An anagram is a word or phrase formed
 * by rearranging the letters of a different word or phrase,
 * typically using all the original letters exactly once.
 *
 * Remark: Whitespaces can be added or removed
 * Example: "New York Times" = "monkeys write"
 *
 */
@Service
class AnagramService {
    private val sortedAnagramToAssociatedWords: MutableMap<String, LinkedHashSet<String>> = mutableMapOf()

    /**
     *    Straightforward implementation, O(NlogN) --> Fine for Small N, so fine for realistic application
     *    such as sentences or phrases.
     */
    fun anagramCheckerStoreAtMatch(word1: String, word2: String): Boolean {

        val orderedWord1 = prepareAndSort(word1)
        val orderedWord2 = prepareAndSort(word2)

        if (orderedWord1.length != orderedWord2.length) return false
        if (orderedWord1 == orderedWord2) {
            fillMemoryMap(orderedWord1, word1, word2)
            return true
        }
        return false
    }

    /**
     * Return associated set of anagrams. Only return Anagrams if input word has been previously used in check function.
     */
    fun findStoredAnagramsOfPreviouslySearched(word: String): List<String>? {
        return getAssociatedAnagrams(
            sortedAnagramToAssociatedWords[prepareAndSort(word)],
            word
        )
    }

    private fun getAssociatedAnagrams(
        allAssociatedAnagrams: LinkedHashSet<String>?,
        word: String
    ): List<String> {
        val associatedAnagramsWithoutInput: List<String>
        if (allAssociatedAnagrams != null) {
            if (allAssociatedAnagrams.contains(word)) associatedAnagramsWithoutInput =
                allAssociatedAnagrams.toList().minus(word) else return listOf()
            return associatedAnagramsWithoutInput
        } else return listOf()
    }


    private fun prepareAndSort(word: String): String {
        val charArray = prepareForAnagramCheck(word)
        Arrays.sort(charArray)
        return String(charArray)
    }

    private fun prepareForAnagramCheck(word: String) = word.filter { !it.isWhitespace() }.lowercase().toCharArray()

    private fun fillMemoryMap(orderedWord: String, word1: String, word2: String) =
        sortedAnagramToAssociatedWords.computeIfAbsent(
            orderedWord
        ) { linkedSetOf() }
            .addAll(listOf(word1, word2))

    /**
     * Alternative, disregarded Method
     * Faster Method for big Input, but less straight-forward and requiring more memory, 256 for ASCII rep
     * O(N), 2 For-Loops, one fixed at ASCII Code size (assuming ascii chars, can be reduced by ordering indices list)
     * -> slower for small words.
     *
     * Also, a book is technically not a phrase != english wiki definition.
     */
    private fun checkAnagramFastForLarge(word1: String, word2: String): Boolean {

        val count = IntArray(ASCII_CHAR_NUMBER)
        val countForKey = IntArray(ASCII_CHAR_NUMBER)
        val string1NoWhiteSpaces = prepareForAnagramCheck(word1)
        val string2NoWhiteSpaces = prepareForAnagramCheck(word2)

        val reducedLength = string1NoWhiteSpaces.size
        if (reducedLength != string2NoWhiteSpaces.size) return false

        for (i in 0..<reducedLength) {
            count[string1NoWhiteSpaces[i].code]++
            count[string2NoWhiteSpaces[i].code]--

            countForKey[string1NoWhiteSpaces[i].code]++
        }
        var orderedAnagram = ""
        for (i in 0..<ASCII_CHAR_NUMBER) {
            if (count[i] != 0) return false
            for (j in 0..<countForKey[i]) orderedAnagram += Char(i)
        }
        fillMemoryMap(orderedAnagram, word1, word2)
        return true
    }
}