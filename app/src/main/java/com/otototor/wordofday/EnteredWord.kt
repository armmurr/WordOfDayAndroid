package com.otototor.wordofday

class EnteredWord (val word: String, val mysteryWord: String ) {
    var containsLetters: HashMap<Char,Int> = hashMapOf()
        private set
    var matchLetters: MutableList<Letters> = mutableListOf()
        private set

    init {
        findLetterInMysteryWord(word)
    }

    private fun  findLetterInMysteryWord(userWord:String)
    {

        var i = 0
        for (ch in mysteryWord) {
            if (userWord.contains(ch, true)) {
                val count = containsLetters[ch]
                if (count != null) {
                    containsLetters[ch] = count + 1
                } else
                    containsLetters[ch] = 1

            }
            matchLetters.add(i, Letters(userWord[i],userWord[i] == ch))

            i++
        }

    }

    class Letters(val key: Char, val value: Boolean)

}