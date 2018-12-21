package br.com.estagio.oletrainning.zup.otmovies.CustomComponents;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.estagio.oletrainning.zup.otmovies.R;

public class ComponentRedToast extends LinearLayout {

    private View view;
    private TextView textView;

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public ComponentRedToast(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init (Context context){
        view = inflate(context,R.layout.component_red_toast,this);
        textView = view.findViewById(R.id.textView_red_toast);
    }
}
