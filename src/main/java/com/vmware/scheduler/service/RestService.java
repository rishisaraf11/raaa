package com.vmware.scheduler.service;

import com.vmware.scheduler.domain.Task;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.stereotype.Component;

/**
 * Created by djoshi on 10/22/2015.
 */
@Component
public class RestService {
    enum RequestMethod {
        GET, POST;
    }

    class RestPayload {
        String url;
        Map<String, String> headers;
        String body;
        RequestMethod method;
    }

    static String handlePost() {
        String url = "https://selfsolve.apple.com/wcResults.do";

        @Deprecated
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        // add header
        post.addHeader("Accept", "application/json");
        post.addHeader("Content-Type", "application/json");

        /*List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("sn", "C02G8416DRJM"));
        urlParameters.add(new BasicNameValuePair("cn", ""));
        urlParameters.add(new BasicNameValuePair("locale", ""));
        urlParameters.add(new BasicNameValuePair("caller", ""));
        urlParameters.add(new BasicNameValuePair("num", "12345"));*/

        //post.setEntity(new UrlEncodedFormEntity(urlParameters));

        HttpResponse response = null;
        StringBuffer result = null;
        try {
            response = client.execute(post);

            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + post.getEntity());
            System.out.println("Response Code : " +
                    response.getStatusLine().getStatusCode());

            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    static String handleGet() {
        String url = "http://www.google.com/search?q=developer";

        @Deprecated
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);

        // add request header
        request.addHeader("Accept", "application/json");
        request.addHeader("Content-Type", "application/json");

        StringBuffer result = null;
        HttpResponse response = null;
        try {
            response = client.execute(request);


        /*System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " +
                response.getStatusLine().getStatusCode());*/

            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public String execute(Task task) {
        //Gson g = new Gson();
        //RestPayload restPayload = g.fromJson(payload.toString(), RestPayload.class);
        System.out.println("Rest Service Executed.");
        /*switch (payload.get("method").toLowerCase()) {
            case "get":
                return handleGet();
            case "post":
                return handlePost();
        }*/
        return "Error";
    }
}
