package br.com.estagio.oletrainning.zup.otmovies.InformTokenAndNewPasswordActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.estagio.oletrainning.zup.otmovies.CustomComponents.ComponentErrorEditText;
import br.com.estagio.oletrainning.zup.otmovies.R;

public class InformTokenAndNewPasswordViewHolder {

    private ImageView imageView;
    private TextView textViewEmail;
    private ComponentErrorEditText componentErrorEditTextToken;
    private TextView textViewReSendToken;
    private ComponentErrorEditText componentErrorEditTextPassword;
    private ComponentErrorEditText componentErrorEditTextConfirmPassword;
    private Button button;

    InformTokenAndNewPasswordViewHolder (View view){
        imageView = view.findViewById(R.id.imageView_backArrow);
        textViewEmail = view.findViewById(R.id.textView_emailEntered);
        componentErrorEditTextToken = view.findViewById(R.id.errorEditText_token);
        textViewReSendToken = view.findViewById(R.id.textView_ReSendToken);
        componentErrorEditTextPassword = view.findViewById(R.id.errorEditText_password);
        componentErrorEditTextConfirmPassword = view.findViewById(R.id.errorEditText_confirmPassword);
        button = view.findViewById(R.id.button_changePassword);
    }
}
