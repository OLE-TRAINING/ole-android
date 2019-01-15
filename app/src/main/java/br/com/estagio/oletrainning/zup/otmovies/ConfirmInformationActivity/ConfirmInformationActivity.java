package br.com.estagio.oletrainning.zup.otmovies.ConfirmInformationActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;

import br.com.estagio.oletrainning.zup.otmovies.InformTokenAndNewPasswordActivity.InformTokenAndNewPasswordActivity;
import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.Services.ErrorMessage;
import br.com.estagio.oletrainning.zup.otmovies.Services.RetrievingUserDatesService;
import br.com.estagio.oletrainning.zup.otmovies.Services.ServiceBuilder;
import br.com.estagio.oletrainning.zup.otmovies.Services.SyncProgressBar;
import br.com.estagio.oletrainning.zup.otmovies.Services.UserDates;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmInformationActivity extends AppCompatActivity {

    private ConfirmInformationViewHolder confirmInformationViewHolder;
    private final Integer MAXSIZEUSERNAME = 15;
    private final String USER_NAME_VALIDATION_STATUS = "userNameContainsError";

    private boolean userNameContainsError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = this.getLayoutInflater().inflate(R.layout.activity_confirm_information, null);
        this.confirmInformationViewHolder = new ConfirmInformationViewHolder(view);
        setContentView(view);

        setupListeners();

        String emailAdd = getIntent().getStringExtra(getString(R.string.EmailPreLogin));
        confirmInformationViewHolder.textViewEmail.setText(emailAdd);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(USER_NAME_VALIDATION_STATUS, userNameContainsError);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        userNameContainsError = savedInstanceState.getBoolean(USER_NAME_VALIDATION_STATUS);
        confirmInformationViewHolder.errorEditText.setErrorVisibility(userNameContainsError);
    }

    private void setupListeners() {
        confirmInformationViewHolder.imageView.setOnClickListener(backArrowOnClickListener);
        confirmInformationViewHolder.errorEditText.getEditText().addTextChangedListener(errorEditTextTextWatcher);
        confirmInformationViewHolder.button.setOnClickListener(buttonNextOnClickListener);
    }

    View.OnClickListener backArrowOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.imageView_backArrow) {
                Intent intent = new Intent(ConfirmInformationActivity.this, ConfirmInformationActivity.class);
                startActivity(intent);
            }
        }
    };

    private View.OnClickListener buttonNextOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            confirmInformationViewHolder.progressBar.setVisibility(View.VISIBLE);
            userNameContainsError = !validateUserName();
            confirmInformationViewHolder.errorEditText.setErrorVisibility(userNameContainsError);
            String emailEntered = confirmInformationViewHolder.textViewEmail.getText().toString().trim();
            if (validateUserName()) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                new SyncProgressBar(ConfirmInformationActivity.this, confirmInformationViewHolder.progressBar).execute();

                RetrievingUserDatesService retrievingUserDatesService = ServiceBuilder.buildService(RetrievingUserDatesService.class);
                Call<UserDates> userDatesCall = retrievingUserDatesService.getUsersDate(emailEntered, "593c3280aedd01364c73000d3ac06d76");
                userDatesCall.enqueue(new Callback<UserDates>() {
                    @Override
                    public void onResponse(Call<UserDates> call, Response<UserDates> response) {
                        UserDates userDates = response.body();
                        if (response.isSuccessful() && userDates != null) {
                            if (userDates.getUsername().equals(confirmInformationViewHolder.errorEditText.getText().toString().trim())) {
                                Intent intent = new Intent(ConfirmInformationActivity.this, InformTokenAndNewPasswordActivity.class);
                                String emailInput = confirmInformationViewHolder.errorEditText.getText().toString().trim();
                                intent.putExtra(getString(R.string.EmailPreLogin), emailInput);
                                startActivity(intent);
                            } else{
                                confirmInformationViewHolder.errorEditText.setMessageError("Nome de usuário ou e-mail não coincidem");
                                confirmInformationViewHolder.errorEditText.setErrorVisibility(true);
                                confirmInformationViewHolder.progressBar.setVisibility(View.INVISIBLE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            }
                        } else {
                            confirmInformationViewHolder.progressBar.setVisibility(View.INVISIBLE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            try {
                                Gson gson = new Gson();
                                Type type = new TypeToken<ErrorMessage>() {}.getType();
                                ErrorMessage errorMessage = gson.fromJson(response.errorBody().charStream(),type);
                                if(errorMessage.getKey().equals("error.invalid.username")){
                                    confirmInformationViewHolder.errorEditText.setErrorVisibility(true);
                                } else {
                                    Toast.makeText(ConfirmInformationActivity.this,errorMessage.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(ConfirmInformationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserDates> call, Throwable t) {
                        confirmInformationViewHolder.progressBar.setVisibility(View.INVISIBLE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        if(t instanceof IOException){
                            Toast.makeText(ConfirmInformationActivity.this,"Ocorreu um erro na conexão", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(ConfirmInformationActivity.this,"Falha ao confirmar usuário", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } else {
                confirmInformationViewHolder.progressBar.setVisibility(View.INVISIBLE);
            }
        }
    };

    TextWatcher errorEditTextTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            userNameContainsError = false;
            confirmInformationViewHolder.errorEditText.setErrorVisibility(false);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private boolean validateUserName() {
        String userName = confirmInformationViewHolder.errorEditText.getText().toString().trim();
        return (!userName.isEmpty() && validateUserNameFormat());
    }

    private boolean validateUserNameFormat() {
        String userName = confirmInformationViewHolder.errorEditText.getText().toString().trim();
        return userName.length() <= MAXSIZEUSERNAME && userName.matches(getString(R.string.RegexOnlyNumberOrLetter));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ConfirmInformationActivity.this, ConfirmInformationActivity.class);
        startActivity(intent);
    }
}
