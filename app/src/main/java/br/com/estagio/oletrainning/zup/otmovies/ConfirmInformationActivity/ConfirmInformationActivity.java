package br.com.estagio.oletrainning.zup.otmovies.ConfirmInformationActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import br.com.estagio.oletrainning.zup.otmovies.R;

public class ConfirmInformationActivity extends AppCompatActivity {

    private ConfirmInformationViewHolder confirmInformationViewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = this.getLayoutInflater().inflate(R.layout.activity_confirm_information, null);
        this.confirmInformationViewHolder = new ConfirmInformationViewHolder(view);
        setContentView(view);

        setupListeners();
    }

    private void setupListeners() {
    }
}
