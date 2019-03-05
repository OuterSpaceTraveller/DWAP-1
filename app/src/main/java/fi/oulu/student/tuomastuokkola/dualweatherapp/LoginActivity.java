package fi.oulu.student.tuomastuokkola.dualweatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {


    private AutoCompleteTextView mEmail;
    private EditText mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = (AutoCompleteTextView) findViewById(R.id.loginEmail);
        mPassword = (EditText) findViewById(R.id.registerPassword);

        mPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView TextView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {

                    return true;
                }
                return false;
            }
        });
    }

    public void registerNewUser(View v) {

        Intent intent = new Intent(this, fi.oulu.student.tuomastuokkola.dualweatherapp.RegisterActivity.class);
        finish();
        startActivity(intent);
    }

}
