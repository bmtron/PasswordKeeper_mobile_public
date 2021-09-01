package com.brendan.passwordkeeper;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.brendan.passwordkeeper.APIStuff.LoginsAsync;
import com.brendan.passwordkeeper.DatabaseControls.LoginsDB;
import com.brendan.passwordkeeper.DatabaseControls.SyncDatabase;
import com.brendan.passwordkeeper.DatabaseControls.logins;
import com.brendan.passwordkeeper.DatabaseControls.master_login;
import com.brendan.passwordkeeper.Globals.GlobalVars;


import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class AppStart extends AppCompatActivity {
    Button signIn;
    Button btnCreateAccount;
    EditText username;
    EditText password;
    String resultString;
    String masterResultString;
    JSONArray apiResults;
    JSONArray masterApiResults;
    private static LoginsDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = Room.databaseBuilder(getApplicationContext(), LoginsDB.class, "passkeep.db").createFromAsset("databases/passkeep.db").allowMainThreadQueries().build();
        assignTextFields(username, password);
        final String url = GlobalVars.loginUrl;
        final String masterUrl = GlobalVars.masterUrl;
        final String authKey = GlobalVars.authKey;
        syncDataFromServer(url, masterUrl, authKey);

        signIn = (Button) findViewById(R.id.btnSignIn);
        btnCreateAccount = (Button) findViewById(R.id.btnCreateAccount);
        setupCreateAccountBtn(btnCreateAccount);
        setupSignInBtn(signIn);
    }
    public void setupSignInBtn(Button btnSignIn) {
        final EditText userNameText = (EditText) findViewById(R.id.txtUsername);
        final EditText passwordText = (EditText) findViewById(R.id.txtPassword);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String usernameString = userNameText.getText().toString();
                String passwordString = passwordText.getText().toString();
                master_login master_user = db.masterLoginDao().getMasterLoginByUserName(usernameString);
                if (master_user == null) {
                    TextView error = (TextView) findViewById(R.id.lblError);
                    error.setVisibility(View.VISIBLE);
                } else {
                    try {
                        if (!Crypto.Decrypt(master_user.m_password).equals(passwordString)) {
                            TextView error = (TextView) findViewById(R.id.lblError);
                            error.setVisibility(View.VISIBLE);
                        } else {
                            Intent goToHome = new Intent(getApplicationContext(), Home.class);
                            startActivity(goToHome);
                            finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }
    public void setupCreateAccountBtn(Button btnCreateAccount) {
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToActivity = new Intent(getApplicationContext(), CreateAccount.class);
                startActivity(goToActivity);
                finish();
            }
        });
    }
    public void assignTextFields(EditText username, EditText password) {
        username = (EditText) findViewById(R.id.txtUsername);
        password = (EditText) findViewById(R.id.txtPassword);
    }
    public void syncDataFromServer(String url, String masterUrl, String authKey)
    {
        final CountDownLatch latch = new CountDownLatch(1);
        new Thread(new LoginsAsync(url, authKey, latch)).start();
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        resultString = GlobalVars.apiResultString;
        final CountDownLatch masterLatch = new CountDownLatch(1);
        new Thread(new LoginsAsync(masterUrl, authKey, masterLatch)).start();
        try{
            masterLatch.await();
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        masterResultString = GlobalVars.apiResultString;
        try {
            apiResults = new JSONArray(resultString);
            masterApiResults = new JSONArray(masterResultString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("JSON results:", apiResults.toString());
        Log.d("JSON master results:", masterApiResults.toString());
        try {
            SyncDatabase.checkForNewRecordsFromServer(apiResults, getApplicationContext());
            SyncDatabase.checkForNewMasterRecordsFromServer(masterApiResults, getApplicationContext());
            SyncDatabase.updateAllLogins(apiResults, getApplicationContext());
            SyncDatabase.updateAllMasterLogins(masterApiResults, getApplicationContext());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}