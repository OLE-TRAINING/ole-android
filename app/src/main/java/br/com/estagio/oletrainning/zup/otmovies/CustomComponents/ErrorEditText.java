package br.com.estagio.oletrainning.zup.otmovies.CustomComponents;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.constraint.ConstraintLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.estagio.oletrainning.zup.otmovies.R;


public class ErrorEditText extends ConstraintLayout {

    private View rootView;
    private EditText editTextField;
    private ImageView imageViewAlert;
    private TextView textViewErrorMessage;
    private final int TYPE_CLASS_TEXT = 0x00000001;

    public ErrorEditText(Context context) {
        super(context);
        init(context);
    }

    public ErrorEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        TypedArray typedArray = context.obtainStyledAttributes(
                attrs, R.styleable.ErrorEditText);

        String hintTextEditText = typedArray.getString(
                R.styleable.ErrorEditText_setHint);
        if (hintTextEditText != null) {
            editTextField.setHint(hintTextEditText);
        }

        String errorMessageTextView = typedArray.getString(
                R.styleable.ErrorEditText_setMessageError);
        if (errorMessageTextView != null) {
            textViewErrorMessage.setText(errorMessageTextView);
        }

        Boolean defaultErrorVisibility = typedArray.getBoolean(R.styleable.ErrorEditText_setInicialErrorVisibility,Boolean.FALSE);
            if(defaultErrorVisibility){
                setErrorVisibility (true);
            } else {
                setErrorVisibility (false);
            }

        Integer setInputType = typedArray.getInt(R.styleable.ErrorEditText_setInputType,TYPE_CLASS_TEXT);
        editTextField.setInputType(setInputType);

        typedArray.recycle();
    }


    public ErrorEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public Editable getText(){
        return editTextField.getText();
    }

    public void init(Context context) {
        rootView = inflate(context,R.layout.error_edit_text,this);
        editTextField = rootView.findViewById(R.id.editText_ErrorEditText);
        imageViewAlert = rootView.findViewById(R.id.imageView_ErrorEditText);
        textViewErrorMessage = rootView.findViewById(R.id.textView_ErrorEditText);

        TextWatcher editTextTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setErrorVisibility (false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        editTextField.addTextChangedListener(editTextTextChangedListener);
    }

    public void setErrorVisibility (boolean visible){
        if(visible){
            editTextField.setBackground(getResources().getDrawable(R.drawable.border_input_error,null));
            textViewErrorMessage.setVisibility(View.VISIBLE);
            imageViewAlert.setVisibility(View.VISIBLE);
        } else {
            editTextField.setBackground(getResources().getDrawable(R.drawable.border_input,null));
            textViewErrorMessage.setVisibility(View.INVISIBLE);
            imageViewAlert.setVisibility(View.INVISIBLE);
        }
    }
}