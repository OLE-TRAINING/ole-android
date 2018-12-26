package br.com.estagio.oletrainning.zup.otmovies.InformTokenAndNewPasswordActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import br.com.estagio.oletrainning.zup.otmovies.R;

public class InformTokenAndNewPasswordActivity extends AppCompatActivity {

    private InformTokenAndNewPasswordViewHolder informTokenAndNewPasswordViewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inform_token_and_new_password);

        View view = this.getLayoutInflater().inflate(R.layout.activity_inform_token_and_new_password, null);
        this.informTokenAndNewPasswordViewHolder = new InformTokenAndNewPasswordViewHolder(view);
        setContentView(view);

        setupListeners();
    }

    private void setupListeners() {
    }
}
