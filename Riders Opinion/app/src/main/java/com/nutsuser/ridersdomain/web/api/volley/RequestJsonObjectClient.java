package com.nutsuser.ridersdomain.web.api.volley;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by admin on 19-02-2016.
 */
public class RequestJsonObjectClient extends Request<JSONObject> {

    JSONObject jsonparams;
    String mString;
    private Response.Listener<JSONObject> jsonObjectListener;
    private Map<String, String> params;


    public RequestJsonObjectClient(int method, String url, Map<String, String> params,
                                   Response.ErrorListener errorListener, Response.Listener<JSONObject> reponseListener) {
        super(method, url, errorListener);
        this.jsonObjectListener = reponseListener;
        this.params = params;
    }

    public RequestJsonObjectClient(String url, JSONObject params, int method,
                                   Response.ErrorListener errorListener, Response.Listener<JSONObject> reponseListener) {
        super(method, url, errorListener);
        this.jsonObjectListener = reponseListener;
        this.jsonparams = params;
    }

    public RequestJsonObjectClient(String url, String params,
                                   Response.ErrorListener errorListener, Response.Listener<JSONObject> reponseListener, int method) {
        super(method, url, errorListener);
        this.jsonObjectListener = reponseListener;
        this.mString = params;
    }


    protected Map<String, String> getParams()
            throws com.android.volley.AuthFailureError {
        return params;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            Log.e("RequestJsonObject ", e.toString());
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            Log.e("RequestJsonObject ", je.toString());
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        jsonObjectListener.onResponse(response);
    }
}

