package com.brendan.passwordkeeper.DatabaseControls;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface MasterLoginDao {
    @Query("SELECT * FROM master_login")
    List<master_login> getAllMasterLogins();

    @Query("SELECT * FROM master_login WHERE master_id = :id")
    master_login getByMasterID(int id);

    @Query("SELECT * FROM master_login WHERE m_username = :username")
    master_login getMasterLoginByUserName(String username);

    @Query("SELECT COUNT(*) FROM master_login WHERE server_master_id = :svid")
    int checkIfSyncedMasterRecordsExist(int svid);

    @Query("SELECT * FROM master_login WHERE server_master_id = :svid")
    master_login getMasterLoginByServerId(int svid);

    @Insert
    void insert(master_login ml);

    @Update
    void update(master_login ml);
}
