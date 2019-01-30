package br.com.estagio.oletrainning.zup.otmovies.FinishYourRegistrationActivity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ResponseModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.Repositories.ValidationRepository;

public class FinishYourRegistrationViewModel extends ViewModel {

    private ValidationRepository repository = new ValidationRepository();

    private final int MAX_SIZE_TOKEN = 6;

    private MutableLiveData<Boolean> tokenContainsErrorStatus = new MutableLiveData<>();

    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    private LiveData<ResponseModel> tokenResponseObservable;

    private LiveData<ResponseModel> tokenresendResponseObservable;

    public MutableLiveData<Boolean> getTokenContainsErrorStatus() {
        return tokenContainsErrorStatus;
    }

    public LiveData<ResponseModel> tokenValidation(String email, String code) {
        tokenResponseObservable = repository.confirmToken(email, code);
        return tokenResponseObservable;
    }

    public LiveData<ResponseModel> resendToken(String email) {
        tokenresendResponseObservable = repository.resendToken(email);
        return tokenresendResponseObservable;
    }

    private boolean validateTokenSize(String tokenEntered) {
        return (tokenEntered.length() == MAX_SIZE_TOKEN);
    }

    public void serviceStarting() {
        isLoading.postValue(true);
    }

    public void serviceEnding() {
        isLoading.postValue(false);
    }

    public void tokenTextChanged() {
        tokenContainsErrorStatus.postValue(false);
    }

    public void tokenEntered(String code) {
        tokenContainsErrorStatus.postValue(!validateTokenSize(code));
    }

    public boolean isValidToken(String code) {
        return validateTokenSize(code);
    }
}

