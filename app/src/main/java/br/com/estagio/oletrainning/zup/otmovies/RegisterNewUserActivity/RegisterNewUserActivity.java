package br.com.estagio.oletrainning.zup.otmovies.RegisterNewUserActivity;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;


import br.com.estagio.oletrainning.zup.otmovies.PreLoginActivity.PreLoginActivity;
import br.com.estagio.oletrainning.zup.otmovies.R;

public class RegisterNewUserActivity extends AppCompatActivity {

    private RegisterNewUserViewHolder registerNewUserViewHolder;

    private final Integer MAXSIZENAME = 50;
    private final Integer MINSIZEPASS = 6;
    private final Integer MAXSIZEPASS = 10;
    private final Integer MAXSIZEUSERNAME = 15;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_new_user);

        ConstraintLayout mConstraintLayout = findViewById(R.id.layout_PreLogin);
        View view = this.getLayoutInflater().inflate(R.layout.activity_pre_login,mConstraintLayout,true);
        this.registerNewUserViewHolder = new RegisterNewUserViewHolder(view);
        setContentView(view);

        defaultVisibility();

        listeners();

        String emailEntered = getIntent().getStringExtra(getString(R.string.EmailPreLogin));
        registerNewUserViewHolder.textViewEmailEntered.setText(emailEntered);
    }


    private void defaultVisibility() {
        registerNewUserViewHolder.textViewEnterNameRegisterError.setVisibility(View.INVISIBLE);
        registerNewUserViewHolder.imageViewEnterNameRegisterError.setVisibility(View.INVISIBLE);
        registerNewUserViewHolder.textViewEnterUserNameError.setVisibility(View.INVISIBLE);
        registerNewUserViewHolder.imageViewEnterUserNameError.setVisibility(View.INVISIBLE);
        registerNewUserViewHolder.textViewEnterPasswordError.setVisibility(View.INVISIBLE);
        registerNewUserViewHolder.imageViewEnterPasswordError.setVisibility(View.INVISIBLE);
    }

    private void listeners() {
        registerNewUserViewHolder.editTextEnterUserName.addTextChangedListener(editTextEnterUserNameTextChangedListener);
        registerNewUserViewHolder.imageViewBackArrow.setOnClickListener(imageViewBackArrowOnClickListener);
        registerNewUserViewHolder.editTextEnterNameRegister.addTextChangedListener(editTextEnterNameTextChangedListener);
        registerNewUserViewHolder.editTextEnterPassword.addTextChangedListener(editTextEnterPasswordTextChangedListener);
        registerNewUserViewHolder.buttonNextRegister.setOnClickListener(buttonNextRegisterOnClickListener);
    }

   private View.OnClickListener imageViewBackArrowOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.imageView_backArrow) {
                Intent intent = new Intent(RegisterNewUserActivity.this, PreLoginActivity.class);
                startActivity(intent);
            }
        }
    };

    private View.OnClickListener buttonNextRegisterOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.button_nextRegister) {
                validateName();
                validateUserName();
                validatePassword();
            }
        }
    };

    private TextWatcher editTextEnterNameTextChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            registerNewUserViewHolder.editTextEnterNameRegister.setBackground(getDrawable(R.drawable.border_email_input));
            registerNewUserViewHolder.imageViewEnterNameRegisterError.setVisibility(View.INVISIBLE);
            registerNewUserViewHolder.textViewEnterNameRegisterError.setVisibility(View.INVISIBLE);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private boolean validateName() {
        String name = registerNewUserViewHolder.editTextEnterNameRegister.getText().toString().trim();
        if (name.isEmpty() || !validateNameFormat()) {
            registerNewUserViewHolder.editTextEnterNameRegister.setBackground(getDrawable(R.drawable.border_email_input_error));
            registerNewUserViewHolder.imageViewEnterNameRegisterError.setVisibility(View.VISIBLE);
            registerNewUserViewHolder.textViewEnterNameRegisterError.setVisibility(View.VISIBLE);
            return false;
        } else {
            registerNewUserViewHolder.editTextEnterNameRegister.setError(null);
            registerNewUserViewHolder.editTextEnterNameRegister.setEnabled(false);
            return true;
        }
    }

    private boolean validateNameFormat() {
        String name = registerNewUserViewHolder.editTextEnterNameRegister.getText().toString().trim();
        if (name.length() <= MAXSIZENAME && name.matches(getString(R.string.RegexForNameUnicode))) {
            return true;
        }
        return false;
    }

    private boolean validateUserNameFormat() {
        String userName = registerNewUserViewHolder.editTextEnterUserName.getText().toString().trim();
        if (userName.length() <= MAXSIZEUSERNAME && userName.matches(getString(R.string.OnlyNumberOrLetter))) {
            return true;
        }
        return false;
    }

    private boolean validateUserName() {
        String name = registerNewUserViewHolder.editTextEnterUserName.getText().toString().trim();
        if (name.isEmpty() || !validateUserNameFormat()) {
            registerNewUserViewHolder.editTextEnterUserName.setBackground(getDrawable(R.drawable.border_email_input_error));
            registerNewUserViewHolder.imageViewEnterUserNameError.setVisibility(View.VISIBLE);
            registerNewUserViewHolder.textViewEnterUserNameError.setVisibility(View.VISIBLE);
            return false;
        } else {
            registerNewUserViewHolder.editTextEnterUserName.setError(null);
            registerNewUserViewHolder.editTextEnterUserName.setEnabled(false);
            return true;
        }
    }

    private TextWatcher editTextEnterUserNameTextChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            registerNewUserViewHolder.editTextEnterUserName.setBackground(getDrawable(R.drawable.border_email_input));
            registerNewUserViewHolder.imageViewEnterUserNameError.setVisibility(View.INVISIBLE);
            registerNewUserViewHolder.textViewEnterUserNameError.setVisibility(View.INVISIBLE);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private boolean validatePasswordFormat() {
        String userName = registerNewUserViewHolder.editTextEnterPassword.getText().toString().trim();
        if (userName.length() >= MINSIZEPASS && userName.length() <= MAXSIZEPASS && userName.matches(getString(R.string.OnlyNumberOrLetter))) {
            return true;
        }
        return false;
    }

    private boolean validatePassword() {
        String name = registerNewUserViewHolder.editTextEnterPassword.getText().toString().trim();
        if (name.isEmpty() || !validatePasswordFormat()) {
            registerNewUserViewHolder.editTextEnterPassword.setBackground(getDrawable(R.drawable.border_email_input_error));
            registerNewUserViewHolder.imageViewEnterPasswordError.setVisibility(View.VISIBLE);
            registerNewUserViewHolder.textViewEnterPasswordError.setVisibility(View.VISIBLE);
            return false;
        } else {
            registerNewUserViewHolder.editTextEnterPassword.setError(null);
            registerNewUserViewHolder.editTextEnterPassword.setEnabled(false);
            return true;
        }
    }

    private TextWatcher editTextEnterPasswordTextChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            registerNewUserViewHolder.editTextEnterPassword.setBackground(getDrawable(R.drawable.border_email_input));
            registerNewUserViewHolder. imageViewEnterPasswordError.setVisibility(View.INVISIBLE);
            registerNewUserViewHolder.textViewEnterPasswordError.setVisibility(View.INVISIBLE);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}