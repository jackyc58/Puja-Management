package com.example.jackyc58.pujamanagement;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jackyc58 on 2016/5/4.
 */
public class Puja_sign_up extends AppCompatActivity {
    Button btn_signUpSearch, btn_signUpAddItem;
    Spinner spn_signUpPujaList;
    TextView tv_signUpTodyDate, tv_signUpUid, tv_signUpGid;
    EditText edt_signUpParticipants;
    ListView lv_signUpItems;

    RequestQueue queue;//隊列改宣告在這，因為整個頁面只需要一個隊列，但請在頁面建立後再實體化
    final String ip = "http://192.168.10.10/";
    final String apiGetPujaList = "puja/getPujaListSimplify";
    final String apiGetSignUpItemList = "puja/getSignUpItemLists";

    Intent intent;
    private static final int SEARCH_PAGE = 1;
    private static final int ADD_ITEM_PAGE = 2;
    private static final int PUJA_ADD_PAGE = 3;


    String pujaPidArray[];  // get pid array, you can use spinner position to get selected pid.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puja_sign_up);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        // init
        getLayoutObj();
        edt_signUpParticipants.setFocusable(false);
        tv_signUpUid.setText("");
        tv_signUpGid.setText("");
        initSpinner();
        initButton();

        // set today date
        Date dt = new Date();   // pick current system date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");   // set format for date
        tv_signUpTodyDate.setText(dateFormat.format(dt));
//        String check = dateFormat.format(dt);

        // Sign up list
        getSignUpItemList();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        getMenuInflater().inflate(R.menu.puja_option_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();

        switch (item.getItemId()) {
            case R.id.option_addPuja:
                intent.setClass(Puja_sign_up.this, Puja_Add.class);
                intent.putExtra("ACT", "ADD");
                startActivityForResult(intent, PUJA_ADD_PAGE);
//                startActivity(intent);
                //Toast.makeText(MainActivity.this,item.getTitle(), Toast.LENGTH_SHORT).show();
                break;
//            case R.id.option_pujaList:
//                intent.setClass(Puja_sign_up.this, Puja_list.class);
//                startActivity(intent);
//                //Toast.makeText(MainActivity.this,item.getTitle(), Toast.LENGTH_SHORT).show();
//                break;

        }

        // TODO Auto-generated method stub
//        return super.onOptionsItemSelected(item);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(this).setTitle("")
                    .setMessage(getString(R.string.exit) + getString(R.string.pujaSignUp) + "?")
                    .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            Puja_sign_up.this.finish();
                        }
                    })
                    .setNegativeButton(getString(R.string.cancel), null)
                    .show();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void getLayoutObj() {
        btn_signUpSearch = (Button) findViewById(R.id.btn_signUpSearch);
        btn_signUpAddItem = (Button) findViewById(R.id.btn_signUpAddItem);
        spn_signUpPujaList = (Spinner) findViewById(R.id.spn_signUpPujaList);
        tv_signUpTodyDate = (TextView) findViewById(R.id.tv_signUpTodyDate);
        tv_signUpUid = (TextView) findViewById(R.id.tv_signUpUid);
        tv_signUpGid = (TextView) findViewById(R.id.tv_signUpGid);
        edt_signUpParticipants = (EditText) findViewById(R.id.edt_signUpParticipants);
        lv_signUpItems = (ListView) findViewById(R.id.lv_signUpItems);
    }


    private void initSpinner() {
        // get Puja list(pid, pname, begin_date)
        getPujaList();

        // spinner
        // spinner need use (key(pid), val(string))
//        List<String> key = new ArrayList<String>();
//        key.add("1");
//        key.add("2");
//        key.add("3");
//
//        String[] earthlyBranch = new String[key.size()];
//        key.toArray(earthlyBranch);
//
////        String[] earthlyBranch = getResources().getStringArray(R.array.earthlyBranch);
//        ArrayAdapter adapter = new ArrayAdapter<String>(Puja_sign_up.this,android.R.layout.simple_spinner_item, earthlyBranch);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spn_signUpPujaList.setAdapter(adapter);
//        spn_signUpPujaList.setOnItemSelectedListener(listen);

    }


