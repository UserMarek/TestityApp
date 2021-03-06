package com.sergeyyaniuk.testity.ui.login;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sergeyyaniuk.testity.R;
import com.sergeyyaniuk.testity.ui.base.BaseDialogNoTitle;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ForgotPasswordDialog extends BaseDialogNoTitle {

    private Unbinder unbinder;

    @BindView(R.id.email_edit_text)
    EditText mEmail;
    @BindView(R.id.ok_button)
    Button mOkButton;
    @BindView(R.id.cancel_button)
    Button mCancelButton;

    ForgotDialogListener mListener;

    private static final String EMAIL = "email";
    String email;

    public interface ForgotDialogListener{
        void sendPassword(String email);
    }

    //required empty constructor
    public ForgotPasswordDialog() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_forgot_password, container);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(EMAIL, email);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mEmail.setText(email);
        }
    }

    @OnClick(R.id.ok_button)
    void positiveButton(){
        if (!validateForm()) {
            return;
        } else if (!isActiveNetwork()){
            Toast.makeText(getActivity(), R.string.no_connection, Toast.LENGTH_SHORT).show();
        }
        mListener.sendPassword(mEmail.getText().toString());
        ForgotPasswordDialog.this.getDialog().dismiss();
    }

    @OnClick(R.id.cancel_button)
    void negativeButton(){
        ForgotPasswordDialog.this.getDialog().dismiss();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (ForgotDialogListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmail.setError(getResources().getString(R.string.required));
            valid = false;
        } else {
            mEmail.setError(null);
        }

        return valid;
    }

    protected boolean isActiveNetwork(){
        ConnectivityManager cm =
                (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}