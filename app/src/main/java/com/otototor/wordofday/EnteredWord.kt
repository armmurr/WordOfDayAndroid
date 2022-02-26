package com.otototor.wordofday

class EnteredWord (word: String, mysteryWord: String ) {
    private var _word = word
    private var _mysteryWord = mysteryWord;
    private var _containsLetters: HashMap<Char,Int> = hashMapOf()
    var matchedLetters: MutableList<MatchedLetter> = mutableListOf()
        private set
    var isWinWord = false
        private set

    init {
        if (word.lowercase() == mysteryWord) {
            isWinWord = true
        }
        findLetterInMysteryWord(_word)
        calculateMatchedLetters(_containsLetters,matchedLetters)
    }

    override fun toString (): String {
        return _word
    }

    private fun findLetterInMysteryWord(userWord:String)
    {
        var i = 0
        for (ch in _mysteryWord) {
            if (userWord.contains(ch, true)) {
                val count = _containsLetters[ch]
                if (count != null) {
                    _containsLetters[ch] = count + 1
                } else {
                    _containsLetters[ch] = 1
                }
            }
            matchedLetters.add(i, MatchedLetter(userWord[i], userWord[i] == ch, null))
            i++
        }
    }

    private fun calculateMatchedLetters (containsLetters:HashMap<Char,Int>, matchedLetters: List<MatchedLetter>){
        var i = 0
        for (letter in matchedLetters) {
            val count = containsLetters[letter.char]
            if (count != null && count > 0) {
                if (matchedLetters[i].isMatched) {
                    matchedLetters[i].color = matchColor
                    containsLetters[letter.char] = count - 1
                }
            }
            i++
        }
        i=0
        for (letter in matchedLetters) {
            val count = containsLetters[letter.char]
            if (count != null && count > 0) {
                if (!matchedLetters[i].isMatched) {
                    matchedLetters[i].color = containsColor
                    containsLetters[letter.char] = count - 1
                }
            }
            i++
        }
    }

    class MatchedLetter(val char: Char, val isMatched: Boolean, var color: Int?)

    companion object {
        const val matchColor = R.color.green
        const val containsColor = R.color.orange
    }

}