package br.com.estagio.oletrainning.zup.otmovies.FinishYourRegistrationActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

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
    }
}
