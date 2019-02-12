package br.com.estagio.oletrainning.zup.otmovies.TokenValidationActivity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.widget.Toast;

import br.com.estagio.oletrainning.zup.otmovies.LoginActivity.LoginActivity;
import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ErrorMessage;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ResponseModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.Repositories.ValidationRepository;

public class TokenValidationViewModel extends ViewModel {

    private ValidationRepository repository = new ValidationRepository();

    private final int MAX_SIZE_TOKEN = 6;

    private String SUCCESS_MESSAGE_VALIDATE_TOKEN = "Código confirmado com sucesso!";

    private String UNAUTHORIZED_TOKEN_KEY = "";

    private LiveData<ResponseModel> tokenResponseObservable;

    private LiveData<ResponseModel> tokenresendResponseObservable;

    private MutableLiveData<Boolean> tokenContainsErrorStatus = new MutableLiveData<>();

    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    private MutableLiveData<String> isValidToken = new MutableLiveData<>();

    public MutableLiveData<String> getIsValidToken() {
        return isValidToken;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MutableLiveData<Boolean> getTokenContainsErrorStatus() {
        return tokenContainsErrorStatus;
    }

    public LiveData<ResponseModel> tokenValidation(String email, String code) {
        tokenResponseObservable = repository.confirmToken(email,code);
        return tokenResponseObservable;
    }

    public LiveData<ResponseModel> resendToken(String email) {
        tokenresendResponseObservable = repository.resendToken(email);
        return tokenresendResponseObservable;
    }

    private boolean validateTokenSize(String tokenEntered) {
        return (tokenEntered.length() == MAX_SIZE_TOKEN);
    }

    public void serviceStarting(){
        isLoading.postValue(true);
    }

    public void serviceEnding(){
        isLoading.postValue(false);
    }

    public void tokenTextChanged(){
        tokenContainsErrorStatus.postValue(false);
    }

    public void tokenEntered(String code){
        tokenContainsErrorStatus.postValue(!validateTokenSize(code));
    }

    public boolean isValidToken(String code){
        return validateTokenSize(code);
    }

    /*Observer<ResponseModel> serviceCallTokenValidation = new Observer<ResponseModel>() {
        @Override
        public void onChanged(@Nullable ResponseModel responseModel) {
            serviceEnding();
            if (responseModel != null) {
                if (responseModel.getCode() == 200) {
                    getIsValidToken().setValue(SUCCESS_MESSAGE_VALIDATE_TOKEN);
                } else {
                    ErrorMessage errorMessage = new ErrorMessage();
                    errorMessage.setKey(responseModel.getKey());
                    errorMessage.setMessage(responseModel.getMessage());
                    if(errorMessage.getKey().equals(getString(R.string.unauthorized_token_key))){
                        tokenValidationViewHolder.errorEditText.setMessageError(errorMessage.getMessage());
                        tokenValidationViewHolder.errorEditText.setErrorVisibility(true);
                    } else if (errorMessage.getKey().equals(getString(R.string.invalid_token_key))) {
                        tokenValidationViewHolder.errorEditText.setMessageError(errorMessage.getMessage());
                        tokenValidationViewHolder.errorEditText.setErrorVisibility(true);
                    } else{
                        Toast.makeText(TokenValidationActivity.this, errorMessage.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                Toast.makeText(TokenValidationActivity.this, getString(R.string.service_or_connection_error_validate_token), Toast.LENGTH_LONG).show();
            }
        }
    };*/
}