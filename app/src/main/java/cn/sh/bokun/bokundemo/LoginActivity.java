package cn.sh.bokun.bokundemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    private AutoCompleteTextView acc;
    private EditText pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}
