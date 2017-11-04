package freshman.sqlitediary2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by gin on 2017/03/14.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Sento.db";
    public static final String TABLE_NAME = "sento_table";
    public static final int DATABASE_VERSION = 1;

    public Context context;
    public File database_path;
    public boolean createDatabase = false;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        this.database_path = context.getDatabasePath(DATABASE_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        super.onOpen(db);
        this.createDatabase = true;

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
        db.execSQL("DROP IF EXISTS " + TABLE_NAME);
    }

    @Override
    public synchronized SQLiteDatabase getReadableDatabase()
    {
        SQLiteDatabase database = super.getReadableDatabase();
        if(createDatabase){
            try{
                database = copyDatabase(database);
            }catch(IOException e){}
        }
        return database;
    }

    @Override
    public synchronized SQLiteDatabase getWritableDatabase()
    {
        SQLiteDatabase database = super.getWritableDatabase();
        if(createDatabase){
            try{
                database = copyDatabase(database);
            }catch(IOException e){}
        }
        return database;
    }

    private SQLiteDatabase copyDatabase(SQLiteDatabase database) throws IOException
    {
        database.close();

        InputStream input = context.getAssets().open(DATABASE_NAME);
        OutputStream output = new FileOutputStream(this.database_path);
        copy(input, output);

        createDatabase = false;

        return super.getWritableDatabase();
    }

    private static int copy(InputStream input, OutputStream output)throws IOException
    {
        byte[] buffer = new byte[1024 * 4];
        int count = 0;
        int n;
        while((n = input.read(buffer)) != -1){
            output.write(buffer, 0, n);
            count += n;
        }
        output.flush();
        output.close();
        input.close();
        return count;
    }
}
