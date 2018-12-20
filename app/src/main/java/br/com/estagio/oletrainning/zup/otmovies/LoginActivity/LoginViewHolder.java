package br.com.estagio.oletrainning.zup.otmovies.LoginActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.estagio.oletrainning.zup.otmovies.CustomComponents.ComponentErrorEditText;
import br.com.estagio.oletrainning.zup.otmovies.R;

public class LoginViewHolder {

        ImageView imageViewBackArrow;
        TextView textViewEmailEntered;
        ComponentErrorEditText customComponentErrorEditTextPassword;
        TextView textViewForgetPassword;
        Button buttonSignIn;

        LoginViewHolder(View view) {
            imageViewBackArrow = view.findViewById(R.id.imageView_backArrow);
            textViewEmailEntered = view.findViewById(R.id.textView_emailEntered);
            textViewForgetPassword = view.findViewById(R.id.textView_password);
            customComponentErrorEditTextPassword = view.findViewById(R.id.errorEditText_login);
            buttonSignIn = view.findViewById(R.id.button_sign_in);
        }
    }

