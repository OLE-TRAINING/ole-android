package br.com.estagio.oletrainning.zup.otmovies.ui.finishYourRegistrationActivity;

import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import br.com.estagio.oletrainning.zup.otmovies.utils.ErrorEditText;
import br.com.estagio.oletrainning.zup.otmovies.R;

public class FinishYourRegistrationViewHolder {

    ImageView imageView;
    TextView textViewEmail;
    ErrorEditText errorEditText;
    TextView textViewReSend;
    Button button;
    ProgressBar progressBar;
    FrameLayout frameLayout;

    FinishYourRegistrationViewHolder(View view){
        imageView = view.findViewById(R.id.imageView_backArrow);
        textViewEmail = view.findViewById(R.id.textView_emailEntered);
        errorEditText = view.findViewById(R.id.errorEditText_token);
        textViewReSend = view.findViewById(R.id.textView_ReSendToken);
        button = view.findViewById(R.id.button_validate);
        progressBar = view.findViewById(R.id.progress_bar);
        frameLayout = view.findViewById(R.id.loading_layout);
    }
}
