package com.stingandroid.firebaseproject.features.shared;

public interface BasePresenter <V extends BaseContractView> {

    void attachView (V mvpView);

    void detachView();

    V getView();
}
