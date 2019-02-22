package br.com.estagio.oletrainning.zup.otmovies.Common.UsefulClass;

public class ConfirmPassword extends Password {

    private final String confirmPassword;

    public ConfirmPassword(String password, String confirmPassword) {
        super(password);
        this.confirmPassword = confirmPassword;
    }

    public boolean isValidConfirmPassword(){
        return validateConfirmPassword();
    }

    public boolean validateMatchNewPassword() {
        return (password.equals(confirmPassword));
    }

    public boolean validateConfirmPassword() {
        return (!confirmPassword.isEmpty() && validateMatchNewPassword());
    }
}
