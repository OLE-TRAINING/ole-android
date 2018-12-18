package br.com.estagio.oletrainning.zup.otmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.estagio.oletrainning.zup.otmovies.CustomComponents.ErrorEditText;

public class TokenValidationActivity extends AppCompatActivity {

    private EditText editText;
    private ImageView imageView;
    private TextView textView;
    ErrorEditText errorEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_validation);

        this.editText = findViewById(R.id.editText_ErrorEditText);
        this.imageView = findViewById(R.id.imageView_ErrorEditText);
        this.textView =  findViewById(R.id.textView_ErrorEditText);

    }
}
