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
    protected final int MAX_SIZE_TOKEN = 6;
    protected final Integer MAX_SIZE_NAME = 50;
    protected final Integer MIN_SIZE_PASS = 6;
    protected final Integer MAX_SIZE_PASS = 10;
    protected final Integer MAX_SIZE_USERNAME = 15;
    protected String REGEX_FOR_NAME = "^[\\p{L} .'-]+$";
    protected String REGEX_ONLY_NUMBER_OR_LETTER = "[a-zA-Z0-9]+";
    protected String REGEX_ONLY_NUMBER_AND_LETTER = "(?:\\d+[a-z]|[a-z]+\\d)[a-z\\d]*";

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
    
    protected boolean isValidName(String name) {
        return validateName(name);
    }

    protected boolean validateName(String name) {
        return (!name.isEmpty() && validateNameFormat(name));
    }

    protected boolean validateNameFormat(String name) {
        return name.length() <= MAX_SIZE_NAME && name.matches(REGEX_FOR_NAME);
    }

    protected boolean isValidEmail(String email) {
        return validateEmail(email);
    }

    protected boolean validateEmail(String email) {
        return (!email.isEmpty() && validateEmailFormat(email));
    }

    protected boolean validateEmailFormat(final String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    protected boolean isValidUserName(String username) {
        return validateUserName(username);
    }
    protected boolean validateUserNameFormat(String userName) {
        return userName.length() <= MAX_SIZE_USERNAME && userName.matches(REGEX_ONLY_NUMBER_OR_LETTER);
    }

    protected boolean validateUserName(String userName) {
        return (!userName.isEmpty() && validateUserNameFormat(userName));
    }

    protected boolean isValidPassword(String password) {
        return validatePassword(password);
    }

    protected boolean validatePassword(String password) {
        return (!password.isEmpty() && validatePasswordFormat(password));
    }


    protected boolean isValidConfirmPassword(String newPassword,String confirmNewPassword){
        return validateConfirmPassword(newPassword,confirmNewPassword);
    }
    
    protected boolean validatePasswordFormat(String password) {
        return password.length() >= MIN_SIZE_PASS && password.length() <= MAX_SIZE_PASS && password.matches(REGEX_ONLY_NUMBER_AND_LETTER);
    }

    protected boolean validateMatchNewPassword(String newPassword,String confirmNewPassword) {
        return (newPassword.equals(confirmNewPassword));
    }

    protected boolean validateConfirmPassword(String newPassword,String confirmNewPassword) {
        return (!confirmNewPassword.isEmpty() && validateMatchNewPassword(newPassword, confirmNewPassword));
    }

    protected boolean isValidToken(String code){
        return validateTokenSize(code);
    }

    protected boolean validateTokenSize(String tokenEntered) {
        return (tokenEntered.length() == MAX_SIZE_TOKEN);
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

    public void removeTokenResendObserver() {
        if (tokenResend != null) {
            tokenResend.removeObserver(tokenResendObserver);
        }
    }
}

   

