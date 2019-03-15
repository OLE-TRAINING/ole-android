package br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Fragments.SessionControl;

import android.app.Application;
import android.content.Intent;
import android.os.CountDownTimer;

import br.com.estagio.oletrainning.zup.otmovies.LoginActivity.LoginActivity;

public class SessionControl extends Application {

    MyCounter timer;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void iniciar() {
        if (timer == null) {
            timer = new MyCounter(30000, 1000);
            timer.start();
        } else {
            timer.start();
        }
    }

    public void parar() {
        if (timer.isRunning) {
            timer.cancel();
        }
    }

    public void touch() {

        if (timer.isRunning) {
            timer.cancel();
            timer.start();
        }
    }

    public Long getTime() {
        return timer.time;
    }

    public class MyCounter extends CountDownTimer {

        private boolean isRunning = false;
        private Long time;

        public MyCounter(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {

            isRunning = false;
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), LoginActivity.class);
            intent.putExtra("TimeOut", true);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }

        @Override
        public void onTick(long millisUntilFinished) {
            time = millisUntilFinished;
            isRunning = true;

            sendBroadcast(new Intent("TIMEOUT"));
        }
    }
}


