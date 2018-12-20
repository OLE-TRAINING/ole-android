package br.com.estagio.oletrainning.zup.otmovies.CustomComponents;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.constraint.ConstraintLayout;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import br.com.estagio.oletrainning.zup.otmovies.R;


public class ComponentErrorEditText extends ConstraintLayout {

    private View view;
    private EditText editText;
    private TextView textView;
    private final int TYPE_CLASS_TEXT = 0x00000001;

    public ComponentErrorEditText(Context context) {
        super(context);
        init(context);
    }

    public EditText getEditText() {
        return editText;
    }

    public ComponentErrorEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        TypedArray typedArray = context.obtainStyledAttributes(
                attrs, R.styleable.ComponentErrorEditText);

        String hintTextEditText = typedArray.getString(
                R.styleable.ComponentErrorEditText_setHint);
        if (hintTextEditText != null) {
            editText.setHint(hintTextEditText);
        }

        String errorMessageTextView = typedArray.getString(
                R.styleable.ComponentErrorEditText_setMessageError);
        if (errorMessageTextView != null) {
            textView.setText(errorMessageTextView);
        }

        Boolean defaultErrorVisibility = typedArray.getBoolean(R.styleable.ComponentErrorEditText_setInicialErrorVisibility,Boolean.FALSE);
            if(defaultErrorVisibility){
                setErrorVisibility (true);
            } else {
                setErrorVisibility (false);
            }

        Integer setInputType = typedArray.getInt(R.styleable.ComponentErrorEditText_setInputType,TYPE_CLASS_TEXT);
        editText.setInputType(setInputType);

        typedArray.recycle();
    }


    public ComponentErrorEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public Editable getText(){
        return editText.getText();
    }

    public void init(Context context) {
        view = inflate(context,R.layout.component_error_edit_text,this);
        editText = view.findViewById(R.id.editText_ErrorEditText);
        textView = view.findViewById(R.id.textView_ErrorEditText);
    }

    public void setErrorVisibility (boolean visible){
        if(visible){
            editText.setBackground(getResources().getDrawable(R.drawable.border_input_error,null));
            textView.setVisibility(View.VISIBLE);
        } else {
            editText.setBackground(getResources().getDrawable(R.drawable.border_input,null));
            textView.setVisibility(View.INVISIBLE);
        }
    }
}