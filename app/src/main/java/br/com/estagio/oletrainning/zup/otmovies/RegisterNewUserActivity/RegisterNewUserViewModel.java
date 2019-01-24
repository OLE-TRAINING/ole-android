package br.com.estagio.oletrainning.zup.otmovies.RegisterNewUserActivity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import br.com.estagio.oletrainning.zup.otmovies.Services.HeadLineRepository.HeadLineRepository;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ResponseModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.UserDates;


public class RegisterNewUserViewModel extends ViewModel {

    private final Integer MAXSIZENAME = 50;
    private final Integer MINSIZEPASS = 6;
    private final Integer MAXSIZEPASS = 10;
    private final Integer MAXSIZEUSERNAME = 15;

    private String REGEXFORNAME = "^[\\p{L} .'-]+$";
    private String REGEXONLYNUMBERORLETTER = "[a-zA-Z0-9]+";
    private String REGEXONLYNUMBERANDLETTER = "(?:\\d+[a-z]|[a-z]+\\d)[a-z\\d]*";

    private HeadLineRepository repository = new HeadLineRepository();

    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    private MutableLiveData<Boolean> nameContainsErrorStatus = new MutableLiveData<>();

    private MutableLiveData<Boolean> userNameContainsErrorStatus = new MutableLiveData<>();

    private MutableLiveData<Boolean> passwordContainsErrorStatus = new MutableLiveData<>();

    private LiveData<ResponseModel> userResponseObservable;

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

    public LiveData<ResponseModel> postUserRegister(@NonNull UserDates userDates) {
        userResponseObservable = repository.postUserRegister(userDates,"593c3280aedd01364c73000d3ac06d76");
        return userResponseObservable;
    }

    public void serviceStarting(){
        isLoading.postValue(true);
    }

    public void serviceEnding(){
        isLoading.postValue(false);
    }

    private boolean validateName(@NonNull String name) {
        return (!name.isEmpty() && validateNameFormat(name));
    }

    private boolean validateUserName(@NonNull String userName) {
        return (!userName.isEmpty() && validateUserNameFormat(userName));
    }

    private boolean validatePassword(@NonNull String password) {
        return (!password.isEmpty() && validatePasswordFormat(password));
    }

    private boolean validateNameFormat(@NonNull String name) {
        return name.length() <= MAXSIZENAME && name.matches(REGEXFORNAME);
    }

    private boolean validateUserNameFormat(@NonNull String userName) {
        return userName.length() <= MAXSIZEUSERNAME && userName.matches(REGEXONLYNUMBERORLETTER);
    }

    private boolean validatePasswordFormat(@NonNull String password) {
        return password.length() >= MINSIZEPASS && password.length() <= MAXSIZEPASS && password.matches(REGEXONLYNUMBERANDLETTER);
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
