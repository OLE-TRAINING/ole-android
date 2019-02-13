package br.com.estagio.oletrainning.zup.otmovies.TokenValidationActivity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.os.Bundle;
import android.support.annotation.Nullable;

import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ErrorMessage;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ResponseModel;

import br.com.estagio.oletrainning.zup.otmovies.Services.Repositories.ValidationRepository;

public class TokenValidationViewModel extends ViewModel {

    private ValidationRepository repository = new ValidationRepository();

    private Bundle bundle;

    private final int MAX_SIZE_TOKEN = 6;

    private String EMAIL_BUNDLE_KEY = "EmailPreLogin";
    private String SUCCESS_MESSAGE_VALIDATE_TOKEN = "Código confirmado com sucesso!";
    private String UNAUTHORIZED_TOKEN_KEY = "error.unauthorized.token";
    private String INVALID_TOKEN_KEY = "error.invalid.token";
    private String SERVICE_OR_CONNECTION_ERROR_VALIDATE_TOKEN = "Falha ao validar o código. Verifique a conexão e tente novamente.";
    private String SUCCESS_RESEND_TOKEN = "Código reenviado com sucesso!";
    private String SERVICE_OR_CONNECTION_ERROR_RESEND_TOKEN = "Falha ao reenviar o código. Verifique a conexão e tente novamente.";

    private LiveData<ResponseModel> tokenValidation;

    private LiveData<ResponseModel> tokenResend;

    private MutableLiveData<Boolean> tokenContainsErrorStatus = new MutableLiveData<>();

    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    private MutableLiveData<String> messageErrorChanged = new MutableLiveData<>();

    private MutableLiveData<String> isErrorMessageForToast = new MutableLiveData<>();

    private MutableLiveData<String> emailChanged = new MutableLiveData<>();

    private MutableLiveData<String> isValidatedToken = new MutableLiveData<>();

    public MutableLiveData<String> getIsValidatedToken() {
        return isValidatedToken;
    }

    public MutableLiveData<String> getEmailChanged() {
        return emailChanged;
    }

    public MutableLiveData<String> getIsErrorMessageForToast() {
        return isErrorMessageForToast;
    }

    public MutableLiveData<String> getMessageErrorChanged() {
        return messageErrorChanged;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MutableLiveData<Boolean> getTokenContainsErrorStatus() {
        return tokenContainsErrorStatus;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
        changeEmail(bundle.getString(EMAIL_BUNDLE_KEY));
    }

    private boolean validateTokenSize(String tokenEntered) {
        return (tokenEntered.length() == MAX_SIZE_TOKEN);
    }

    public void tokenTextChanged(){
        tokenContainsErrorStatus.postValue(false);
    }

    public void tokenEntered(String code){
        tokenContainsErrorStatus.postValue(!validateTokenSize(code));
        if (isValidToken(code)) {
            String email = bundle.getString(EMAIL_BUNDLE_KEY);
            executeServiceTokenValidation(email,code);
        }
    }

    public void tokenForwardingRequested(){
        String email = bundle.getString(EMAIL_BUNDLE_KEY);
        executeServiceTokenResend(email);
    }

    private boolean isValidToken(String code){
        return validateTokenSize(code);
    }

    private Observer<ResponseModel> tokenValidationObserver = new Observer<ResponseModel>() {
        @Override
        public void onChanged(@Nullable ResponseModel responseModel) {
            isLoading.setValue(false);
            if (responseModel != null) {
                if (responseModel.getCode() == 200) {
                    getIsValidatedToken().setValue(SUCCESS_MESSAGE_VALIDATE_TOKEN);
                } else {
                    ErrorMessage errorMessage = new ErrorMessage();
                    errorMessage.setKey(responseModel.getKey());
                    errorMessage.setMessage(responseModel.getMessage());
                    if(errorMessage.getKey().equals(UNAUTHORIZED_TOKEN_KEY)){
                        getMessageErrorChanged().setValue(errorMessage.getMessage());
                    } else if (errorMessage.getKey().equals(INVALID_TOKEN_KEY)) {
                        getMessageErrorChanged().setValue(errorMessage.getMessage());
                    } else{
                        getIsErrorMessageForToast().setValue(errorMessage.getMessage());
                    }
                }
            } else {
                getIsErrorMessageForToast().setValue(SERVICE_OR_CONNECTION_ERROR_VALIDATE_TOKEN);
            }
        }
    };

    private Observer<ResponseModel> tokenResendObserver = new Observer<ResponseModel>() {
        @Override
        public void onChanged(@Nullable ResponseModel responseModel) {
            isLoading.setValue(false);
            if (responseModel != null) {
                if (responseModel.getCode() == 200) {
                    getIsErrorMessageForToast().setValue(SUCCESS_RESEND_TOKEN);
                } else {
                    getIsErrorMessageForToast().setValue(responseModel.getMessage());
                }
            } else {
                getIsErrorMessageForToast().setValue(SERVICE_OR_CONNECTION_ERROR_RESEND_TOKEN);
            }
        }
    };

    private void changeEmail(String email){
        emailChanged.setValue(email);
    }

    private void executeServiceTokenValidation(String email,String code) {
        isLoading.setValue(true);
        tokenValidation = repository.confirmToken(email,code);
        tokenValidation.observeForever(tokenValidationObserver);
    }

    private void executeServiceTokenResend(String email) {
        isLoading.setValue(true);
        tokenResend = repository.resendToken(email);
        tokenResend.observeForever(tokenResendObserver);
    }

    public void removeObserver() {
        if (tokenValidation != null && tokenResend != null) {
            tokenValidation.removeObserver(tokenValidationObserver);
            tokenResend.removeObserver(tokenResendObserver);
        }
    }
}