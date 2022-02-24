package com.otototor.wordofday

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText


class
MainActivity : AppCompatActivity() {
    private var mysteryWord = ""
    private lateinit var wordsViewAdapter: WordsViewAdapter
    private lateinit var newGameButton: Button
    private lateinit var okGameButton: Button
    private var gameState = GameState(GameStatus.NG, mutableListOf())
    private lateinit var textInputEditText: TextInputEditText
    private lateinit var words: List<String>
    var sharePref: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharePref = getSharedPreferences("GameState", Context.MODE_PRIVATE)
        newGameButton = findViewById(R.id.newGameBtn)
        okGameButton = findViewById(R.id.okButton)
        wordsViewAdapter = WordsViewAdapter(this@MainActivity)
        textInputEditText = findViewById(R.id.textInputEditText)
        textInputEditText.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN
            ) {
                processGame()
                true
            } else {
                false
            }
        }

        words = Words().WordsLits
        resumeGame()
        mysteryWord = gameState.mysteryWord
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun enterTheWord(view: View) {
        processGame()
    }

    fun newGame(view: View) {
        gameState.newGame()
        newGameButton.visibility = View.INVISIBLE
        okGameButton.isEnabled = true
        textInputEditText.isEnabled = true
        wordsViewAdapter.clearWords()
        mysteryWord = gameState.mysteryWord
        saveData()
    }

    private fun processGame() {
        hideKeyboard(currentFocus ?: View(this))
        var inputText = textInputEditText.text.toString()
        if (inputText?.length == 5) {
            var found = findInDatabase(inputText)
            if (!found) {
                val myToast = Toast.makeText(
                    this,
                    getString(R.string.info_we_unknown_that_word),
                    Toast.LENGTH_SHORT
                )
                myToast.show()
            } else {

                val word = EnteredWord(inputText, mysteryWord)
                gameState.nextState(word)

                wordsViewAdapter.fillLettersInFiveLettersView(
                    word,
                    gameState.gameStatus - 1,
                    inputText
                )
                if (gameState.isWin) {
                    winToast(true)
                    enableNewGameBtn()

                } else {
                    if (gameState.gameStatus == GameStatus.W6 && !gameState.isWin) {
                        winToast(false)
                        enableNewGameBtn()

                    }
                }

            }
        } else {
            val myToast =
                Toast.makeText(this, getString(R.string.info_need_five_letters), Toast.LENGTH_SHORT)
            myToast.show()
        }
        textInputEditText.setText("")
        saveData()
    }

    private fun winToast(isWin: Boolean) {
        var text = ""
        text = if (isWin) {
            getString(R.string.info_win) + " " + mysteryWord.uppercase()
        } else {
            getString(R.string.info_loose) + " " + mysteryWord.uppercase()

        }
        val myToast =
            Toast.makeText(this, text, Toast.LENGTH_LONG)
        myToast.show()
    }

    private fun findInDatabase(word: String): Boolean {
        var found: Boolean = false
        if (words.contains(word.lowercase())) {
            found = true
        }
        return found
    }

    fun saveData() {
        var editor = sharePref?.edit()
        var wordSet: MutableSet<String> = mutableSetOf()
        for (e in gameState.enteredWords) {
            wordSet.add(e.word)
        }
        editor?.putInt("GameState", gameState.gameStatus)
        editor?.putStringSet("GameStrings", wordSet)
        editor?.putString("MysteryWord", mysteryWord)
        editor?.apply()
    }

    fun resumeGame() {
        var wordSet = sharePref?.getStringSet("GameStrings", null)
        var gameStatus = sharePref?.getInt("GameState", GameStatus.NG)
        var mysteryWord = sharePref?.getString("MysteryWord", null)

        if (wordSet != null) {
            if (gameStatus != null) {
                if (mysteryWord != null) {
                    gameState.resumeGame(wordSet, gameStatus, mysteryWord)
                    wordsViewAdapter.clearWords()
                    for ((i, word) in gameState.enteredWords.withIndex()){
                        wordsViewAdapter.fillLettersInFiveLettersView(
                            word,
                            i,
                            word.word
                        )
                    }
                }
            }
        }
        if (gameStatus == GameStatus.W6) {
            enableNewGameBtn()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        saveData()
    }

    fun enableNewGameBtn() {
        newGameButton.visibility = View.VISIBLE
        okGameButton.isEnabled = false
        textInputEditText.isEnabled = false
    }

//    override fun onResume() {
//        super.onResume()
//        resumeGame()
//
//    }
//
//    override fun onApplyThemeResource(theme: Resources.Theme?, resid: Int, first: Boolean) {
//        super.onApplyThemeResource(theme, resid, first)
//        resumeGame()
//    }

}



