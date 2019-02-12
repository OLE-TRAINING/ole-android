package br.com.estagio.oletrainning.zup.otmovies.RegisterNewUserActivity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ErrorMessage;
import br.com.estagio.oletrainning.zup.otmovies.Services.Repositories.UserRepository;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ResponseModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.UserData;

public class RegisterNewUserViewModel extends ViewModel {

    private final Integer MAX_SIZE_NAME = 50;
    private final Integer MIN_SIZE_PASS = 6;
    private final Integer MAX_SIZE_PASS = 10;
    private final Integer MAX_SIZE_USERNAME = 15;

    private String REGEX_FOR_NAME = "^[\\p{L} .'-]+$";
    private String REGEX_ONLY_NUMBER_OR_LETTER = "[a-zA-Z0-9]+";
    private String REGEX_ONLY_NUMBER_AND_LETTER = "(?:\\d+[a-z]|[a-z]+\\d)[a-z\\d]*";
    private String SUCCESSFULLY_REGISTERED = "Usuário cadastrado com sucesso!";
    private String SERVICE_OR_CONNECTION_ERROR_REGISTER = "Falha ao registrar seu cadastro. Verifique a conexão e tente novamente.";

    private UserRepository repository = new UserRepository();

    private LiveData<ResponseModel> registerUser;

    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    private MutableLiveData<Boolean> nameContainsErrorStatus = new MutableLiveData<>();

    private MutableLiveData<Boolean> userNameContainsErrorStatus = new MutableLiveData<>();

    private MutableLiveData<Boolean> passwordContainsErrorStatus = new MutableLiveData<>();

    private MutableLiveData<String> isRegistered = new MutableLiveData<>();

    private MutableLiveData<Boolean> isInvalidName = new MutableLiveData<>();

    private MutableLiveData<Boolean> isInvalidUsername = new MutableLiveData<>();

    private MutableLiveData<Boolean> isInvalidPassword = new MutableLiveData<>();

    private MutableLiveData<Boolean> isUsernameDuplicated = new MutableLiveData<>();

    private MutableLiveData<String> hasUnknownError = new MutableLiveData<>();

    public MutableLiveData<String> getHasUnknownError() {
        return hasUnknownError;
    }

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

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
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

    private boolean validateName(String name) {
        return (!name.isEmpty() && validateNameFormat(name));
    }

    private boolean validateUserName(String userName) {
        return (!userName.isEmpty() && validateUserNameFormat(userName));
    }

    private boolean validatePassword(String password) {
        return (!password.isEmpty() && validatePasswordFormat(password));
    }

    private boolean validateNameFormat(String name) {
        return name.length() <= MAX_SIZE_NAME && name.matches(REGEX_FOR_NAME);
    }

    private boolean validateUserNameFormat(String userName) {
        return userName.length() <= MAX_SIZE_USERNAME && userName.matches(REGEX_ONLY_NUMBER_OR_LETTER);
    }

    private boolean validatePasswordFormat(String password) {
        return password.length() >= MIN_SIZE_PASS && password.length() <= MAX_SIZE_PASS && password.matches(REGEX_ONLY_NUMBER_AND_LETTER);
    }

    private boolean isValidName(String name) {
        return validateName(name);
    }

    private boolean isValidUserName(String username) {
        return validateUserName(username);
    }

    private boolean isValidPassword(String password) {
        return validatePassword(password);
    }

    public void completedForm(String email, String name, String username, String password) {
        nameContainsErrorStatus.postValue(!validateName(name));
        userNameContainsErrorStatus.postValue(!validateUserName(username));
        passwordContainsErrorStatus.postValue(!validatePassword(password));
        if (isValidName(name) && isValidUserName(username) && isValidPassword(password)) {
            UserData userData = new UserData();
            userData.setEmail(email);
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
                            getHasUnknownError().setValue(errorMessage.getMessage());
                            break;
                    }
                }
            } else {
                getHasUnknownError().setValue(SERVICE_OR_CONNECTION_ERROR_REGISTER);
            }
        }

    };

    private void executeServiceRegisterUser(UserData userData) {
        isLoading.setValue(true);
        registerUser = repository.postUserRegister(userData);
        registerUser.observeForever(responseRegisterUserObserver);
    }

    public void removeObserver() {
        if (registerUser != null) {
            registerUser.removeObserver(responseRegisterUserObserver);
        }
    }
}