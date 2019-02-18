package br.com.estagio.oletrainning.zup.otmovies.Common;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.os.Bundle;
import android.support.annotation.Nullable;

import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ResponseModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.Repositories.ValidationRepository;

public class CommonViewModel extends ViewModel {

    protected ValidationRepository validationRepository = new ValidationRepository();

    protected Bundle bundle;
    protected String EMAIL_BUNDLE_KEY = "EmailPreLogin";

    protected String SUCCESS_RESEND_TOKEN = "Código reenviado com sucesso!";
    protected String SERVICE_OR_CONNECTION_ERROR_RESEND_TOKEN = "Falha ao reenviar o código. Verifique a conexão e tente novamente.";

    protected MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    protected MutableLiveData<String> emailChanged = new MutableLiveData<>();

    protected LiveData<ResponseModel> tokenResend;

    protected MutableLiveData<String> isErrorMessageForToast = new MutableLiveData<>();

    protected MutableLiveData<String> forwardedToken = new MutableLiveData<>();

    public MutableLiveData<String> getIsErrorMessageForToast() {
        return isErrorMessageForToast;
    }

    public LiveData<ResponseModel> getTokenResend() {
        return tokenResend;
    }

    public MutableLiveData<String> getEmailChanged() {
        return emailChanged;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MutableLiveData<String> getForwardedToken() {
        return forwardedToken;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
        changeEmail(bundle.getString(EMAIL_BUNDLE_KEY));
    }

    protected void changeEmail(String email){
        emailChanged.setValue(email);
    }

    protected Observer<ResponseModel> tokenResendObserver = new Observer<ResponseModel>() {
        @Override
        public void onChanged(@Nullable ResponseModel responseModel) {
            isLoading.setValue(false);
            if (responseModel != null) {
                if (responseModel.getCode() == 200) {
                    getIsErrorMessageForToast().setValue(SUCCESS_RESEND_TOKEN);
                    getForwardedToken().setValue(SUCCESS_RESEND_TOKEN);
                } else {
                    getIsErrorMessageForToast().setValue(responseModel.getMessage());
                }
            } else {
                getIsErrorMessageForToast().setValue(SERVICE_OR_CONNECTION_ERROR_RESEND_TOKEN);
            }
        }
    };

    protected void executeServiceTokenResend(String email) {
        isLoading.setValue(true);
        tokenResend = validationRepository.resendToken(email);
        tokenResend.observeForever(tokenResendObserver);
    }

    public void tokenForwardingRequested(){
        String email = bundle.getString(EMAIL_BUNDLE_KEY);
        executeServiceTokenResend(email);
    }

    public void removeObserver() {
        if (tokenResend != null) {
            tokenResend.removeObserver(tokenResendObserver);
        }
    }
}