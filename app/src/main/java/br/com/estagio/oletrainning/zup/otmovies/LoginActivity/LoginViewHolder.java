package br.com.estagio.oletrainning.zup.otmovies.LoginActivity;

import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.estagio.oletrainning.zup.otmovies.CustomComponents.ComponentErrorEditText;
import br.com.estagio.oletrainning.zup.otmovies.R;

public class LoginViewHolder {

        ImageView imageViewBackArrow;
        TextView textViewEmailEntered;
        ComponentErrorEditText errorEditTextPassword;
        TextView textViewForgetPassword;
        LinearLayout linearLayout;
        Button buttonSignIn;
        ConstraintLayout constraintLayout;


        LoginViewHolder(View view) {
            imageViewBackArrow = view.findViewById(R.id.imageView_backArrow);
            textViewEmailEntered = view.findViewById(R.id.textView_emailEntered);
            textViewForgetPassword = view.findViewById(R.id.textView_password);
            errorEditTextPassword = view.findViewById(R.id.errorEditText_login);
            constraintLayout = view.findViewById(R.id.layout_login);
            linearLayout = view.findViewById(R.id.linearLayout_red_toast);
            buttonSignIn = view.findViewById(R.id.button_sign_in);
        }
    }

