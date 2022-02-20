package com.otototor.wordofday

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText


class
MainActivity : AppCompatActivity() {
    private lateinit var fivelettersblock_1: View
    private lateinit var fivelettersblock_2: View
    private lateinit var fivelettersblock_3: View
    private lateinit var fivelettersblock_4: View
    private lateinit var fivelettersblock_5: View
    private lateinit var fivelettersblock_6: View
    private  var gameStatus: Int = GameStatus.NG

    private lateinit var textInputEditText: TextInputEditText
    private lateinit var words: List<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fivelettersblock_1 = findViewById(R.id.fivelettersblock_1)
        fivelettersblock_2 = findViewById(R.id.fivelettersblock_2)
        fivelettersblock_3 = findViewById(R.id.fivelettersblock_3)
        fivelettersblock_4 = findViewById(R.id.fivelettersblock_4)
        fivelettersblock_5 = findViewById(R.id.fivelettersblock_5)
        fivelettersblock_6 = findViewById(R.id.fivelettersblock_6)
        textInputEditText = findViewById(R.id.textInputEditText)
        words = Words().WordsLits
    }

    fun enterTheWord(view: View) {
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
                fillLettersInFiveLettersView(fivelettersblock_1,inputText)
            }
        } else {
            val myToast =
                Toast.makeText(this, getString(R.string.info_need_five_letters), Toast.LENGTH_SHORT)
            myToast.show()
        }
    }

    fun findInDatabase(word: String): Boolean {
        var finded: Boolean = false
        if (words.contains(word.lowercase())) {
            finded = true
        }
        return finded
    }

    fun fillLettersInFiveLettersView (view : View, word : String) {
        var recs = listOf<TextView>(
            view.findViewById(R.id.rec_1),
            view.findViewById(R.id.rec_2),
            view.findViewById(R.id.rec_3),
            view.findViewById(R.id.rec_4),
            view.findViewById(R.id.rec_5))

        var i = 0
        for (ch in word){
            recs[i] .text = ch.toString()
            i++
        }
    }

    fun  FindLetterInMysteryWord(userWord:String)//: List<String>
    {

    }
}



