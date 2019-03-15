package br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Fragments.Home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import br.com.estagio.oletrainning.zup.otmovies.LoginActivity.LoginActivity;

public class DialogSessionExpired extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Sessão Expirada!")
                .setTitle("Aviso:")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    }
                });

        getDialog().setCanceledOnTouchOutside(false);

        return builder.create();
    }
}
