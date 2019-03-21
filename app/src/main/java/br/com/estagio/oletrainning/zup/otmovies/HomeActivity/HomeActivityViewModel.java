package br.com.estagio.oletrainning.zup.otmovies.HomeActivity;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;

import br.com.estagio.oletrainning.zup.otmovies.Common.CommonViewModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.Repositories.FilmRepository;

public class HomeActivityViewModel extends CommonViewModel {

    private FilmRepository filmRepository = new FilmRepository();

    private MutableLiveData<Boolean> homeTellerIsSessionExpired = new MutableLiveData<>();

    public MutableLiveData<Boolean> getHomeTellerIsSessionExpired() {
        return homeTellerIsSessionExpired;
    }

    public void startSessionServiceObserver(){
        filmRepository.getIsSessionExpiredService().observeForever(isSessionExpiredServiceObserver);
    }

    private Observer<Boolean> isSessionExpiredServiceObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean isSessionExpired) {
            if(isSessionExpired){
                homeTellerIsSessionExpired.setValue(true);
            }
        }
    };

    @Override
    public void removeObserver() {
        super.removeObserver();
        if (filmRepository.getIsSessionExpiredService() != null)  {
            filmRepository.getIsSessionExpiredService().removeObserver(isSessionExpiredServiceObserver);
        }
    }



}
