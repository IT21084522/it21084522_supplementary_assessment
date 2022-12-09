package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "items.db";

    public DBHelper( Context context) {super(context, DATABASE_NAME, null, 1);}

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + FoodItems.Items.TABLE_NAME + " (" +
                        FoodItems.Items._ID+ " INTEGER PRIMARY KEY," +
                        FoodItems.Items.COLUMN_NAME_ITEM+ " TEXT,"+
                        FoodItems.Items.COLUMN_NAME_PRICE+ " TEXT)";

        db.execSQL(SQL_CREATE_ENTRIES);

    }

    public Long addInfo(String userName, String password){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(FoodItems.Items.COLUMN_NAME_ITEM, item);
        values.put(FoodItems.Items.COLUMN_NAME_PRICE, price);

        return db.insert(FoodItems.Items.TABLE_NAME, null, values);
    }

    // new code
    public List readAllInfo(){
        SQLiteDatabase db = getReadableDatabase();

        String [] projection = {
                FoodItems.Items._ID,
                FoodItems.Items.COLUMN_NAME_ITEM,
                FoodItems.Items.COLUMN_NAME_PRICE
        };

        String sortOrder = UsersMaster.Users.COLUMN_NAME_ITEM + " DESC";

        Cursor cursor = db.query(
                FoodItems.Items.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        List info = new ArrayList<>();

        while (cursor.moveToNext()){
            String item = cursor.getString(cursor.getColumnIndexOrThrow(FoodItems.Items.COLUMN_NAME_ITEM));
            String price = cursor.getString(cursor.getColumnIndexOrThrow(FoodItems.Items.COLUMN_NAME_PRICE));

            info.add(item+": "+price);
        }
        cursor.close();

        return info;
    }

    public void deleteInfo(String userName){
        SQLiteDatabase db = getReadableDatabase();
        String selection = FoodItems.Items.COLUMN_NAME_ITEM + " LIKE ?";
        String [] selectionArgs = { userName };
        db.delete(FoodItems.Items.TABLE_NAME,selection,selectionArgs);


    }

    public void updateInfo(View view, String item, String price){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(FoodItems.Items.COLUMN_NAME_PRICE, price);

        String selection = UsersMaster.Users.COLUMN_NAME_ITEM + " Like ?";
        String[] selectionArgs = {item};

        int count = db.update(
                FoodItems.Items.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );

        Snackbar snackbar = Snackbar.make(view, count + " rows effected",Snackbar.LENGTH_LONG);
        snackbar.setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE);
        snackbar.show();



    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
