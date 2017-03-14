package com.samyotech.exitpoll.https;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.samyotech.exitpoll.jsonparser.JSONParser;
import com.samyotech.exitpoll.utils.Consts;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class NetworkTask extends AsyncTask<String, Void, Boolean> {

    String match;
    ContentValues contentValues;
    Context ctx;
    String readableMsg = "";

    public NetworkTask(String match, ContentValues contentValues, Context ctx) {
        this.match = match;
        this.contentValues = contentValues;
        this.ctx = ctx;
    }

    // you may separate this or combined to caller class.
    public interface AsyncResponse {
        void processFinish(boolean output, String message);

//        void processFinish(boolean output, String message, JSONObject jsonObject);
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
    protected Boolean doInBackground(String... urls) {

        try {
            String response = null;
            if (urls[0].equals(Consts.POST_METHOD)) {
                response = excutePost(Consts.BASE_URL + match);
            } else {
                response = excuteGet(Consts.BASE_URL + match);
            }

            JSONParser jsonParser = new JSONParser(ctx, response);
            if (jsonParser != null) {
                if (match.equals(Consts.REGISTER_METHOD)) {
                    readableMsg = jsonParser.MESSAGE;
                    jsonParser.signupResponse();
                    return jsonParser.RESULT;
                } else if (match.equals(Consts.LOGIN_METHOD)) {
                    readableMsg = jsonParser.MESSAGE;
                    jsonParser.loginResponse();
                    return jsonParser.RESULT;
                } else if (match.equals(Consts.DATA_METHOD)) {
                    readableMsg = jsonParser.MESSAGE;
                    jsonParser.dataResponse();
                    return jsonParser.RESULT;
                } else if (match.equals(Consts.RESET_METHOD)) {
                    readableMsg = jsonParser.MESSAGE;
                    jsonParser.resetpassResponse();
                    return jsonParser.RESULT;
                } else if (match.equals(Consts.FORGOT_PASSWORd)) {
                    readableMsg = jsonParser.MESSAGE;
                    jsonParser.forgetresponse();
                    return jsonParser.RESULT;
                } else if (match.equals(Consts.FAVOURITE_METHOD)) {
                    readableMsg = jsonParser.MESSAGE;
                    jsonParser.favouriteresponse();
                    return jsonParser.RESULT;
                } else if (match.equals(Consts.DELETEFAV_METHOD)) {
                    readableMsg = jsonParser.MESSAGE;
                    jsonParser.deletefavresponse();
                    return jsonParser.RESULT;
                } else if (match.equals(Consts.VIEWFAV_METHOD)) {
                    readableMsg = jsonParser.MESSAGE;
                    jsonParser.viewfavresponse();
                    return jsonParser.RESULT;
                } else if (match.equals(Consts.ALLFAV_METHOD)) {
                    readableMsg = jsonParser.MESSAGE;
                    jsonParser.allfavresponse();
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

    protected void onPostExecute(Boolean result) {
        if (delegate != null)
            this.delegate.processFinish(result, readableMsg);
    }


    public String excutePost(String targetURL) {
        URL url;
        HttpURLConnection connection = null;
        try {
            //Create connection
            url = new URL(targetURL);
//            connection = MySSLSocketFactory.getHttpUrlConnection(url);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Length", "" +
                    Integer.toString(getQuery(contentValues).getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setConnectTimeout(12000);
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            try {
                //Send request
                Log.e("connection", "" + connection);
                DataOutputStream wr = new DataOutputStream(
                        connection.getOutputStream());
                wr.writeBytes(getQuery(contentValues));
                wr.flush();
                wr.close();
            } catch (SocketTimeoutException sto) {
                sto.printStackTrace();
                return null;
            }

            StringBuffer response = null;
            try {
                //Get Response
                InputStream is = connection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                response = new StringBuffer();
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                }
                rd.close();
                Log.e("response", "" + response);
            } catch (InterruptedIOException ie) {
                ie.printStackTrace();
            }
            return response.toString();

        } catch (Exception e) {

            e.printStackTrace();
            return null;

        } finally {

            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public String excuteGet(String targetURL) {
        URL url;
        HttpURLConnection connection = null;
        StringBuilder responseOutput = null;
        try {
            //Create connection
            url = new URL(targetURL);
//            connection = MySSLSocketFactory.getHttpUrlConnection(url);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(12000);
            connection.setReadTimeout(12000);

            int responseCode = connection.getResponseCode();

            final StringBuilder output = new StringBuilder("Request URL " + url);
            output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
            output.append(System.getProperty("line.separator") + "Type " + "GET");
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            responseOutput = new StringBuilder();
            System.out.println("output===============" + br);
            while ((line = br.readLine()) != null) {
                responseOutput.append(line);
            }
            br.close();


            Log.e("responseOutput", "" + responseOutput);


        } catch (SocketTimeoutException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;

        } finally {

            if (connection != null) {
                connection.disconnect();
            }
        }
        return responseOutput.toString();
    }

    private String getQuery(ContentValues params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        try {
            Set<Map.Entry<String, Object>> s = params.valueSet();
            Iterator itr = s.iterator();
            while (itr.hasNext()) {
                Map.Entry me = (Map.Entry) itr.next();
                String key = me.getKey().toString();
                String value = (String) me.getValue();

                if (first)
                    first = false;
                else
                    result.append("&");
                //result.append(URLEncoder.encode(key, "UTF-8"));
                result.append(key);
                result.append("=");
                result.append(URLEncoder.encode(value, "UTF-8"));

            }
            return result.toString();
        } catch (Exception e) {
        }
        return result.toString();
    }
}