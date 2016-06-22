package com.example.jackyc58.pujamanagement;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by jackyc58 on 2016/5/18.
 */
public class PublicLib {
    RequestQueue queue;//隊列改宣告在這，因為整個頁面只需要一個隊列，但請在頁面建立後再實體化
    final String ip = "http://192.168.10.10/";

//    Map<String, String> params;
    Context context;

    public PublicLib() {
    }

    public void send_post(Context context, String apiName, final Map<String, String> params) {
        this.context = context;
//        this.params = params;
        Log.d("puja", "apiName=" + apiName);

        String url = ip + apiName;                  //改用 post 的方式登入，重要資料就不會顯示在網址上了，比較安全

        getRequestQueue();                          //呼叫隊列用的函式

        //2) 定義要放到隊列中執行用的 StringRequest
        StringRequest stringRequest = new StringRequest(//需要 4 個參數
                Request.Method.POST,                //改用 post 的方式傳送資料，但需要改寫一個內建的函式 getParams()
                url,                                //執行請求用的網址
                new Response.Listener<String>() {   //處理回應的字串用的匿名函式
                    @Override
                    public void onResponse(String response) {//改寫處理的函式
//                        tv.setText(response);//因為會用到外部的參數 tv，所以外部的參數 tv 要宣告成 final
                            Log.d("puja", "response=" + response);
                        try {
                            JSONObject jsonRootObject = new JSONObject(response);
//                            API_TOKEN = jsonRootObject.getString("token").toString();
                            Log.d("puja", "ret=" + jsonRootObject.getString("ret").toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {      //處理錯誤回應用的匿名函式。請注意：若找不到 ip 會 return TimeOut。
                    @Override
                    public void onErrorResponse(VolleyError error) {//改寫處理的函式
//                        tv.setText("回傳錯誤");
                        Log.d("puja", "Error:" + error.toString());//除錯訊息
                    }
                }
        ) {//使用 post 方式時，改寫內建函式的位置要注意，是接在 StringRequest() 後面
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {//請查一下 Map, HashMap 是做什麼用的
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("email", "chennanbang@gmail.com");//暫時在這輸入帳號密碼，方便測試
//                params.put("password", "0933596597");

                return params;
            }
        };

        //3) 把要執行的 StringRequest 加到隊列中執行
        queue.add(stringRequest);
    }


    public RequestQueue getRequestQueue(){//檢查隊列是否已經初始化，若沒有就初始化
        if(queue == null){
//            queue = Volley.newRequestQueue(getApplicationContext());
            queue = Volley.newRequestQueue(context);
        }
        return queue;
    }
}
