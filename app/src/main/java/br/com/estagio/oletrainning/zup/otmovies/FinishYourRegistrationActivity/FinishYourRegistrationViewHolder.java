package br.com.estagio.oletrainning.zup.otmovies.FinishYourRegistrationActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.estagio.oletrainning.zup.otmovies.CustomComponents.ComponentErrorEditText;
import br.com.estagio.oletrainning.zup.otmovies.R;

public class FinishYourRegistrationViewHolder {

    private ImageView imageView;
    private TextView textViewEmail;
    private ComponentErrorEditText componentErrorEditText;
    private TextView textViewReSend;
    private Button button;

    FinishYourRegistrationViewHolder(View view){
        imageView = view.findViewById(R.id.imageView_backArrow);
        textViewEmail = view.findViewById(R.id.textView_emailEntered);
        componentErrorEditText = view.findViewById(R.id.errorEditText_token);
        textViewReSend = view.findViewById(R.id.textView_ReSendToken);
        button = view.findViewById(R.id.button_validate);
    }
}
