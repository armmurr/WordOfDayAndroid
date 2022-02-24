package com.otototor.wordofday

class GameState ( gameStatus:Int,  enteredWords: MutableList<EnteredWord>) {
    var gameStatus = gameStatus
        private set
    var enteredWords = enteredWords
        private set
    private val words = Words().WordsLits
    var isWin:Boolean = false
    var mysteryWord = ""
        private set
    init {
        newGame()
    }
    fun newGame () {
        enteredWords = mutableListOf()
        generateMysteryWord()
        isWin = false
        gameStatus = GameStatus.NG
    }
    fun nextState(word: EnteredWord) {
        isWin = word.word.lowercase() == mysteryWord
        when {
            gameStatus == GameStatus.W6 -> {
                newGame()
            }
            else -> {
                gameStatus++
                enteredWords.add(word)
            }
        }

    }

    private fun generateMysteryWord() {
        var word = ""
        var i =  (0 until words.count()).random()
        word = words[i]
        mysteryWord = word
    }

    fun resumeGame(wordSet: MutableSet<String>, gameStatus: Int, mysteryWord: String){
        this.gameStatus = gameStatus
        this.mysteryWord = mysteryWord
        this.enteredWords = mutableListOf()
        if (wordSet != null) {
            for (word in wordSet){
               enteredWords.add(EnteredWord(word,mysteryWord))
            }
        }

    }
}