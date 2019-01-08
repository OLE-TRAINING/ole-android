package br.com.estagio.oletrainning.zup.otmovies.TokenValidationActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.estagio.oletrainning.zup.otmovies.CustomComponents.ComponentErrorEditText;
import br.com.estagio.oletrainning.zup.otmovies.R;

public class TokenValidationViewHolder {

    ImageView imageView;
    TextView textViewEmail;
    ComponentErrorEditText errorEditText;
    TextView textViewReSendToken;
    Button button;

    TokenValidationViewHolder(View view){
        imageView = view.findViewById(R.id.imageView_backArrow);
        textViewEmail = view.findViewById(R.id.textView_emailEntered);
        errorEditText = view.findViewById(R.id.errorEditText_token);
        textViewReSendToken = view.findViewById(R.id.textView_ReSendToken);
        button = view.findViewById(R.id.button_validate);
    }
}
