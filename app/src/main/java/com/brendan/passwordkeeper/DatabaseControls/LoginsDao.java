package com.brendan.passwordkeeper.DatabaseControls;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface LoginsDao {
    @Query("SELECT * FROM logins")
    List<logins> getAllLogins();

    @Query("SELECT * FROM logins WHERE login_id = :id")
    logins getByLogID(int id);

    @Query("SELECT * FROM logins WHERE username = :username")
    logins getByUserName(String username);

    @Query("SELECT COUNT(*) FROM logins WHERE server_login_id = :svid")
    int checkIfSyncedRecordExists(int svid);

    @Query("SELECT * FROM logins WHERE server_login_id = :svid")
    logins getLoginByServerId(int svid);

    @Update(entity = logins.class)
    void updateLogins(logins login);

    @Insert
    void insert(logins login);
}
