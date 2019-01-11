package br.com.estagio.oletrainning.zup.otmovies.TokenValidationActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import br.com.estagio.oletrainning.zup.otmovies.PreLoginActivity.PreLoginActivity;
import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.RegisterNewUserActivity.RegisterNewUserActivity;

public class TokenValidationActivity extends AppCompatActivity {

    private TokenValidationViewHolder tokenValidationViewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = this.getLayoutInflater().inflate(R.layout.activity_token_validation, null);
        this.tokenValidationViewHolder = new TokenValidationViewHolder(view);
        setContentView(view);

        setupListeners();

        String emailAdd = getIntent().getStringExtra(getString(R.string.emailregister));
        tokenValidationViewHolder.textViewEmail.setText(emailAdd);
    }

    private void setupListeners() {
        tokenValidationViewHolder.button.setOnClickListener(buttonOnClickListener);
        tokenValidationViewHolder.imageView.setOnClickListener(imageViewBackArrowOnClickListener);
        tokenValidationViewHolder.errorEditText.getEditText().addTextChangedListener(errorEditTextTextWatcher);
    }

    View.OnClickListener buttonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    TextWatcher errorEditTextTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    View.OnClickListener imageViewBackArrowOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.imageView_backArrow) {
                Intent intent = new Intent(TokenValidationActivity.this, PreLoginActivity.class);
                startActivity(intent);
            }

        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(TokenValidationActivity.this, PreLoginActivity.class);
        startActivity(intent);
    }
}
