package br.com.estagio.oletrainning.zup.otmovies.Services.Singleton;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import br.com.estagio.oletrainning.zup.otmovies.LoginActivity.LoginActivity;

public enum SingletonAlertDialogSession {

    INSTANCE;

    private AlertDialog.Builder alertDialogBuilder;

    private void setAlertDialogBuilder(AlertDialog.Builder alertDialogBuilder) {
        this.alertDialogBuilder = alertDialogBuilder;
    }

    public static void createAlertDialogBuilder(final Activity activity){
        SingletonAlertDialogSession singletonAlertDialogSession = SingletonAlertDialogSession.INSTANCE;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setMessage("Sua sessão expirou, favor fazer login novamente!")
                .setTitle("Aviso:")
                .setCancelable(false)
                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(activity, LoginActivity.class);
                        activity.startActivity(intent);
                    }
                });
        singletonAlertDialogSession.setAlertDialogBuilder(alertDialogBuilder);
    }

    public AlertDialog.Builder getAlertDialogBuilder(){
        if(alertDialogBuilder != null){
            return alertDialogBuilder;
        }
        return null;
    }
}
