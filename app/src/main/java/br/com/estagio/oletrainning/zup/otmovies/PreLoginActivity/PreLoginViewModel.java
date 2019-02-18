package br.com.estagio.oletrainning.zup.otmovies.PreLoginActivity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;

import android.support.annotation.Nullable;

import br.com.estagio.oletrainning.zup.otmovies.Common.CommonViewModel;
import br.com.estagio.oletrainning.zup.otmovies.Common.UsefulClass.Email;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ResponseModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.Repositories.UserRepository;

public class PreLoginViewModel extends CommonViewModel {

    private Email email;
    private String REGISTERED = "REGISTERED";
    private String PENDING = "PENDING";
    private String INEXISTENT = "INEXISTENT";
    private String ERROR_INVALID_EMAIL = "error.invalid.email";
    private String ERROR_SERVICE_OR_CONNECTION_EMAIL = "Falha ao validar seu email. Verifique a conexão e tente novamente.";


    private UserRepository repository = new UserRepository();

    private LiveData<ResponseModel> userData;

    private MutableLiveData<Boolean> emailContainsErrorStatus = new MutableLiveData<>();

    private MutableLiveData<String> registrationStatus = new MutableLiveData<>();

    private MutableLiveData<Boolean> isInvalidEmail = new MutableLiveData<>();

    public MutableLiveData<Boolean> getIsInvalidEmail() {
        return isInvalidEmail;
    }

    public MutableLiveData<String> getRegistrationStatus() {
        return registrationStatus;
    }

    public MutableLiveData<Boolean> getEmailContainsErrorStatus() {
        return emailContainsErrorStatus;
    }

    public void textChanged() {
        emailContainsErrorStatus.postValue(false);
    }

    public void emailEntered(String emailEntered) {
        email = new Email(emailEntered);
        emailContainsErrorStatus.postValue(!email.validateEmail());
        if (email.isValidEmail()) {
            executeServiceCallGetUserData(emailEntered);
        }
    }

    private Observer<ResponseModel> getUserResponseObserver = new Observer<ResponseModel>() {
        @Override
        public void onChanged(@Nullable ResponseModel responseModel) {
            isLoading.postValue(false);
            if (responseModel != null) {
                if (responseModel.getRegistrationStatus() != null) {
                    if (responseModel.getRegistrationStatus().equals(REGISTERED)) {
                        getRegistrationStatus().setValue(REGISTERED);
                    } else if (responseModel.getRegistrationStatus().equals(PENDING)) {
                        getRegistrationStatus().setValue(PENDING);
                    } else if (responseModel.getRegistrationStatus().equals(INEXISTENT)) {
                        getRegistrationStatus().setValue(INEXISTENT);
                    }
                } else {
                    if (responseModel.getKey().equals(ERROR_INVALID_EMAIL)) {
                        getIsInvalidEmail().setValue(true);
                    } else {
                        getIsErrorMessageForToast().setValue(responseModel.getMessage());
                    }
                }
            } else {
                getIsErrorMessageForToast().setValue(ERROR_SERVICE_OR_CONNECTION_EMAIL);
            }
        }

    };

    private void executeServiceCallGetUserData(String email) {
        isLoading.postValue(true);
        userData = repository.getUserData(email);
        userData.observeForever(getUserResponseObserver);
    }

    @Override
    public void removeObserver() {
        super.removeObserver();
        if (userData != null) {
            userData.removeObserver(getUserResponseObserver);
        }
    }
}