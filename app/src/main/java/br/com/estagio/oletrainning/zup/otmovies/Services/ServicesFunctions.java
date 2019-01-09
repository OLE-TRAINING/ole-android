package br.com.estagio.oletrainning.zup.otmovies.Services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServicesFunctions {
    private ErrorMessage errorMessage = new ErrorMessage();

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }

    public ErrorMessage isPostRequestOk(UserDates body, RegisterNewUserService regNewUserService, Call<Void> voidCall) {
        voidCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                    if(response.raw().code() == 200){
                        getErrorMessage().setMessage("Usuário cadastrado com sucesso!");
                    } else {
                            try {
                                Gson gson = new Gson();
                                Type type = new TypeToken<ErrorMessage>() {}.getType();
                                ErrorMessage errorMessage = new ErrorMessage();
                                errorMessage = gson.fromJson(response.errorBody().charStream(),type);
                                getErrorMessage().setMessage(errorMessage.getMessage());
                            } catch (Exception e) {
                                getErrorMessage().setMessage(e.getMessage());
                            }
                    }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                getErrorMessage().setMessage("Falha ao criar usuário");
            }
        });
        return getErrorMessage();
    }
}
