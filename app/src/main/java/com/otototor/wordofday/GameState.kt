package com.otototor.wordofday

class GameState ( gameStatus:Int,  enteredWords: MutableList<EnteredWord>) {
    var gameStatus = gameStatus
        private set
    var enteredWords = enteredWords
        private set
    private val words = Words().WordsLits
    var isWin:Boolean = false
        private set
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
        isWin = word.isWinWord
        when (gameStatus) {
            GameStatus.W6 -> {
                newGame()
            }
            else -> {
                gameStatus++
                enteredWords.add(word)
            }
        }

    }

    private fun generateMysteryWord() {
        var word: String
        var i =  (0 until words.count()).random()
        word = words[i]
        mysteryWord = word
    }

    fun resumeGame(wordList: MutableList<String>, gameStatus: Int, mysteryWord: String, isWin:Boolean){
        this.gameStatus = gameStatus
        this.mysteryWord = mysteryWord
        this.enteredWords = mutableListOf()
        this.isWin = isWin
        if (wordList.count() > 0) {
            for (word in wordList){
               enteredWords.add(EnteredWord(word,mysteryWord))
            }
        }
    }
}