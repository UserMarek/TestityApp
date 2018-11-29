package com.sergeyyaniuk.testity.ui.createTest.questions;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.sergeyyaniuk.testity.App;
import com.sergeyyaniuk.testity.R;
import com.sergeyyaniuk.testity.data.model.Answer;
import com.sergeyyaniuk.testity.data.model.Question;
import com.sergeyyaniuk.testity.di.module.QuestionsListModule;
import com.sergeyyaniuk.testity.ui.base.BaseActivity;
import com.sergeyyaniuk.testity.ui.main.MainActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuestionsActivity extends BaseActivity implements QuestionsListFragment.QuestionsListListener,
        DetailQuestionFragment.DetailQuestionListener {

    public static final String QUESTION_ID = "question_id";

    @BindView(R.id.create_toolbar)
    Toolbar mToolbar;

    @Inject
    QuestionsPresenter mPresenter;

    String mTestId, mTestTitle;
    boolean isTestOnline;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        App.get(this).getAppComponent().create(new QuestionsListModule(this)).inject(this);
        ButterKnife.bind(this);
        mPresenter.onCreate();
        //get data from intent
        mTestId = getIntent().getStringExtra("test_id");
        mTestTitle = getIntent().getStringExtra("test_title");
        isTestOnline = getIntent().getBooleanExtra("is_online", false);
        //setup actionbar
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(mTestTitle);
        showQuestionsListFragment();
    }

    public void showQuestionsListFragment(){
        QuestionsListFragment questionsListFragment = new QuestionsListFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragmentContainer, questionsListFragment);
        transaction.disallowAddToBackStack();
        transaction.commit();
    }

    public void showDetailQuestionFragment(){
        DetailQuestionFragment detailQuestionFragment = new DetailQuestionFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, detailQuestionFragment);
        transaction.disallowAddToBackStack();
        transaction.commit();
    }


    @Override
    public void onAddNewQuestion() {
        showDetailQuestionFragment();
    }

    @Override
    public void onTestCompleted() {
        startActivity(new Intent(QuestionsActivity.this, MainActivity.class));
    }

    @Override
    public void onClickQuestion(String questionId) {
        DetailQuestionFragment detailQuestionFragment = new DetailQuestionFragment();
        Bundle arguments = new Bundle();
        arguments.putString(QUESTION_ID, questionId);
        detailQuestionFragment.setArguments(arguments);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, detailQuestionFragment);
        transaction.disallowAddToBackStack();
        transaction.commit();
    }

    @Override
    public void onSwipedQuestion(String questionId) {
        mPresenter.deleteQuestion(questionId, isTestOnline);
        mPresenter.deleteAnswerList(questionId, isTestOnline);
    }

    @Override
    public void onAddEditQuestionCompleted(Question question, List<Answer> answers, boolean isUpdating) {
        if (isUpdating){
            mPresenter.updateQuestion(question, isTestOnline);
        } else {
            mPresenter.saveQuestion(question, isTestOnline);
        }
        mPresenter.saveAnswerList(answers, isTestOnline);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.getNumberOfQuestions(isTestOnline);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }
}
