package br.com.estagio.oletrainning.zup.otmovies.Services.Singleton;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public enum SingletonAccessToken {

    INSTANCE;

    private String accessToken;

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public static void saveAccessToken(String token){
        SingletonAccessToken singletonAccessToken = SingletonAccessToken.INSTANCE;
        singletonAccessToken.setAccessToken(token);
        if (singletonAccessToken.accessToken != null){
            Log.d("SINGLETON_TOKEN_SAVED", singletonAccessToken.accessToken);
        }

    }

    public String getLastestAuth(){
        if(accessToken != null && !accessToken.isEmpty()){
            return accessToken;
        }
        return null;
    }
}