//    AdapterView.OnItemSelectedListener listen = new AdapterView.OnItemSelectedListener(){
//
//        @Override
//        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//            // TODO Auto-generated method stub
//            Toast.makeText(Puja_sign_up.this, position+"" , Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onNothingSelected(AdapterView<?> parent) {
//            // TODO Auto-generated method stub
//
//        }
//    };

    private void initButton() {
        // add sign up item Button
        btn_signUpAddItem.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_signUpParticipants.length() == 0) {
                    alertMsg(getString(R.string.input) + getString(R.string.signUpJoiner) + "!");
                    return;
                }

                intent = new Intent(Puja_sign_up.this, Puja_sign_up_add_item.class);
                intent.putExtra("ACT", "ADD");
                intent.putExtra("PNAME", Integer.toString(spn_signUpPujaList.getSelectedItemPosition()));
                intent.putExtra("DATE", tv_signUpTodyDate.getText().toString());
                intent.putExtra("UID", tv_signUpUid.getText().toString());
                intent.putExtra("USERNAME", edt_signUpParticipants.getText().toString());
                intent.putExtra("GID", tv_signUpGid.getText().toString());
                startActivityForResult(intent, ADD_ITEM_PAGE);

            }
        });

        btn_signUpSearch.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Puja_sign_up.this, Search.class);
                intent.putExtra("ACT", "SIGNUP");
                startActivityForResult(intent, SEARCH_PAGE);
            }
        });
    }


    private void getPujaList() {
        String url = ip + apiGetPujaList;                  //改用 post 的方式登入，重要資料就不會顯示在網址上了，比較安全
//        final HashMap<String, String> head_map = new HashMap<String, String>();

        Log.d("puja", "apiName=" + apiGetPujaList);

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
                            String year;
                            String spn_pujaArray[];
                            List<String> pidArray = new ArrayList<String>();
                            List<String> pujaListArray = new ArrayList<String>();

                            // ListView show
                            JSONArray jsonarray = new JSONArray(response);

//                            if (jsonarray.length() > 0) {
                            Resources res =getResources();
                            String[] pujaNameItems = res.getStringArray(R.array.puja_array);

                            List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject obj = jsonarray.getJSONObject(i);

                                // get year by begin_date
                                if (obj.isNull("begin_date"))
                                    year = "";
                                else
                                    year = obj.getString("begin_date").substring(0, 4);

                                pujaListArray.add(year + " " + pujaNameItems[Integer.parseInt(obj.getString("pname"))]);
                                pidArray.add(obj.getString("pid"));
                            }

                            spn_pujaArray = new String[pujaListArray.size()];
                            pujaListArray.toArray(spn_pujaArray);

                            pujaPidArray = new String[pidArray.size()];
                            pidArray.toArray(pujaPidArray);

                            ArrayAdapter adapter = new ArrayAdapter<String>(Puja_sign_up.this,android.R.layout.simple_spinner_item, spn_pujaArray);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spn_signUpPujaList.setAdapter(adapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("puja", "Puja_sign_up.java get response fail!!");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        if (requestCode == SEARCH_PAGE) {
            TextView participants = (TextView) findViewById(R.id.edt_signUpParticipants);
            TextView uid = (TextView) findViewById(R.id.tv_signUpUid);
            TextView gid = (TextView) findViewById(R.id.tv_signUpGid);

            participants.setText(data.getCharSequenceExtra("USERNAME"));
            uid.setText(data.getCharSequenceExtra("UID"));
            gid.setText(data.getCharSequenceExtra("GID"));

            getSignUpItemList();

        } else if (requestCode == ADD_ITEM_PAGE) {
            getSignUpItemList();
        } else if (requestCode == PUJA_ADD_PAGE) {
            getPujaList();
        }


    }



    private void getSignUpItemList() {
        if (tv_signUpUid.getText().toString().isEmpty())
            return;

        if (spn_signUpPujaList.getCount() == 0) {
            alertMsg(getString(R.string.signUpMsg1));
            return;
        }



        String url = ip + apiGetSignUpItemList;                  //改用 post 的方式登入，重要資料就不會顯示在網址上了，比較安全
//        final HashMap<String, String> head_map = new HashMap<String, String>();

        Log.d("puja", "apiName=" + apiGetSignUpItemList);

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
//                            Resources res =getResources();
//                            String[] pujaNameItems = res.getStringArray(R.array.puja_array);

                            // get Resources
                            Resources res =getResources();
                            String[] signUpItems = res.getStringArray(R.array.signUpItems);

                            // list
                            List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

                            for (int i = 0; i < jsonarray.length(); i++) {
                                String item_liver, item_dier, item_money;
                                int nItem_id;
                                JSONObject obj = jsonarray.getJSONObject(i);

                                // sid, item_id, item_liver, item_dier, item_money, notes
                                if (obj.isNull("item_id"))
                                    nItem_id = 0;
                                else
                                    nItem_id = obj.getInt("item_id");


                                HashMap<String, String> map = new HashMap<String, String>();

                                map.put("item_name", signUpItems[nItem_id]);

                                if (obj.isNull("item_liver"))
                                    item_liver = "";
                                else
                                    item_liver = obj.getString("item_liver");

                                if (obj.isNull("item_dier"))
                                    item_dier = "";
                                else
                                    item_dier = obj.getString("item_dier");

                                if (obj.isNull("item_money"))
                                    item_money = "";
                                else
                                    item_money = obj.getString("item_money");

                                if (nItem_id >= 0 && nItem_id <= 2) {
                                    map.put("item_desc", getString(R.string.signUpJoiner) + ":" + item_liver);
                                } else if (nItem_id >= 3 && nItem_id <= 6) {
                                    map.put("item_desc", getString(R.string.signUpLiver) + ":" + item_liver + ";  " + getString(R.string.signUpDier) + ":" + item_dier);
                                } else if (nItem_id >= 7) {
                                    map.put("item_desc", getString(R.string.signUpJoiner) + ":" + item_liver + ";  " + getString(R.string.signUpMoney) + ":" + item_money + getString(R.string.signUpMoneyDollar));
                                }

                                if (obj.isNull("notes"))
                                    map.put("notes", "");
                                else
                                    map.put("notes", obj.getString("notes"));

                                map.put("sid", obj.getString("sid"));

                                list.add(map);
                            }

                            ListAdapter adapter = new SimpleAdapter(Puja_sign_up.this, list, R.layout.puja_sign_up_list_item, new String[]{"item_name", "item_desc", "notes", "sid"}, new int[]{R.id.tv_pujaListRow1, R.id.tv_pujaListRow2, R.id.tv_pujaListRow3, R.id.tv_pujaListRow4});
                            lv_signUpItems.setAdapter(adapter);
                            lv_signUpItems.setOnItemClickListener(signUpItemList);

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
                    params.put("uid", tv_signUpUid.getText().toString());
                    params.put("pname", Integer.toString(spn_signUpPujaList.getSelectedItemPosition()));

                return params;
            }
        };

        //3) 把要執行的 StringRequest 加到隊列中執行e
        queue.add(stringRequest);
    }


    AdapterView.OnItemClickListener signUpItemList = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub
            TextView tv_sid = (TextView) view.findViewById(R.id.tv_pujaListRow4);

            intent = new Intent(Puja_sign_up.this, Puja_sign_up_add_item.class);
            intent.putExtra("ACT", "MOD");
            intent.putExtra("SID", tv_sid.getText().toString());
            intent.putExtra("PNAME", Integer.toString(spn_signUpPujaList.getSelectedItemPosition()));
            intent.putExtra("DATE", tv_signUpTodyDate.getText().toString());
            intent.putExtra("UID", tv_signUpUid.getText().toString());
            intent.putExtra("USERNAME", edt_signUpParticipants.getText().toString());
            intent.putExtra("GID", tv_signUpGid.getText().toString());
            startActivityForResult(intent, ADD_ITEM_PAGE);
        }
    };


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
}
