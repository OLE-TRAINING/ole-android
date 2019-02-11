package br.com.estagio.oletrainning.zup.otmovies.LoginActivity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ResponseModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.Repositories.ValidationRepository;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.UserData;

public class LoginViewModel extends ViewModel {

    private final Integer MIN_SIZE_PASS = 6;
    private final Integer MAX_SIZE_PASS = 10;

    private String REGEX_ONLY_NUMBER_AND_LETTER = "(?:\\d+[a-z]|[a-z]+\\d)[a-z\\d]*";
    private String KEY_INVALID_PASSWORD = "error.invalid.password";
    private String KEY_UNAUTHORIZED_LOGIN =  "error.unauthorized.login";
    private String KEY_UNAUTHORIZED_PASSWORD = "error.unauthorized.password";
    
    private ValidationRepository repository = new ValidationRepository();

    private MutableLiveData<Boolean> passwordContainsErrorStatus = new MutableLiveData<>();

    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    private LiveData<ResponseModel> passValidationResponseObservable;

    private LiveData<ResponseModel> tokenresendResponseObservable;

    public LiveData<ResponseModel> passwordValidation(String email, String password) {
        UserData userData = new UserData();
        userData.setEmail(email);
        userData.setPassword(password);
        passValidationResponseObservable = repository.passwordValidate(userData);
        return passValidationResponseObservable;
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
    }

    public void serviceStarting(){
        isLoading.postValue(true);
    }

    public void serviceEnding(){
        isLoading.postValue(false);
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

    public LiveData<ResponseModel> resendToken(String email) {
        tokenresendResponseObservable = repository.resendToken(email);
        return tokenresendResponseObservable;
    }

    public boolean isMessageToPutInTopToast(String key){
        return (key.equals(KEY_INVALID_PASSWORD)
                || key.equals(KEY_UNAUTHORIZED_LOGIN)
                || key.equals(KEY_UNAUTHORIZED_PASSWORD));
    }
}