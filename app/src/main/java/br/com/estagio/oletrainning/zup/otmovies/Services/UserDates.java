package br.com.estagio.oletrainning.zup.otmovies.Services;

import android.content.Intent;
import android.widget.Toast;

import br.com.estagio.oletrainning.zup.otmovies.LoginActivity.LoginActivity;
import br.com.estagio.oletrainning.zup.otmovies.RegisterNewUserActivity.RegisterNewUserActivity;
import retrofit2.Call;
import retrofit2.Callback;

public class UserDates {

    private String email;
    private String completeName;
    private String username;
    private String registrationStatus;
    private String password;

    public String getEmail() {
        return email;
    }

    public String getCompleteName() {
        return completeName;
    }

    public String getUsername() {
        return username;
    }

    public String getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(String registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCompleteName(String completeName) {
        this.completeName = completeName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
