package com.example.timeaboutproject.httpaccess;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class HttpAccessForVolley {
	private static RequestQueue mQueue;
	public static getJsonStr jsonStr;
	public void setJsonStr(getJsonStr jsonStr) {
		this.jsonStr = jsonStr;
	}
	public interface getJsonStr{
		public void volleyAccessForJson(String strJson);
	}
	public HttpAccessForVolley(RequestQueue mQueue,final String url, JSONObject param) {
		JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
				url, param, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject jsonObject) {
						Log.e("TAG", jsonObject.toString());
						jsonStr.volleyAccessForJson(jsonObject.toString());
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						Log.e("TAG", volleyError.getMessage(), volleyError);
						jsonStr.volleyAccessForJson(volleyError.getMessage());
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put("Accept", "application/json");
				headers.put("Content-Type", "application/json; charset=UTF-8");
				return headers;
			}
		};
		// 超时时间10s,最大重连次数2次
		request.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 2, 1.0f));
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		mQueue.add(request);
	}
}
