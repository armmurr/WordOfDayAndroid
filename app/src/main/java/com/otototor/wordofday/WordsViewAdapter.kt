package com.otototor.wordofday

import android.content.Context
import android.content.res.TypedArray
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class WordsViewAdapter(private val activity: AppCompatActivity) {

    private val lettersBlocs = listOf<View>(
        activity.findViewById(R.id.fivelettersblock_1),
        activity.findViewById(R.id.fivelettersblock_2),
        activity.findViewById(R.id.fivelettersblock_3),
        activity.findViewById(R.id.fivelettersblock_4),
        activity.findViewById(R.id.fivelettersblock_5),
        activity.findViewById(R.id.fivelettersblock_6)
    )


    private fun getRecs(wordPos: Int): List<TextView> {
        val view = lettersBlocs[wordPos]
        return listOf<TextView>(
            view.findViewById(R.id.rec_1),
            view.findViewById(R.id.rec_2),
            view.findViewById(R.id.rec_3),
            view.findViewById(R.id.rec_4),
            view.findViewById(R.id.rec_5)
        )
    }

    fun clearWords() {
        for (i in 0 until lettersBlocs.count()){
            var recs = getRecs(i)
            for (j in recs){
                j.text = ""
                j.setTextColor(getTextColor(activity,android.R.attr.textColorSecondary))
            }
        }
    }

    fun fillLettersInFiveLettersView (enteredWord: EnteredWord, wordPos : Int, word : String) {
        var recs = getRecs(wordPos)
        var i = 0
        for (ch in word){
            recs[i].text = ch.lowercase()
            i++
        }
        coloringBasedOnWord(enteredWord,wordPos)
    }

    private fun coloringBasedOnWord (enteredWord: EnteredWord, wordPos: Int){
        val recs = getRecs(wordPos)
        val word = enteredWord.word
        val containsLetters = enteredWord.containsLetters
        val matchLetters = enteredWord.matchLetters
        var i = 0

        for (ch in word) {
            val count = containsLetters[ch]
            if (count != null && count > 0) {
                if (count > 1 && isHaveMatchWithThisWord(ch, matchLetters)) {
                    containsLetters[ch] = count - 1
                } else {
                    containsLetters[ch] = count - 1
                    recs[i].setTextColor(ContextCompat.getColor(activity, R.color.orange))
                }
            }
            if (matchLetters[i].value) {
                recs[i].setTextColor(ContextCompat.getColor(activity, R.color.green))
            }
            i++
        }
    }

    fun countOfLetterInWord (ch:Char,word:String):Int{ //кошка - шика надо переделывать
        var i =0
        for (wCh in word){
            if (wCh == ch) i++
        }
        return i
    }

    fun isHaveMatchWithThisWord(ch:Char, matchLetters:MutableList<EnteredWord.Letters>):Boolean {
        for (j in matchLetters){
            if (j.key == ch) {
                return  true
            }
        }
        return  false
    }

    private fun getTextColor(context: Context, attrId: Int): Int {
        val typedArray: TypedArray = context.theme.obtainStyledAttributes(intArrayOf(attrId))
        val textColor = typedArray.getColor(0, 0)
        typedArray.recycle()
        return textColor
    }
}