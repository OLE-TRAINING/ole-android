package br.com.estagio.oletrainning.zup.otmovies.Common;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ResponseModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.UserData;
import br.com.estagio.oletrainning.zup.otmovies.Services.Repositories.ValidationRepository;
import br.com.estagio.oletrainning.zup.otmovies.Services.Singleton.SingletonEmail;

public abstract class CommonViewModel extends ViewModel {

    protected ValidationRepository validationRepository = new ValidationRepository();
    protected int SUCCESS_CODE = 200;
    protected int SESSION_EXPIRED_CODE = 401;
    protected String SUCCESS_RESEND_TOKEN = "Código reenviado com sucesso!";
    protected String SERVICE_OR_CONNECTION_ERROR_RESEND_TOKEN = "Falha ao reenviar o código. Verifique a conexão e tente novamente.";

    protected MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    protected LiveData<ResponseModel<UserData>> tokenResend;

    protected MutableLiveData<String> isErrorMessageForToast = new MutableLiveData<>();

    protected MutableLiveData<String> forwardedToken = new MutableLiveData<>();

    private MutableLiveData<String> isMessageSuccessForToast = new MutableLiveData<>();

    public MutableLiveData<String> getIsMessageSuccessForToast() {
        return isMessageSuccessForToast;
    }

    public MutableLiveData<String> getIsErrorMessageForToast() {
        return isErrorMessageForToast;
    }

    public LiveData<ResponseModel<UserData>> getTokenResend() {
        return tokenResend;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MutableLiveData<String> getForwardedToken() {
        return forwardedToken;
    }

    protected Observer<ResponseModel<UserData>> tokenResendObserver = new Observer<ResponseModel<UserData>>() {
        @Override
        public void onChanged(@Nullable ResponseModel<UserData> responseModel) {
            isLoading.setValue(false);
            if (responseModel != null) {
                if (responseModel.getCode() == SUCCESS_CODE) {
                    getIsMessageSuccessForToast().setValue(SUCCESS_RESEND_TOKEN);
                    getForwardedToken().setValue(SUCCESS_RESEND_TOKEN);
                } else {
                    getIsErrorMessageForToast().setValue(responseModel.getErrorMessage().getMessage());
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
        String email = SingletonEmail.INSTANCE.getEmail();
        executeServiceTokenResend(email);
    }

    public void removeObserver() {
        if (tokenResend != null) {
            tokenResend.removeObserver(tokenResendObserver);
        }
    }
}