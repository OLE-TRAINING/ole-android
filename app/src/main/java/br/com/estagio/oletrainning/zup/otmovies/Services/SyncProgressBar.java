package br.com.estagio.oletrainning.zup.otmovies.Services;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ProgressBar;

public class SyncProgressBar extends AsyncTask<Object, Object, String> {

    private ProgressBar progressBar;
    private int total = 0;
    private static int PROGRESS = 20;

    public SyncProgressBar(Context context, ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Object... params) {
        try {

            Thread.sleep(500);

            for (int i=0; i<8; i++) {
                publishProgress();
                Thread.sleep(500);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Object... values) {
        total += PROGRESS;
        progressBar.incrementProgressBy(PROGRESS);

        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        progressBar.setVisibility(ProgressBar.INVISIBLE);
        super.onPostExecute(result);
    }
}