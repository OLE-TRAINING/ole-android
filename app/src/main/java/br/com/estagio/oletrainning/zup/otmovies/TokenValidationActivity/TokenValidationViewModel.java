package br.com.estagio.oletrainning.zup.otmovies.TokenValidationActivity;

import android.arch.lifecycle.ViewModel;

public class TokenValidationViewModel extends ViewModel {

    private final int MAXSIZETOKEN = 6;

    private boolean validateTokenSize(String tokenEntered) {
        return (tokenEntered.length() == MAXSIZETOKEN);
    }
}
