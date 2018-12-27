package br.com.estagio.oletrainning.zup.otmovies.PreLoginActivity;


import android.view.View;
import android.widget.Button;

import br.com.estagio.oletrainning.zup.otmovies.CustomComponents.ComponentErrorEditText;
import br.com.estagio.oletrainning.zup.otmovies.R;

class PreLoginViewHolder{

    Button buttonNextPreLogin;
    ComponentErrorEditText errorEditTextEmail;

    PreLoginViewHolder(View view) {
        buttonNextPreLogin = view.findViewById(R.id.button_nextPreLogin);
        errorEditTextEmail = view.findViewById(R.id.errorEditText_enterEmail);
    }
}
