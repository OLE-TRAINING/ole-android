package br.com.estagio.oletrainning.zup.otmovies.LoginActivity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;

import android.support.annotation.Nullable;

import br.com.estagio.oletrainning.zup.otmovies.Common.CommonViewModel;
import br.com.estagio.oletrainning.zup.otmovies.Common.UsefulClass.Password;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ResponseModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.UserData;

public class LoginViewModel extends CommonViewModel {

    private Password password;
    private String KEY_INVALID_PASSWORD = "error.invalid.password";
    private String KEY_UNAUTHORIZED_LOGIN =  "error.unauthorized.login";
    private String KEY_UNAUTHORIZED_PASSWORD = "error.unauthorized.password";
    private String SUCCESS_MESSAGE_LOGIN = "Senha confirmada, login autorizado!";
    private String SERVICE_OR_CONNECTION_ERROR_LOGIN = "Falha ao validar sua senha. Verifique a conexão e tente novamente.";

    private LiveData<ResponseModel<UserData>> passwordValidation;

    private MutableLiveData<Boolean> passwordContainsErrorStatus = new MutableLiveData<>();

    private MutableLiveData<String> messageErrorChanged = new MutableLiveData<>();

    private MutableLiveData<String> isValidatedPassword = new MutableLiveData<>();


    public MutableLiveData<String> getIsValidatedPassword() {
        return isValidatedPassword;
    }

    public MutableLiveData<String> getMessageErrorChanged() {
        return messageErrorChanged;
    }

    public MutableLiveData<Boolean> getPasswordContainsErrorStatus() {
        return passwordContainsErrorStatus;
    }

    public void passwordEntered(String passwordEntered){
        password = new Password(passwordEntered);
        passwordContainsErrorStatus.postValue(!password.validatePassword());
        if (password.isValidPassword()) {
            UserData userData = new UserData();
            userData.setEmail(bundle.getString(EMAIL_BUNDLE_KEY));
            userData.setPassword(passwordEntered);
            executeServicePasswordValidation(userData);
        }
    }

    public void changeSuccessMessageResendToken(){
        SUCCESS_RESEND_TOKEN = "Foi enviado um código para seu e-mail!";
    }

    @Override
    public void tokenForwardingRequested() {
        super.tokenForwardingRequested();
        passwordContainsErrorStatus.setValue(false);
        changeSuccessMessageResendToken();
        String email = bundle.getString(EMAIL_BUNDLE_KEY);
        executeServiceTokenResend(email);
    }

    public void passwordTextChanged(){
        passwordContainsErrorStatus.postValue(false);
    }

    public void setPasswordContainsErrorStatus(boolean containsErrorPassword) {
        passwordContainsErrorStatus.postValue(containsErrorPassword);
    }

    public boolean isMessageErrorTopToast(String key){
        return (key.equals(KEY_INVALID_PASSWORD)
                || key.equals(KEY_UNAUTHORIZED_LOGIN)
                || key.equals(KEY_UNAUTHORIZED_PASSWORD));
    }

    private Observer<ResponseModel<UserData>> passwordValidationObserver = new Observer<ResponseModel<UserData>>() {
        @Override
        public void onChanged(@Nullable ResponseModel<UserData> responseModel) {
            isLoading.setValue(false);
            if (responseModel != null) {
                if (responseModel.getCode() == 200) {
                    getIsValidatedPassword().setValue(SUCCESS_MESSAGE_LOGIN);
                } else {
                    String key = responseModel.getErrorMessage().getKey();
                    String message = responseModel.getErrorMessage().getMessage();
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

    private void executeServicePasswordValidation(UserData userData) {
        isLoading.setValue(true);
        passwordValidation = validationRepository.passwordValidate(userData);
        passwordValidation.observeForever(passwordValidationObserver);
    }

    @Override
    public void removeObserver() {
        super.removeObserver();
        if (passwordValidation != null) {
            passwordValidation.removeObserver(passwordValidationObserver);
        }
    }
}