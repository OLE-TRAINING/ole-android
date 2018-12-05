package br.com.estagio.oletrainning.zup.otmovies.PreLoginActivity;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.RegisterNewUserActivity.RegisterNewUserActivity;


public class PreLoginActivity extends AppCompatActivity {

    private PreLoginViewHolder preLoginViewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ConstraintLayout mConstraintLayout = findViewById(R.id.layout_PreLogin);
        View view = this.getLayoutInflater().inflate(R.layout.activity_pre_login,mConstraintLayout,true);
        this.preLoginViewHolder = new PreLoginViewHolder(view);
        setContentView(view);

        preLoginViewHolder.textViewEmailErrorPreLogin.setVisibility(View.INVISIBLE);
        preLoginViewHolder.imageViewEmailErrorPreLogin.setVisibility(View.INVISIBLE);

        preLoginViewHolder.editTextEnterEmail.addTextChangedListener(editTextEnterEmailTextChangedListener);
        preLoginViewHolder.buttonNextPreLogin.setOnClickListener(buttonNextPreLoginOnClickListener);
    }



    private View.OnClickListener buttonNextPreLoginOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.button_nextPreLogin && validateEmail()) {
                Intent intent = new Intent(PreLoginActivity.this,RegisterNewUserActivity.class);
                String emailInput = preLoginViewHolder.editTextEnterEmail.getText().toString().trim();
                intent.putExtra(getString(R.string.EmailPreLogin),emailInput);
                startActivity(intent);
            }
        }
    };

    private boolean validateEmail() {
        String emailInput = preLoginViewHolder.editTextEnterEmail.getText().toString().trim();
        if (emailInput.isEmpty() || !validateEmailFormat(emailInput)) {
            preLoginViewHolder.editTextEnterEmail.setBackground(getDrawable(R.drawable.border_email_input_error));
            preLoginViewHolder.imageViewEmailErrorPreLogin.setVisibility(View.VISIBLE);
            preLoginViewHolder.textViewEmailErrorPreLogin.setVisibility(View.VISIBLE);
            return false;
        } else {
            preLoginViewHolder.editTextEnterEmail.setError(null);
            preLoginViewHolder.editTextEnterEmail.setEnabled(false);
            preLoginViewHolder.editTextEnterEmail.setBackground(getDrawable(R.drawable.border_email_input));
            preLoginViewHolder.imageViewEmailErrorPreLogin.setVisibility(View.INVISIBLE);
            preLoginViewHolder.textViewEmailErrorPreLogin.setVisibility(View.INVISIBLE);
            return true;
        }
    }

    private boolean validateEmailFormat(final String email) {
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return true;
        }
        return false;
    }

    private TextWatcher editTextEnterEmailTextChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            preLoginViewHolder.editTextEnterEmail.setBackground(getDrawable(R.drawable.border_email_input));
            preLoginViewHolder.imageViewEmailErrorPreLogin.setVisibility(View.INVISIBLE);
            preLoginViewHolder.textViewEmailErrorPreLogin.setVisibility(View.INVISIBLE);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
