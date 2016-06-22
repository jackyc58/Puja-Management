package com.example.jackyc58.pujamanagement;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.android.volley.RequestQueue;

public class MainActivity extends AppCompatActivity {
    Button btn_pujaSignUp, btn_member, btn_pujaList;


    RequestQueue queue;//隊列改宣告在這，因為整個頁面只需要一個隊列，但請在頁面建立後再實體化
//    final String ip = "http://192.168.10.10/";
//    final String apiName = "puja/getNewGid";

    String gid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        btn_pujaSignUp = (Button) findViewById(R.id.btn_pujaSignUp);
        btn_member = (Button) findViewById(R.id.btn_member);
        btn_pujaList = (Button) findViewById(R.id.btn_pujaList);


        btn_pujaSignUp.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, Puja_sign_up.class);
                startActivity(intent);
            }
        });

        btn_member.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("ACT", "NEW");
                intent.setClass(MainActivity.this, Search.class);
                startActivity(intent);
            }
        });

        btn_pujaList.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, Puja_list.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(this).setTitle("")
                    .setMessage(getString(R.string.finishProg))
                    .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            MainActivity.this.finish();
                        }
                    })
                    .setNegativeButton(getString(R.string.cancel), null)
                    .show();
        }
        return super.onKeyDown(keyCode, event);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // TODO Auto-generated method stub
//        getMenuInflater().inflate(R.menu.main, menu);
//
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        Intent intent = new Intent();
//
//        switch (item.getItemId()) {
//            case R.id.action_search:
//                intent.setClass(MainActivity.this, Search.class);
//                startActivity(intent);
//                break;
//            case R.id.item2:
//                getNewGid();
//                break;
//            case R.id.item3:
//                intent.setClass(MainActivity.this, Puja_Add.class);
//                intent.putExtra("ACT", "ADD");
//                startActivity(intent);
//                //Toast.makeText(MainActivity.this,item.getTitle(), Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.item4:
//                intent.setClass(MainActivity.this, Puja_list.class);
//                startActivity(intent);
//                //Toast.makeText(MainActivity.this,item.getTitle(), Toast.LENGTH_SHORT).show();
//                break;
//
//        }
//
//        // TODO Auto-generated method stub
////        return super.onOptionsItemSelected(item);
//        return true;
//    }


//    private void getNewGid() {
//        final Intent intent = new Intent();
//
//        Log.d("puja", "apiName=" + apiName);
//
//        String url = ip + apiName;                  //改用 post 的方式登入，重要資料就不會顯示在網址上了，比較安全
//
//        getRequestQueue();                          //呼叫隊列用的函式
//
//        //2) 定義要放到隊列中執行用的 StringRequest
//        StringRequest stringRequest = new StringRequest(//需要 4 個參數
//                Request.Method.POST,                //改用 post 的方式傳送資料，但需要改寫一個內建的函式 getParams()
//                url,                                //執行請求用的網址
//                new Response.Listener<String>() {   //處理回應的字串用的匿名函式
//                    @Override
//                    public void onResponse(String response) {//改寫處理的函式
////                        tv.setText(response);//因為會用到外部的參數 tv，所以外部的參數 tv 要宣告成 final
//                        Log.d("puja", "response=" + response);
//                        try {
//                            JSONObject jsonRootObject = new JSONObject(response);
//                            gid = jsonRootObject.getString("gid").toString();
////                            Log.d("puja", "ret=" + jsonRootObject.getString("ret").toString());
////                            Log.d("puja", "ret=" + response);
//                            Log.i("puja", "main page gid=" + gid);
//                            intent.putExtra("GID", gid);
//                            intent.putExtra("ACT", "ADD");
//                            intent.setClass(MainActivity.this, Member_Add.class);
//                            startActivity(intent);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {      //處理錯誤回應用的匿名函式。請注意：若找不到 ip 會 return TimeOut。
//                    @Override
//                    public void onErrorResponse(VolleyError error) {//改寫處理的函式
////                        tv.setText("回傳錯誤");
//                        Log.d("puja", "Error:" + error.toString());//除錯訊息
//                    }
//                }
//        ) {//使用 post 方式時，改寫內建函式的位置要注意，是接在 StringRequest() 後面
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {//請查一下 Map, HashMap 是做什麼用的
//                Map<String, String> params = new HashMap<String, String>();
////                params.put("gid", gid);
//                return params;
//            }
//        };
//
//        //3) 把要執行的 StringRequest 加到隊列中執行
//        queue.add(stringRequest);
//    }
//
//    public RequestQueue getRequestQueue() {//檢查隊列是否已經初始化，若沒有就初始化
//        if (queue == null) {
//            queue = Volley.newRequestQueue(getApplicationContext());
//        }
//        return queue;
//    }

}
