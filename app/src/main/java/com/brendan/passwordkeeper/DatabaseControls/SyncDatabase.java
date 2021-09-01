package com.brendan.passwordkeeper.DatabaseControls;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.room.Room;

public class SyncDatabase {
    private static LoginsDB db;


    public static void checkForNewRecordsFromServer(JSONArray serverResults, Context con) throws JSONException {
        db = Room.databaseBuilder(con, LoginsDB.class, "passkeep.db").createFromAsset("databases/passkeep.db").allowMainThreadQueries().build();
        for (int i = 0; i < serverResults.length(); i++) {
            JSONObject serverObj = serverResults.getJSONObject(i);
            int serverID = Integer.parseInt(serverObj.getString("login_id"));
            int localCount = db.loginsDao().checkIfSyncedRecordExists(serverID);

            if (localCount < 1) {
                logins newLogin = new logins();
                newLogin.account = serverObj.getString("account");
                newLogin.username = serverObj.getString("username");
                newLogin.password = serverObj.getString("password");
                newLogin.website = serverObj.getString("website");
                newLogin.price_per_month = serverObj.getString("price_per_month");
                newLogin.renewal_date = serverObj.getString("renewal_date");
                newLogin.server_login_id = serverObj.getInt("login_id");

                db.loginsDao().insert(newLogin);
            }
        }
    }

    public static void checkForNewMasterRecordsFromServer(JSONArray serverResults, Context con) throws JSONException {
        db = Room.databaseBuilder(con, LoginsDB.class, "passkeep.db").createFromAsset("databases/passkeep.db").allowMainThreadQueries().build();
        for (int i = 0; i < serverResults.length(); i++) {
            JSONObject serverObj = serverResults.getJSONObject(i);

            int serverID = serverObj.getInt("master_id");
            int localCount = db.masterLoginDao().checkIfSyncedMasterRecordsExist(serverID);

            if (localCount < 1) {
                master_login newMasterLogin = new master_login();

                newMasterLogin.m_username = serverObj.getString("m_username");
                newMasterLogin.m_password = serverObj.getString("m_password");
                newMasterLogin.reset_key = serverObj.getString("reset_key");
                newMasterLogin.email = serverObj.getString("email");
                newMasterLogin.server_master_id = serverObj.getInt("master_id");

                db.masterLoginDao().insert(newMasterLogin);
            }
        }
    }

    public static void updateAllLogins(JSONArray serverResults, Context con) throws JSONException {
        db = Room.databaseBuilder(con, LoginsDB.class, "passkeep.db").createFromAsset("databases/passkeep.db").allowMainThreadQueries().build();
        for (int i = 0; i < serverResults.length(); i++) {
            JSONObject serverObj = serverResults.getJSONObject(i);

            logins login = db.loginsDao().getLoginByServerId(serverObj.getInt("login_id"));

            login.password = serverObj.getString("password");

            db.loginsDao().updateLogins(login);

        }
    }
    public static void updateAllMasterLogins(JSONArray serverResults, Context con) throws JSONException {
        db = Room.databaseBuilder(con, LoginsDB.class, "passkeep.db").createFromAsset("databases/passkeep.db").allowMainThreadQueries().build();
        for (int i = 0; i < serverResults.length(); i++) {
            JSONObject serverObj = serverResults.getJSONObject(i);

            master_login masterLogin = db.masterLoginDao().getMasterLoginByServerId(serverObj.getInt("master_id"));

            masterLogin.m_password = serverObj.getString("m_password");

            db.masterLoginDao().update(masterLogin);
        }
    }
}
