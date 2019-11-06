package com.stingandroid.firebaseproject.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.stingandroid.firebaseproject.data.models.User;
import com.stingandroid.firebaseproject.injection.annotations.ApplicationScope;
import com.stingandroid.firebaseproject.utils.Constants;

import javax.inject.Inject;

@ApplicationScope
public class PreferenceManager {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    @Inject
    public PreferenceManager(Context context){
        prefs = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public long getTimeOut(){
        return 60000000L;
    }

    public String getBaseURL(){
        return "http://www.omdbapi.com/";
    }

    public User getUserData(){
        User user = new User();
        String userData = prefs.getString(Constants.USER_DATA,"");
        if (TextUtils.isEmpty(userData)){
            setUserData(user);
        } else {
            user = new Gson().fromJson(userData,User.class);
        }
        return user;
    }

    public void setUserData(User user){
        editor.putString(Constants.USER_DATA,new Gson().toJson(user,User.class)).commit();
    }

    public boolean isLoginShowingRestart(){ return prefs.getBoolean(Constants.IS_LOGIN_SHOWING,false); }

    public void setLoginShowing(boolean isLoginShowing){
        editor.putBoolean(Constants.IS_LOGIN_SHOWING,isLoginShowing).commit();
    }


}
