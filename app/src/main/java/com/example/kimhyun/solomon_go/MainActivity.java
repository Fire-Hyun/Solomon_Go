package com.example.kimhyun.solomon_go;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

        ImageView btn_Main_Profile, btn_Main_Solostop, btn_Main_Nearsolomon, btn_Main_Bag, btn_Main_Setting, btn_Main_Recently;

        Button btn_Main_Logout;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            Log.d("111", "111");
            btn_Main_Profile = (ImageView) findViewById(R.id.btn_Main_Profile);
            btn_Main_Solostop = (ImageView) findViewById(R.id.btn_Main_Solostop);
            btn_Main_Nearsolomon = (ImageView) findViewById(R.id.btn_Main_Nearsolomon);
            btn_Main_Bag = (ImageView) findViewById(R.id.btn_Main_Bag);
            btn_Main_Setting = (ImageView) findViewById(R.id.btn_Main_Setting);
            btn_Main_Recently = (ImageView) findViewById(R.id.btn_Main_Recently);
            btn_Main_Logout = (Button) findViewById(R.id.btn_Main_Logout);

            btn_Main_Profile.setOnClickListener(this);
            btn_Main_Solostop.setOnClickListener(this);
            btn_Main_Nearsolomon.setOnClickListener(this);
            btn_Main_Bag.setOnClickListener(this);
            btn_Main_Setting.setOnClickListener(this);
            btn_Main_Recently.setOnClickListener(this);
            btn_Main_Logout.setOnClickListener(this);
        }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) { //종료방지
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
                String buttonMessage = "어플을 종료하시겠습니까?";
                String buttonYes = "Yes";
                String buttonNo = "No";

                new AlertDialog.Builder(MainActivity.this)
                        .setMessage(buttonMessage)
                        .setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                moveTaskToBack(true);
                                finish();
                            }
                        })
                        .setNegativeButton(buttonNo, null)
                        .show();
        }
        return true;
    }


    @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()){
                case R.id.btn_Main_Profile:
                    intent = new Intent(MainActivity.this, ProfileActivity.class);
                    break;
                case R.id.btn_Main_Solostop:
                    intent = new Intent(MainActivity.this, SolostopActivity.class);
                    break;
                case R.id.btn_Main_Setting:
                    intent = new Intent(MainActivity.this, SettingActivity.class);
                    break;
                case R.id.btn_Main_Nearsolomon:
                    intent = new Intent(MainActivity.this, NearsolomonActivity.class);
                    break;
                case R.id.btn_Main_Bag:
                    intent = new Intent(MainActivity.this, BagActivity.class);
                    break;
                case R.id.btn_Main_Recently:
                    intent = new Intent(MainActivity.this, DialogAcitivy.class);//여기서 이미지나 프로필내용을 보내주어야 한다
                    //intent = new Intent(MainActivity.this, RecentlyActivity.class);
                    break;
                case R.id.btn_Main_Logout:
                    SharedPreferences auto_login = getSharedPreferences("setting", 0);
                    SharedPreferences.Editor editor = auto_login.edit();

                    editor.clear();
                    editor.commit();


                    intent = new Intent(MainActivity.this, LoginActivity.class);
                    break;
            }
            startActivity(intent);

        }
    }