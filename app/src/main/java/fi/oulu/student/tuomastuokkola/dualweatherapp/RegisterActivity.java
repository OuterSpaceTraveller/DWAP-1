package fi.oulu.student.tuomastuokkola.dualweatherapp;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    public static final String CHAT_PREFS = "ChatPrefs";
    public static final String DISPLAY_NAME_KEY = "username";

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mConfirmPasswordView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.loginEmail);
        mPasswordView = (EditText) findViewById(R.id.registerPassword);
        mPasswordView = (EditText) findViewById(R.id.registerNewPassword);

        mConfirmPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.registerButton || id == EditorInfo.IME_NULL) {
                    attemptRegistration();
                    return true;
                }
                return false;
            }
        });

    }
        public void signUp(View v) {
            attemptRegistration();
        }

        private void attemptRegistration() {

            mEmailView.setError(null);
            mPasswordView.setError(null);

            String email = mEmailView.getText().toString();
            String password = mPasswordView.getText().toString();

            boolean cancel = false;
            View focusView = null;

            if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
                mPasswordView.setError("Password too short or does not match.");
                focusView = mPasswordView;
                cancel = true;
            }

            if (TextUtils.isEmpty(email)) {
                mEmailView.setError("this field is required");
                focusView = mEmailView;
                cancel = true;
            } else if (!isEmailValid(email)) {
                mEmailView.setError("This email address is invalid");
                focusView = mEmailView;
                cancel = true;
            }

            if (cancel) {
                focusView.requestFocus();
            }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {

        return true;
    }

}