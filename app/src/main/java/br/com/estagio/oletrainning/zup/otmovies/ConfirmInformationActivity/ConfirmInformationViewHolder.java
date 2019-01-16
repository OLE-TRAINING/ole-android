package br.com.estagio.oletrainning.zup.otmovies.ConfirmInformationActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import br.com.estagio.oletrainning.zup.otmovies.CustomComponents.ComponentErrorEditText;
import br.com.estagio.oletrainning.zup.otmovies.R;

public class ConfirmInformationViewHolder {

    ImageView imageView;
    TextView textViewEmail;
    ComponentErrorEditText errorEditText;
    Button button;
    ProgressBar progressBar;
    TextView textViewForgotUserName;

    ConfirmInformationViewHolder (View view) {
        imageView = view.findViewById(R.id.imageView_backArrow);
        textViewEmail = view.findViewById(R.id.textView_emailEntered);
        errorEditText = view.findViewById(R.id.errorEditText_token);
        button = view.findViewById(R.id.button_next);
        progressBar = view.findViewById(R.id.progress_bar);
        textViewForgotUserName = view.findViewById(R.id.textView_username);
    }

}
