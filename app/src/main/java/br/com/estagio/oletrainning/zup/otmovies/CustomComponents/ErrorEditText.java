package br.com.estagio.oletrainning.zup.otmovies.CustomComponents;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.estagio.oletrainning.zup.otmovies.R;

public class ErrorEditText extends ConstraintLayout {

    private EditText editTextField;
    private ImageView imageViewAlert;
    private TextView textViewErrorMessage;

    public void setEditTextField(EditText editTextField) {
        this.editTextField = editTextField;
    }

    public void setImageViewAlert(ImageView imageViewAlert) {
        this.imageViewAlert = imageViewAlert;
    }

    public void setTextViewErrorMessage(TextView textViewErrorMessage) {
        this.textViewErrorMessage = textViewErrorMessage;
    }

    public ErrorEditText(Context context) {
        super(context);
    }

    public ErrorEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.editTextField = findViewById(R.id.editText_ErrorEditText);
        this.imageViewAlert = findViewById(R.id.imageView_ErrorEditText);
        this.textViewErrorMessage = findViewById(R.id.textView_ErrorEditText);

    }

    public ErrorEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setErrorVisibility (boolean visible, boolean isValid ){
        if(visible && !isValid){
            this.editTextField.setBackground(getResources().getDrawable(R.drawable.border_input_error,null));
            this.textViewErrorMessage.setVisibility(View.VISIBLE);
            this.imageViewAlert.setVisibility(View.VISIBLE);
        } else {
            this.editTextField.setBackground(getResources().getDrawable(R.drawable.border_input,null));
            this.textViewErrorMessage.setVisibility(View.INVISIBLE);
            this.imageViewAlert.setVisibility(View.INVISIBLE);
        }
    }

    public void setHintEditText(String text){
        editTextField.setHint(text);
    }

    public void setMessageError(String text){
        textViewErrorMessage.setText(text);
    }
}
