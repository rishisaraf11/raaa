package com.vmware.scheduler.service;

import java.util.Map;

/**
 * Created by djoshi on 10/22/2015.
 */
public class Rest {
    enum RequestMethod{
        GET, POST;
    }

    class RestPayload {
        String url;
        Map<String, String> headers;
        String body;
        RequestMethod method;
    }

    public static String execute( Map<String, String> payload) {
        //Gson g = new Gson();
        //RestPayload restPayload = g.fromJson(payload.toString(), RestPayload.class);
        return payload.get("url");
    }
}
