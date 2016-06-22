package com.example.jackyc58.pujamanagement;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jackyc58 on 2016/5/5.
 */
public class Puja_list extends AppCompatActivity {

    ListView lv_pujaList;
    Button btn_pujaAdd;

    RequestQueue queue;//隊列改宣告在這，因為整個頁面只需要一個隊列，但請在頁面建立後再實體化
    final String ip = "http://192.168.10.10/";
    final String apiName = "puja/getPujaList";

    private static final int PUJA_ADD_PAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puja_list);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        // init
        getLayoutObj();
        initButton();

        // get household list by gid
        getPujaList();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(this).setTitle("")
                    .setMessage(getString(R.string.exit) + getString(R.string.pujaList) + "?")
                    .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            Puja_list.this.finish();
                        }
                    })
                    .setNegativeButton(getString(R.string.cancel), null)
                    .show();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void getLayoutObj() {
        lv_pujaList = (ListView) findViewById(R.id.lv_pujaList);
        btn_pujaAdd = (Button) findViewById(R.id.btn_pujaAdd);
    }

    private void initButton() {
        // Save Button
        btn_pujaAdd.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Puja_list.this, Puja_Add.class);
                intent.putExtra("ACT", "ADD");
                startActivityForResult(intent, PUJA_ADD_PAGE);
            }
        });
    }

    private void getPujaList() {
        String url = ip + apiName;                  //改用 post 的方式登入，重要資料就不會顯示在網址上了，比較安全
//        final HashMap<String, String> head_map = new HashMap<String, String>();

        Log.d("puja", "apiName=" + apiName);

        getRequestQueue();                          //呼叫隊列用的函式

        //2) 定義要放到隊列中執行用的 StringRequest
        StringRequest stringRequest = new StringRequest(//需要 4 個參數
                Request.Method.POST,                //改用 post 的方式傳送資料，但需要改寫一個內建的函式 getParams()
                url,                                //執行請求用的網址
                new Response.Listener<String>() {   //處理回應的字串用的匿名函式
                    @Override
                    public void onResponse(String response) {//改寫處理的函式
                        Log.d("puja", "response=" + response);

                        try {
                            // ListView show
                            JSONArray jsonarray = new JSONArray(response);

//                            if (jsonarray.length() > 0) {
                            Resources res =getResources();
                            String[] pujaNameItems = res.getStringArray(R.array.puja_array);

                            List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

                            for (int i = 0; i < jsonarray.length(); i++) {
                                String begin_date, end_date;
                                JSONObject obj = jsonarray.getJSONObject(i);



                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("pname", pujaNameItems[Integer.parseInt(obj.getString("pname"))]);

                                if (obj.isNull("begin_date"))
                                    begin_date = "";
                                else
                                    begin_date = obj.getString("begin_date");
                                if (obj.isNull("end_date"))
                                    end_date = "";
                                else
                                    end_date = obj.getString("end_date");
                                map.put("period", begin_date + " ~ " + end_date);

                                map.put("pid", obj.getString("pid"));

                                list.add(map);
                            }

                            ListAdapter adapter = new SimpleAdapter(Puja_list.this, list, R.layout.puja_list_item, new String[]{"pname", "period", "pid"}, new int[]{R.id.tv_pujaListCol1, R.id.tv_pujaListCol2, R.id.tv_pujaListCol3});
                            lv_pujaList.setAdapter(adapter);
                            lv_pujaList.setOnItemClickListener(pujaList);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("puja", "Household.java get response fail!!");
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
                Map<String, String> params = new HashMap<String, String>();
//                params.put("gid", gid);
                return params;
            }
        };

        //3) 把要執行的 StringRequest 加到隊列中執行e
        queue.add(stringRequest);
    }


    public RequestQueue getRequestQueue(){//檢查隊列是否已經初始化，若沒有就初始化
        if(queue == null){
            queue = Volley.newRequestQueue(getApplicationContext());
//            queue = Volley.newRequestQueue(context);
        }
        return queue;
    }

    AdapterView.OnItemClickListener pujaList = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub
            TextView tv_pid = (TextView) view.findViewById(R.id.tv_pujaListCol3);
//            Toast.makeText(Household.this, "第" + (position + 1) + "攔" + tv_uid.getText(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra("PID", tv_pid.getText().toString());
            intent.putExtra("ACT", "MOD");  // modify
            intent.setClass(Puja_list.this, Puja_Add.class);
            startActivity(intent);
            Puja_list.this.finish();
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PUJA_ADD_PAGE)
            getPujaList();
    }

}
