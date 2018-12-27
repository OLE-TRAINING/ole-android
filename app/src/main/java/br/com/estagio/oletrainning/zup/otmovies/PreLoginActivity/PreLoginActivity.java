package br.com.estagio.oletrainning.zup.otmovies.PreLoginActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.RegisterNewUserActivity.RegisterNewUserActivity;


public class PreLoginActivity extends AppCompatActivity {

    private PreLoginViewHolder preLoginViewHolder;
    private boolean emailContainsError;
    private final String EMAIL_VALIDATION_STATUS = "emailContainsError";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = this.getLayoutInflater().inflate(R.layout.activity_pre_login, null);
        this.preLoginViewHolder = new PreLoginViewHolder(view);
        setContentView(view);

        setupListeners();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(EMAIL_VALIDATION_STATUS, emailContainsError);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        emailContainsError = savedInstanceState.getBoolean(EMAIL_VALIDATION_STATUS);
        preLoginViewHolder.errorEditTextEmail.setErrorVisibility(emailContainsError);
    }


    private void setupListeners() {
        preLoginViewHolder.buttonNextPreLogin.setOnClickListener(buttonNextPreLoginOnClickListener);
        preLoginViewHolder.errorEditTextEmail.getEditText().addTextChangedListener(editTextPasswordTextChangedListener);

    }

    private View.OnClickListener buttonNextPreLoginOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
                emailContainsError = !validateEmail();
                preLoginViewHolder.errorEditTextEmail.setErrorVisibility(emailContainsError);
                if (validateEmail()) {
                    Intent intent = new Intent(PreLoginActivity.this, RegisterNewUserActivity.class);
                    String emailInput = preLoginViewHolder.errorEditTextEmail.getText().toString().trim();
                    intent.putExtra(getString(R.string.EmailPreLogin), emailInput);
                    startActivity(intent);
                }

        }
    };

    private boolean validateEmail() {
        String emailInput = preLoginViewHolder.errorEditTextEmail.getText().toString().trim();
        return (!emailInput.isEmpty() && validateEmailFormat(emailInput));
    }

    private boolean validateEmailFormat(final String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private TextWatcher editTextPasswordTextChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            emailContainsError = false;
            preLoginViewHolder.errorEditTextEmail.setErrorVisibility(false);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}