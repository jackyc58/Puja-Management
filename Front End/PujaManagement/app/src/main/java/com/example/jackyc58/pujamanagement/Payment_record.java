package com.example.jackyc58.pujamanagement;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by jackyc58 on 2016/5/4.
 */
public class Payment_record extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_record);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
    }
}
