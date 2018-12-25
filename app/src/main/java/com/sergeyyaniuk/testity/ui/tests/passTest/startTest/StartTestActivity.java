package com.sergeyyaniuk.testity.ui.tests.passTest.startTest;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.sergeyyaniuk.testity.R;
import com.sergeyyaniuk.testity.ui.base.BaseActivity;
import com.sergeyyaniuk.testity.ui.tests.passTest.passTest.PassTestActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StartTestActivity extends BaseActivity {

    public static final String TEST_ID = "test_id";
    public static final String NAME = "name";

    @BindView(R.id.name_edit_text)
    EditText mNameEditText;

    private String mTestId, mName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_test);
        ButterKnife.bind(this);
        mTestId = getIntent().getStringExtra("test_id");
    }

    @OnClick(R.id.start_test_btn)
    public void onStartTest() {
        if (!validateForm()) {
            return;
        }
        mName = mNameEditText.getText().toString();
        Intent intent = new Intent(StartTestActivity.this, PassTestActivity.class);
        intent.putExtra(TEST_ID, mTestId);
        intent.putExtra(NAME, mName);
        startActivity(intent);
    }

    //required not empty fields
    public boolean validateForm() {
        boolean valid = true;
        mName = mNameEditText.getText().toString();
        if (TextUtils.isEmpty(mName)) {
            mNameEditText.setError(getResources().getString(R.string.required));
            valid = false;
        } else {
            mNameEditText.setError(null);
        }
        return valid;
    }
}
