package br.com.estagio.oletrainning.zup.otmovies.FinishYourRegistrationActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import br.com.estagio.oletrainning.zup.otmovies.PreLoginActivity.PreLoginActivity;
import br.com.estagio.oletrainning.zup.otmovies.R;

public class FinishYourRegistrationActivity extends AppCompatActivity {

    private FinishYourRegistrationViewHolder finishYourRegistrationViewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        View view = this.getLayoutInflater().inflate(R.layout.activity_finish_your_registration, null);
        this.finishYourRegistrationViewHolder = new FinishYourRegistrationViewHolder(view);
        setContentView(view);

        setupListeners();
    }

    private void setupListeners() {
        finishYourRegistrationViewHolder.imageView.setOnClickListener(backArrowOnClickListener);
        finishYourRegistrationViewHolder.errorEditText.getEditText().addTextChangedListener(errorEditTextTextWatcher);
    }

    View.OnClickListener backArrowOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.imageView_backArrow) {
                Intent intent = new Intent(FinishYourRegistrationActivity.this, PreLoginActivity.class);
                startActivity(intent);
            }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(FinishYourRegistrationActivity.this, PreLoginActivity.class);
        startActivity(intent);
    }
}
