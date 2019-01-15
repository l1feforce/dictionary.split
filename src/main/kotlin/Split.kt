import java.io.File

enum class Positions {
    Null, IsWord, IsPartOfSpeech, IsWordsMeaning, Skip
}

fun split(input: String) {
    val dictionary = File(input)
    val listOfWords = mutableListOf<String>()
    val listOfWordsMeanings = mutableListOf<MutableList<String>>()
    var positions = Positions.IsWord
    var word = ""
    dictionary.forEachLine {
        when (positions) {
            Positions.Null -> {
                positions = Positions.IsWord
                return@forEachLine
            }
            Positions.IsWord -> word = it
            Positions.IsPartOfSpeech -> {
                if (it.trim() == "ж." || it.trim() == "м.") {
                    listOfWords.add(word)
                    listOfWordsMeanings.add(mutableListOf(""))
                } else {
                    positions = Positions.Skip
                }
            }
            Positions.IsWordsMeaning -> listOfWordsMeanings[listOfWords.size - 1].add(it.trim())
            Positions.Skip -> {
                positions = Positions.IsPartOfSpeech
                return@forEachLine
            }
        }
        positions = when (positions) {
            Positions.Null -> Positions.IsWord
            Positions.IsWord -> Positions.IsPartOfSpeech
            Positions.IsPartOfSpeech -> Positions.IsWordsMeaning
            Positions.IsWordsMeaning -> if (it.isBlank()) Positions.Null else Positions.IsWordsMeaning
            Positions.Skip -> if (it.isBlank()) Positions.IsWord else Positions.Skip
        }
    }

    println(listOfWords[1])
    println(listOfWordsMeanings.size)

    listOfWords.forEachIndexed { index, string ->
        File("input/output.txt").bufferedWriter().write("$string - ")
        listOfWordsMeanings[index].forEach {
            File("input/output.txt").bufferedWriter().write("$it, ")
        }
        File("input/output.txt").bufferedWriter().newLine()
    }
}

fun main(args: Array<String>) {
    split("input/efremova.txt")
}