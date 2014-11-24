package com.example.eduardobaltazar.mypofin;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class NetServices extends AsyncTask<Integer, Void, String>
{
    public static final String WS_CALL_PROCESS_INSERT = "insert";
    public static final String WS_CALL_PROCESS_UPDATE = "update";
    public static final String WS_CALL_PROCESS_DELETE = "delete";
    public static final int WS_CALL_GET_LOGIN = 0;
    public static final int WS_CALL_POST_REGISTER = 1;
    public static final int WS_CALL_GET_POINT = 2;
    public static final int WS_CALL_GET_POINTSLIST = 3;
    public static final int WS_CALL_POST_POINT = 4;
    private final String URL_WS_GET_LOGIN = "http://spaco2710.byethost9.com/login.php";
    private final String URL_WS_POST_REGISTER = "http://spaco2710.byethost9.com/signup.php";
    private final String URL_WS_GET_POINT = "http://spaco2710.byethost9.com/point_info.php";
    private final String URL_WS_GET_POINTSLIST = "http://spaco2710.byethost9.com/points.php";
    private final String URL_WS_POST_POINT = "http://spaco2710.byethost9.com/point_up.php";

    private final int STATUS_OK = 200;
    private final int STATUS_CREATED = 201;
    private final int STATUS_ERROR = 400;
    private final int STATUS_NOT_FOUND = 404;
    private final int STATUS_NOT_ACCEPTABLE = 406;
    private final int STATUS_UNKNOWN = 300;
    private final String ERROR = "WebServices not responding";
    private OnBackgroundTaskCallback callbacks;
    private FunctionProgressBar fprogressbar;
    private UserModel userModel;
    private PointModel pointModel;
    private int status;
    private String process;
    
    public NetServices(OnBackgroundTaskCallback callbacks, UserModel userModel, PointModel pointModel, String process){
    	this.callbacks = callbacks;
        this.userModel = userModel;
        this.pointModel = pointModel;
        this.process = process;
    }

    public void setProgressBarBack(FunctionProgressBar fprogressbar) {
        this.fprogressbar = fprogressbar;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (fprogressbar!=null) {
            fprogressbar.showProgress();
        }
    }

	protected String doInBackground(Integer... wsCall) {
    	String response = null;
    		try{
                switch(wsCall[0]){
                    case WS_CALL_GET_LOGIN:
                        response = connectionPOST(WS_CALL_GET_LOGIN);
                        break;

                    case WS_CALL_POST_REGISTER:
                        response = connectionPOST(WS_CALL_POST_REGISTER);
                        break;

                    case WS_CALL_GET_POINT:
                        response = connectionPOST(WS_CALL_GET_POINT);
                        break;

                    case WS_CALL_GET_POINTSLIST:
                        response = connectionPOST(WS_CALL_GET_POINTSLIST);
                        break;

                    case WS_CALL_POST_POINT:
                        response = connectionPOST(WS_CALL_POST_POINT);
                        break;
                }
    		}catch(Exception e){
    			e.printStackTrace();
    		}

    	return response;
    }

    private String connectionPOST(int ws_opc) {
        HttpPost httpPost = new HttpPost();
        JSONObject objson = new JSONObject();

        switch(ws_opc){
            case WS_CALL_GET_LOGIN:
                httpPost = new HttpPost(URL_WS_GET_LOGIN);
                try {
                    objson.put("email", userModel.getEmail());
                    objson.put("password", userModel.getPassword());
                } catch (JSONException e) {
                }
                break;

            case WS_CALL_GET_POINT:
                httpPost = new HttpPost(URL_WS_GET_POINT);
                try {
                    objson.put("point_id", pointModel.getId());
                } catch (JSONException e) {
                }
                break;

            case WS_CALL_GET_POINTSLIST:
                httpPost = new HttpPost(URL_WS_GET_POINTSLIST);
                try {
                    objson.put("user_id", userModel.getId());
                } catch (JSONException e) {
                }
                break;

            case WS_CALL_POST_REGISTER:
                httpPost = new HttpPost(URL_WS_POST_REGISTER);
                try {
                    objson.put("email", userModel.getEmail());
                    objson.put("password", userModel.getPassword());
                    objson.put("name", userModel.getName());
                } catch (JSONException e) {
                }
                break;

            case WS_CALL_POST_POINT:
                httpPost = new HttpPost(URL_WS_POST_POINT);
                try {
                    objson.put("point_id", pointModel.getId());
                    objson.put("user_id", pointModel.getUser_id());
                    objson.put("title", pointModel.getTitle());
                    objson.put("latitude", pointModel.getLatitude());
                    objson.put("longitude", pointModel.getLongitude());
                    objson.put("comment", pointModel.getComment());
                    objson.put("process", process);
                } catch (JSONException e) {
                }
                break;
        }

        //httpPost.setHeader("X-Parse-Application-Id", APP_ID);
        //httpPost.setHeader("X-Parse-REST-API-Key", REST_API_KEY);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");

        try {
            StringEntity se = new StringEntity(objson.toString());
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httpPost.setEntity((HttpEntity) se);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        HttpParams httpParameters = new BasicHttpParams();
        int timeoutConnection = 12000; //Timeout until a connection is established.
        int timeoutSocket = 12000; //Timeout for waiting for data.

        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

        HttpClient httpClient = new DefaultHttpClient(httpParameters);

        try{
            HttpResponse response = httpClient.execute(httpPost);
            status = response.getStatusLine().getStatusCode();

            switch(status){
                case STATUS_OK:
                case STATUS_CREATED:
                case STATUS_ERROR:
                case STATUS_NOT_ACCEPTABLE:
                    HttpEntity e = response.getEntity();
                    return parseData(e.getContent());

                case STATUS_NOT_FOUND:
                case STATUS_UNKNOWN:
                default:
                    return ERROR;
            }
        }
        catch (ClientProtocolException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return ERROR;
    }

    private String parseData(InputStream content) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(content, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            content.close();
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ERROR;
    }

    protected void onPostExecute(String response) {
        if (fprogressbar!=null) {
            fprogressbar.hideProgress();
        }

        switch(status){
            case STATUS_OK:
            case STATUS_CREATED:
            case STATUS_NOT_ACCEPTABLE:
                callbacks.onTaskCompleted(response);
            break;

            case STATUS_NOT_FOUND:
            case STATUS_UNKNOWN:
            case STATUS_ERROR:
            default:
                callbacks.onTaskError(response);
            break;
        }
    }
}