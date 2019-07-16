package com.login.hofo;

public class UrlApi {

    private static final String urlServer = "https://6b9fe003.ngrok.io/";

    private static final String urlApi = urlServer+"api/";

    public static String getUrlApi() {
        return urlApi;
    }

    public static String getUrlServer() {
        return urlServer;
    }
}
