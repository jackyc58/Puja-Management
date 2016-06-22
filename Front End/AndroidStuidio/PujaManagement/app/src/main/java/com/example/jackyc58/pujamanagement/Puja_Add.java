package com.example.jackyc58.pujamanagement;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jackyc58 on 2016/5/4.
 */
public class Puja_Add extends AppCompatActivity {
    Button btn_pujaAdd, btn_pujaDel, btn_startDate, btn_stopDate;
    Spinner spn_name;
    TextView tv_startDate, tv_stopDate, tv_pujaTitle;
    EditText edt_note;

    private DatePickerDialog datePickerDialog1, datePickerDialog2;


    String pid, act;

    RequestQueue queue;//隊列改宣告在這，因為整個頁面只需要一個隊列，但請在頁面建立後再實體化
    final String ip = "http://192.168.10.10/";
    final String apiAddPuja = "puja/addPuja";
    final String apiModPuja = "puja/modifyPujaItem";
    final String apiGetPuja = "puja/getPujaItem";
    final String apiDelPuja = "puja/deletePujaItem";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puja_add);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        getLayoutObj();
        initPujaName();
        initDate();
        initButton();

        tv_pujaTitle.setText(getString(R.string.addPuja));
        btn_pujaDel.setVisibility(View.INVISIBLE);

        // get param GID
        Intent intent = getIntent();
        act = intent.getStringExtra("ACT");



        // modify puja, act = MOD
        if (act.equals("MOD")) {
            pid = intent.getStringExtra("PID");
            tv_pujaTitle.setText(getString(R.string.modifyPuja));
            btn_pujaDel.setVisibility(View.VISIBLE);
            getPujaFields();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
//            intent.setClass(Puja_Add.this, Puja_list.class);
//            startActivity(intent);
            setResult(RESULT_OK, intent);

            Puja_Add.this.finish();
        }

        return super.onKeyDown(keyCode, event);
    }

    private void getLayoutObj() {
        tv_pujaTitle = (TextView) findViewById(R.id.tv_pujaTitle);

        btn_pujaAdd = (Button) findViewById(R.id.btn_pujaAdd);
        btn_pujaDel = (Button) findViewById(R.id.btn_pujaDel);

        btn_startDate = (Button) findViewById(R.id.btn_pujaStartDate);
        btn_stopDate = (Button) findViewById(R.id.btn_pujaStopDate);
        spn_name = (Spinner) findViewById(R.id.spn_pujaName);
        tv_startDate = (TextView) findViewById(R.id.tv_pujaStartDate);
        tv_stopDate = (TextView) findViewById(R.id.tv_pujaStopDate);
        edt_note = (EditText) findViewById(R.id.edt_pujaNote);
    }

    private void initPujaName() {

        //設定字串陣列-adapter
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this, R.array.puja_array, android.R.layout.simple_spinner_item);
        //自訂下拉選單-Spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_name.setAdapter(adapter);
//        spn_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(parent.getContext(), "你所選的是-" + parent.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
    }

    private void initDate() {

        GregorianCalendar calendar = new GregorianCalendar();

        // set Start
        //執行DatePickerDialog()，設定/讀出年、月、日，並顯示在文字欄位TextView01
        datePickerDialog1 = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                tv_startDate.setText(year + "/" + (monthOfYear + 1)  + "/" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));


        //按下btn_startDate按鈕時，執行DatePickerDialog()
        btn_startDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                datePickerDialog1.show();
            }
        });

        // set Stop
        datePickerDialog2 = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                tv_stopDate.setText(year + "/" + (monthOfYear + 1)  + "/" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));


        //按下btn_startDate按鈕時，執行DatePickerDialog()
        btn_stopDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                datePickerDialog2.show();
            }
        });


        // set today date
        Date dt = new Date();   // pick current system date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");   // set format for date
        tv_startDate.setText(dateFormat.format(dt));
        tv_stopDate.setText(dateFormat.format(dt));
    }

    private void initButton() {
        // Save Button
        btn_pujaAdd.setOnClickListener( new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(Puja_Add.this, "Add new", Toast.LENGTH_LONG).show();
                // check fields
                if (tv_startDate.length() == 0 || tv_stopDate.length() == 0) {
                    alertMsg(getString(R.string.inputPujaDate));
                    return;
                }


                Map<String, String> params = new HashMap<String, String>();
                if (act.equals("MOD"))
                    params.put("pid", pid);
                params.put("pname", Integer.toString(spn_name.getSelectedItemPosition()));
                params.put("begin_date", tv_startDate.getText().toString());
                params.put("end_date", tv_stopDate.getText().toString());
                params.put("notes", edt_note.getText().toString());

                // send to server
                http_SetNewMember(params);

            }
        });

        // Delete Button
        btn_pujaDel.setOnClickListener( new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Puja_Add.this).setTitle("訊息")
                        .setMessage(getString(R.string.confirmDel))
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                                act = "DEL";
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("pid", pid);


                                // send to server
                                http_SetNewMember(params);
                            }
                        })
                        .setNegativeButton(getString(R.string.cancel), null)
                        .show();
            }
        });
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

    private void http_SetNewMember(final Map<String, String> params) {
        String url;
        if (act.equals("ADD"))
            url = ip + apiAddPuja;                  //改用 post 的方式登入，重要資料就不會顯示在網址上了，比較安全
        else if (act.equals("MOD"))
            url = ip + apiModPuja;
        else
            url = ip + apiDelPuja;

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

                            int allCount = jsonRootObject.getInt("allCount");
//                            if (allCount > 0) {
                                Intent intent = new Intent();
//                                intent.setClass(Puja_Add.this, Puja_list.class);
                                setResult(RESULT_OK, intent);
//                            }

                            Puja_Add.this.finish();
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

    private void getPujaFields() {
//        Log.d("puja", "apiName=" + apiGetMember);

        String url = ip + apiGetPuja;                  //改用 post 的方式登入，重要資料就不會顯示在網址上了，比較安全

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

                            spn_name.setSelection(jsonRootObject.getInt("pname"));

                            if (jsonRootObject.isNull("begin_date"))
                                tv_startDate.setText("");
                            else
                                tv_startDate.setText(jsonRootObject.getString("begin_date").toString());

                            if (jsonRootObject.isNull("end_date"))
                                tv_stopDate.setText("");
                            else
                                tv_stopDate.setText(jsonRootObject.getString("end_date").toString());

                            edt_note.setText(jsonRootObject.getString("notes").toString());

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
                params.put("pid", pid);
                return params;
            }
        };

        //3) 把要執行的 StringRequest 加到隊列中執行
        queue.add(stringRequest);
    }
}
