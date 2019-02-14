package br.com.estagio.oletrainning.zup.otmovies.InformTokenAndNewPasswordActivity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.os.Bundle;
import android.support.annotation.Nullable;

import br.com.estagio.oletrainning.zup.otmovies.Services.Model.BodyChangePassword;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ResponseModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.Repositories.ValidationRepository;

public class InformTokenAndNewPasswordViewModel extends ViewModel {

    private ValidationRepository repository = new ValidationRepository();

    private final int MAX_SIZE_TOKEN = 6;
    private final Integer MIN_SIZE_PASS = 6;
    private final Integer MAX_SIZE_PASS = 10;

    private Bundle bundle;

    private String REGEX_ONLY_NUMBER_AND_LETTER = "(?:\\d+[a-z]|[a-z]+\\d)[a-z\\d]*";

    private String EMAIL_BUNDLE_KEY = "EmailPreLogin";
    private String INVALID_PASSWORD_MISMATCH_KEY = "error.invalid.password.mismatch";
    private String INVALID_PASSWORD_KEY = "error.invalid.password";
    private String UNAUTHORIZED_TOKEN_KEY = "error.unauthorized.token";
    private String ERROR_RESOURCE_TOKEN_KEY = "error.resource.token";
    private String SUCCESS_MESSAGE_CHANGE_PASS = "Senha alterada com sucesso!";
    private String SERVICE_OR_CONNECTION_ERROR_CHANGE_PASSWORD = "Falha ao validar alterar a senha. Verifique a conexão e tente novamente.";
    private String SUCCESS_RESEND_TOKEN = "Código reenviado com sucesso!";
    private String SERVICE_OR_CONNECTION_ERROR_RESEND_TOKEN = "Falha ao reenviar o código. Verifique a conexão e tente novamente.";

    private MutableLiveData<Boolean> tokenContainsErrorStatus = new MutableLiveData<>();

    private MutableLiveData<Boolean> passwordContainsErrorStatus = new MutableLiveData<>();

    private MutableLiveData<Boolean> confirmPasswordContainsErrorStatus = new MutableLiveData<>();

    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    private MutableLiveData<String> passwordChanged = new MutableLiveData<>();

    private MutableLiveData<String> isErrorMessageToPasswordInput = new MutableLiveData<>();

    private MutableLiveData<String> isErrorMessageToTokenInput = new MutableLiveData<>();

    private MutableLiveData<String> isErrorMessageForToast = new MutableLiveData<>();

    private MutableLiveData<String> emailChanged = new MutableLiveData<>();

    private MutableLiveData<Boolean> showPasswordConfirmationInput = new MutableLiveData<>();

    private LiveData<ResponseModel> validateTokenAndChangePass;

    private LiveData<ResponseModel> tokenResend;

    public MutableLiveData<Boolean> getShowPasswordConfirmationInput() {
        return showPasswordConfirmationInput;
    }

    public MutableLiveData<String> getEmailChanged() {
        return emailChanged;
    }

    public MutableLiveData<String> getIsErrorMessageForToast() {
        return isErrorMessageForToast;
    }

    public MutableLiveData<String> getIsErrorMessageToTokenInput() {
        return isErrorMessageToTokenInput;
    }

    public MutableLiveData<String> getIsErrorMessageToPasswordInput() {
        return isErrorMessageToPasswordInput;
    }

    public MutableLiveData<String> getPasswordChanged() {
        return passwordChanged;
    }

