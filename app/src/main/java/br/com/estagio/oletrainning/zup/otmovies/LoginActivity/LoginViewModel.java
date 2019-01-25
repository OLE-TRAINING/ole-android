package br.com.estagio.oletrainning.zup.otmovies.LoginActivity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import br.com.estagio.oletrainning.zup.otmovies.Services.HeadLineRepository.HeadLineRepository;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ResponseModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.UserDates;

public class LoginViewModel extends ViewModel {

    private final Integer MINSIZEPASS = 6;
    private final Integer MAXSIZEPASS = 10;

    private String REGEXONLYNUMBERANDLETTER = "(?:\\d+[a-z]|[a-z]+\\d)[a-z\\d]*";

    private HeadLineRepository repository = new HeadLineRepository();

    private MutableLiveData<Boolean> passwordContainsErrorStatus = new MutableLiveData<>();

    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    private LiveData<ResponseModel> passValidationResponseObservable;

    private LiveData<ResponseModel> tokenresendResponseObservable;

    public LiveData<ResponseModel> passwordValidation(@NonNull UserDates userDates) {
        passValidationResponseObservable = repository.passwordValidate(userDates,"593c3280aedd01364c73000d3ac06d76");
        return passValidationResponseObservable;
    }

    public MutableLiveData<Boolean> getPasswordContainsErrorStatus() {
        return passwordContainsErrorStatus;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    private boolean validatePassword(@NonNull String password) {
        return (!password.isEmpty() && validatePasswordFormat(password));
    }

    private boolean validatePasswordFormat(@NonNull String password) {
        return password.length() >= MINSIZEPASS && password.length() <= MAXSIZEPASS && password.matches(REGEXONLYNUMBERANDLETTER);
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

    public LiveData<ResponseModel> resendToken(@NonNull String email) {
        tokenresendResponseObservable = repository.resendToken(email,"593c3280aedd01364c73000d3ac06d76");
        return tokenresendResponseObservable;
    }
}
