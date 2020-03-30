package com.openinstitute.nuru.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.openinstitute.nuru.PostActivity;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;

public class WebAsyncPost extends AsyncTask<String, Void, String> {

    private PostActivity caller_transfer;

    OnAppAsyncPostComplete caller;
    //private Activity form_sender;
    private Context form_sender;
    String method = "POST";
    List<NameValuePair> parameters = null;
    ProgressDialog pDialog = null;

    String account_key = null;
    String category_id = null;
    String report_data = null;
    String report_session = null;
    String post_url = null;

    public WebAsyncPost(Context a, String _account_key, String _category_id, String _report_data, String _post_url, String m) {


        //caller = (OnAppAsyncPostComplete) a;
        form_sender = a;
        //this.context = a;
        method = m;

        account_key = _account_key;
        category_id = _category_id;
        report_data = _report_data;
        //report_session = _report_session;
        post_url = _post_url;

        if(category_id.equals("transfer")){
            caller_transfer = (PostActivity) a;
        }

    }

    // Interface to be implemented by calling activity
    public interface OnAppAsyncPostComplete {
        void asyncResponse(String response);
    }

    @Override
    protected String doInBackground(String... params) {



        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(post_url);

        // BasicNameValuePair ==> mechanism for Keys+values to pass over the URL
        BasicNameValuePair account_key_BNVP = new BasicNameValuePair("param_account_key", account_key);
        BasicNameValuePair category_id_BNVP = new BasicNameValuePair("param_category_id", category_id);
        BasicNameValuePair report_data_BNVP = new BasicNameValuePair("param_report_data", report_data);

        // We add the content that we want to pass with the POST request to as name-value pairs
        //Now we put those sending details to an ArrayList with type safe of NameValuePair
        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        nameValuePairList.add(account_key_BNVP);
        nameValuePairList.add(category_id_BNVP);
        nameValuePairList.add(report_data_BNVP);

        /*Log.d("nameValuePairList", nameValuePairList.toString());*/

        try {
            // UrlEncodedFormEntity is an entity composed of a list of url-encoded pairs.
            //This is typically useful while sending an HTTP POST request.
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);

            // setEntity() hands the entity (here it is urlEncodedFormEntity) to the request.
            httpPost.setEntity(urlEncodedFormEntity);

            try {
                // HttpResponse is an interface just like HttpPost.
                //Therefore we can't initialize them
                HttpResponse httpResponse = httpClient.execute(httpPost);

                // According to the JAVA API, InputStream constructor do nothing.
                //So we can't initialize InputStream although it is not an interface
                InputStream inputStream = httpResponse.getEntity().getContent();

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder stringBuilder = new StringBuilder();

                String bufferedStrChunk = null;

                while((bufferedStrChunk = bufferedReader.readLine()) != null){
                    stringBuilder.append(bufferedStrChunk);
                }

                return stringBuilder.toString();

            } catch (ClientProtocolException cpe) {
                System.out.println("First Exception caz of HttpResponse :" + cpe);
                cpe.printStackTrace();
            } catch (IOException ioe) {
                System.out.println("Second Exception caz of HttpResponse :" + ioe);
                ioe.printStackTrace();
            }

        } catch (UnsupportedEncodingException uee) {
            System.out.println("An Exception given because of UrlEncodedFormEntity argument :" + uee);
            uee.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            //return false;
        }

        //caller.asyncResponse(result);

        if(category_id.equals("transfer")){
            caller_transfer.asyncResponse(result);
        }

    }
}
/*

AppAsyncPost sendPostReqAsyncTask = new AppAsyncPost();
sendPostReqAsyncTask.execute(account_key, category_id, report_data);
*/
