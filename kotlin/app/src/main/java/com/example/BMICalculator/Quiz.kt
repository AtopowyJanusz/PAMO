package com.example.BMICalculator

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.kalkulatorbmi.Quiz.MyAlertDialogFragment.OnYesNoClick
import java.util.*

class Quiz : AppCompatActivity() 
{
    var quizQuestionList: ArrayList<Question?> = object : ArrayList<Question?>() 
    {
        init 
        {
            add(Question("Ile posiłków dziennie powinno się jeść?", ArrayList(Arrays.asList("2", "4", "5")), "5"))
            add(Question("Ile litrów wody powinno się pić w ciągu dnia?", ArrayList(Arrays.asList("1", "1.5", "2")), "1.5"))
            add(Question("Który z posiłków jest najważniejszy?", ArrayList(Arrays.asList("Śniadanie", "Obiad", "Kolacja")), "Śniadanie"))
            add(Question("Co ile godzin powinno się spożywać posiłki?", ArrayList(Arrays.asList("1-2", "2-3", "3-4")), "3-4"))
            add(Question("Ile godzin snu potrzebuje przeciętny człowiek?", ArrayList(Arrays.asList("4-6", "7-8", "9-10")), "7-8"))
            add(Question("Czym najlepiej ugasić pragnienie po wysiłku fizycznym?", ArrayList(Arrays.asList("Woda", "Sok", "Kawa")), "Woda"))
        }
    }
    private var handler : Handler? = null
    private val guessRows = 3 
    private val answerList: MutableList<String>? = null
    private var correctAnswer: String? = null
    private var totalGuesses= 0
    private var correctAnswers= 0
    private var questionTextView: TextView? = null
    private val quizLayout: LinearLayout? = null
    private var questionNumberTextView: TextView? = null
    private lateinit var guessLinearLayouts: Array<LinearLayout?>
    private var answerTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) 
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        handler = Handler()
        questionNumberTextView = findViewById(R.id.questionNumberTextView)
        questionTextView = findViewById(R.id.question)
        guessLinearLayouts = arrayOfNulls(3)
        guessLinearLayouts[0] = findViewById(R.id.row1LinearLayout)
        guessLinearLayouts[1] = findViewById(R.id.row2LinearLayout)
        guessLinearLayouts[2] = findViewById(R.id.row3LinearLayout)
        answerTextView = findViewById(R.id.answerTextView)
        for (row in guessLinearLayouts) 
        {
            val button = row!!.getChildAt(0) as Button
            button.setOnClickListener(guessButtonListener)
        }
        questionNumberTextView?.text = getString(R.string.question, 1, QUEST_IN_QUIZ)
        resetQuiz()
    }

    fun resetQuiz() 
    {
        answerList?.clear()
        correctAnswers = 0
        totalGuesses = 0
        loadNextQuest()
    }

    private fun disableButtons() 
    {
        for (row in 0 until guessRows) 
        {
            val guessRow = guessLinearLayouts[row]
            for (i in 0 until guessRow!!.childCount) guessRow.getChildAt(i).isEnabled = false
        }
    }

    class MyAlertDialogFragment : DialogFragment() 
    {
        private var yesNoClick: OnYesNoClick? = null
        fun setOnYesNoClick(yesNoClick: OnYesNoClick?) 
        {
            this.yesNoClick = yesNoClick
        }
        override fun onCreateDialog(bundle: Bundle?): Dialog 
        {
            val totalGuesses = requireArguments().getInt("totalGuesses")
            val builder = AlertDialog.Builder(activity)
            builder.setMessage(getString(R.string.results,totalGuesses, 100 * QUEST_IN_QUIZ / totalGuesses.toDouble()))
            builder.setPositiveButton(R.string.reset_quiz) 
            { 
                dialog, id -> yesNoClick!!.onYesClicked() 
            }
            return builder.create()
        }

        interface OnYesNoClick 
        {
            fun onYesClicked()
        }

        companion object 
        {
            fun newInstance(totalGuesses: Int): MyAlertDialogFragment 
            {
                val frag = MyAlertDialogFragment()
                val args = Bundle()
                args.putInt("totalGuesses", totalGuesses)
                frag.arguments = args
                return frag
            }
        }
    }

    private fun showYesNoDialog() 
    {
        val yesNoAlert = MyAlertDialogFragment.newInstance(totalGuesses)
        yesNoAlert.show(supportFragmentManager, "quiz results")
        yesNoAlert.setOnYesNoClick(object : OnYesNoClick 
        {
            override fun onYesClicked() 
            {
                resetQuiz()
            }
        })
    }

    private fun loadNextQuest() 
    {
        val nextQuestion = quizQuestionList[correctAnswers]
        if (nextQuestion != null) 
        {
            correctAnswer = nextQuestion.correctAnswer
        }
        answerTextView!!.text = ""
        if (nextQuestion != null) 
        {
            questionTextView!!.text = nextQuestion.question
        }
        questionNumberTextView!!.text = getString(R.string.question, correctAnswers + 1, QUEST_IN_QUIZ)
        for (row in 0 until guessRows) 
        {
            for (column in 0 until guessLinearLayouts[row]!!.childCount) 
            {
                val newGuessButton = guessLinearLayouts[row]!!.getChildAt(column) as Button
                newGuessButton.isEnabled = true
                if (nextQuestion != null) 
                {
                    newGuessButton.text = nextQuestion.answers[row]
                }
            }
        }
    }

    private val guessButtonListener = View.OnClickListener 
    { v ->
        val guessButton = v as Button
        val guess = guessButton.text.toString()
        val answer = correctAnswer
        ++totalGuesses 
        if (guess == answer) {
            ++correctAnswers
            answerTextView!!.text = "$answer!"
            answerTextView!!.setTextColor(resources.getColor(R.color.correct_answer, applicationContext.theme))
            disableButtons()
            if (correctAnswers == QUEST_IN_QUIZ) 
            {
                showYesNoDialog()
            }
            else
            {
                handler!!.postDelayed({ loadNextQuest() }, 2000)
            }
        }
        else
        {
            answerTextView!!.setText(R.string.incorrect_answer)
            answerTextView!!.setTextColor(resources.getColor(R.color.incorrect_answer, applicationContext.theme))
            guessButton.isEnabled = false
        }
    }
    companion object 
    {
        private const val QUEST_IN_QUIZ = 6
    }
}