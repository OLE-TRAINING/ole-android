package br.com.estagio.oletrainning.zup.otmovies.PreLoginActivity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ResponseModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.Repositories.UserRepository;

public class PreLoginViewModel extends ViewModel {

    private String REGISTERED = "REGISTERED";
    private String PENDING = "PENDING";
    private String INEXISTENT = "INEXISTENT";
    private String ERROR_INVALID_EMAIL = "error.invalid.email";
    private String ERROR_SERVICE_OR_CONNECTION_EMAIL = "Falha ao validar seu email. Verifique a conexão e tente novamente.";

    private UserRepository repository = new UserRepository();

    private MutableLiveData<Boolean> emailContainsErrorStatus = new MutableLiveData<>();

    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    private LiveData<ResponseModel> userResponseObservable;

    private MutableLiveData<Boolean> isRegisteredUser = new MutableLiveData<>();

    private MutableLiveData<Boolean> isPendingUser = new MutableLiveData<>();

    private MutableLiveData<Boolean> isInexistentUser = new MutableLiveData<>();

    private MutableLiveData<Boolean> isInvalidEmail = new MutableLiveData<>();

    private MutableLiveData<String>  hasUnknownError = new MutableLiveData<>();

    public MutableLiveData<String> getHasUnknownError() {
        return hasUnknownError;
    }

    public MutableLiveData<Boolean> getIsInvalidEmail() {
        return isInvalidEmail;
    }

    public MutableLiveData<Boolean> getIsRegisteredUser() {
        return isRegisteredUser;
    }

    public MutableLiveData<Boolean> getIsPendingUser() {
        return isPendingUser;
    }

    public MutableLiveData<Boolean> getIsInexistentUser() {
        return isInexistentUser;
    }

    public LiveData<ResponseModel> getUserResponse(String email) {
        userResponseObservable = repository.getUserDate(email);
        return userResponseObservable;
    }

    public MutableLiveData<Boolean> getEmailContainsErrorStatus() {
        return emailContainsErrorStatus;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    private boolean validateEmail( String email) {
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

    private Observer<ResponseModel> serviceCallObserver = new Observer<ResponseModel>() {
        @Override
        public void onChanged(@Nullable ResponseModel responseModel) {
            serviceEnding();
            if (responseModel != null) {
                if (responseModel.getRegistrationStatus() != null) {
                    if (responseModel.getRegistrationStatus().equals(REGISTERED)) {
                        getIsRegisteredUser().setValue(true);
                    } else if (responseModel.getRegistrationStatus().equals(PENDING)) {
                        getIsPendingUser().setValue(true);
                    } else if (responseModel.getRegistrationStatus().equals(INEXISTENT)) {
                        getIsInexistentUser().setValue(true);
                    }
                } else {
                    if (responseModel.getKey().equals(ERROR_INVALID_EMAIL)) {
                        getIsInvalidEmail().setValue(true);
                    } else {
                        getHasUnknownError().setValue(responseModel.getMessage());
                    }
                }
            } else {
                getHasUnknownError().setValue(ERROR_SERVICE_OR_CONNECTION_EMAIL);
            }
        }

    };

    public Observer<ResponseModel> getServiceCallObserver() {
        return serviceCallObserver;
    }
}