package com.vmware.scheduler.service;

import com.vmware.scheduler.domain.Task;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.BasicClientConnectionManager;
import org.springframework.stereotype.Component;

/**
 * Created by djoshi on 10/22/2015.
 */
@Component
public class RestService {
    private static ClientConnectionManager connectionManager;
    enum RequestMethod {
        GET, POST;
    }

    class RestPayload {
        String url;
        Map<String, String> headers;
        String body;
        RequestMethod method;
    }

    private static ClientConnectionManager getConnManager() throws Exception {
        if (connectionManager == null) {
            SSLSocketFactory sslSocketFactory = new SSLSocketFactory(new TrustStrategy() {
                public boolean isTrusted(
                      final X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            });
            Scheme httpsScheme = new Scheme("https", 443, sslSocketFactory);
            SchemeRegistry schemeRegistry = new SchemeRegistry();
            schemeRegistry.register(httpsScheme);

            connectionManager = new BasicClientConnectionManager(schemeRegistry);
        }
        return connectionManager;
    }

    static String handlePost(String url, List<Map<String,String>> headers, List<Map<String,String>> params,String payload) {
        /*List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("sn", "C02G8416DRJM"));
        urlParameters.add(new BasicNameValuePair("cn", ""));
        urlParameters.add(new BasicNameValuePair("num", "12345"));*/

        //post.setEntity(new UrlEncodedFormEntity(urlParameters));

        HttpResponse response = null;
        StringBuffer result = null;
        try {
            @Deprecated
            HttpClient client = new DefaultHttpClient(getConnManager());
            HttpPost post = new HttpPost(url);

            for(Map<String,String> header : headers){
                post.addHeader(header.get("key"),header.get("value"));
            }
            post.setEntity(new StringEntity(payload));
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
        } catch (Exception e) {
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

    public String execute(Map<String, Object> payload) {
        //Gson g = new Gson();
        //RestPayload restPayload = g.fromJson(payload.toString(), RestPayload.class);
        System.out.println("Rest Service Executed.");
        switch (payload.get("method").toString()) {
            case "GET":
                return handleGet();
            case "POST":
                return handlePost(payload.get("url").toString(),(List<Map<String,String>>)payload.get("headers"),(List<Map<String,String>>)payload.get("params"),payload.get("payload").toString());
        }
        return "Error";
    }
}
