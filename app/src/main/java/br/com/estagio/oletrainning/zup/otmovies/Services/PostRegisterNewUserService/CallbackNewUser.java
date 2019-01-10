package br.com.estagio.oletrainning.zup.otmovies.Services.PostRegisterNewUserService;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ResourceBundle;

import br.com.estagio.oletrainning.zup.otmovies.CustomComponents.ComponentErrorEditText;
import br.com.estagio.oletrainning.zup.otmovies.LoginActivity.LoginActivity;
import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.RegisterNewUserActivity.RegisterNewUserViewHolder;
import br.com.estagio.oletrainning.zup.otmovies.Services.ErrorMessage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallbackNewUser implements Callback<Void> {

    private Context currentActivity;
    private ErrorMessage errorMessage;

    public CallbackNewUser(Context currentActivity){
        this.currentActivity = currentActivity;
        this.errorMessage = new ErrorMessage();
    }

    @Override
    public void onResponse(Call call, Response response) {
        showResponse(response);
    }

    @Override
    public void onFailure(Call call, Throwable t) {
        Toast.makeText(currentActivity,"Falha ao criar usuário", Toast.LENGTH_LONG).show();
    }

    private void showResponse(Response response){
        ErrorMessage errorMessage = new ErrorMessage();

        if((response.code() == 200)){
            errorMessage.setKey("200");
            errorMessage.setMessage("Usuário cadastrado com Sucesso");
        } else {
            try {
                Gson gson = new Gson();
                Type type = new TypeToken<ErrorMessage>() {}.getType();
                errorMessage =  gson.fromJson(response.errorBody().charStream(),type);
            } catch (Exception e) {
                errorMessage.setMessage(e.getMessage());
            }
        }
        this.errorMessage.setMessage(errorMessage.getMessage());
        this.errorMessage.setMessage(errorMessage.getKey());
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }
}
