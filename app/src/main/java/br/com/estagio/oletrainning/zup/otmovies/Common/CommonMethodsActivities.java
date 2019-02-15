package br.com.estagio.oletrainning.zup.otmovies.Common;

import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import br.com.estagio.oletrainning.zup.otmovies.CustomComponents.AsyncTaskProgressBar.SyncProgressBar;
import br.com.estagio.oletrainning.zup.otmovies.PreLoginActivity.PreLoginActivity;

import static android.support.v4.content.ContextCompat.getColor;

public class CommonMethodsActivities {

    public void hideKeyword(Window window){
        window.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void colorStatusBar(Window window, Context context, int color, Boolean isClearColor) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        View view = window.getDecorView();
        if(isClearColor){
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            view.setSystemUiVisibility(View.GONE);
        }
        window.setStatusBarColor(getColor(context,color));
    }

    public void hideKeyboardFrom(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(PreLoginActivity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void loadingExecutor(Boolean isLoading, ProgressBar progressBar, Window window, Context context){
        if (isLoading != null) {
            if (isLoading) {
                progressBar.setVisibility(View.VISIBLE);
                window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                new SyncProgressBar(context,progressBar).execute();
            } else {
                progressBar.setProgress(100);
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        }
    }
}
