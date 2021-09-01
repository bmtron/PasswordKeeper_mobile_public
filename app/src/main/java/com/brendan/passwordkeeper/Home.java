package com.brendan.passwordkeeper;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.brendan.passwordkeeper.Adapters.LoginsListAdapter;
import com.brendan.passwordkeeper.DatabaseControls.LoginsDB;
import com.brendan.passwordkeeper.DatabaseControls.logins;

import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Home extends AppCompatActivity {
    Button btnBackToHome;
    private ListView listView;
    private LoginsListAdapter loginsAdapter;
    private static LoginsDB db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        db = Room.databaseBuilder(getApplicationContext(), LoginsDB.class, "passkeep.db").createFromAsset("databases/passkeep.db").allowMainThreadQueries().build();
        btnBackToHome = (Button) findViewById(R.id.btnBackToSignIn);
        btnBackToHome.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                Intent backToLogin = new Intent(getApplicationContext(), AppStart.class);
                startActivity(backToLogin);
                finish();
            }
        });
        getAllLogins();
    }

    private void getAllLogins() {
        ArrayList<logins> allLogins = (ArrayList<logins>) db.loginsDao().getAllLogins();
        listView = (ListView) findViewById(R.id.LoginsListView);
        loginsAdapter = new LoginsListAdapter(this, allLogins);
        listView.setAdapter(loginsAdapter);


    }
}