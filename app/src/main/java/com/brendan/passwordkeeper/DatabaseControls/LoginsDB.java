package com.brendan.passwordkeeper.DatabaseControls;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {logins.class, master_login.class}, version = 1)
public abstract class LoginsDB extends RoomDatabase {
    public abstract LoginsDao loginsDao();

    public abstract MasterLoginDao masterLoginDao();
}
