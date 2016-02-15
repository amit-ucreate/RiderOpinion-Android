package com.nutsuser.ridersdomain.web.api.volley;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by karan@ucreate.co.in on 11/26/2015
 * to POST user information on server.
 */
public class RequestJsonObject extends Request<JSONObject> {

    private Response.Listener<JSONObject> jsonObjectListener;
    private Map<String, String> params;

    public RequestJsonObject(int method, String url, Map<String, String> params,
                             Response.ErrorListener errorListener, Response.Listener<JSONObject> reponseListener) {
        super(method, url, errorListener);
        this.jsonObjectListener = reponseListener;
        this.params = params;
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
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json; charset=utf-8");
        headers.put("token", "ffb1c885-7264-4901-84bc-afc741d7a091");
        return headers;
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        jsonObjectListener.onResponse(response);
    }
}
