package br.com.estagio.oletrainning.zup.otmovies.TokenValidationActivity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import br.com.estagio.oletrainning.zup.otmovies.Services.HeadLineRepository.HeadLineRepository;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ResponseModel;

public class TokenValidationViewModel extends ViewModel {

    private HeadLineRepository repository = new HeadLineRepository();

    private final int MAXSIZETOKEN = 6;

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

    public LiveData<ResponseModel> tokenValidation(@NonNull String email, String code) {
        tokenResponseObservable = repository.confirmToken(email,code,"593c3280aedd01364c73000d3ac06d76");
        return tokenResponseObservable;
    }

    public LiveData<ResponseModel> resendToken(@NonNull String email, String code) {
        tokenresendResponseObservable = repository.resendtoken(email,"593c3280aedd01364c73000d3ac06d76");
        return tokenresendResponseObservable;
    }

    private boolean validateTokenSize(@NonNull String tokenEntered) {
        return (tokenEntered.length() == MAXSIZETOKEN);
    }

    public void serviceStarting(){
        isLoading.postValue(true);
    }

    public void serviceEnding(){
        isLoading.postValue(false);
    }

    public void tokenTextChanged(){
        tokenContainsErrorStatus.postValue(false);
    }

    public void tokenEntered(String code){
        tokenContainsErrorStatus.postValue(!validateTokenSize(code));
    }

    public boolean isValidToken(String code){
        return validateTokenSize(code);
    }

}
