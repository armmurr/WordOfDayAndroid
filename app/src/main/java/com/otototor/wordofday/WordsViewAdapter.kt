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

    fun fillLettersInFiveLettersView (enteredWord: EnteredWord, wordPos : Int) {
        var recs = getRecs(wordPos)
        val matchLetters = enteredWord.matchedLetters

        for ((i, letter) in matchLetters.withIndex()) {
            recs[i].text = letter.char.lowercase()
            if (letter.color != null)
                recs[i].setTextColor(ContextCompat.getColor(activity, letter.color!!))
        }
    }

    private fun getTextColor(context: Context, attrId: Int): Int {
        val typedArray: TypedArray = context.theme.obtainStyledAttributes(intArrayOf(attrId))
        val textColor = typedArray.getColor(0, 0)
        typedArray.recycle()
        return textColor
    }
}