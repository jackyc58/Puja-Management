package com.example.jackyc58.pujamanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
 * Created by jackyc58 on 2016/6/1.
 */
public class Puja_sign_up_add_item extends AppCompatActivity {
    Spinner spn_signUpItem;
    Button btn_singUpItemAdd, btn_signUpItemFind, btn_singUpItemDel;
    TextView tv_signUpItemJoiner, tv_signUpItemTitle;
    EditText edt_signUpItemJoiner, edt_signUpItemDier, edt_signUpItemMoney, edt_signUpItemNotes;
    LinearLayout ll_signUpItemDier, ll_signUpItemMoney;

    Intent intent;
    String pname, date, uid, gid, username, act, sid;

    String[] userItems;


    RequestQueue queue;//隊列改宣告在這，因為整個頁面只需要一個隊列，但請在頁面建立後再實體化
    final String ip = "http://192.168.10.10/";
    final String apiAddSignUpItem = "puja/addSignUpItem";
    final String apiModSignUpItem = "puja/modifySignUpItem";
    final String apiGetSignUpItem = "puja/getSignUpItem";
    final String apiDelSignUpItem = "puja/deleteSignUpItem";
    final String apiGetHouseholdUsers = "puja/getHouseholdUsers";

    protected static final int MENU_FIRST = Menu.FIRST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puja_sign_up_add_item);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        // init
        getLayoutObj();
        initSpinner();
        initButton();


        // get Internt parameter
        intent = getIntent();
        act = intent.getStringExtra("ACT");


        pname = intent.getStringExtra("PNAME");
        date = intent.getStringExtra("DATE");
        uid = intent.getStringExtra("UID");
        gid = intent.getStringExtra("GID");
        username = intent.getStringExtra("USERNAME");


        // modify member, act = MOD
        if (act.equals("MOD")) {
            sid = intent.getStringExtra("SID");
            tv_signUpItemTitle.setText(getString(R.string.modifySignUpItem));
            btn_singUpItemDel.setVisibility(View.VISIBLE);
            getSignUpItemFields();
        } else {
            btn_singUpItemDel.setVisibility(View.INVISIBLE);
        }

        // get User lists by gid
        getHouseholdUsers();


    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            new android.app.AlertDialog.Builder(this).setTitle("")
//                    .setMessage("離開法會報名?")
//                    .setPositiveButton("確定", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialoginterface, int i) {
//                            finish();
//                        }
//                    })
//                    .setNegativeButton("取消", null)
//                    .show();
//        }
        return super.onKeyDown(keyCode, event);
    }

    private void getLayoutObj() {
        spn_signUpItem = (Spinner) findViewById(R.id.spn_signUpItem);

        btn_singUpItemAdd = (Button) findViewById(R.id.btn_singUpItemAdd);
        btn_signUpItemFind = (Button) findViewById(R.id.btn_signUpItemFind);
        btn_singUpItemDel = (Button) findViewById(R.id.btn_singUpItemDel);

        tv_signUpItemJoiner = (TextView) findViewById(R.id.tv_signUpItemJoiner);
        tv_signUpItemTitle = (TextView) findViewById(R.id.tv_signUpItemTitle);

        edt_signUpItemJoiner = (EditText) findViewById(R.id.edt_signUpItemJoiner);
        edt_signUpItemDier = (EditText) findViewById(R.id.edt_signUpItemDier);
        edt_signUpItemMoney = (EditText) findViewById(R.id.edt_signUpItemMoney);
        edt_signUpItemNotes = (EditText) findViewById(R.id.edt_signUpItemNotes);

        ll_signUpItemDier = (LinearLayout) findViewById(R.id.ll_signUpItemDier);
        ll_signUpItemMoney = (LinearLayout) findViewById(R.id.ll_signUpItemMoney);
    }

    private void initButton() {

        btn_singUpItemAdd.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add sign up item (pname, signup_date, signup_uid, signup_username, gid, item_id, item_name, item_liver, item_dier, itme_money, notes)
                Map<String, String> params = new HashMap<String, String>();

                params.put("pname", pname);
                params.put("signup_date", date);
                params.put("signup_uid", uid);
                params.put("signup_username", username);
                params.put("gid", gid);
                params.put("item_id", Integer.toString(spn_signUpItem.getSelectedItemPosition()));
                params.put("item_name", spn_signUpItem.getSelectedItem().toString());
                params.put("item_liver", edt_signUpItemJoiner.getText().toString());
                params.put("item_dier", edt_signUpItemDier.getText().toString());
                params.put("item_money", edt_signUpItemMoney.getText().toString());
                params.put("notes", edt_signUpItemNotes.getText().toString());

                if (act.equals("MOD"))
                    params.put("sid", sid);

                // send to server
                http_update_signUpItem(params);


            }
        });

        btn_signUpItemFind.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(Puja_sign_up_add_item.this, v);
                for (int i=0; i<userItems.length; i++)
                    popupMenu.getMenu().add(0, MENU_FIRST, 0, userItems[i]);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (edt_signUpItemJoiner.length() != 0)
                            edt_signUpItemJoiner.append(",");
                        edt_signUpItemJoiner.append(item.getTitle());

                        return true;
                    }
                });

                popupMenu.show();
            }
        });

        btn_singUpItemDel.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> params = new HashMap<String, String>();
                params.put("sid", sid);

                // send to server
                act = "DEL";
                http_update_signUpItem(params);
            }
        });
    }



    private void initSpinner() {


        //設定字串陣列-adapter
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this, R.array.signUpItems, android.R.layout.simple_spinner_item);
        //自訂下拉選單-Spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_signUpItem.setAdapter(adapter);
        spn_signUpItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spn_signUpItem.getSelectedItemPosition() >=0 && spn_signUpItem.getSelectedItemPosition() <= 2 ) {
                    ll_signUpItemMoney.setVisibility(View.GONE);
                    ll_signUpItemDier.setVisibility(View.GONE);
                    tv_signUpItemJoiner.setText("參加者");
                } else if (spn_signUpItem.getSelectedItemPosition() >= 3 && spn_signUpItem.getSelectedItemPosition() <= 6 ) {
                    ll_signUpItemMoney.setVisibility(View.GONE);
                    ll_signUpItemDier.setVisibility(View.VISIBLE);
                    tv_signUpItemJoiner.setText("陽上");
                } else if (spn_signUpItem.getSelectedItemPosition() >= 7 ) {
                    ll_signUpItemMoney.setVisibility(View.VISIBLE);
                    ll_signUpItemDier.setVisibility(View.GONE);
                    tv_signUpItemJoiner.setText("參加者");
                }


