package br.com.estagio.oletrainning.zup.otmovies.PreLoginActivity;

import android.arch.lifecycle.MutableLiveData;

import android.arch.lifecycle.ViewModel;



public class PreLoginViewModel extends ViewModel {

    private MutableLiveData<Boolean> emailErrorStatus = new MutableLiveData<>();

    public MutableLiveData<Boolean> getEmailErrorStatus() {
        return emailErrorStatus;
    }

    private boolean validateEmail(String email) {
        return (!email.isEmpty() && validateEmailFormat(email));
    }

    private boolean validateEmailFormat(final String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void textChanged(){
        emailErrorStatus.postValue(true);
    }

    public void emailEntered(String email){
        emailErrorStatus.postValue(validateEmail(email));
    }

}
