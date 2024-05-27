package tp.anagraminteractive

import org.example.service.AnagramService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AnagramInteractiveApplication

val anagramService = AnagramService()

fun main(args: Array<String>) {
    runApplication<AnagramInteractiveApplication>(*args)

    println(
        "Hello. This Program provides 2 functions. Function 1 checks if two words are anagrams" +
                " and stores if that the case. Function 2 returns previously searched anagrams of a word. "
    )
    while (true) {
        checkFunction()
    }
}

private fun checkFunction() {

    println("Do you want to call the first or the second function? Please Enter 1 or 2.")
    val func = readLine()

    if (func == "1") {
        println("Enter the first word to be checked.")
        val word1: String = readln()
        println("Enter the second word to be checked.")
        val word2: String = readln()


        println(anagramService.anagramCheckerStoreAtMatch(word1, word2))
    } else if (func == "2") {

        println("Enter the word, you would like to find the previously stored anagrams for.")
        val word: String = readln()
        println(anagramService.findStoredAnagramsOfPreviouslySearched(word))
    }

}
