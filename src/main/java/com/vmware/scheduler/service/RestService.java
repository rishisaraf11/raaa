package com.vmware.scheduler.service;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.vmware.scheduler.domain.Task;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
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
    private static String dcphost = "http://10.112.74.239:8000";
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
//            HttpClient client = new DefaultHttpClient(getConnManager());
            HttpClient client = new DefaultHttpClient();
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
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "failed";
    }

    public  static class ApplicationStatusResponse{
        public int getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }

        int statusCode;

    }

    static String handleGet(String url) {

        @Deprecated
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);

        // add request header
        request.addHeader("Accept", "application/json");

        StringBuffer result = null;
        HttpResponse response = null;
        try {
            response = client.execute(request);


        System.out.println("Response Code : " +
                response.getStatusLine().getStatusCode());

            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            Gson gson = new Gson();
            ApplicationStatusResponse applicationResponse = gson.fromJson(rd, ApplicationStatusResponse.class);

            if(applicationResponse != null){
                if(applicationResponse.getStatusCode() == -1){
                    return "failed";
                }
            }
            result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "failed";
    }

    public String execute(Map<String, Object> payload) {
        System.out.println("Rest Service Executed.");
        switch (payload.get("method").toString()) {
            case "GET":
                String statusUrl = dcphost+"/dcp/status";
//                String statusUrl = "http://localhost:8000"+"/dcp/status";
                return handleGet(statusUrl);
            case "POST":
                String dcpServiceUrl = dcphost+"/dcp/execute/";
                Map<String,String> contentHeader = new HashMap<>();
                contentHeader.put("key","Content-Type");
                contentHeader.put("value","application/json");
                List<Map<String,String>> headers = new ArrayList<Map<String,String>>(){{add(contentHeader);}};
                Map<String, Object> dcpInfo = new HashMap<String, Object>(){{
                    put("url",payload.get("url").toString());
                    put("method",payload.get("method").toString());
                    put("body",payload.get("payload").toString());
                    put("username","fritz@coke.com");
                    put("password","password");
                }};

                ObjectMapper mapper = new ObjectMapper();
                String dcpPayload = null;
                try {
                    dcpPayload = mapper.writeValueAsString(dcpInfo);
                    return handlePost(dcpServiceUrl,headers,new ArrayList<Map<String,String>>(),dcpPayload);
                } catch (Exception e){
                    e.printStackTrace();
                }
        }
        return "Error";
    }
}
