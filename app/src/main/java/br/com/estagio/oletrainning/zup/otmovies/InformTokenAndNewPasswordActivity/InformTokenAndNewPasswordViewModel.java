package br.com.estagio.oletrainning.zup.otmovies.InformTokenAndNewPasswordActivity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import br.com.estagio.oletrainning.zup.otmovies.Common.CommonViewModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.BodyChangePassword;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ResponseModel;


public class InformTokenAndNewPasswordViewModel extends CommonViewModel {

    private String INVALID_PASSWORD_MISMATCH_KEY = "error.invalid.password.mismatch";
    private String INVALID_PASSWORD_KEY = "error.invalid.password";
    private String UNAUTHORIZED_TOKEN_KEY = "error.unauthorized.token";
    private String ERROR_RESOURCE_TOKEN_KEY = "error.resource.token";
    private String SUCCESS_MESSAGE_CHANGE_PASS = "Senha alterada com sucesso!";
    private String SERVICE_OR_CONNECTION_ERROR_CHANGE_PASSWORD = "Falha ao validar alterar a senha. Verifique a conexão e tente novamente.";

    private MutableLiveData<Boolean> tokenContainsErrorStatus = new MutableLiveData<>();

    private MutableLiveData<Boolean> passwordContainsErrorStatus = new MutableLiveData<>();

    private MutableLiveData<Boolean> confirmPasswordContainsErrorStatus = new MutableLiveData<>();

    private MutableLiveData<String> passwordChanged = new MutableLiveData<>();

    private MutableLiveData<String> isErrorMessageToPasswordInput = new MutableLiveData<>();

    private MutableLiveData<String> isErrorMessageToTokenInput = new MutableLiveData<>();

    private MutableLiveData<Boolean> showPasswordConfirmationInput = new MutableLiveData<>();

    private LiveData<ResponseModel> validateTokenAndChangePass;

    public MutableLiveData<Boolean> getShowPasswordConfirmationInput() {
        return showPasswordConfirmationInput;
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

    public MutableLiveData<Boolean> getPasswordContainsErrorStatus() {
        return passwordContainsErrorStatus;
    }

    public MutableLiveData<Boolean> getConfirmPasswordContainsErrorStatus() {
        return confirmPasswordContainsErrorStatus;
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

    public void confirmPasswordTextChanged(){
        confirmPasswordContainsErrorStatus.postValue(false);
    }

    public void tokenTextChanged(){
        tokenContainsErrorStatus.postValue(false);
    }

    public void passwordTextChanged(){
        passwordContainsErrorStatus.postValue(false);
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

    private void executeServiceValidateTokenAndChangePass(BodyChangePassword bodyChangePassword) {
        isLoading.setValue(true);
        validateTokenAndChangePass = validationRepository.validateTokenAndChangePass(bodyChangePassword);
        validateTokenAndChangePass.observeForever(serviceValidateTokenAndChangePassObserver);
    }

    @Override
    public void removeObserver() {
        if (validateTokenAndChangePass != null && tokenResend != null) {
            validateTokenAndChangePass.removeObserver(serviceValidateTokenAndChangePassObserver);
            tokenResend.removeObserver(tokenResendObserver);
        }
    }
}