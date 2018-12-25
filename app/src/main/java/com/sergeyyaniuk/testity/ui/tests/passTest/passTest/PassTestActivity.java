package com.sergeyyaniuk.testity.ui.tests.passTest.passTest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sergeyyaniuk.testity.App;
import com.sergeyyaniuk.testity.R;
import com.sergeyyaniuk.testity.data.model.Answer;
import com.sergeyyaniuk.testity.data.model.Question;
import com.sergeyyaniuk.testity.data.model.Result;
import com.sergeyyaniuk.testity.di.module.PassTestModule;
import com.sergeyyaniuk.testity.ui.base.BaseActivity;
import com.sergeyyaniuk.testity.ui.tests.passTest.endTest.EndTestActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PassTestActivity extends BaseActivity {

    private static final String KEY_INDEX = "index";
    private static final String KEY_NUMBER_OF_CORRECT = "number_of_correct";
    private static final String KEY_SCORE = "score";

    @Inject
    PassTestPresenter mPresenter;

    AnswerListAdapter mAdapter;

    @BindView(R.id.answers_rec_view)
    RecyclerView mAnswersRecView;

    @BindView(R.id.question_tv)
    TextView mQuestionTV;

    @BindView(R.id.next_question_btn)
    Button mNextQuesButton;

    @BindView(R.id.test_progress)
    ProgressBar mTestProgress;

    String mTestId, mApplicantName;
    private int mCurrentIndex;
    private int mCorrectAnswers;
    private int mNumberOfTries;
    private int mNumberOfChance;
    List<Question> mQuestionList;
    List<Answer> mAnswerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_test);
        App.get(this).getAppComponent().create(new PassTestModule(this))
                .inject(this);  //inject presenter
        mPresenter.onCreate();  //create CompositeDisposable
        ButterKnife.bind(this);
        //get data from intent
        mTestId = getIntent().getStringExtra("test_id");
        mApplicantName = getIntent().getStringExtra("name");
        mPresenter.cleanTotalCorr();  //clean number of total correct answers
        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mCorrectAnswers = savedInstanceState.getInt(KEY_NUMBER_OF_CORRECT, 0);
        }
        mPresenter.loadQuestions(mTestId); //load data from database
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putInt(KEY_NUMBER_OF_CORRECT, mCorrectAnswers);
    }

    //invoke from presenter
    public void setQuestionList(List<Question> questions){
        mQuestionList = new ArrayList<>(questions);
        updateData();
    }

    public void updateData(){
        updateQuestion();  //update question TextView
        loadAnswers();  //load answerList from database by questionId
        setTestProgress(); //set current test progress
        mCurrentIndex++;
    }

    private void updateQuestion(){
        String questionText = mQuestionList.get(mCurrentIndex).getQuestionText();
        mQuestionTV.setText(questionText);
    }

    private void loadAnswers(){
        String questionId = mQuestionList.get(mCurrentIndex).getId();
        mPresenter.loadAnswers(questionId);
    }

    private void setTestProgress(){
        int progress = (mCurrentIndex * 100) / mQuestionList.size();
        mTestProgress.setProgress(progress);
    }

    //invoke from presenter after answers loaded
    public void updateAnswers(List<Answer> answers){
        mAnswerList = new ArrayList<>(answers);
        mAnswersRecView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new AnswerListAdapter(mAnswerList, answerClickListener);
        mAnswersRecView.setAdapter(mAdapter);
        mNumberOfTries = 0;
        mNumberOfChance = 0;
        updateNumOfChance(answers);
    }

    private void updateNumOfChance(List<Answer> answers){
        for(Answer answer : answers){
            if (answer.isCorrect()){
                mNumberOfChance = mNumberOfChance + 1;
            }
        }
    }

    AnswerListAdapter.AnswerClickListener answerClickListener = new AnswerListAdapter.AnswerClickListener() {
        @Override
        public void onAnswerClick(boolean isCorrect) {
            mNumberOfTries = mNumberOfTries + 1;
            if (mNumberOfTries <= mNumberOfChance && isCorrect){
                mCorrectAnswers = mCorrectAnswers + 1;
            }
        }
    };

    @OnClick(R.id.next_question_btn)
    public void onNextQuestion(){
        if (mCurrentIndex < mQuestionList.size()){
            updateData();
        } else {
            saveResults();
        }
    }

    private void saveResults(){
        int totalCorrectAnswers = mPresenter.getNumberOfCorrect();
        double score = (mCorrectAnswers * 100) / totalCorrectAnswers;
        String resultId = generateResultId();
        Result result = new Result(resultId, mTestId, mApplicantName, score);
        mPresenter.saveResult(result);
        showResults(score);
    }

    private void showResults(double score){
        Intent intent = new Intent(PassTestActivity.this, EndTestActivity.class);
        intent.putExtra(KEY_SCORE, score);
        startActivity(intent);
    }

    private String generateResultId(){
        @SuppressLint("SimpleDateFormat")
        String currentTime = new SimpleDateFormat("yyMMddHHmmss").format(new Date());
        return mTestId + mApplicantName + currentTime;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }
}
