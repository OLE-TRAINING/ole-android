package br.com.estagio.oletrainning.zup.otmovies.RegisterNewUserActivity;


import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.estagio.oletrainning.zup.otmovies.CustomComponents.ComponentErrorEditText;
import br.com.estagio.oletrainning.zup.otmovies.R;

class RegisterNewUserViewHolder {

    ImageView imageViewBackArrow;
    TextView textViewEmailEntered;
    ComponentErrorEditText errorEditTextName;
    ComponentErrorEditText errorEditTextUserName;
    ComponentErrorEditText errorEditTextPassword;
    Button buttonNextRegister;

    RegisterNewUserViewHolder(View view) {
        imageViewBackArrow = view.findViewById(R.id.imageView_backArrow);
        textViewEmailEntered = view.findViewById(R.id.textView_emailEntered);
        errorEditTextName = view.findViewById(R.id.errorEditText_enterName);
        errorEditTextUserName = view.findViewById(R.id.errorEditText_enterUserName);
        errorEditTextPassword = view.findViewById(R.id.errorEditText_enterPassword);
        buttonNextRegister = view.findViewById(R.id.button_nextRegister);
    }
}
