package com.unosquare.finalapp;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class NetServices extends AsyncTask<Integer, Void, String>
{
    public static final int WS_CALL_GET = 0;
    public static final int WS_CALL_POST = 1;
	private final String URL_WS_GET = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22greenland%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
	private final String URL_WS_POST = "https://api.parse.com/1/installations";
    private final String APP_ID = "j2aLnaENSizTFXE725Lww3MTtqiMfEQhVO5HTmSD";
    private final String REST_API_KEY = "jmKBd9wASHKDiBFLaQvExQY1xPh50OPONajo1VZN";
    private final int STATUS_OK = 200;
    private final int STATUS_CREATED = 201;
    private final int STATUS_ERROR = 400;
    private final int STATUS_NOT_FOUND = 404;
    private final int STATUS_UNKNOWN = 300;
    private final String ERROR = "error";
    private OnBackgroundTaskCallback callbacks;
    private FunctionProgressBar fprogressbar;
    private int status;
    
    public NetServices(OnBackgroundTaskCallback callbacks){
    	this.callbacks = callbacks;
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
                    case WS_CALL_GET:
                        response = connectionGET();
                        break;

                    case WS_CALL_POST:
                        response = connectionPOST();
                        break;
                }
    		}catch(Exception e){
    			e.printStackTrace();
    		}

    	return response;
    }

    private String connectionGET(){
		HttpGet httpGet = new HttpGet(URL_WS_GET);

		httpGet.setHeader("Accept", "application/json");
		httpGet.setHeader("Content-type", "application/json");
		
		HttpParams httpParameters = new BasicHttpParams();
		int timeoutConnection = 10000; //Timeout until a connection is established.
		int timeoutSocket = 10000; //Timeout for waiting for data.

		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

        HttpClient httpClient = new DefaultHttpClient(httpParameters);
		
		try{
            HttpResponse response = httpClient.execute(httpGet);
			status = response.getStatusLine().getStatusCode();

            switch(status){
                case STATUS_OK:
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

    private String connectionPOST() {
        HttpPost httpPost = new HttpPost(URL_WS_POST);

        httpPost.setHeader("X-Parse-Application-Id", APP_ID);
        httpPost.setHeader("X-Parse-REST-API-Key", REST_API_KEY);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("deviceType", "android"));
        nameValuePairs.add(new BasicNameValuePair("deviceToken", "anyDeviceToken"));
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        HttpParams httpParameters = new BasicHttpParams();
        int timeoutConnection = 10000; //Timeout until a connection is established.
        int timeoutSocket = 10000; //Timeout for waiting for data.

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