package br.com.estagio.oletrainning.zup.otmovies.PreLoginActivity;


import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.estagio.oletrainning.zup.otmovies.R;

class PreLoginViewHolder{

    EditText editTextEnterEmail;
    ImageView imageViewEmailErrorPreLogin;
    TextView textViewEmailErrorPreLogin;
    Button buttonNextPreLogin;

    PreLoginViewHolder(View view) {
        editTextEnterEmail = view.findViewById(R.id.editText_enterEmail);
        textViewEmailErrorPreLogin = view.findViewById(R.id.textView_EmailErrorPreLogin);
        imageViewEmailErrorPreLogin = view.findViewById(R.id.imageView_EmailErrorPreLogin);
        buttonNextPreLogin = view.findViewById(R.id.button_nextPreLogin);
    }
}
