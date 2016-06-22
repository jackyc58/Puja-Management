package com.example.jackyc58.pujamanagement;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
 * Created by jackyc58 on 2016/5/4.
 */
public class Household extends AppCompatActivity {

    Button btn_householdAdd, btn_householdSignUp, btn_householdStatus, btn_householdPay;
    TextView tv_householdName, tv_householdAddr;
    ListView lv_householdList;

    String gid, act;

    RequestQueue queue;//隊列改宣告在這，因為整個頁面只需要一個隊列，但請在頁面建立後再實體化
    final String ip = "http://192.168.10.10/";
    final String apiName = "puja/getHouseholdList";


    private static final int MEMBER_ADD_PAGE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.household);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        // get param GID
        Intent intent = getIntent();
        gid = intent.getStringExtra("GID");
        act = intent.getStringExtra("ACT");
//        Log.i("puja", "Household gid=" + gid);



        // init
        getLayoutObj();

        // get household list by gid
        btn_householdSignUp.setVisibility(View.GONE);
        btn_householdStatus.setVisibility(View.GONE);
        btn_householdPay.setVisibility(View.GONE);

        if (act.equals("NEW")) {
            intent.putExtra("GID", gid);
            intent.putExtra("ACT", "ADD");
            intent.setClass(Household.this, Member_Add.class);
            startActivityForResult(intent, MEMBER_ADD_PAGE);
        } else
            getHouseholdList();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(this).setTitle("")
                    .setMessage(getString(R.string.exit) + getString(R.string.householdData) +"?")
                    .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            Household.this.finish();
                        }
                    })
                    .setNegativeButton(getString(R.string.cancel), null)
                    .show();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void getLayoutObj() {
        btn_householdAdd = (Button) findViewById(R.id.btn_householdAdd);
        btn_householdSignUp = (Button) findViewById(R.id.btn_householdSignUP);
        btn_householdStatus = (Button) findViewById(R.id.btn_householdStatus);
        btn_householdPay = (Button) findViewById(R.id.btn_householdPay);

        tv_householdName = (TextView) findViewById(R.id.tv_householdName);
        tv_householdAddr = (TextView) findViewById(R.id.tv_householdAddr);

        lv_householdList = (ListView) findViewById(R.id.lv_householdList);
    }

    public void on_addMember_click(View view) {
        Intent intent = new Intent();
        intent.putExtra("GID", gid);
        intent.putExtra("ACT", "ADD");
        intent.setClass(Household.this, Member_Add.class);
        startActivityForResult(intent, MEMBER_ADD_PAGE);
//        startActivity(intent);
//        Household.this.finish();
    }

    private void getHouseholdList() {
        String url = ip + apiName;                  //改用 post 的方式登入，重要資料就不會顯示在網址上了，比較安全
        final HashMap<String, String> head_map = new HashMap<String, String>();

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

                            if (jsonarray.length() > 0) {
                                List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
                                for (int i = 0; i < jsonarray.length(); i++) {
                                    JSONObject obj = jsonarray.getJSONObject(i);


                                    if (!obj.isNull("head") && (obj.getInt("head") == 1 || i == 0)) {
                                        head_map.put("username", obj.getString("username"));
                                        head_map.put("address", obj.getString("address"));
                                        head_map.put("gid", obj.getString("gid"));
                                    }

                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("username", obj.getString("username"));
                                    map.put("uid", obj.getString("uid"));
                                    map.put("tel", obj.getString("tel"));

                                    list.add(map);
                                }

                                ListAdapter adapter = new SimpleAdapter(Household.this, list, R.layout.my_list_item, new String[]{"username", "tel", "uid"}, new int[]{R.id.textView1, R.id.textView2, R.id.textView3});
                                lv_householdList.setAdapter(adapter);
                                lv_householdList.setOnItemClickListener(householdList);

                                // show head info.
                                tv_householdName.setText(head_map.get("username"));
                                tv_householdAddr.setText(head_map.get("address"));
                            } else {
                                Household.this.finish();
//                                Intent intent = new Intent();
//                                intent.putExtra("GID", gid);
//                                intent.putExtra("ACT", "ADD");
//                                intent.setClass(Household.this, Member_Add.class);
//                                startActivityForResult(intent, MEMBER_ADD_PAGE);

                            }

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
                params.put("gid", gid);
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

    AdapterView.OnItemClickListener householdList = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub
            TextView tv_uid = (TextView) view.findViewById(R.id.textView3);
//            Toast.makeText(Household.this, "第" + (position + 1) + "攔" + tv_uid.getText(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra("GID", gid);
            intent.putExtra("UID", tv_uid.getText());
            intent.putExtra("ACT", "MOD");  // modify
            intent.setClass(Household.this, Member_Add.class);
            startActivityForResult(intent, MEMBER_ADD_PAGE);
//            Household.this.finish();
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        getHouseholdList();
    }


}
