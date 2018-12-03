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

public class RegisterNewUserActivity extends AppCompatActivity {
    private ImageView imageViewBackArrow;
    private TextView textViewEmailEntered;
    private EditText editTextEnterNameRegister;
    private ImageView imageViewEnterNameRegisterError;
    private TextView textViewEnterNameRegisterError;
    private EditText editTextEnterUserName;
    private TextView textViewEnterUserNameError;
    private ImageView imageViewEnterUserNameError;
    private EditText editTextEnterPassword;
    private TextView textViewEnterPasswordError;
    private ImageView imageViewEnterPasswordError;
    private Button buttonNextRegister;

    private final Integer MAXSIZENAME = 50;
    private final Integer MINSIZEPASS = 6;
    private final Integer MAXSIZEPASS = 10;
    private final Integer MAXSIZEUSERNAME = 15;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_new_user);

        findViewByIds();

        defaultVisibility();

        listeners();

        String emailEntered = getIntent().getStringExtra(getString(R.string.EmailPreLogin));
        textViewEmailEntered.setText(emailEntered);
    }

    private void findViewByIds() {
        this.textViewEmailEntered = findViewById(R.id.textView_emailEntered);
        this.imageViewBackArrow = findViewById(R.id.imageView_backArrow);
        this.imageViewEnterNameRegisterError = findViewById(R.id.imageView_enterNameRegisterError);
        this.editTextEnterNameRegister = findViewById(R.id.editText_enterName);
        this.editTextEnterUserName = findViewById(R.id.editText_enterUserName);
        this.textViewEnterUserNameError = findViewById(R.id.textView_enterUserNameError);
        this.textViewEnterNameRegisterError = findViewById(R.id.textView_enterNameRegisterError);
        this.imageViewEnterUserNameError = findViewById(R.id.imageView_enterUserNameError);
        this.editTextEnterPassword = findViewById(R.id.editText_enterPassword);
        this.textViewEnterPasswordError = findViewById(R.id.textView_enterPasswordError);
        this.imageViewEnterPasswordError = findViewById(R.id.imageView_enterPasswordError);
        this.buttonNextRegister = findViewById(R.id.button_nextRegister);
    }

    private void defaultVisibility() {
        textViewEnterNameRegisterError.setVisibility(View.INVISIBLE);
        imageViewEnterNameRegisterError.setVisibility(View.INVISIBLE);
        textViewEnterUserNameError.setVisibility(View.INVISIBLE);
        imageViewEnterUserNameError.setVisibility(View.INVISIBLE);
        textViewEnterPasswordError.setVisibility(View.INVISIBLE);
        imageViewEnterPasswordError.setVisibility(View.INVISIBLE);
    }

    private void listeners() {
        editTextEnterUserName.addTextChangedListener(editTextEnterUserNameTextChangedListener);
        imageViewBackArrow.setOnClickListener(imageViewBackArrowOnClickListener);
        editTextEnterNameRegister.addTextChangedListener(editTextEnterNameTextChangedListener);
        editTextEnterPassword.addTextChangedListener(editTextEnterPasswordTextChangedListener);
        buttonNextRegister.setOnClickListener(buttonNextRegisterOnClickListener);
    }

    View.OnClickListener imageViewBackArrowOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.imageView_backArrow) {
                Intent intent = new Intent(RegisterNewUserActivity.this, PreLoginActivity.class);
                startActivity(intent);
            }
        }
    };

    View.OnClickListener buttonNextRegisterOnClickListener = new View.OnClickListener() {
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

    TextWatcher editTextEnterNameTextChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            editTextEnterNameRegister.setBackground(getDrawable(R.drawable.border_email_input));
            imageViewEnterNameRegisterError.setVisibility(View.INVISIBLE);
            textViewEnterNameRegisterError.setVisibility(View.INVISIBLE);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private boolean validateName() {
        String name = editTextEnterNameRegister.getText().toString().trim();
        if (name.isEmpty() || !validateNameFormat()) {
            editTextEnterNameRegister.setBackground(getDrawable(R.drawable.border_email_input_error));
            imageViewEnterNameRegisterError.setVisibility(View.VISIBLE);
            textViewEnterNameRegisterError.setVisibility(View.VISIBLE);
            return false;
        } else {
            editTextEnterNameRegister.setError(null);
            editTextEnterNameRegister.setEnabled(false);
            return true;
        }
    }

    private boolean validateNameFormat() {
        String name = editTextEnterNameRegister.getText().toString().trim();
        if (name.length() <= MAXSIZENAME && name.matches(getString(R.string.RegexForNameUnicode))) {
            return true;
        }
        return false;
    }

    private boolean validateUserNameFormat() {
        String userName = editTextEnterUserName.getText().toString().trim();
        if (userName.length() <= MAXSIZEUSERNAME && userName.matches(getString(R.string.OnlyNumberOrLetter))) {
            return true;
        }
        return false;
    }

    private boolean validateUserName() {
        String name = editTextEnterUserName.getText().toString().trim();
        if (name.isEmpty() || !validateUserNameFormat()) {
            editTextEnterUserName.setBackground(getDrawable(R.drawable.border_email_input_error));
            imageViewEnterUserNameError.setVisibility(View.VISIBLE);
            textViewEnterUserNameError.setVisibility(View.VISIBLE);
            return false;
        } else {
            editTextEnterUserName.setError(null);
            editTextEnterUserName.setEnabled(false);
            return true;
        }
    }

    TextWatcher editTextEnterUserNameTextChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            editTextEnterUserName.setBackground(getDrawable(R.drawable.border_email_input));
            imageViewEnterUserNameError.setVisibility(View.INVISIBLE);
            textViewEnterUserNameError.setVisibility(View.INVISIBLE);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private boolean validatePasswordFormat() {
        String userName = editTextEnterPassword.getText().toString().trim();
        if (userName.length() >= MINSIZEPASS && userName.length() <= MAXSIZEPASS && userName.matches(getString(R.string.OnlyNumberOrLetter))) {
            return true;
        }
        return false;
    }

    private boolean validatePassword() {
        String name = editTextEnterPassword.getText().toString().trim();
        if (name.isEmpty() || !validatePasswordFormat()) {
            editTextEnterPassword.setBackground(getDrawable(R.drawable.border_email_input_error));
            imageViewEnterPasswordError.setVisibility(View.VISIBLE);
            textViewEnterPasswordError.setVisibility(View.VISIBLE);
            return false;
        } else {
            editTextEnterPassword.setError(null);
            editTextEnterPassword.setEnabled(false);
            return true;
        }
    }

    TextWatcher editTextEnterPasswordTextChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            editTextEnterPassword.setBackground(getDrawable(R.drawable.border_email_input));
            imageViewEnterPasswordError.setVisibility(View.INVISIBLE);
            textViewEnterPasswordError.setVisibility(View.INVISIBLE);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}