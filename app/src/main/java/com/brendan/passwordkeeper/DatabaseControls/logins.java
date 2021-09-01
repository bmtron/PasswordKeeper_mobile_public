package com.brendan.passwordkeeper.DatabaseControls;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class logins {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public Integer login_id;

    @ColumnInfo(name = "account")
    @NonNull
    public String account;

    @ColumnInfo(name = "username")
    public String username;

    @ColumnInfo(name = "password")
    public String password;

    @ColumnInfo(name = "website")
    public String website;

    @ColumnInfo(name = "FK_master_id")
    public Integer fk_master_id;

    @ColumnInfo(name = "price_per_month")
    public String price_per_month;

    @ColumnInfo(name = "renewal_date")
    public String renewal_date;

    @ColumnInfo(name = "server_login_id")
    public Integer server_login_id;
}