//                Toast.makeText(parent.getContext(), "你所選的是-" + parent.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }



    private void http_update_signUpItem(final Map<String, String> params) {
        String url;
        if (act.equals("ADD"))
            url = ip + apiAddSignUpItem;                  //改用 post 的方式登入，重要資料就不會顯示在網址上了，比較安全
        else if (act.equals("MOD"))
            url = ip + apiModSignUpItem;
        else
            url = ip + apiDelSignUpItem;

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
                            JSONObject jsonRootObject = new JSONObject(response);

//                            int allCount = jsonRootObject.getInt("allCount");
//                            if (allCount > 0) {
//                                Intent intent = new Intent();
//                                intent.setClass(Puja_Add.this, Puja_list.class);
//                                startActivity(intent);
//                            }
//
//                            Puja_Add.this.finish();

                            setResult(RESULT_OK, intent);
                            finish();
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
//                params.put("gid", gid);
                return params;
            }
        };

        //3) 把要執行的 StringRequest 加到隊列中執行
        queue.add(stringRequest);
    }

    public RequestQueue getRequestQueue() {//檢查隊列是否已經初始化，若沒有就初始化
        if (queue == null) {
            queue = Volley.newRequestQueue(getApplicationContext());
        }
        return queue;
    }


    private void getSignUpItemFields() {
//        Log.d("puja", "apiName=" + apiGetMember);

        String url = ip + apiGetSignUpItem;                  //改用 post 的方式登入，重要資料就不會顯示在網址上了，比較安全

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
                            JSONArray jsonarray = new JSONArray(response);
                            JSONObject jsonRootObject = jsonarray.getJSONObject(0);
//                            JSONObject jsonRootObject = new JSONObject(response);

                            // 'item_id', 'item_liver', 'item_dier', 'item_money', 'notes'
                            spn_signUpItem.setSelection(jsonRootObject.getInt("item_id"));

                            if (jsonRootObject.isNull("item_liver"))
                                edt_signUpItemJoiner.setText("");
                            else
                                edt_signUpItemJoiner.setText(jsonRootObject.getString("item_liver").toString());

                            if (jsonRootObject.isNull("item_dier"))
                                edt_signUpItemDier.setText("");
                            else
                                edt_signUpItemDier.setText(jsonRootObject.getString("item_dier").toString());

                            if (jsonRootObject.isNull("item_money"))
                                edt_signUpItemMoney.setText("");
                            else
                                edt_signUpItemMoney.setText(jsonRootObject.getString("item_money").toString());

                            edt_signUpItemNotes.setText(jsonRootObject.getString("notes").toString());

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
                params.put("sid", sid);
                return params;
            }
        };

        //3) 把要執行的 StringRequest 加到隊列中執行
        queue.add(stringRequest);
    }


    private void getHouseholdUsers() {
        String url = ip + apiGetHouseholdUsers;                  //改用 post 的方式登入，重要資料就不會顯示在網址上了，比較安全
        final HashMap<String, String> head_map = new HashMap<String, String>();

        Log.d("puja", "apiName=" + apiGetHouseholdUsers);

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
                                userItems = new String[jsonarray.length()];

                                List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
                                for (int i = 0; i < jsonarray.length(); i++) {
                                    JSONObject obj = jsonarray.getJSONObject(i);

                                    userItems[i] = obj.getString("username");
                                }
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

}
