package com.liftdom.liftdom;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SignInActivity2 extends AppCompatActivity {

    @BindView(R.id.signin_email) EditText signInEmail;
    @BindView(R.id.signin_password) EditText signInPassword;
    @BindView(R.id.signup_email) EditText signUpEmail;
    @BindView(R.id.signup_password) EditText signUpPassword;
    @BindView(R.id.signup_username) EditText signUpUsername;
    @BindView(R.id.signInTitle) TextView signInTitle;
    @BindView(R.id.email_sign_up_button) TextView signUpButton;
    @BindView(R.id.email_sign_in_button) TextView signInButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in2);

        ButterKnife.bind(this);

        Typeface lobster = Typeface.createFromAsset(getAssets(), "fonts/Lobster-Regular.ttf");
        signInTitle.setTypeface(lobster);
        signInButton.setTypeface(lobster);
        signUpButton.setTypeface(lobster);


    }
}
