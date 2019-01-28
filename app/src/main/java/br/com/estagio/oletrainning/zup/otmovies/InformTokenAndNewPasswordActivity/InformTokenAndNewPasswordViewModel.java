package br.com.estagio.oletrainning.zup.otmovies.InformTokenAndNewPasswordActivity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import br.com.estagio.oletrainning.zup.otmovies.Services.BodyChangePassword;
import br.com.estagio.oletrainning.zup.otmovies.Services.HeadLineRepository.HeadLineRepository;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ResponseModel;

public class InformTokenAndNewPasswordViewModel extends ViewModel {

    private HeadLineRepository repository = new HeadLineRepository();

    private final int MAXSIZETOKEN = 6;
    private final Integer MINSIZEPASS = 6;
    private final Integer MAXSIZEPASS = 10;
    private String REGEXONLYNUMBERANDLETTER = "(?:\\d+[a-z]|[a-z]+\\d)[a-z\\d]*";

    private MutableLiveData<Boolean> tokenContainsErrorStatus = new MutableLiveData<>();

    private MutableLiveData<Boolean> passwordContainsErrorStatus = new MutableLiveData<>();

    private MutableLiveData<Boolean> confirmPasswordContainsErrorStatus = new MutableLiveData<>();

    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public LiveData<ResponseModel> validateTokenAndChangePass(@NonNull BodyChangePassword bodyChangePassword) {
        validateTokenAndChangePassObservable = repository.validateTokenAndChangePass(bodyChangePassword,"593c3280aedd01364c73000d3ac06d76");
        return validateTokenAndChangePassObservable;
    }

    public LiveData<ResponseModel> resendToken(@NonNull String email) {
        tokenresendResponseObservable = repository.resendtoken(email,"593c3280aedd01364c73000d3ac06d76");
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

    public boolean isValidConfirmPassword(String newPassword, String confirmNewPassword){
        return validateConfirmPassword(newPassword,confirmNewPassword);
    }

    public void tokenEntered(String code){
        tokenContainsErrorStatus.postValue(!validateTokenSize(code));
    }

    public void passwordEntered(String password){
        passwordContainsErrorStatus.postValue(!validatePassword(password));
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

    private boolean validatePassword(@NonNull String password) {
        return (!password.isEmpty() && validatePasswordFormat(password));
    }

    private boolean validatePasswordFormat(@NonNull String password) {
        return password.length() >= MINSIZEPASS && password.length() <= MAXSIZEPASS && password.matches(REGEXONLYNUMBERANDLETTER);
    }

    private boolean validateTokenSize(@NonNull String tokenEntered) {
        return (tokenEntered.length() == MAXSIZETOKEN);
    }

    private boolean validateMatchNewPassword(@NonNull String newPassword, @NonNull String confirmNewPassword) {
        return (newPassword.equals(confirmNewPassword));
    }

    private boolean validateConfirmPassword(@NonNull String newPassword, @NonNull String confirmNewPassword) {
        return (!confirmNewPassword.isEmpty() && validateMatchNewPassword(newPassword, confirmNewPassword));
    }

    public boolean isEmptyTokenInput(@NonNull String token){
        return token.isEmpty();
    }

    public boolean isEmptyPasswordInput(@NonNull String password){
        return password.isEmpty();
    }

    public boolean isEmptyConfirmPasswordInput(@NonNull String confirmNewPassword){
        return confirmNewPassword.isEmpty();
    }

    public void serviceStarting(){
        isLoading.postValue(true);
    }

    public void serviceEnding(){
        isLoading.postValue(false);
    }
}
