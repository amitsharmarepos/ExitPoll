package com.samyotech.exitpoll.sharedpref;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samyotech.exitpoll.dto.Album;
import com.samyotech.exitpoll.dto.Favdto;
import com.samyotech.exitpoll.dto.UserDTO;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SharedPrefrence {
    public static SharedPreferences myPrefs;
    public static SharedPreferences.Editor prefsEditor;
    public static SharedPrefrence myObj;


    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String DOB = "dob";
    public static final String EMAIL = "email";
    public static final String MOBILE = "mobile";
    public static final String PASSWORD = "password";
    public static final String USER_EMAIL = "user_email";
    public static final String POLITATION_RECORD = "politationrecord";
    public static final String VIEW_POLITATION_RECORD = "viewpolitationrecord";
    public static final String FAV_RECORD = "favlist";
    public static final String FORGOT_PASS_EMAIL = "forgotPassEmail";

    public static final String IS_LOGIN = "isLogin";
    public static final String IS_FAV = "isfav";
    public static final String USER_DETAIL = "userDetails";
    public static final String TOKAN = "tokan";
    public static final String NOTIFIOCATION_ENABLE = "notificationAlert";

    private SharedPrefrence() {

    }


    public void setIntValue(String Tag, int value) {
        prefsEditor.putInt(Tag, value);
        prefsEditor.apply();
    }

    public int getIntValue(String Tag) {
        return myPrefs.getInt(Tag, 0);

    }


    public void setValue(String Tag, String token) {
        prefsEditor.putString(Tag, token);
        prefsEditor.commit();
    }


    public String getValue(String Tag) {
        return myPrefs.getString(Tag, "default");
    }

    public boolean getBooleanValue(String Tag) {
        if (Tag.equals(IS_LOGIN))
            return myPrefs.getBoolean(Tag, false);
        if (Tag.equals(IS_FAV))
            return myPrefs.getBoolean(Tag, false);
        if (Tag.equals(NOTIFIOCATION_ENABLE))
            return myPrefs.getBoolean(Tag, true);
        return myPrefs.getBoolean(Tag, false);

    }

    public void setBooleanValue(String Tag, boolean token) {
        prefsEditor.putBoolean(Tag, token);
        prefsEditor.commit();
    }

    public static SharedPrefrence getInstance(Context ctx) {
        if (myObj == null) {
            myObj = new SharedPrefrence();
            myPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
            prefsEditor = myPrefs.edit();
        }
        return myObj;
    }

    public void setList(String Tag, ArrayList<Album> lst) {
        Gson gson = new Gson();
        String json = gson.toJson(lst);
        prefsEditor.putString(Tag, json);
        prefsEditor.commit();
    }

    public ArrayList<Album> getList(String Tag) {
        String obj = myPrefs.getString(Tag, "defValue");
        if (obj.equals("defValue")) {
            return null;
        } else {
            Type type = new TypeToken<ArrayList<Album>>() {
            }.getType();
            Gson gson = new Gson();
            ArrayList<Album> List = gson.fromJson(obj, type);
            return List;
        }
    }
    public void setFavList(String Tag, ArrayList<Favdto> lst) {
        Gson gson = new Gson();
        String json = gson.toJson(lst);
        prefsEditor.putString(Tag, json);
        prefsEditor.commit();
    }

    public ArrayList<Favdto> getFavFList(String Tag) {
        String obj = myPrefs.getString(Tag, "defValue");
        if (obj.equals("defValue")) {
            return null;
        } else {
            Type type = new TypeToken<ArrayList<Favdto>>() {
            }.getType();
            Gson gson = new Gson();
            ArrayList<Favdto> List = gson.fromJson(obj, type);
            return List;
        }
    }


    public void setUserDetails(UserDTO userDTO, String tag) {

        //convert to string using gson
        Gson gson = new Gson();
        String hashMapString = gson.toJson(userDTO);

        prefsEditor.putString(tag, hashMapString);
        prefsEditor.apply();
    }

    public UserDTO getUserDetails(String tag) {
        String obj = myPrefs.getString(tag, "defValue");
        if (obj.equals("defValue")) {
            return new UserDTO();
        } else {

            Gson gson = new Gson();
            String storedHashMapString = myPrefs.getString(tag, "");
            Type type = new TypeToken<UserDTO>() {
            }.getType();
            UserDTO testHashMap = gson.fromJson(storedHashMapString, type);

            return testHashMap;
        }
    }

    public void clearPreferences(String key) {
        prefsEditor.remove(key);
        prefsEditor.commit();
    }


}
