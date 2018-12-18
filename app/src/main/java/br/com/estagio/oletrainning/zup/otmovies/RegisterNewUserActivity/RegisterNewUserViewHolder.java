package br.com.estagio.oletrainning.zup.otmovies.RegisterNewUserActivity;


import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.estagio.oletrainning.zup.otmovies.CustomComponents.ErrorEditText;
import br.com.estagio.oletrainning.zup.otmovies.R;

class RegisterNewUserViewHolder {

    ImageView imageViewBackArrow;
    TextView textViewEmailEntered;
    ErrorEditText errorEditTextEnterNameRegister;
    ErrorEditText errorEditTextEnterUserName;
    ErrorEditText errorEditTextEnterPassword;
    Button buttonNextRegister;

    RegisterNewUserViewHolder(View view) {
        imageViewBackArrow = view.findViewById(R.id.imageView_backArrow);
        textViewEmailEntered = view.findViewById(R.id.textView_emailEntered);
        errorEditTextEnterNameRegister = view.findViewById(R.id.errorEditText_enterName);
        errorEditTextEnterUserName = view.findViewById(R.id.errorEditText_enterUserName);
        errorEditTextEnterPassword = view.findViewById(R.id.errorEditText_enterPassword);
        buttonNextRegister = view.findViewById(R.id.button_nextRegister);
    }
}
