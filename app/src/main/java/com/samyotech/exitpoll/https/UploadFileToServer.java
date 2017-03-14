package com.samyotech.exitpoll.https;


import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import com.samyotech.exitpoll.dto.BaseDTO;
import com.samyotech.exitpoll.https.HttpClient;
import com.samyotech.exitpoll.jsonparser.JSONParser;
import com.samyotech.exitpoll.utils.Consts;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class UploadFileToServer extends AsyncTask<String, Void, Boolean> {
    String match;
    ContentValues contentValuesForParam;
    ContentValues contentValuesForByte;
    Context ctx;
    String readableMsg = "";
    ArrayList<String> urls = new ArrayList<String>();
    HttpClient client;
    BaseDTO baseDTO;

    public UploadFileToServer(String match, ContentValues contentValuesForParam, ContentValues contentValuesForByte, Context ctx) {
        this.match = match;
        this.contentValuesForParam = contentValuesForParam;
        this.ctx = ctx;
        this.contentValuesForByte = contentValuesForByte;
    }

    public interface AsyncResponse {


        void processFinish(boolean output, String message, BaseDTO baseDTO);

        void processFinish(boolean output, String message);
    }

    public AsyncResponse delegate = null;

    public void setOnTaskFinishedEvent(AsyncResponse delegate) {
        if (delegate != null) {
            this.delegate = delegate;
        }
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Boolean doInBackground(String... urlsServer) {

        try {
            String response = null;
            client = new HttpClient(Consts.BASE_URL + match);
            client.connectForMultipart();
            addParam();
            addByte();

            client.finishMultipart();
            response = client.getResponse();

            JSONParser jsonParser = new JSONParser(ctx, response);
            if (jsonParser != null) {
                if (match.equals(Consts.UPDATE_USER_PROFILE)) {
                    readableMsg = jsonParser.MESSAGE;
                    jsonParser.userUpdate();
                    return jsonParser.RESULT;
                }


                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    protected void onPostExecute(Boolean result) {

        if (delegate != null && (match.equals(Consts.UPDATE_USER_PROFILE))) {
            this.delegate.processFinish(result, readableMsg);
        }


    }

    private void addParam() throws UnsupportedEncodingException {
        try {
            Set<Map.Entry<String, Object>> s = contentValuesForParam.valueSet();
            Iterator itr = s.iterator();
            while (itr.hasNext()) {
                Map.Entry me = (Map.Entry) itr.next();
                String key = me.getKey().toString();
                String value = (String) me.getValue();

                // client.addFormPart(URLEncoder.encode(key, "UTF-8"), URLEncoder.encode(value, "UTF-8"));
                client.addFormPart(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addByte() throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        try {
            Set<Map.Entry<String, Object>> s = contentValuesForByte.valueSet();
            Iterator itr = s.iterator();
            while (itr.hasNext()) {
                Map.Entry me = (Map.Entry) itr.next();
                String key = me.getKey().toString();
                byte[] value = (byte[]) me.getValue();
                String image_name = "ExitPoll "+System.currentTimeMillis()+".png";
                client.addFilePart(key, image_name, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}