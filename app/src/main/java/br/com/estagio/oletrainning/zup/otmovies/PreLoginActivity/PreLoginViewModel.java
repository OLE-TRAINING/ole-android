package br.com.estagio.oletrainning.zup.otmovies.PreLoginActivity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ResponseModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.HeadLineRepository.HeadLineRepository;


public class PreLoginViewModel extends ViewModel {

    private HeadLineRepository repository = new HeadLineRepository();

    private MutableLiveData<Boolean> emailContainsErrorStatus = new MutableLiveData<>();

    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    private LiveData<ResponseModel> userResponseObservable;

    public LiveData<ResponseModel> getUserResponse(@NonNull String email) {
        userResponseObservable = repository.getUserDate(email);
        return userResponseObservable;
    }

    public MutableLiveData<Boolean> getEmailContainsErrorStatus() {
        return emailContainsErrorStatus;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    private boolean validateEmail(@NonNull String email) {
        return (!email.isEmpty() && validateEmailFormat(email));
    }

    private boolean validateEmailFormat(final String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void textChanged(){
        emailContainsErrorStatus.postValue(false);
    }

    public void emailEntered(String email){
        emailContainsErrorStatus.postValue(!validateEmail(email));
    }

    public void serviceStarting(){
        isLoading.postValue(true);
    }

    public void serviceEnding(){
        isLoading.postValue(false);
    }

    public boolean isValidEmail(String email){
        return validateEmail(email);
    }
}
