package com.brendan.passwordkeeper;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import com.brendan.passwordkeeper.DatabaseControls.LoginsDB;
import com.brendan.passwordkeeper.DatabaseControls.logins;

public class LoginDisplay extends AppCompatActivity {
    private static LoginsDB db;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_display);
        db = Room.databaseBuilder(getApplicationContext(), LoginsDB.class, "passkeep.db").createFromAsset("databases/passkeep.db").allowMainThreadQueries().build();
        Bundle extras = getIntent().getExtras();
        int loginId = extras.getInt("loginRecID");
        TextView test = (TextView) findViewById(R.id.test1);
        logins currentLogin = db.loginsDao().getByLogID(loginId);
        try {
            test.setText(Crypto.Decrypt(currentLogin.password));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}