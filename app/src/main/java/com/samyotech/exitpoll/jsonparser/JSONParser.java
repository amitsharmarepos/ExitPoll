package com.samyotech.exitpoll.jsonparser;

import android.content.Context;
import android.util.Log;

import com.samyotech.exitpoll.dto.Album;
import com.samyotech.exitpoll.dto.Favdto;
import com.samyotech.exitpoll.dto.UserDTO;
import com.samyotech.exitpoll.sharedpref.SharedPrefrence;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class JSONParser {
    String jsonObjResponse;
    public String ERROR = "";
    public String MESSAGE = "";
    public boolean RESULT = false;
    public Context context;
    public static String PIC_KEY = "profilepic";
    private SharedPrefrence sharedPrefrence;
    JSONObject jObj;


    public static String TAG_ERROR = "error";
    public static String TAG_MESSAGE = "message";


    public JSONParser(Context context, String response) {
        this.context = context;
        this.jsonObjResponse = response;
        sharedPrefrence = SharedPrefrence.getInstance(context);

        try {
            jObj = new JSONObject(response);
            ERROR = getJsonString(jObj, TAG_ERROR);

            MESSAGE = getJsonString(jObj, TAG_MESSAGE);
            if (ERROR.equals("true"))
                RESULT = false;
            else
                RESULT = true;

        } catch (JSONException e) {
            jObj = null;
            e.printStackTrace();
        }
    }

    public static boolean getBoolean(JSONObject jObj, String val) {
        if (val.equals("true"))
            return true;
        else
            return false;
    }

    public static JSONObject getJsonObject(JSONObject obj, String parameter) {
        try {
            return obj.getJSONObject(parameter);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject();
        }

    }

    public static String getJsonString(JSONObject obj, String parameter) {
        try {
            return obj.getString(parameter);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    public static JSONArray getJsonArray(JSONObject obj, String parameter) {
        try {
            return obj.getJSONArray(parameter);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONArray();
        }

    }


    public void signupResponse() {

        try {

            Log.e("Signup response", "" + jObj.toString());
            String Status = getJsonString(jObj, "status");
            Boolean signupCheck = Boolean.valueOf(Status);
            Log.e("signupCheck", "" + signupCheck);
            sharedPrefrence.setBooleanValue("signupCheck", signupCheck);
            JSONObject obj = getJsonObject(jObj, "profile");
            UserDTO userDTO = new UserDTO();
            userDTO.setName(getJsonString(obj, "name"));
            userDTO.setDob(getJsonString(obj, "dob"));
            userDTO.setMobileno(getJsonString(obj, "mobile"));
            userDTO.setDoor(getJsonString(obj, "address"));
            userDTO.setPincode(getJsonString(obj, "pincode"));

            sharedPrefrence.setUserDetails(userDTO, SharedPrefrence.USER_DETAIL);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loginResponse() {
        try {


            String loginStatus = getJsonString(jObj, "status");
            Boolean loginCheck = Boolean.valueOf(loginStatus);
            Log.e("loginStatus", "" + loginCheck);
            JSONObject obj = getJsonObject(jObj, "profile");
            Log.e("login response", "" + obj.toString());
            sharedPrefrence.setBooleanValue("loginStatus", loginCheck);

            UserDTO userDTO = new UserDTO();
            userDTO.setName(getJsonString(obj, "name"));
            userDTO.setDob(getJsonString(obj, "dob"));
            userDTO.setMobileno(getJsonString(obj, "mobile"));
            userDTO.setDoor(getJsonString(obj, "address"));
            userDTO.setPincode(getJsonString(obj, "pincode"));

            sharedPrefrence.setUserDetails(userDTO, SharedPrefrence.USER_DETAIL);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dataResponse() {
        try {
            ArrayList<Album> albumList = new ArrayList<Album>();
            Log.e("data response", "" + jObj.toString());
            String dataStatus = getJsonString(jObj, "status");
            Boolean dataCheck = Boolean.valueOf(dataStatus);
            Log.e("dataStatus", "" + dataCheck);
            sharedPrefrence.setBooleanValue("dataCheck", dataCheck);

            JSONArray appointment = getJsonArray(jObj, "data");
            for (int i = 0; i < appointment.length(); i++) {

                JSONObject jobject = appointment.getJSONObject(i);
                Album album = new Album();
                Log.e("poliid", getJsonString(jobject, "politicsid"));
                album.setId(getJsonString(jobject, "politicsid"));

                album.setName(getJsonString(jobject, "cname"));
                album.setPost(getJsonString(jobject, "cpost"));
                album.setPartyname(getJsonString(jobject, "cparty"));
                album.setWhichpost(getJsonString(jobject, "cwhichpost"));
                album.setEducation(getJsonString(jobject, "ceducation"));
                album.setAge(getJsonString(jobject, "cage"));
                album.setImg(getJsonString(jobject, "cimg"));

                albumList.add(album);

            }
            sharedPrefrence.setList(SharedPrefrence.POLITATION_RECORD, albumList);
//            MainActivity mn = (MainActivity) context;
//            mn.check();

            Log.e("POLITATION_RECORD", "<-- get POLITATION detail..." + sharedPrefrence.getList(SharedPrefrence.POLITATION_RECORD));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetpassResponse() {
        try {

            Log.e("resetpass response", "" + jObj.toString());
            String resetStatus = getJsonString(jObj, "status");
            Boolean resetCheck = Boolean.valueOf(resetStatus);
            Log.e("resetStatus", "" + resetCheck);
            sharedPrefrence.setBooleanValue("resetStatus", resetCheck);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void userUpdate() {
        try {
            Log.e("userUpdate response", "" + jObj.toString());

            JSONObject obj = getJsonObject(jObj, "profile");
            Log.e("userUpdate response", "" + obj.toString());
            UserDTO userDTO = new UserDTO();
            userDTO.setName(getJsonString(obj, "name"));
            userDTO.setDob(getJsonString(obj, "dob"));
            userDTO.setMobileno(getJsonString(obj, "mobile"));
            userDTO.setImage(getJsonString(obj, "img_url"));
            userDTO.setDoor(getJsonString(obj, "address"));
            userDTO.setPincode(getJsonString(obj, "pincode"));

            sharedPrefrence.setUserDetails(userDTO, SharedPrefrence.USER_DETAIL);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void forgetresponse() {
        try {

            Log.e("forgetpass response", "" + jObj.toString());
            String forgetstatus = getJsonString(jObj, "status");
            Boolean resetCheck = Boolean.valueOf(forgetstatus);
            sharedPrefrence.setBooleanValue("forgetstatus", resetCheck);
            sharedPrefrence.setValue("message", getJsonString(jObj, "message"));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void favouriteresponse() {
        Log.e("favourite response", "" + jObj.toString());
    }

    public void deletefavresponse() {
        Log.e("deletefav response", "" + jObj.toString());
    }

    public void viewfavresponse() {

        try {
            ArrayList<Album> albumList = new ArrayList<Album>();
            Log.e("viewfavourite response", "" + jObj.toString());
            String viewfavStatus = getJsonString(jObj, "status");
            Boolean viewfavCheck = Boolean.valueOf(viewfavStatus);
            Log.e("viewfavStatus", "" + viewfavCheck);
            sharedPrefrence.setBooleanValue("viewfavCheck", viewfavCheck);

            JSONArray appointment = getJsonArray(jObj, "data");
            for (int i = 0; i < appointment.length(); i++) {

                JSONObject jobject = appointment.getJSONObject(i);
                Album album = new Album();
                Log.e("poliid", getJsonString(jobject, "politicsid"));
                album.setId(getJsonString(jobject, "politicsid"));

                album.setName(getJsonString(jobject, "cname"));
                album.setPost(getJsonString(jobject, "cpost"));
                album.setPartyname(getJsonString(jobject, "cparty"));
                album.setWhichpost(getJsonString(jobject, "cwhichpost"));
                album.setEducation(getJsonString(jobject, "ceducation"));
                album.setAge(getJsonString(jobject, "cage"));
                album.setImg(getJsonString(jobject, "cimg"));

                albumList.add(album);

            }
            sharedPrefrence.setList(SharedPrefrence.VIEW_POLITATION_RECORD, albumList);

            Log.e("VIEW_POLITATION_RECORD", "<-- get POLITATION detail..." + sharedPrefrence.getList(SharedPrefrence.VIEW_POLITATION_RECORD));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void allfavresponse() {

        try {
            Log.e("allfavresponse", "" + jObj.toString());
            ArrayList<Favdto> favList = new ArrayList<Favdto>();
            String message = jObj.getString("message");
            String status = jObj.getString("status");
            JSONArray array = getJsonArray(jObj, "data");
            for (int i = 0; i < array.length(); i++) {

                JSONObject jobject = array.getJSONObject(i);
                Favdto fav = new Favdto();
                Log.e("poliid", getJsonString(jobject, "politicsid"));

                fav.setPoliticsid(getJsonString(jobject, "politicsid"));
                fav.setUseremail(getJsonString(jobject, "useremail"));
                fav.setSno(getJsonString(jobject, "sno"));

                favList.add(fav);

            }

            sharedPrefrence.setFavList(SharedPrefrence.FAV_RECORD, favList);

            Log.e("allfavresponse", "<-- get allfavresponse detail..." + sharedPrefrence.getFavFList(SharedPrefrence.FAV_RECORD));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}