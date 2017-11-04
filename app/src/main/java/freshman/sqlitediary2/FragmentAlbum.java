package freshman.sqlitediary2;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.InputStream;

import static android.app.Activity.RESULT_OK;

/**
 * Created by gin on 2017/03/14.
 */

public class FragmentAlbum extends Fragment {

    public static int ALBUM_CODE_1 = 1;
    public static int ALBUM_CODE_2 = 2;
    public static int ALBUM_CODE_3 = 3;
    public static int ALBUM_CODE_4 = 4;

    DatabaseHelper databaseHelper;
    SQLiteDatabase database;
    Cursor res;
    int row_id;
    public static final String TABLE_NAME = "sento_table";

    ImageView album_card_view_image_1, album_card_view_image_2, album_card_view_image_3, album_card_view_image_4;
    ImageButton album_card_view_button_1, album_card_view_button_2, album_card_view_button_3, album_card_view_button_4;


    String database_pic0_path = null;
    String database_pic1_path = null;
    String database_pic2_path = null;
    String database_pic3_path = null;


    View view;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        container.removeView(view);

        view = inflater.inflate(R.layout.fragment_album, container, false);


        Bundle bundle = getArguments();
        row_id = bundle.getInt("row_id");


        databaseHelper = new DatabaseHelper(getContext());
        database = databaseHelper.getWritableDatabase();
        res = database.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME, null);
        res.moveToPosition(row_id);



        database_pic0_path = res.getString(10);
        database_pic1_path = res.getString(11);
        database_pic2_path = res.getString(12);
        database_pic3_path = res.getString(13);



        album_card_view_image_1 = (ImageView)view.findViewById(R.id.album_card_view_image_1);
        if(database_pic0_path != null){
            Uri uri = Uri.parse(database_pic0_path);
            try{
                InputStream is = getContext().getContentResolver().openInputStream(uri);
                Bitmap bmp = BitmapFactory.decodeStream(is);
                album_card_view_image_1.setImageBitmap(bmp);
            }catch(Exception e){
                Log.i("Error", "EXCEPTIONwwwwW", e);
            }
        }


        album_card_view_image_2 = (ImageView)view.findViewById(R.id.album_card_view_image_2);
        if(database_pic1_path != null){
            Uri uri = Uri.parse(database_pic1_path);
            try{
                InputStream is = getContext().getContentResolver().openInputStream(uri);
                Bitmap bmp = BitmapFactory.decodeStream(is);
                album_card_view_image_2.setImageBitmap(bmp);
            }catch(Exception e){}
        }
        album_card_view_image_3 = (ImageView)view.findViewById(R.id.album_card_view_image_3);
        if(database_pic2_path != null){
            Uri uri = Uri.parse(database_pic2_path);
            try{
                InputStream is = getContext().getContentResolver().openInputStream(uri);
                Bitmap bmp = BitmapFactory.decodeStream(is);
                album_card_view_image_3.setImageBitmap(bmp);
            }catch(Exception e){}
        }
        album_card_view_image_4 = (ImageView)view.findViewById(R.id.album_card_view_image_4);
        if(database_pic3_path != null){
            Uri uri = Uri.parse(database_pic3_path);
            try{
                InputStream is = getContext().getContentResolver().openInputStream(uri);
                Bitmap bmp = BitmapFactory.decodeStream(is);
                album_card_view_image_4.setImageBitmap(bmp);
            }catch(Exception e){}
        }




        album_card_view_button_1 = (ImageButton)view.findViewById(R.id.album_card_view_button_1);
        album_card_view_button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                startActivityForResult(intent, ALBUM_CODE_1);
            }
        });

        album_card_view_button_2 = (ImageButton)view.findViewById(R.id.album_card_view_button_2);
        album_card_view_button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                startActivityForResult(intent, ALBUM_CODE_2);
            }
        });

        album_card_view_button_3 = (ImageButton)view.findViewById(R.id.album_card_view_button_3);
        album_card_view_button_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                startActivityForResult(intent, ALBUM_CODE_3);
            }
        });

        album_card_view_button_4 = (ImageButton)view.findViewById(R.id.album_card_view_button_4);
        album_card_view_button_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                startActivityForResult(intent, ALBUM_CODE_4);
            }
        });




        return view;

    }

    public void onActivityResult(int reqcode, int result, Intent intent)
    {

        int ID = res.getInt(0);

        if(reqcode == ALBUM_CODE_1 && result == RESULT_OK){
            Uri uri = intent.getData();
            final int takeFlags = intent.getFlags()
                    & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            getContext().getContentResolver().takePersistableUriPermission(uri, takeFlags);
            try {
                Bitmap bmp = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                album_card_view_image_1.setImageBitmap(bmp);
                String pic0_path = uri.toString();
                ContentValues contentValues = new ContentValues();
                contentValues.put("PIC0", pic0_path);
                database.update(TABLE_NAME, contentValues, "id = " + ID, null);
            }catch(Exception e){}
        }else if(reqcode == ALBUM_CODE_2 && result == RESULT_OK) {
            Uri uri = intent.getData();
            final int takeFlags = intent.getFlags()
                    & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            getContext().getContentResolver().takePersistableUriPermission(uri, takeFlags);
            try {
                Bitmap bmp = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                album_card_view_image_2.setImageBitmap(bmp);
                ContentValues contentValues = new ContentValues();
                String pic1_path = uri.toString();
                contentValues.put("PIC1", pic1_path);
                database.update(TABLE_NAME, contentValues, "id = " + ID, null);
            } catch (Exception e) {
            }
        }else if(reqcode == ALBUM_CODE_3 && result == RESULT_OK) {
            Uri uri = intent.getData();
            final int takeFlags = intent.getFlags()
                    & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            getContext().getContentResolver().takePersistableUriPermission(uri, takeFlags);
            try {
                Bitmap bmp = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                album_card_view_image_3.setImageBitmap(bmp);
                ContentValues contentValues = new ContentValues();
                String pic2_path = uri.toString();
                contentValues.put("PIC2", pic2_path);
                database.update(TABLE_NAME, contentValues, "id = " + ID, null);
            } catch (Exception e) {
            }
        }else if(reqcode == ALBUM_CODE_4 && result == RESULT_OK) {
            Uri uri = intent.getData();
            final int takeFlags = intent.getFlags()
                    & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            getContext().getContentResolver().takePersistableUriPermission(uri, takeFlags);
            try {
                Bitmap bmp = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                album_card_view_image_4.setImageBitmap(bmp);
                ContentValues contentValues = new ContentValues();
                String pic3_path = uri.toString();
                contentValues.put("PIC3", pic3_path);
                database.update(TABLE_NAME, contentValues, "id = " + ID, null);
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        database.close();
        res.close();
    }
}
