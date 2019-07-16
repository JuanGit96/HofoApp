package com.login.hofo;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;

import static com.login.hofo.UrlApi.getUrlServer;

public class ImageProfileRequest extends ImageRequest {


    private static final String USER_REQUEST_URL =  getUrlServer()+"storage/";

    ImageProfileRequest (String imageInServer, Response.Listener<Bitmap> listener, Response.ErrorListener errorListener)
    {
        super(USER_REQUEST_URL+imageInServer, listener, 0, 0,
                ImageView.ScaleType.CENTER,null, errorListener);
    }

}
