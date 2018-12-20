package br.com.estagio.oletrainning.zup.otmovies.LoginActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import br.com.estagio.oletrainning.zup.otmovies.R;

public class LoginActivity extends AppCompatActivity {

    private LoginViewHolder loginViewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = this.getLayoutInflater().inflate(R.layout.activity_login, null);
        this.loginViewHolder = new LoginViewHolder(view);
        setContentView(view);

        setupListeners();
    }

    private void setupListeners() {
        loginViewHolder.buttonSignIn.setOnClickListener(buttonSignInOnClickListener);
        loginViewHolder.textViewForgetPassword.setOnClickListener(textViewForgetPasswordOnClickListener);
    }

    View.OnClickListener buttonSignInOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showToast();
        }
    };

    private void showToast() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.component_red_toast, (ViewGroup) findViewById(R.id.toast_layout_root));
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.TOP|Gravity.FILL_HORIZONTAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    View.OnClickListener textViewForgetPasswordOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
}
