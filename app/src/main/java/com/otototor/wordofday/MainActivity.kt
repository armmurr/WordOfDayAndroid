package com.otototor.wordofday

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.TypedArray
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText


class
MainActivity : AppCompatActivity() {
    private var gameStatus: Int = GameStatus.NG
    private var mysteryWord = ""
    private lateinit var lettersBlocs: List<View>

    private lateinit var textInputEditText: TextInputEditText
    private lateinit var words: List<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lettersBlocs = listOf<View>(
            findViewById(R.id.fivelettersblock_1),
            findViewById(R.id.fivelettersblock_2),
            findViewById(R.id.fivelettersblock_3),
            findViewById(R.id.fivelettersblock_4),
            findViewById(R.id.fivelettersblock_5),
            findViewById(R.id.fivelettersblock_6))
        textInputEditText = findViewById(R.id.textInputEditText)
        words = Words().WordsLits
        generateMysteryWord()
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    fun enterTheWord(view: View) {
        if (gameStatus == GameStatus.NG) {
            clearWords()
            generateMysteryWord()
            gameStatus = GameStatus.W1
        }
        var inputText = textInputEditText.text.toString()
        if (inputText?.length == 5) {
            var finded = findInDatabase(inputText)
            if (!finded) {
                val myToast = Toast.makeText(
                    this,
                    getString(R.string.info_we_unknown_that_word),
                    Toast.LENGTH_SHORT
                )
                myToast.show()
            } else {
                fillLettersInFiveLettersView(lettersBlocs[gameStatus-1],inputText)
                FindLetterInMysteryWord(inputText,lettersBlocs[gameStatus-1])
                if (inputText.lowercase() == mysteryWord) { winToast(true) }
                else {
                    if (gameStatus == GameStatus.W6) {
                        gameStatus = GameStatus.NG
                        if (inputText.lowercase() != mysteryWord) winToast(false)
                    } else gameStatus++
                }
            }
        } else {
            val myToast =
                Toast.makeText(this, getString(R.string.info_need_five_letters), Toast.LENGTH_SHORT)
            myToast.show()
        }
        textInputEditText.setText("")
    }

    fun winToast(isWin: Boolean) {
        var text = ""
        if (isWin) {
            text = getString(R.string.info_win) + " " + mysteryWord.uppercase()
        } else {
            text = getString(R.string.info_loose) + " " + mysteryWord.uppercase()

        }
        val myToast =
            Toast.makeText(this, text, Toast.LENGTH_LONG)
        myToast.show()
        gameStatus = GameStatus.NG
    }

    fun findInDatabase(word: String): Boolean {
        var finded: Boolean = false
        if (words.contains(word.lowercase())) {
            finded = true
        }
        return finded
    }

    fun fillLettersInFiveLettersView (view : View, word : String) {
        var recs = getRecs(view)
        var i = 0
        for (ch in word){
            recs[i].text = ch.lowercase().toString()
            i++
        }
    }

    fun clearWords() {
        for (i in lettersBlocs){
            var recs = getRecs(i)
            for (j in recs){
                j.text = ""
                j.setTextColor(getTextColor(this,android.R.attr.textColorSecondary))
            }
        }
    }

    fun getTextColor(context: Context, attrId: Int): Int {
        val typedArray: TypedArray = context.getTheme().obtainStyledAttributes(intArrayOf(attrId))
        val textColor = typedArray.getColor(0, 0)
        typedArray.recycle()
        return textColor
    }

    fun generateMysteryWord() {
        var word = ""
        var i =  (0..words.count() - 1).random()
        word = words.get(i)
        mysteryWord = word
    }

    fun getRecs (view : View): List<TextView> {
        return listOf<TextView>(
            view.findViewById(R.id.rec_1),
            view.findViewById(R.id.rec_2),
            view.findViewById(R.id.rec_3),
            view.findViewById(R.id.rec_4),
            view.findViewById(R.id.rec_5))
    }

    fun  FindLetterInMysteryWord(userWord:String, view : View)
    {
        var recs = getRecs(view)
        var i = 0
        for (ch in userWord) {
            if (mysteryWord.contains(ch,true)){
                recs[i].setTextColor(ContextCompat.getColor(this,R.color.orange))
            }
            if (mysteryWord[i] == ch)
            {
                recs[i].setTextColor(ContextCompat.getColor(this,R.color.green))
            }
            i++
        }

    }
}



