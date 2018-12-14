package br.com.estagio.oletrainning.zup.otmovies;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.estagio.oletrainning.zup.otmovies.CustomComponents.ErrorEditText;

public class TokenValidationActivity extends AppCompatActivity {

    private EditText editTextField;
    private ImageView imageViewAlert;
    private TextView textViewErrorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_validation);
        ErrorEditText errorEditText = new ErrorEditText(this,getResources().getLayout(R.layout.error_edit_text));

        this.editTextField = new EditText(this);
        this.editTextField = findViewById(R.id.editText_ErrorEditText);
        this.imageViewAlert = new ImageView(this);
        this.imageViewAlert = findViewById(R.id.imageView_ErrorEditText);
        this.textViewErrorMessage = new TextView(this);
        this.textViewErrorMessage = findViewById(R.id.textView_ErrorEditText);

        errorEditText.setEditTextField(editTextField);
        errorEditText.setImageViewAlert(imageViewAlert);
        errorEditText.setTextViewErrorMessage(textViewErrorMessage);
    }
}
