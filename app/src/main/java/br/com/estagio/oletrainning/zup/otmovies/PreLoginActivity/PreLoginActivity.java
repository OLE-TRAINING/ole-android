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
    private boolean emailValidationStatus;
    private final String EMAIL_VALIDATION_STATUS = "emailValidationStatus";

    public void setEmailValidationStatus(boolean emailValidationStatus) {
        this.emailValidationStatus = emailValidationStatus;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = this.getLayoutInflater().inflate(R.layout.activity_pre_login, null);
        this.preLoginViewHolder = new PreLoginViewHolder(view);
        setContentView(view);

        setupListeners();

        setEmailErrorVisibility(false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(EMAIL_VALIDATION_STATUS, emailValidationStatus);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        emailValidationStatus = savedInstanceState.getBoolean(EMAIL_VALIDATION_STATUS);
        setEmailErrorVisibility(!emailValidationStatus);
    }

    private void setEmailErrorVisibility (Boolean visible){
        if(visible){
            preLoginViewHolder.editTextEnterEmail.setBackground(getDrawable(R.drawable.border_input_error));
            preLoginViewHolder.textViewEmailErrorPreLogin.setVisibility(View.VISIBLE);
            preLoginViewHolder.imageViewEmailErrorPreLogin.setVisibility(View.VISIBLE);
            setEmailValidationStatus(false);
        } else {
            preLoginViewHolder.editTextEnterEmail.setBackground(getDrawable(R.drawable.border_input));
            preLoginViewHolder.textViewEmailErrorPreLogin.setVisibility(View.INVISIBLE);
            preLoginViewHolder.imageViewEmailErrorPreLogin.setVisibility(View.INVISIBLE);
            setEmailValidationStatus(true);
        }
    }

    private void setupListeners() {
        preLoginViewHolder.editTextEnterEmail.addTextChangedListener(editTextEnterEmailTextChangedListener);
        preLoginViewHolder.buttonNextPreLogin.setOnClickListener(buttonNextPreLoginOnClickListener);
    }

    private View.OnClickListener buttonNextPreLoginOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
                emailValidationStatus = validateEmail();
                setEmailErrorVisibility(!emailValidationStatus);
                if (emailValidationStatus) {
                    Intent intent = new Intent(PreLoginActivity.this, RegisterNewUserActivity.class);
                    String emailInput = preLoginViewHolder.editTextEnterEmail.getText().toString().trim();
                    intent.putExtra(getString(R.string.EmailPreLogin), emailInput);
                    startActivity(intent);
                }

        }
    };

    private boolean validateEmail() {
        String emailInput = preLoginViewHolder.editTextEnterEmail.getText().toString().trim();
        return (!emailInput.isEmpty() && validateEmailFormat(emailInput));
    }

    private boolean validateEmailFormat(final String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private TextWatcher editTextEnterEmailTextChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            setEmailErrorVisibility(false);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };
}