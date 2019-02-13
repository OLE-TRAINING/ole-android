package br.com.estagio.oletrainning.zup.otmovies.InformTokenAndNewPasswordActivity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import br.com.estagio.oletrainning.zup.otmovies.Services.Model.BodyChangePassword;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ResponseModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.Repositories.ValidationRepository;

public class InformTokenAndNewPasswordViewModel extends ViewModel {

    private ValidationRepository repository = new ValidationRepository();

    private final int MAX_SIZE_TOKEN = 6;
    private final Integer MIN_SIZE_PASS = 6;
    private final Integer MAX_SIZE_PASS = 10;

    private String REGEX_ONLY_NUMBER_AND_LETTER = "(?:\\d+[a-z]|[a-z]+\\d)[a-z\\d]*";

    private String INVALID_PASSWORD_MISMATCH_KEY = "error.invalid.password.mismatch";
    private String INVALID_PASSWORD_KEY = "error.invalid.password";
    private String UNAUTHORIZED_TOKEN_KEY = "error.unauthorized.token";
    private String ERROR_RESOURCE_TOKEN_KEY = "error.resource.token";

    private MutableLiveData<Boolean> tokenContainsErrorStatus = new MutableLiveData<>();

    private MutableLiveData<Boolean> passwordContainsErrorStatus = new MutableLiveData<>();

    private MutableLiveData<Boolean> confirmPasswordContainsErrorStatus = new MutableLiveData<>();

    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public LiveData<ResponseModel> validateTokenAndChangePass(String email,String token,String password,String confirmPassword) {
        BodyChangePassword bodyChangePassword = new BodyChangePassword();
        bodyChangePassword.setEmail(email);
        bodyChangePassword.setConfirmationToken(token);
        bodyChangePassword.setNewPassword(password);
        bodyChangePassword.setNewPasswordConfirmation(confirmPassword);
        validateTokenAndChangePassObservable = repository.validateTokenAndChangePass(bodyChangePassword);
        return validateTokenAndChangePassObservable;
    }

    public LiveData<ResponseModel> resendToken(String email) {
        tokenresendResponseObservable = repository.resendToken(email);
        return tokenresendResponseObservable;
    }

    private LiveData<ResponseModel> validateTokenAndChangePassObservable;

    private LiveData<ResponseModel> tokenresendResponseObservable;

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

    public boolean isValidPassword(String password){
        return validatePassword(password);
    }

    public boolean isValidToken(String code){
        return validateTokenSize(code);
    }

    public boolean isValidConfirmPassword(String newPassword,String confirmNewPassword){
        return validateConfirmPassword(newPassword,confirmNewPassword);
    }

    public void tokenEntered(String code){
        tokenContainsErrorStatus.postValue(!validateTokenSize(code));
    }

    public void passwordEntered(String password){
        passwordContainsErrorStatus.postValue(!validatePassword(password));
    }
    public void confirmPasswordEntered(String confirmPassword){
        confirmPasswordContainsErrorStatus.postValue(!validatePassword(confirmPassword));
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

    public void serviceStarting(){
        isLoading.postValue(true);
    }

    public void serviceEnding(){
        isLoading.postValue(false);
    }

    public boolean isErrorMessageKeyToPasswordInput(String key){
        return (key.equals(INVALID_PASSWORD_MISMATCH_KEY)
                || key.equals(INVALID_PASSWORD_KEY));
    }

    public boolean isErrorMessageKeyToTokenInput(String key){
        return (key.equals(UNAUTHORIZED_TOKEN_KEY)
                || key.equals(ERROR_RESOURCE_TOKEN_KEY));
    }
}