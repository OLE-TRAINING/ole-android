package br.com.estagio.oletrainning.zup.otmovies.Services;

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