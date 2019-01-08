package br.com.estagio.oletrainning.zup.otmovies.TokenValidationActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import br.com.estagio.oletrainning.zup.otmovies.R;

public class TokenValidationActivity extends AppCompatActivity {

    private TokenValidationViewHolder tokenValidationViewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = this.getLayoutInflater().inflate(R.layout.activity_token_validation, null);
        this.tokenValidationViewHolder = new TokenValidationViewHolder(view);
        setContentView(view);

        setupListeners();
    }

    private void setupListeners() {
        tokenValidationViewHolder.button.setOnClickListener(buttonOnClickListener);
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
}
