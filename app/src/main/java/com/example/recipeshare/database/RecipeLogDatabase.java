package com.example.recipeshare.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.recipeshare.MainActivity;
import com.example.recipeshare.database.entities.RecipeLog;
import com.example.recipeshare.database.entities.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//TODO: Refer to Gymlog video 3 @ 16 min

@Database(entities = {RecipeLog.class, User.class}, version = 2, exportSchema = false)
public abstract class RecipeLogDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "RecipeLog_database";
    public static final String RECIPE_LOG_TABLE = "recipeLogTable";
    public static final String USER_TABLE = "user_table";
    private static volatile RecipeLogDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4; //TODO: is this correct?
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static RecipeLogDatabase getDatabase(final Context context){
        if (INSTANCE == null){
            synchronized (RecipeLogDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            RecipeLogDatabase.class,
                                    DATABASE_NAME
                            )
                            .fallbackToDestructiveMigration()
                            .addCallback(addDefaultValues)
                            .build();

                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback addDefaultValues = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db){
            super.onCreate(db);
            Log.i(MainActivity.TAG, "DATABASE CREATED!");
            //TODO: add databaseWriteExecutor.execute(() -> {...}
        }
    };


    public abstract RecipeLogDAO recipeLogDAO();
    public abstract UserDAO userDAO();

}