    public MutableLiveData<Boolean> getTokenContainsErrorStatus() {
        return tokenContainsErrorStatus;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MutableLiveData<Boolean> getPasswordContainsErrorStatus() {
        return passwordContainsErrorStatus;
    }

    public MutableLiveData<Boolean> getConfirmPasswordContainsErrorStatus() {
        return confirmPasswordContainsErrorStatus;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
        changeEmail(bundle.getString(EMAIL_BUNDLE_KEY));
    }

    private boolean isValidPassword(String password){
        return validatePassword(password);
    }

    private boolean isValidToken(String code){
        return validateTokenSize(code);
    }

    private boolean isValidConfirmPassword(String newPassword,String confirmNewPassword){
        return validateConfirmPassword(newPassword,confirmNewPassword);
    }

    public void showPasswordConfirmationInput (String password){
        if(isValidPassword(password)){
            getShowPasswordConfirmationInput().setValue(true);
        } else {
            getShowPasswordConfirmationInput().setValue(false);
        }
    }

    public void completedForm(String code, String password, String confirmPassword){
        tokenContainsErrorStatus.postValue(!validateTokenSize(code));
        passwordContainsErrorStatus.postValue(!validatePassword(password));
        confirmPasswordContainsErrorStatus.postValue(!validatePassword(confirmPassword));
        if(isValidToken(code)&& isValidPassword(password) && isValidConfirmPassword(password,confirmPassword)){
            BodyChangePassword bodyChangePassword = new BodyChangePassword();
            bodyChangePassword.setEmail(bundle.getString(EMAIL_BUNDLE_KEY));
            bodyChangePassword.setConfirmationToken(code);
            bodyChangePassword.setNewPassword(password);
            bodyChangePassword.setNewPasswordConfirmation(confirmPassword);
            executeServiceValidateTokenAndChangePass(bodyChangePassword);
        }
    }

    public void tokenTextChanged(){
        tokenContainsErrorStatus.postValue(false);
    }

    public void passwordTextChanged(){
        passwordContainsErrorStatus.postValue(false);
    }

    public void confirmPasswordTextChanged(){
        confirmPasswordContainsErrorStatus.postValue(false);
    }

    private boolean validatePassword(String password) {
        return (!password.isEmpty() && validatePasswordFormat(password));
    }

    private boolean validatePasswordFormat(String password) {
        return password.length() >= MIN_SIZE_PASS && password.length() <= MAX_SIZE_PASS && password.matches(REGEX_ONLY_NUMBER_AND_LETTER);
    }

    private boolean validateTokenSize(String tokenEntered) {
        return (tokenEntered.length() == MAX_SIZE_TOKEN);
    }

    private boolean validateMatchNewPassword(String newPassword,String confirmNewPassword) {
        return (newPassword.equals(confirmNewPassword));
    }

    private boolean validateConfirmPassword(String newPassword,String confirmNewPassword) {
        return (!confirmNewPassword.isEmpty() && validateMatchNewPassword(newPassword, confirmNewPassword));
    }

    private boolean isErrorMessageKeyToPasswordInput(String key){
        return (key.equals(INVALID_PASSWORD_MISMATCH_KEY)
                || key.equals(INVALID_PASSWORD_KEY));
    }

    private boolean isErrorMessageKeyToTokenInput(String key){
        return (key.equals(UNAUTHORIZED_TOKEN_KEY)
                || key.equals(ERROR_RESOURCE_TOKEN_KEY));
    }

    private Observer<ResponseModel> serviceValidateTokenAndChangePassObserver = new Observer<ResponseModel>() {
        @Override
        public void onChanged(@Nullable ResponseModel responseModel) {
            isLoading.setValue(false);
            if (responseModel != null) {
                if (responseModel.getCode() == 200) {
                    getPasswordChanged().setValue(SUCCESS_MESSAGE_CHANGE_PASS);
                } else {
                    String key = responseModel.getKey();
                    String message = responseModel.getMessage();
                    if (isErrorMessageKeyToPasswordInput(key)) {
                        getIsErrorMessageToPasswordInput().setValue(message);
                    } else if (isErrorMessageKeyToTokenInput(key)) {
                        getIsErrorMessageToTokenInput().setValue(message);
                    } else {
                        getIsErrorMessageForToast().setValue(message);
                    }
                }
            } else {
                getIsErrorMessageForToast().setValue(SERVICE_OR_CONNECTION_ERROR_CHANGE_PASSWORD);
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

    public void tokenForwardingRequested(){
        String email = bundle.getString(EMAIL_BUNDLE_KEY);
        executeServiceTokenResend(email);
    }


    private void changeEmail(String email){
        emailChanged.setValue(email);
    }

    private void executeServiceValidateTokenAndChangePass(BodyChangePassword bodyChangePassword) {
        isLoading.setValue(true);
        validateTokenAndChangePass = repository.validateTokenAndChangePass(bodyChangePassword);
        validateTokenAndChangePass.observeForever(serviceValidateTokenAndChangePassObserver);
    }

    private void executeServiceTokenResend(String email) {
        isLoading.setValue(true);
        tokenResend = repository.resendToken(email);
        tokenResend.observeForever(tokenResendObserver);
    }

    public void removeObserver() {
        if (validateTokenAndChangePass != null && tokenResend != null) {
            validateTokenAndChangePass.removeObserver(serviceValidateTokenAndChangePassObserver);
            tokenResend.removeObserver(tokenResendObserver);
        }
    }
}