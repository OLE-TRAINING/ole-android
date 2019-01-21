package br.com.estagio.oletrainning.zup.otmovies.Services.ViewModel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.database.Observable;
import android.support.annotation.NonNull;

import java.util.Objects;

import br.com.estagio.oletrainning.zup.otmovies.Services.Model.UserResponse;
import br.com.estagio.oletrainning.zup.otmovies.Services.UserDates;


public class UserDatesViewModel extends ViewModel {

    private LiveData<UserResponse> newsResponseObservable;

    private static final MutableLiveData MUTABLE_LIVE_DATA = new MutableLiveData();
    {
        MUTABLE_LIVE_DATA.setValue(null);
    }

    public UserDatesViewModel(@NonNull HeadLineRepository headLineRepository)
    {
        newsResponseObservable = HeadLineRepository.getInstance()
                .getHeadLine("in","593c3280aedd01364c73000d3ac06d76");
    }

    public LiveData<UserResponse> getNewsResponseObservable() {
        return newsResponseObservable;
    }
}
