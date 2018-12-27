package br.com.estagio.oletrainning.zup.otmovies.FinishYourRegistrationActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.estagio.oletrainning.zup.otmovies.CustomComponents.ComponentErrorEditText;
import br.com.estagio.oletrainning.zup.otmovies.R;

public class FinishYourRegistrationViewHolder {

    ImageView imageView;
    TextView textViewEmail;
    ComponentErrorEditText errorEditText;
    TextView textViewReSend;
    Button button;

    FinishYourRegistrationViewHolder(View view){
        imageView = view.findViewById(R.id.imageView_backArrow);
        textViewEmail = view.findViewById(R.id.textView_emailEntered);
        errorEditText = view.findViewById(R.id.errorEditText_token);
        textViewReSend = view.findViewById(R.id.textView_ReSendToken);
        button = view.findViewById(R.id.button_validate);
    }
}
