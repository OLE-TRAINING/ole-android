package br.com.estagio.oletrainning.zup.otmovies.Services.Singleton;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public enum SingletonAccessToken {

    INSTANCE;

    private List <String> accessToken;

    SingletonAccessToken() {
        this.accessToken = new ArrayList<>();
    }

    public boolean isTokenSaved(String token){
        return accessToken.add(token);
    }

    public static void saveAccessToken(String token){
        SingletonAccessToken singletonAccessToken = SingletonAccessToken.INSTANCE;
        Log.d("SINGLETON_TOKEN_SAVED", String.valueOf(singletonAccessToken.isTokenSaved(token)));
    }

    public String getLastestAuth(){
        if(accessToken.size()>0){
            return accessToken.get(accessToken.size()-1);
        }
        return null;
    }
}
