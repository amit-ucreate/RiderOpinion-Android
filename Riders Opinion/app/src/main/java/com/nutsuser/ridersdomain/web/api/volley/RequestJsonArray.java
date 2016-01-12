package com.nutsuser.ridersdomain.web.api.volley;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by karan@ucreate.co.in to get JSON response
 * from web services on 12/4/2015.
 */
public class RequestJsonArray extends Request<JSONArray> {


    private Response.Listener<JSONArray> jsonArrayListener;
    private Map<String, String> params;

    public RequestJsonArray(int method, String url, Map<String, String> params,
                            Response.ErrorListener errorListener, Response.Listener<JSONArray> reponseListener) {
        super(method, url, errorListener);
        this.jsonArrayListener = reponseListener;
        this.params = params;
    }

    protected Map<String, String> getParams()
            throws com.android.volley.AuthFailureError {
        return params;
    }
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json; charset=utf-8");
        return headers;
    }
    @Override
    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONArray(jsonString),
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
    protected void deliverResponse(JSONArray response) {
        jsonArrayListener.onResponse(response);
    }
}

