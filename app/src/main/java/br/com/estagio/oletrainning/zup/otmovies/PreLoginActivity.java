package br.com.estagio.oletrainning.zup.otmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class PreLoginActivity extends AppCompatActivity {

    private EditText editTextEnterEmail;
    private ImageView imageViewEmailErrorPreLogin;
    private TextView textViewEmailErrorPreLogin;
    private Button buttonNextPreLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_login);

        findViewByIds();

        textViewEmailErrorPreLogin.setVisibility(View.INVISIBLE);
        imageViewEmailErrorPreLogin.setVisibility(View.INVISIBLE);

        editTextEnterEmail.addTextChangedListener(editTextEnterEmailTextChangedListener);
        buttonNextPreLogin.setOnClickListener(buttonNextPreLoginOnClickListener);
    }

    private void findViewByIds() {
        this.editTextEnterEmail = findViewById(R.id.editText_enterEmail);
        this.textViewEmailErrorPreLogin = findViewById(R.id.textView_EmailErrorPreLogin);
        this.imageViewEmailErrorPreLogin = findViewById(R.id.imageView_EmailErrorPreLogin);
        this.buttonNextPreLogin = findViewById(R.id.button_nextPreLogin);

    }

    View.OnClickListener buttonNextPreLoginOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.button_nextPreLogin && validateEmail()) {
                Intent intent = new Intent(PreLoginActivity.this,RegisterNewUserActivity.class);
                String emailInput = editTextEnterEmail.getText().toString().trim();
                intent.putExtra(getString(R.string.EmailPreLogin),emailInput);
                startActivity(intent);
            }
        }
    };

    private boolean validateEmail() {
        String emailInput = editTextEnterEmail.getText().toString().trim();
        if (emailInput.isEmpty() || !validateEmailFormat(emailInput)) {
            editTextEnterEmail.setBackground(getDrawable(R.drawable.border_email_input_error));
            imageViewEmailErrorPreLogin.setVisibility(View.VISIBLE);
            textViewEmailErrorPreLogin.setVisibility(View.VISIBLE);
            return false;
        } else {
            editTextEnterEmail.setError(null);
            editTextEnterEmail.setEnabled(false);
            editTextEnterEmail.setBackground(getDrawable(R.drawable.border_email_input));
            imageViewEmailErrorPreLogin.setVisibility(View.INVISIBLE);
            textViewEmailErrorPreLogin.setVisibility(View.INVISIBLE);
            return true;
        }
    }

    private boolean validateEmailFormat(final String email) {
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return true;
        }
        return false;
    }

    TextWatcher editTextEnterEmailTextChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            editTextEnterEmail.setBackground(getDrawable(R.drawable.border_email_input));
            imageViewEmailErrorPreLogin.setVisibility(View.INVISIBLE);
            textViewEmailErrorPreLogin.setVisibility(View.INVISIBLE);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
