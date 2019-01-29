package br.com.estagio.oletrainning.zup.otmovies.RegisterNewUserActivity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import br.com.estagio.oletrainning.zup.otmovies.Services.Repositories.UserRepository;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ResponseModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.UserDates;


public class RegisterNewUserViewModel extends ViewModel {

    private final Integer MAX_SIZE_NAME = 50;
    private final Integer MIN_SIZE_PASS = 6;
    private final Integer MAX_SIZE_PASS = 10;
    private final Integer MAX_SIZE_USERNAME = 15;

    private String REGEX_FOR_NAME = "^[\\p{L} .'-]+$";
    private String REGEX_ONLY_NUMBER_OR_LETTER = "[a-zA-Z0-9]+";
    private String REGEX_ONLY_NUMBER_AND_LETTER = "(?:\\d+[a-z]|[a-z]+\\d)[a-z\\d]*";
    private String KEY_AUTENTICATION_SERVICE = "593c3280aedd01364c73000d3ac06d76";

    private UserRepository repository = new UserRepository();

    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    private MutableLiveData<Boolean> nameContainsErrorStatus = new MutableLiveData<>();

    private MutableLiveData<Boolean> userNameContainsErrorStatus = new MutableLiveData<>();

    private MutableLiveData<Boolean> passwordContainsErrorStatus = new MutableLiveData<>();

    private LiveData<ResponseModel> registerResponseObservable;

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

    public LiveData<ResponseModel> postUserRegister(String email,String name,String username, String password) {
        UserDates userDates = new UserDates();
        userDates.setEmail(email);
        userDates.setCompleteName(name);
        userDates.setUsername(username);
        userDates.setPassword(password);
        registerResponseObservable = repository.postUserRegister(userDates,KEY_AUTENTICATION_SERVICE);
        return registerResponseObservable;
    }

    public void serviceStarting(){
        isLoading.postValue(true);
    }

    public void serviceEnding(){
        isLoading.postValue(false);
    }

    private boolean validateName( String name) {
        return (!name.isEmpty() && validateNameFormat(name));
    }

    private boolean validateUserName( String userName) {
        return (!userName.isEmpty() && validateUserNameFormat(userName));
    }

    private boolean validatePassword( String password) {
        return (!password.isEmpty() && validatePasswordFormat(password));
    }

    private boolean validateNameFormat( String name) {
        return name.length() <= MAX_SIZE_NAME && name.matches(REGEX_FOR_NAME);
    }

    private boolean validateUserNameFormat( String userName) {
        return userName.length() <= MAX_SIZE_USERNAME && userName.matches(REGEX_ONLY_NUMBER_OR_LETTER);
    }

    private boolean validatePasswordFormat( String password) {
        return password.length() >= MIN_SIZE_PASS && password.length() <= MAX_SIZE_PASS && password.matches(REGEX_ONLY_NUMBER_AND_LETTER);
    }

    public boolean isValidName(String name){
        return validateName(name);
    }

    public boolean isValidUserName(String username){
        return validateUserName(username);
    }

    public boolean isValidPassword(String password){
        return validatePassword(password);
    }

    public void nameEntered(String name){
        nameContainsErrorStatus.postValue(!validateName(name));
    }

    public void userNameEntered(String username){
        userNameContainsErrorStatus.postValue(!validateUserName(username));
    }

    public void passwordEntered(String password){
        passwordContainsErrorStatus.postValue(!validatePassword(password));
    }

    public void nameTextChanged(){
        nameContainsErrorStatus.postValue(false);
    }

    public void userNameTextChanged(){
        userNameContainsErrorStatus.postValue(false);
    }

    public void passwordTextChanged(){
        passwordContainsErrorStatus.postValue(false);
    }
}
