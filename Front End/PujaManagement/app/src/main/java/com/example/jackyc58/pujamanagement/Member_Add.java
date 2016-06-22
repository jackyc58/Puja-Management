package com.example.jackyc58.pujamanagement;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by jackyc58 on 2016/5/4.
 */
public class Member_Add extends AppCompatActivity {

    EditText edt_addMbName, edt_addMbTel, edt_addMbEmail, edt_addMbAddr, edt_addMbNote;
    TextView tv_addMbBornDate, tv_addMbDieDate, tv_addMbTitle;
    CheckBox cb_addMbHead, cb_addMbDie;
    Button btn_addMbBornDate, btn_addMbDieDate, btn_addMbSave, btn_addMbDel;
    Spinner spn_addMbBornTime, spn_addMbDieTime;

    private DatePickerDialog dpd_born, dpd_die;

    String gid, uid, act, prev_page;

    Intent intent;

    RequestQueue queue;//隊列改宣告在這，因為整個頁面只需要一個隊列，但請在頁面建立後再實體化
    final String ip = "http://192.168.10.10/";
    final String apiAddMember = "puja/addMember";
    final String apiModMember = "puja/modifyMemberItem";
    final String apiGetMember = "puja/getMemberItem";
    final String apiDelMember = "puja/deleteMemberItem";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_layout);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        // init
        getLayoutObj();
        initDate();
        initBtnListener();
        initSpinner();

        tv_addMbTitle.setText(getString(R.string.addMember));
        btn_addMbDel.setVisibility(View.INVISIBLE);

        // get param GID
        intent = getIntent();
        gid = intent.getStringExtra("GID");
        uid = intent.getStringExtra("UID");
        act = intent.getStringExtra("ACT");
        Log.i("puja", "Member_Add gid=" + gid);

        // modify member, act = MOD
        if (act.equals("MOD")) {
            tv_addMbTitle.setText(getString(R.string.modifyMember));
            btn_addMbDel.setVisibility(View.VISIBLE);
            getFields();
        }


    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
//            intent.putExtra("GID", gid);
//            intent.putExtra("ACT", "OTHERS");
//            intent.setClass(Member_Add.this, Household.class);
//            startActivity(intent);
            setResult(RESULT_OK, intent);
            Member_Add.this.finish();
        }

        return super.onKeyDown(keyCode, event);
    }

    private void getLayoutObj() {
        edt_addMbName = (EditText) findViewById(R.id.edt_addMbName);
        edt_addMbTel = (EditText) findViewById(R.id.edt_addMbTel);
        edt_addMbEmail = (EditText) findViewById(R.id.edt_addMbEmail);
        edt_addMbAddr = (EditText) findViewById(R.id.edt_addMbAddr);
        edt_addMbNote = (EditText) findViewById(R.id.edt_addMbNote);

        tv_addMbTitle = (TextView) findViewById(R.id.tv_addMbTitle);
        tv_addMbBornDate = (TextView) findViewById(R.id.tv_addMbBornDate);
        tv_addMbDieDate = (TextView) findViewById(R.id.tv_addMbDieDate);
        cb_addMbHead = (CheckBox) findViewById(R.id.cb_addMbHead);
        cb_addMbDie = (CheckBox) findViewById(R.id.cb_addMbDie);

        btn_addMbBornDate = (Button) findViewById(R.id.btn_addMbBornDate);
        btn_addMbDieDate = (Button) findViewById(R.id.btn_addMbDieDate);
        btn_addMbSave = (Button) findViewById(R.id.btn_addMbSave);
        btn_addMbDel = (Button) findViewById(R.id.btn_addMbDel);

        spn_addMbBornTime = (Spinner) findViewById(R.id.spn_addMbBornTime);
        spn_addMbDieTime = (Spinner) findViewById(R.id.spn_addMbDieTime);

    }

    private void initSpinner() {
        String[] earthlyBranch = getResources().getStringArray(R.array.earthlyBranch);
        ArrayAdapter adapter = new ArrayAdapter<String>(Member_Add.this,android.R.layout.simple_spinner_item, earthlyBranch);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_addMbBornTime.setAdapter(adapter);
//        spn_addMbBornTime.setOnItemSelectedListener(sl_bornTime);

        spn_addMbDieTime.setAdapter(adapter);
    }

