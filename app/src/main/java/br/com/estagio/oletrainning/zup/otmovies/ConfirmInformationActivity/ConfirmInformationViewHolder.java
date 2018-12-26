package br.com.estagio.oletrainning.zup.otmovies.ConfirmInformationActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.estagio.oletrainning.zup.otmovies.CustomComponents.ComponentErrorEditText;
import br.com.estagio.oletrainning.zup.otmovies.R;

public class ConfirmInformationViewHolder {

    private ImageView imageView;
    private TextView textViewEmail;
    private ComponentErrorEditText componentErrorEditText;
    private Button button;

    ConfirmInformationViewHolder (View view) {
        imageView = view.findViewById(R.id.imageView_backArrow);
        textViewEmail = view.findViewById(R.id.textView_emailEntered);
        componentErrorEditText = view.findViewById(R.id.errorEditText_token);
        button = view.findViewWithTag(R.id.button_next);
    }

}
