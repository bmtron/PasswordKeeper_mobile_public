package com.brendan.passwordkeeper.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.brendan.passwordkeeper.Crypto;
import com.brendan.passwordkeeper.DatabaseControls.logins;
import com.brendan.passwordkeeper.LoginDisplay;
import com.brendan.passwordkeeper.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

public class LoginsListAdapter extends ArrayAdapter {
    private Context con;
    private ArrayList<logins> loginsList;
    public LoginsListAdapter(@NonNull Context context, ArrayList<logins> list) {
        super(context, 0, list);
        con = context;
        loginsList = list;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;

        if (listItem == null) {
            listItem = LayoutInflater.from(con).inflate(R.layout.logins_list, parent, false);
        }

        final logins currentLogin = loginsList.get(position);

        TextView loginAccount = (TextView) listItem.findViewById(R.id.logAccountName);
        loginAccount.setText(currentLogin.account);

        final TextView loginPassword = (TextView) listItem.findViewById(R.id.logPassword);
        loginPassword.setText("Hidden");


        final Button btnShowHidePassword = (Button) listItem.findViewById(R.id.btnShowHide);
        btnShowHidePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToLoginView = new Intent(con, LoginDisplay.class);
                goToLoginView.putExtra("loginRecID", currentLogin.login_id);
                con.startActivity(goToLoginView);
            }
        });
        return listItem;
    }
}
