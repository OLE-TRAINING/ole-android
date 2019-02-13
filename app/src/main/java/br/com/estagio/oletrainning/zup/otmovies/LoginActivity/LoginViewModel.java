package br.com.estagio.oletrainning.zup.otmovies.LoginActivity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;

import android.os.Bundle;
import android.support.annotation.Nullable;

import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ResponseModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.Repositories.ValidationRepository;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.UserData;

public class LoginViewModel extends ViewModel {

    private final Integer MIN_SIZE_PASS = 6;
    private final Integer MAX_SIZE_PASS = 10;

    private String EMAIL_BUNDLE_KEY = "EmailPreLogin";
    private String REGEX_ONLY_NUMBER_AND_LETTER = "(?:\\d+[a-z]|[a-z]+\\d)[a-z\\d]*";
    private String KEY_INVALID_PASSWORD = "error.invalid.password";
    private String KEY_UNAUTHORIZED_LOGIN =  "error.unauthorized.login";
    private String KEY_UNAUTHORIZED_PASSWORD = "error.unauthorized.password";
    private String SUCCESS_MESSAGE_LOGIN = "Senha confirmada, login autorizado!";
    private String SERVICE_OR_CONNECTION_ERROR_LOGIN = "Falha ao validar sua senha. Verifique a conexão e tente novamente.";
    private String SUCCESS_MESSAGE_EMAIL = "Foi enviado um código para seu e-mail!";
    private String SERVICE_OR_CONNECTION_ERROR_TOKEN = "Falha ao enviar código para o e-mail. Verifique a conexão e tente novamente.";

    private ValidationRepository repository = new ValidationRepository();

    private Bundle bundle;

    private LiveData<ResponseModel> passwordValidation;

    private LiveData<ResponseModel> tokenResend;

    private MutableLiveData<Boolean> passwordContainsErrorStatus = new MutableLiveData<>();

    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    private MutableLiveData<String> messageErrorChanged = new MutableLiveData<>();

    private MutableLiveData<String> isErrorMessageForToast = new MutableLiveData<>();

    private MutableLiveData<String> emailChanged = new MutableLiveData<>();

    private MutableLiveData<String> isValidatedPassword = new MutableLiveData<>();

    private MutableLiveData<String> forwardedToken = new MutableLiveData<>();

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
        changeEmail(bundle.getString(EMAIL_BUNDLE_KEY));
    }

    public MutableLiveData<String> getForwardedToken() {
        return forwardedToken;
    }

    public MutableLiveData<String> getIsValidatedPassword() {
        return isValidatedPassword;
    }

    public MutableLiveData<String> getMessageErrorChanged() {
        return messageErrorChanged;
    }

    public MutableLiveData<String> getIsErrorMessageForToast() {
        return isErrorMessageForToast;
    }

    public MutableLiveData<String> getEmailChanged() {
        return emailChanged;
    }

    public MutableLiveData<Boolean> getPasswordContainsErrorStatus() {
        return passwordContainsErrorStatus;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    private boolean validatePassword(String password) {
        return (!password.isEmpty() && validatePasswordFormat(password));
    }

    private boolean validatePasswordFormat(String password) {
        return password.length() >= MIN_SIZE_PASS && password.length() <= MAX_SIZE_PASS && password.matches(REGEX_ONLY_NUMBER_AND_LETTER);
    }

    public void passwordEntered(String password){
        passwordContainsErrorStatus.postValue(!validatePassword(password));
        if (isValidPassword(password)) {
            UserData userData = new UserData();
            userData.setEmail(bundle.getString(EMAIL_BUNDLE_KEY));
            userData.setPassword(password);
            executeServicePasswordValidation(userData);
        }
    }

    public void passwordTextChanged(){
        passwordContainsErrorStatus.postValue(false);
    }

    public boolean isValidPassword(String password){
        return validatePassword(password);
    }

    public void setPasswordContainsErrorStatus(boolean containsErrorPassword) {
        passwordContainsErrorStatus.postValue(containsErrorPassword);
    }

    public boolean isMessageErrorTopToast(String key){
        return (key.equals(KEY_INVALID_PASSWORD)
                || key.equals(KEY_UNAUTHORIZED_LOGIN)
                || key.equals(KEY_UNAUTHORIZED_PASSWORD));
    }

    private Observer<ResponseModel> passwordValidationObserver = new Observer<ResponseModel>() {
        @Override
        public void onChanged(@Nullable ResponseModel responseModel) {
            isLoading.setValue(false);
            if (responseModel != null) {
                if (responseModel.getCode() == 200) {
                    getIsValidatedPassword().setValue(SUCCESS_MESSAGE_LOGIN);
                } else {
                    String key = responseModel.getKey();
                    String message = responseModel.getMessage();
                    if (isMessageErrorTopToast(key)) {
                        getMessageErrorChanged().setValue(message);
                    } else {
                        getIsErrorMessageForToast().setValue(message);
                    }
                }
            } else {
                getIsErrorMessageForToast().setValue(SERVICE_OR_CONNECTION_ERROR_LOGIN);
            }
        }

    };

    private Observer<ResponseModel> tokenResendObserver = new Observer<ResponseModel>() {
        @Override
        public void onChanged(@Nullable ResponseModel responseModel) {
            isLoading.setValue(false);
            if (responseModel != null) {
                if (responseModel.getCode() == 200) {
                    getForwardedToken().setValue(SUCCESS_MESSAGE_EMAIL);
                } else {
                    getIsErrorMessageForToast().setValue(responseModel.getMessage());
                }
            } else {
                getIsErrorMessageForToast().setValue(SERVICE_OR_CONNECTION_ERROR_TOKEN);
            }
        }
    };

    public void tokenForwardingRequested(){
        String email = bundle.getString(EMAIL_BUNDLE_KEY);
        executeServiceTokenResend(email);
    }

    private void changeEmail(String email){
        emailChanged.setValue(email);
    }

    private void executeServicePasswordValidation(UserData userData) {
        isLoading.setValue(true);
        passwordValidation = repository.passwordValidate(userData);
        passwordValidation.observeForever(passwordValidationObserver);
    }

    private void executeServiceTokenResend(String email) {
        isLoading.setValue(true);
        tokenResend = repository.resendToken(email);
        tokenResend.observeForever(tokenResendObserver);
    }

    public void removeObserver() {
        if (passwordValidation != null && tokenResend != null) {
            passwordValidation.removeObserver(passwordValidationObserver);
            tokenResend.removeObserver(tokenResendObserver);
        }
    }
}