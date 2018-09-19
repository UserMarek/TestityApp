package com.sergeyyaniuk.testity.firebase;

import android.app.Application;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.sergeyyaniuk.testity.R;
import com.sergeyyaniuk.testity.ui.base.BaseActivity;

public class Authentication {

    private Application mApplication;
    private FirebaseAuth mAuth;

    //for google auth
    GoogleApiClient mGoogleApiClient;

    //for facebook auth;
    CallbackManager mCallbackManager;

    public Authentication(Application application){
        this.mApplication = application;
        this.mAuth = FirebaseAuth.getInstance();
    }

    public Intent getUserWithGoogle(BaseActivity activity){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .enableAutoManage(activity, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        return Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
    }

    public Task<AuthResult> getAuthWithGoogle(final BaseActivity activity, GoogleSignInAccount account){
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        return mAuth.signInWithCredential(credential);
    }

    public CallbackManager getUserWithFacebook() {
        FacebookSdk.sdkInitialize(mApplication);
        mCallbackManager = CallbackManager.Factory.create();
        return mCallbackManager;
    }

    public Task<AuthResult> getAuthWithFacebook(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        return mAuth.signInWithCredential(credential);
    }

    public Task<AuthResult> getUserWithEmail(String email, String password) {
        return mAuth.signInWithEmailAndPassword(email, password);
    }

    public Task<AuthResult> createUserWithEmail(String email, String password) {
        return mAuth.createUserWithEmailAndPassword(email, password);
    }
}
