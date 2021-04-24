package com.example.kalkulatorbmi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class Quiz extends AppCompatActivity {
    private static final int QUEST_IN_QUIZ = 6;
    ArrayList<Question> quizQuestionList = new ArrayList<Question>() {
        {
            add(new Question("Ile posiłków dziennie powinno się jeść?", new ArrayList<String>(Arrays.asList(
                    "2", "4", "5"
            )), "5"));
            add(new Question("Ile litrów wody powinno się pić w ciągu dnia?", new ArrayList<String>(Arrays.asList(
                    "1", "1.5", "2"
            )), "1.5"));
            add(new Question("Który z posiłków jest najważniejszy?", new ArrayList<String>(Arrays.asList(
                    "Śniadanie", "Obiad", "Kolacja"
            )), "Śniadanie"));
            add(new Question("Co ile godzin powinno się spożywać posiłki?", new ArrayList<String>(Arrays.asList(
                    "1-2", "2-3", "3-4"
            )), "3-4"));
            add(new Question("Ile godzin snu potrzebuje przeciętny człowiek?", new ArrayList<String>(Arrays.asList(
                    "4-6", "7-8", "9-10"
            )), "7-8"));
            add(new Question("Czym najlepiej ugasić pragnienie po wysiłku fizycznym?", new ArrayList<String>(Arrays.asList(
                    "Woda", "Sok", "Kawa"
            )), "Woda"));
        }
    };
    private Handler handler;
    private int guessRows = 3;
    private List<String> answerList;
    private String correctAnswer;
    private int totalGuesses;
    private int correctAnswers; 
    private TextView questionTextView;
    private LinearLayout quizLayout;
    private TextView questionNumberTextView;
    private LinearLayout[] guessLinearLayouts;
    private TextView answerTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        handler = new Handler();
        questionNumberTextView = findViewById(R.id.questionNumberTextView);
        questionTextView = findViewById(R.id.question);
        guessLinearLayouts = new LinearLayout[3];
        guessLinearLayouts[0] = findViewById(R.id.row1LinearLayout);
        guessLinearLayouts[1] = findViewById(R.id.row2LinearLayout);
        guessLinearLayouts[2] = findViewById(R.id.row3LinearLayout);
        answerTextView = findViewById(R.id.answerTextView);
        for (LinearLayout row : guessLinearLayouts) {
            Button button = (Button) row.getChildAt(0);
            button.setOnClickListener(guessButtonListener);
        }
        questionNumberTextView.setText(
                getString(R.string.question, 1, QUEST_IN_QUIZ));
        resetQuiz();
    }
    public void resetQuiz() {
        if (answerList != null) {
            answerList.clear();
        }
        correctAnswers = 0;
        totalGuesses = 0;
        loadNextQuest();
    }
    private void disableButtons() {
        for (int row = 0; row < guessRows; row++) {
            LinearLayout guessRow = guessLinearLayouts[row];
            for (int i = 0; i < guessRow.getChildCount(); i++)
                guessRow.getChildAt(i).setEnabled(false);
        }
    }
    public static class MyAlertDialogFragment extends DialogFragment {
        private OnYesNoClick yesNoClick;
        public static MyAlertDialogFragment newInstance(int totalGuesses) {
            MyAlertDialogFragment frag = new MyAlertDialogFragment();
            Bundle args = new Bundle();
            args.putInt("totalGuesses", totalGuesses);
            frag.setArguments(args);
            return frag;
        }
        public void setOnYesNoClick(OnYesNoClick yesNoClick) {
            this.yesNoClick = yesNoClick;
        }
        @Override
        public Dialog onCreateDialog(Bundle bundle) {
            int totalGuesses = getArguments().getInt("totalGuesses");
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(getActivity());
            builder.setMessage(getString(R.string.results,totalGuesses,(100 * QUEST_IN_QUIZ / (double) totalGuesses)));
            builder.setPositiveButton(R.string.reset_quiz,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int id) {
                            yesNoClick.onYesClicked();
                        }
                    }
            );
            return builder.create();
        }
        public interface OnYesNoClick {
            void onYesClicked();
        }
    }
    private void showYesNoDialog() {
        MyAlertDialogFragment yesNoAlert = MyAlertDialogFragment.newInstance(
                totalGuesses);
        yesNoAlert.show(getSupportFragmentManager(), "quiz results");

        yesNoAlert.setOnYesNoClick(new MyAlertDialogFragment.OnYesNoClick() {
            @Override
            public void onYesClicked() {
                resetQuiz();
            }
        });
    }
    private void loadNextQuest() {
        Question nextQuestion = quizQuestionList.get(correctAnswers);
        correctAnswer = nextQuestion.correctAnswer;
        answerTextView.setText("");
        questionTextView.setText(nextQuestion.question);
        questionNumberTextView.setText(getString(R.string.question, (correctAnswers + 1), QUEST_IN_QUIZ));
        for (int row = 0; row < guessRows; row++) {
            for (int column = 0;column < guessLinearLayouts[row].getChildCount();column++) {
                Button newGuessButton =(Button) guessLinearLayouts[row].getChildAt(column);
                newGuessButton.setEnabled(true);
                newGuessButton.setText(nextQuestion.answers.get(row));
            }
        }
    }
    private View.OnClickListener guessButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button guessButton = ((Button) v);
            String guess = guessButton.getText().toString();
            String answer = correctAnswer;
            ++totalGuesses;
            if (guess.equals(answer)) {
                ++correctAnswers;
                answerTextView.setText(answer + "!");
                answerTextView.setTextColor(getResources().getColor(R.color.correct_answer,getApplicationContext().getTheme()));
                disableButtons();
                if (correctAnswers == QUEST_IN_QUIZ) {
                    showYesNoDialog();
                } else {
                    handler.postDelayed(
                            new Runnable() {
                                @Override
                                public void run() {
                                    loadNextQuest();
                                }
                            }, 2000);
                }
            } else {
                answerTextView.setText(R.string.incorrect_answer);
                answerTextView.setTextColor(getResources().getColor(R.color.incorrect_answer, getApplicationContext().getTheme()));
                guessButton.setEnabled(false);
            }
        }
    };
}