package br.com.estagio.oletrainning.zup.otmovies.RegisterNewUserActivity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import br.com.estagio.oletrainning.zup.otmovies.Common.CommonViewModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ErrorMessage;
import br.com.estagio.oletrainning.zup.otmovies.Services.Repositories.UserRepository;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ResponseModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.UserData;

public class RegisterNewUserViewModel extends CommonViewModel {

    private String SUCCESSFULLY_REGISTERED = "Usuário cadastrado com sucesso!";
    private String SERVICE_OR_CONNECTION_ERROR_REGISTER = "Falha ao registrar seu cadastro. Verifique a conexão e tente novamente.";

    private UserRepository repository = new UserRepository();

    private LiveData<ResponseModel> registerUser;

    private MutableLiveData<Boolean> nameContainsErrorStatus = new MutableLiveData<>();

    private MutableLiveData<Boolean> userNameContainsErrorStatus = new MutableLiveData<>();

    private MutableLiveData<Boolean> passwordContainsErrorStatus = new MutableLiveData<>();

    private MutableLiveData<String> isRegistered = new MutableLiveData<>();

    private MutableLiveData<Boolean> isInvalidName = new MutableLiveData<>();

    private MutableLiveData<Boolean> isInvalidUsername = new MutableLiveData<>();

    private MutableLiveData<Boolean> isInvalidPassword = new MutableLiveData<>();

    private MutableLiveData<Boolean> isUsernameDuplicated = new MutableLiveData<>();

    public MutableLiveData<Boolean> getIsUsernameDuplicated() {
        return isUsernameDuplicated;
    }

    public MutableLiveData<Boolean> getIsInvalidPassword() {
        return isInvalidPassword;
    }

    public MutableLiveData<Boolean> getIsInvalidUsername() {
        return isInvalidUsername;
    }

    public MutableLiveData<Boolean> getIsInvalidName() {
        return isInvalidName;
    }

    public MutableLiveData<String> getIsRegistered() {
        return isRegistered;
    }

    public MutableLiveData<Boolean> getNameContainsErrorStatus() {
        return nameContainsErrorStatus;
    }

    public MutableLiveData<Boolean> getUserNameContainsErrorStatus() {
        return userNameContainsErrorStatus;
    }

    public MutableLiveData<Boolean> getPasswordContainsErrorStatus() {
        return passwordContainsErrorStatus;
    }

    public void completedForm(String name, String username, String password) {
        nameContainsErrorStatus.postValue(!validateName(name));
        userNameContainsErrorStatus.postValue(!validateUserName(username));
        passwordContainsErrorStatus.postValue(!validatePassword(password));
        if (isValidName(name) && isValidUserName(username) && isValidPassword(password)) {
            UserData userData = new UserData();
            userData.setEmail(bundle.getString(EMAIL_BUNDLE_KEY));
            userData.setCompleteName(name);
            userData.setUsername(username);
            userData.setPassword(password);
            executeServiceRegisterUser(userData);
        }
    }

    public void nameTextChanged() {
        nameContainsErrorStatus.postValue(false);
    }

    public void userNameTextChanged() {
        userNameContainsErrorStatus.postValue(false);
    }

    public void passwordTextChanged() {
        passwordContainsErrorStatus.postValue(false);
    }

    private Observer<ResponseModel> responseRegisterUserObserver = new Observer<ResponseModel>() {
        @Override
        public void onChanged(@Nullable ResponseModel responseModel) {
            isLoading.setValue(false);
            if (responseModel != null) {
                if (responseModel.getCode() == 200) {
                    getIsRegistered().setValue(SUCCESSFULLY_REGISTERED);
                } else {
                    ErrorMessage errorMessage = new ErrorMessage();
                    errorMessage.setKey(responseModel.getKey());
                    errorMessage.setMessage(responseModel.getMessage());
                    switch (errorMessage.getKey()) {
                        case "error.invalid.name":
                            getIsInvalidName().setValue(true);
                            break;
                        case "error.invalid.username":
                            getIsInvalidUsername().setValue(true);
                            break;
                        case "error.invalid.password":
                            getIsInvalidPassword().setValue(true);
                            break;
                        case "error.resource.username.duplicated":
                            getIsUsernameDuplicated().setValue(true);
                            break;
                        default:
                            getIsErrorMessageForToast().setValue(errorMessage.getMessage());
                            break;
                    }
                }
            } else {
                getIsErrorMessageForToast().setValue(SERVICE_OR_CONNECTION_ERROR_REGISTER);
            }
        }

    };

    private void executeServiceRegisterUser(UserData userData) {
        isLoading.setValue(true);
        registerUser = repository.postUserRegister(userData);
        registerUser.observeForever(responseRegisterUserObserver);
    }

    @Override
    public void removeObserver() {
        super.removeObserver();
        if (registerUser != null) {
            registerUser.removeObserver(responseRegisterUserObserver);
        }
    }
}