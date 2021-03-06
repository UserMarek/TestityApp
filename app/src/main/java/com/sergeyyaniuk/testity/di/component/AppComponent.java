package com.sergeyyaniuk.testity.di.component;


import com.sergeyyaniuk.testity.di.module.ApplicationModule;
import com.sergeyyaniuk.testity.di.module.CreateTestModule;
import com.sergeyyaniuk.testity.di.module.EditListModule;
import com.sergeyyaniuk.testity.di.module.EditQuestionModule;
import com.sergeyyaniuk.testity.di.module.EditTestFragModule;
import com.sergeyyaniuk.testity.di.module.EditTestModule;
import com.sergeyyaniuk.testity.di.module.FPassTestModule;
import com.sergeyyaniuk.testity.di.module.FindListModule;
import com.sergeyyaniuk.testity.di.module.LocalResultsModule;
import com.sergeyyaniuk.testity.di.module.LoginModule;
import com.sergeyyaniuk.testity.di.module.MainModule;
import com.sergeyyaniuk.testity.di.module.MyTestsModule;
import com.sergeyyaniuk.testity.di.module.OnlineResultsModule;
import com.sergeyyaniuk.testity.di.module.PassTestModule;
import com.sergeyyaniuk.testity.di.module.QuestionDetailFragModule;
import com.sergeyyaniuk.testity.di.module.QuestionsListModule;
import com.sergeyyaniuk.testity.di.module.QuestionsListFragModule;
import com.sergeyyaniuk.testity.di.module.ResultsModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface AppComponent {

    /* ----------Activity components --------------*/
    LoginComponent create(LoginModule loginModule);
    MainComponent create(MainModule mainModule);
    CreateTestComponent create(CreateTestModule createTestModule);
    QuestionsListComponent create(QuestionsListModule questionsListModule);
    MyTestsComponent create(MyTestsModule myTestsModule);
    EditComponent create(EditTestModule editTestModule);
    PassTestComponent create(PassTestModule passTestModule);
    FindListComponent create(FindListModule findListModule);
    FPassTestComponent create(FPassTestModule fPassTestModule);
    ResultsComponent create(ResultsModule resultsModule);


    /* ----------Fragment components --------------*/
    QuestionsListFragComponent create(QuestionsListFragModule questionsListFragModule);
    QuestionDetailFragComponent create(QuestionDetailFragModule questionDetailFragModule);
    EditFragComponent create(EditTestFragModule editTestFragModule);
    EditListComponent create(EditListModule editListModule);
    EditQuestionComponent create(EditQuestionModule editQuestionModule);
    LocalResultsComponent create(LocalResultsModule localResultsModule);
    OnlineResultsComponent create(OnlineResultsModule onlineResultsModule);
}
