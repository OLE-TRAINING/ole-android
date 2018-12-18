package br.com.estagio.oletrainning.zup.otmovies.PreLoginActivity;


import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.estagio.oletrainning.zup.otmovies.CustomComponents.ErrorEditText;
import br.com.estagio.oletrainning.zup.otmovies.R;

class PreLoginViewHolder{

    Button buttonNextPreLogin;
    ErrorEditText errorEditTextEnterEmail;

    PreLoginViewHolder(View view) {
        buttonNextPreLogin = view.findViewById(R.id.button_nextPreLogin);
        errorEditTextEnterEmail = view.findViewById(R.id.errorEditText_enterEmail);
    }
}
