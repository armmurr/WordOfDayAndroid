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
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
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
    private val helper:Helpers = Helpers()
    //lateinit var mAdView : AdView
    var sharePref: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharePref = getSharedPreferences("GameState", MODE_PRIVATE)
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
        newGameButton.visibility = View.GONE
        okGameButton.isEnabled = true
        textInputEditText.isEnabled = true
        wordsViewAdapter.clearWords()
        mysteryWord = gameState.mysteryWord
        saveData()
    }

    private fun processGame() {
        hideKeyboard(currentFocus ?: View(this))
        val inputText = textInputEditText.text.toString()
        if (inputText.length == 5) {
            val found = findInDatabase(inputText)
            if (!found) {
                helper.showToast(
                    this,
                    getString(R.string.info_we_unknown_that_word),
                    Toast.LENGTH_SHORT
                )
            } else {

                val word = EnteredWord(inputText, mysteryWord)
                gameState.nextState(word)

                wordsViewAdapter.fillLettersInFiveLettersView(
                    word,
                    gameState.gameStatus - 1
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
            helper.showToast(this, getString(R.string.info_need_five_letters), Toast.LENGTH_SHORT)
        }
        textInputEditText.setText("")
        saveData()
    }

    private fun winToast(isWin: Boolean) {
        val text: String = if (isWin) {
            getString(R.string.info_win) + " " + mysteryWord.uppercase()
        } else {
            getString(R.string.info_loose) + " " + mysteryWord.uppercase()
        }
        helper.showToast(this, text, Toast.LENGTH_LONG)

    }

    private fun findInDatabase(word: String): Boolean {
        var found: Boolean = false
        if (words.contains(word.lowercase())) {
            found = true
        }
        return found
    }

    private fun saveData() {
        var editor = sharePref?.edit()

        for (i in 0..5) {
            var word = gameState.enteredWords.getOrNull(i)
            if (word != null) {
                editor?.putString("GameStrings$i", word.toString())
            } else
                editor?.putString("GameStrings$i", null)

        }
        editor?.putInt("GameState", gameState.gameStatus)
        editor?.putString("MysteryWord", mysteryWord)
        editor?.putBoolean("IsWin", gameState.isWin)
        editor?.apply()
    }

    private fun resumeGame() {
        try {
            val wordList: MutableList<String> = mutableListOf()
            for (i in 0..5) {
                val word = sharePref?.getString("GameStrings$i", null)
                if (word != null) {
                    wordList.add(word)
                }
            }

            val gameStatus = sharePref?.getInt("GameState", GameStatus.NG)
            val mysteryWord = sharePref?.getString("MysteryWord", null)
            val isWin = sharePref?.getBoolean("IsWin", true)

            if (wordList.count() > 0) {
                if (gameStatus != null && mysteryWord != null && isWin != null) {
                    gameState.resumeGame(wordList, gameStatus, mysteryWord, isWin)
                    wordsViewAdapter.clearWords()
                    for ((i, word) in gameState.enteredWords.withIndex()) {
                        wordsViewAdapter.fillLettersInFiveLettersView(
                            word,
                            i
                        )
                    }
                }
            }
            if (gameStatus == GameStatus.W6 || gameState.isWin) {
                enableNewGameBtn()
            }
        } catch (e: Exception) {
            e.message?.let {
                helper.showToast(
                    this,
                    it,
                    Toast.LENGTH_SHORT
                )
            }
            gameState.newGame()
            saveData()
        }
    }



    private fun enableNewGameBtn() {
        newGameButton.visibility = View.VISIBLE
        okGameButton.isEnabled = false
        textInputEditText.isEnabled = false
    }

    override fun onResume() {
        super.onResume()
       // mAdView.resume()
    }
    override fun onPause() {
        super.onPause()
        //mAdView.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        saveData()
        //mAdView.destroy()
    }

    private fun adsInit() {
        val configuration = RequestConfiguration.Builder().setTestDeviceIds(listOf("5863910537C21309C6102FB2ADE898D2","02CD0377E76C9F3E1126A2C416BE480E")).build()
        MobileAds.setRequestConfiguration(configuration)
        MobileAds.initialize(this)


        //mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()

        //mAdView.loadAd(adRequest)
    }



}



