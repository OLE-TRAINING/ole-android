package br.com.estagio.oletrainning.zup.otmovies.TokenValidationActivity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import br.com.estagio.oletrainning.zup.otmovies.Common.CommonViewModel;
import br.com.estagio.oletrainning.zup.otmovies.Common.UsefulClass.Token;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ErrorMessage;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ResponseModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.UserData;

public class TokenValidationViewModel extends CommonViewModel {

    private Token token;
    private String SUCCESS_MESSAGE_VALIDATE_TOKEN = "Código confirmado com sucesso!";
    private String UNAUTHORIZED_TOKEN_KEY = "error.unauthorized.token";
    private String INVALID_TOKEN_KEY = "error.invalid.token";
    private String SERVICE_OR_CONNECTION_ERROR_VALIDATE_TOKEN = "Falha ao validar o código. Verifique a conexão e tente novamente.";

    private LiveData<ResponseModel<UserData>> tokenValidation;

    private MutableLiveData<Boolean> tokenContainsErrorStatus = new MutableLiveData<>();

    private MutableLiveData<String> messageErrorChanged = new MutableLiveData<>();

    private MutableLiveData<String> isValidatedToken = new MutableLiveData<>();

    public MutableLiveData<String> getIsValidatedToken() {
        return isValidatedToken;
    }

    public MutableLiveData<String> getMessageErrorChanged() {
        return messageErrorChanged;
    }

    public MutableLiveData<Boolean> getTokenContainsErrorStatus() {
        return tokenContainsErrorStatus;
    }

    public void tokenTextChanged(){
        tokenContainsErrorStatus.postValue(false);
    }

    public void tokenEntered(String code){
        token = new Token(code);
        tokenContainsErrorStatus.postValue(!token.validateTokenSize());
        if (token.isValidToken()) {
            String email = bundle.getString(EMAIL_BUNDLE_KEY);
            executeServiceTokenValidation(email,code);
        }
    }

    private Observer<ResponseModel<UserData>> tokenValidationObserver = new Observer<ResponseModel<UserData>>() {
        @Override
        public void onChanged(@Nullable ResponseModel<UserData> responseModel) {
            isLoading.setValue(false);
            if (responseModel != null) {
                if (responseModel.getCode() == 200) {
                    getIsValidatedToken().setValue(SUCCESS_MESSAGE_VALIDATE_TOKEN);
                } else {
                    ErrorMessage errorMessage = new ErrorMessage();
                    errorMessage.setKey(responseModel.getErrorMessage().getKey());
                    errorMessage.setMessage(responseModel.getErrorMessage().getMessage());
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

    private void executeServiceTokenValidation(String email,String code) {
        isLoading.setValue(true);
        tokenValidation = validationRepository.confirmToken(email,code);
        tokenValidation.observeForever(tokenValidationObserver);
    }

    @Override
    public void removeObserver() {
        super.removeObserver();
        if (tokenValidation != null && tokenResend != null) {
            tokenValidation.removeObserver(tokenValidationObserver);
            tokenResend.removeObserver(tokenResendObserver);
        }
    }
}