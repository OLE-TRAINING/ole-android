package br.com.estagio.oletrainning.zup.otmovies.PreLoginActivity;

import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import br.com.estagio.oletrainning.zup.otmovies.CustomComponents.ComponentErrorEditText;
import br.com.estagio.oletrainning.zup.otmovies.R;

class PreLoginViewHolder{

    Button buttonNextPreLogin;
    ComponentErrorEditText errorEditTextEmail;
    ProgressBar progressBar;
    FrameLayout frameLayout;

    PreLoginViewHolder(View view) {
        buttonNextPreLogin = view.findViewById(R.id.button_nextPreLogin);
        errorEditTextEmail = view.findViewById(R.id.errorEditText_enterEmail);
        progressBar = view.findViewById(R.id.progress_bar);
        frameLayout = view.findViewById(R.id.loading_layout);
    }
}