package br.com.estagio.oletrainning.zup.otmovies.TokenValidationActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import br.com.estagio.oletrainning.zup.otmovies.R;

public class TokenValidationActivity extends AppCompatActivity {

    private TokenValidationViewHolder tokenValidationViewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_validation);

        View view = this.getLayoutInflater().inflate(R.layout.activity_login, null);
        this.tokenValidationViewHolder = new TokenValidationViewHolder(view);
        setContentView(view);

        setupListeners();
    }

    private void setupListeners() {
    }
}
