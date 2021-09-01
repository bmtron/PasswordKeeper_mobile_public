package com.brendan.passwordkeeper.DatabaseControls;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class master_login {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public Integer master_id;

    @ColumnInfo(name = "m_username")
    public String m_username;

    @ColumnInfo(name = "m_password")
    public String m_password;

    @ColumnInfo(name = "reset_key")
    public String reset_key;

    @ColumnInfo(name = "email")
    public String email;

    @ColumnInfo(name = "server_master_id")
    public Integer server_master_id;

}
