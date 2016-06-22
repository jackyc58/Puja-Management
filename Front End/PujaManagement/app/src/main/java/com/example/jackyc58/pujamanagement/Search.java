package com.example.jackyc58.pujamanagement;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
public class Search extends AppCompatActivity {
    EditText edt_searchName;
    Button btn_search;
    ListView lv_search;

    Intent intent;
    CharSequence act;

    RequestQueue queue;//隊列改宣告在這，因為整個頁面只需要一個隊列，但請在頁面建立後再實體化
    String ip = "http://192.168.10.10/";
    String apiSearch = "puja/search";
    String apiAddMember = "puja/getNewGid";


    private static final int MEMBER_ADD_PAGE = 1;
    private static final int HOUSEHOLD_PAGE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        intent = getIntent();
        act = intent.getCharSequenceExtra("ACT");

        getLayoutObj();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        getMenuInflater().inflate(R.menu.main, menu);
//        if (!act.equals("SIGNUP")) {
//            MenuItem item = menu.getItem(0);
//            item.setTitle(getString(R.string.householdData));
//        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        getNewGid();
//        if (act.equals("SIGNUP")) {
//            intent.putExtra("ACT", "SIGNUP");
//            intent.setClass(Search.this, Member_Add.class);
//            startActivityForResult(intent, MEMBER_ADD_PAGE);
//        } else {
//            intent.putExtra("ACT", "ADD");
//            intent.setClass(Search.this, Member_Add.class);
//            startActivity(intent);
//            finish();
//        }
        // TODO Auto-generated method stub
//        return super.onOptionsItemSelected(item);
        return true;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            Intent intent = new Intent();
//            intent.putExtra("GID", gid);
////            intent.setClass(Member_Add.this, Household.class);
////            startActivity(intent);
            setResult(RESULT_OK, intent);
            finish();
        }

        return super.onKeyDown(keyCode, event);
    }

    private void getLayoutObj() {
        edt_searchName = (EditText) findViewById(R.id.edt_searchName);
        btn_search = (Button) findViewById(R.id.btn_search);
        lv_search = (ListView) findViewById(R.id.lv_search);
    }

    public void on_search_click(View view) {
        //　check search user name not empty
        if (edt_searchName.getText().toString().isEmpty())
            alertMsg(getString(R.string.inputSearchName));
        else
            getSearchList();
    }

    private void alertMsg(String msg)
    {
        new AlertDialog.Builder(this).setTitle(getString(R.string.wrong))
                .setMessage(msg)
                .setPositiveButton(getString(R.string.ok), null)
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialoginterface, int i)
//                    {
//                        finish();
//                    }
//                })
//                .setNegativeButton("but2", null)
                .show();
    }


    private void getSearchList() {
        String url = ip + apiSearch;                  //改用 post 的方式登入，重要資料就不會顯示在網址上了，比較安全

        Log.d("puja", "apiName=" + apiSearch);

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

                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("gid", obj.getString("gid"));
                                    map.put("uid", obj.getString("uid"));
                                    map.put("username", obj.getString("username"));
                                    map.put("head_username", obj.getString("head_username"));
                                    map.put("address", obj.getString("address"));

                                    list.add(map);
                                }

                                ListAdapter adapter = new SimpleAdapter(Search.this, list, R.layout.search_list_item, new String[]{"username", "head_username", "address", "gid", "uid"}, new int[]{R.id.tv_searchListCol1, R.id.tv_searchListCol2, R.id.tv_searchListCol3, R.id.tv_searchListCol4, R.id.tv_searchListCol5});
                                lv_search.setAdapter(adapter);
                                lv_search.setOnItemClickListener(searchList);
                            } else {
                                alertMsg("'" + edt_searchName.getText().toString().trim() + "' " + getString(R.string.noName) + "!");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("puja", "Search.java get response fail!!");
                        }
                    }
                },
                new Response.ErrorListener() {      //處理錯誤回應用的匿名函式。請注意：若找不到 ip 會 return TimeOut。
                    @Override
                    public void onErrorResponse(VolleyError error) {//改寫處理的函式
                        Log.d("puja", "Error:" + error.toString());//除錯訊息
                    }
                }
        ) {//使用 post 方式時，改寫內建函式的位置要注意，是接在 StringRequest() 後面
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {//請查一下 Map, HashMap 是做什麼用的
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", edt_searchName.getText().toString().trim());
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

    AdapterView.OnItemClickListener searchList = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub
            if (act.equals("SIGNUP")) {
                TextView uid = (TextView) view.findViewById(R.id.tv_searchListCol5);
                TextView gid = (TextView) view.findViewById(R.id.tv_searchListCol4);
                TextView username = (TextView) findViewById(R.id.tv_searchListCol1);
                intent.putExtra("UID", uid.getText().toString().trim());
                intent.putExtra("GID", gid.getText().toString().trim());
                intent.putExtra("USERNAME", username.getText().toString().trim());
                setResult(RESULT_OK, intent);
                finish();
            } else {
                TextView gid = (TextView) view.findViewById(R.id.tv_searchListCol4);
//            Toast.makeText(Household.this, "第" + (position + 1) + "攔" + tv_uid.getText(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("GID", gid.getText().toString().trim());
                intent.putExtra("ACT", "NORMAL");
                intent.setClass(Search.this, Household.class);
                startActivity(intent);
            }

        }
    };

    private void getNewGid() {
        Log.d("puja", "apiName=" + apiAddMember);

        String url = ip + apiAddMember;                  //改用 post 的方式登入，重要資料就不會顯示在網址上了，比較安全

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
                            String gid;
//                            final Intent intent = new Intent();

                            JSONObject jsonRootObject = new JSONObject(response);
                            gid = jsonRootObject.getString("gid").toString();
//                            Log.d("puja", "ret=" + jsonRootObject.getString("ret").toString());
//                            Log.d("puja", "ret=" + response);
                            Log.i("puja", "main page gid=" + gid);
                            intent.putExtra("GID", gid);
                            if (act.equals("SIGNUP")) {
                                intent.putExtra("ACT", "SIGNUP");
                                intent.setClass(Search.this, Member_Add.class);
                                startActivityForResult(intent, MEMBER_ADD_PAGE);
                            } else {
//                                intent.putExtra("ACT", "ADD");
//                                intent.setClass(Search.this, Member_Add.class);
                                intent.putExtra("ACT", "NEW");
                                intent.setClass(Search.this, Household.class);
//                                startActivityForResult(intent, HOUSEHOLD_PAGE);
                                startActivity(intent);
//                                finish();
                            }
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
                Map<String, String> params = new HashMap<String, String>();
//                params.put("gid", gid);
                return params;
            }
        };

        //3) 把要執行的 StringRequest 加到隊列中執行
        queue.add(stringRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        intent.putExtra("UID", data.getCharSequenceExtra("UID"));
        intent.putExtra("USERNAME", data.getCharSequenceExtra("USERNAME"));
        setResult(RESULT_OK, intent);
        finish();

    }
}
