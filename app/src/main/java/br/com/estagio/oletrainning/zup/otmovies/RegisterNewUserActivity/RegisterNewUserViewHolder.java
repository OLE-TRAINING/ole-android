package br.com.estagio.oletrainning.zup.otmovies.RegisterNewUserActivity;


import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.estagio.oletrainning.zup.otmovies.R;

class RegisterNewUserViewHolder {

     ImageView imageViewBackArrow;
     TextView textViewEmailEntered;
     EditText editTextEnterNameRegister;
     ImageView imageViewEnterNameRegisterError;
     TextView textViewEnterNameRegisterError;
     EditText editTextEnterUserName;
     TextView textViewEnterUserNameError;
     ImageView imageViewEnterUserNameError;
     EditText editTextEnterPassword;
     TextView textViewEnterPasswordError;
     ImageView imageViewEnterPasswordError;
     Button buttonNextRegister;

    RegisterNewUserViewHolder(View view) {
        textViewEmailEntered = view.findViewById(R.id.textView_emailEntered);
        imageViewBackArrow = view.findViewById(R.id.imageView_backArrow);
        imageViewEnterNameRegisterError = view.findViewById(R.id.imageView_enterNameRegisterError);
        editTextEnterNameRegister = view.findViewById(R.id.editText_enterName);
        editTextEnterUserName = view.findViewById(R.id.editText_enterUserName);
        textViewEnterUserNameError = view.findViewById(R.id.textView_enterUserNameError);
        textViewEnterNameRegisterError = view.findViewById(R.id.textView_enterNameRegisterError);
        imageViewEnterUserNameError = view.findViewById(R.id.imageView_enterUserNameError);
        editTextEnterPassword = view.findViewById(R.id.editText_enterPassword);
        textViewEnterPasswordError = view.findViewById(R.id.textView_enterPasswordError);
        imageViewEnterPasswordError = view.findViewById(R.id.imageView_enterPasswordError);
        buttonNextRegister = view.findViewById(R.id.button_nextRegister);
    }
}