//    AdapterView.OnItemSelectedListener sl_bornTime = new AdapterView.OnItemSelectedListener(){
//
//        @Override
//        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//            // TODO Auto-generated method stub
////            TextView tv = (TextView)view.findViewById(android.R.id.text1);
////            Toast.makeText(MainActivity.this,tv.getText() , Toast.LENGTH_SHORT).show();
////            cityCode = (char)(position+65);
//            tv_dbg.setText(Integer.toString(position));
//        }
//
//        @Override
//        public void onNothingSelected(AdapterView<?> parent) {
//            // TODO Auto-generated method stub
//
//        }
//    };

    private void initDate() {

        GregorianCalendar calendar = new GregorianCalendar();

        // set Born date
        //執行DatePickerDialog()，設定/讀出年、月、日，並顯示在文字欄位TextView01
        dpd_born = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                tv_addMbBornDate.setText(year + "/" + (monthOfYear + 1)  + "/" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        //按下btn_addMbBornDate按鈕時，執行DatePickerDialog()
        btn_addMbBornDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dpd_born.show();
            }
        });


        // set Die date
        dpd_die = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                tv_addMbDieDate.setText(year + "/" + (monthOfYear + 1)  + "/" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        //按下btn_addMbDieDate按鈕時，執行DatePickerDialog()
        btn_addMbDieDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dpd_die.show();
            }
        });

    }

    private void initBtnListener() {

        // Save Button
        btn_addMbSave.setOnClickListener( new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                // check fields
                if (edt_addMbName.getText().toString().isEmpty()) {
                    alertMsg(getString(R.string.inputUserName));
                    return;
                }

                if (cb_addMbHead.isChecked() && edt_addMbTel.getText().toString().isEmpty()){
                    alertMsg(getString(R.string.inputTel));
                    return;
                }

                if (cb_addMbHead.isChecked() && edt_addMbAddr.getText().toString().isEmpty()){
                    alertMsg(getString(R.string.inputAddr));
                    return;
                }



                // make parameter
                Map<String, String> params = new HashMap<String, String>();
                if (act.equals("MOD"))
                    params.put("uid", uid);

                params.put("gid", gid);
                params.put("username", edt_addMbName.getText().toString().trim());
                params.put("address", edt_addMbAddr.getText().toString());
                params.put("tel", edt_addMbTel.getText().toString());
                if (cb_addMbHead.isChecked())
                    params.put("head", "1");
                else
                    params.put("head", "0");
                if (cb_addMbDie.isChecked())
                    params.put("dead", "1");
                else
                    params.put("dead", "0");
                params.put("born_date", tv_addMbBornDate.getText().toString().trim());
                params.put("born_time", Integer.toString(spn_addMbBornTime.getSelectedItemPosition()));
                params.put("die_date", tv_addMbDieDate.getText().toString().trim());
                params.put("die_time", Integer.toString(spn_addMbDieTime.getSelectedItemPosition()));
                params.put("notes", edt_addMbNote.getText().toString().trim());
                params.put("email", edt_addMbEmail.getText().toString().trim());//暫時在這輸入帳號密碼，方便測試

                // send to server
                http_SetNewMember(params, gid);

            }
        });

        btn_addMbDel.setOnClickListener( new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Member_Add.this).setTitle(getString(R.string.message))
                        .setMessage(getString(R.string.confirmDel))
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i)
                            {
                                act = "DEL";
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("uid", uid);
                                params.put("gid", gid);

                                // send to server
                                http_SetNewMember(params, gid);
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

//    private boolean alertMsgConfirm(String titleMsg, String msg)
//    {
//        final String status;
//        status = "CANCEL";
//
//        new AlertDialog.Builder(this).setTitle(titleMsg)
//                .setMessage(msg)
////                .setPositiveButton("OK", null)
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialoginterface, int i)
//                    {
////                        status = "OK";
//                    }
//                })
//                .setNegativeButton("Cancel", null)
//                .show();
//
//        if (status.equals("OK"))
//            return true;
//        else
//            return false;
//    }

    private void http_SetNewMember(final Map<String, String> params, final String gid) {
        String url;
        if (act.equals("ADD") || act.equals("SIGNUP"))
            url = ip + apiAddMember;                  //改用 post 的方式登入，重要資料就不會顯示在網址上了，比較安全
        else if (act.equals("MOD"))
            url = ip + apiModMember;
        else
            url = ip + apiDelMember;

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


                            if (act.equals("SIGNUP")) {
                                intent.putExtra("UID", jsonRootObject.getString("uid"));
                                intent.putExtra("USERNAME", edt_addMbName.getText().toString().trim());
                                intent.putExtra("GID", gid);
                                setResult(RESULT_OK, intent);
                                finish();
                            } else {
                                Intent intent = new Intent();
                                int gidCount = jsonRootObject.getInt("gidCount");
                                if (gidCount > 0) {
                                    intent.putExtra("GID", gid);
//                                    intent.setClass(Member_Add.this, Household.class);
                                    setResult(RESULT_OK, intent);
                                    finish();
//                                    startActivity(intent);
                                }

//                                Member_Add.this.finish();
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

    private void getFields() {
        Log.d("puja", "apiName=" + apiGetMember);

        String url = ip + apiGetMember;                  //改用 post 的方式登入，重要資料就不會顯示在網址上了，比較安全

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

//                            Log.d("puja", "ret=" + jsonRootObject.getString("ret").toString());
//                            Log.d("puja", "ret=" + response);
//                            'username', 'email', 'head', 'address', 'tel', 'dead', 'born_date', 'born_time', 'die_date', 'die_time', 'notes'

//
                            edt_addMbName.setText(jsonRootObject.getString("username").toString());
                            edt_addMbAddr.setText(jsonRootObject.getString("address").toString());
                            edt_addMbEmail.setText(jsonRootObject.getString("email").toString());
                            edt_addMbTel.setText(jsonRootObject.getString("tel").toString());
                            edt_addMbNote.setText(jsonRootObject.getString("notes").toString());


                            if (jsonRootObject.isNull("head") || jsonRootObject.getString("head").toString().equals("0"))
                                cb_addMbHead.setChecked(false);
                            else
                                cb_addMbHead.setChecked(true);

                            String dead = jsonRootObject.getString("dead").toString();
                            if (jsonRootObject.isNull("dead") || jsonRootObject.getString("dead").toString().equals("0"))
                                cb_addMbDie.setChecked(false);
                            else
                                cb_addMbDie.setChecked(true);

                            if (jsonRootObject.isNull("born_date"))
                                tv_addMbBornDate.setText("");
                            else
                                tv_addMbBornDate.setText(jsonRootObject.getString("born_date").toString());

                            spn_addMbBornTime.setSelection(jsonRootObject.getInt("born_time"));

                            if (jsonRootObject.isNull("die_date"))
                                tv_addMbDieDate.setText("");
                            else
                                tv_addMbDieDate.setText(jsonRootObject.getString("die_date").toString());

                            spn_addMbDieTime.setSelection(jsonRootObject.getInt("die_time"));


//                            Intent intent = new Intent();
//                            intent.putExtra("GID", gid);
//                            intent.setClass(Member_Add.this, Household.class);
//                            startActivity(intent);
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
                params.put("uid", uid);
                return params;
            }
        };

        //3) 把要執行的 StringRequest 加到隊列中執行
        queue.add(stringRequest);
    }
}
