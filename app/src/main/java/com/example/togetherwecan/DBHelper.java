package com.example.togetherwecan;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {
    private Context context;

    public DBHelper(Context context){
        super(context,ContractSchema.DATABASE.DATABASE_NAME,null,ContractSchema.DATABASE.DATABASE_VERSION);
        this.context=context;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try{
          //  sqLiteDatabase.execSQL(ContractSchema.NOTE.TABLE_CREATE);
            sqLiteDatabase.execSQL("PRAGMA foreign_key=on ");
            sqLiteDatabase.execSQL(ContractSchema.BOOK.CREATE_TABLE);
            sqLiteDatabase.execSQL(ContractSchema.TOPIC.CREATE_TABLE);
            sqLiteDatabase.execSQL(ContractSchema.SUBTOPIC.CREATE_TABLE);
            sqLiteDatabase.execSQL(ContractSchema.NOTE.CREATE_TABLE);
            //sqLiteDatabase.execSQL();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("PRAGMA foreign_key=on ");   /*allows usage of foreign keys */
        sqLiteDatabase.execSQL(ContractSchema.NOTE.DROP_TABLE);
        sqLiteDatabase.execSQL(ContractSchema.SUBTOPIC.DROP_TABLE);
        sqLiteDatabase.execSQL(ContractSchema.TOPIC.DROP_TABLE);
        sqLiteDatabase.execSQL(ContractSchema.BOOK.DROP_TABLE);
        onCreate(sqLiteDatabase);
    }


    public boolean add(String task, String[] args){
        getWritableDatabase().execSQL("PRAGMA foreign_key=on ");
        switch (task){
            case "note":
                getWritableDatabase().execSQL(ContractSchema.NOTE.ADD_NOTE,args);
                getWritableDatabase().close();
                return true;
            case "subtopic":
                getWritableDatabase().execSQL(ContractSchema.SUBTOPIC.ADD,args);
                getWritableDatabase().close();
                return true;
            case "topic":
                getWritableDatabase().execSQL(ContractSchema.TOPIC.ADD,args);
                getWritableDatabase().close();
                return true;
            case "book":
                getWritableDatabase().execSQL(ContractSchema.BOOK.ADD,args);
                getWritableDatabase().close();
                return true;
            default:
                return false;
        }
    }
    public boolean delete(String task, String[] args){
        getWritableDatabase().execSQL("PRAGMA foreign_key=on ");
        switch (task){
            case "note":
                getWritableDatabase().execSQL(ContractSchema.NOTE.DELETE_NOTE,args);
                getWritableDatabase().close();
                return true;
            case "subtopic":
                getWritableDatabase().execSQL(ContractSchema.NOTE.DELETE_SUBTOPIC,args);
                getWritableDatabase().execSQL(ContractSchema.SUBTOPIC.DELETE,args);
                getWritableDatabase().close();
                return true;
            case "topic":
                getWritableDatabase().execSQL(ContractSchema.NOTE.DELETE_TOPIC,args);
                getWritableDatabase().execSQL(ContractSchema.SUBTOPIC.DELETE_TOPIC,args);
                getWritableDatabase().execSQL(ContractSchema.TOPIC.DELETE,args);
                getWritableDatabase().close();
                return true;
            case "book":
                getWritableDatabase().execSQL(ContractSchema.NOTE.DELETE_BOOK,args);
                getWritableDatabase().execSQL(ContractSchema.SUBTOPIC.DELETE_BOOK,args);
                getWritableDatabase().execSQL(ContractSchema.TOPIC.DELETE_BOOK,args);
                getWritableDatabase().execSQL(ContractSchema.BOOK.DELETE,args);
                getWritableDatabase().close();
                return true;
        }
        return false;
    }

    public boolean modify(String task, String[] args){
        getWritableDatabase().execSQL("PRAGMA foreign_key=on ");
        switch (task){
            case "note":
                //args consist of name,summary and id
                getWritableDatabase().execSQL(ContractSchema.NOTE.UPDATE_NOTE,args);
                return true;
            case "subtopic":
                getWritableDatabase().execSQL(ContractSchema.SUBTOPIC.UPDATE,args);
                return true;
            case "topic":
                getWritableDatabase().execSQL(ContractSchema.TOPIC.UPDATE,args);
                return true;
            case "book":
                getWritableDatabase().execSQL(ContractSchema.BOOK.UPDATE,args);
                return true;
        }
        return false;
    }

     public Cursor fetch(String task, String[] args,String mode){
         getReadableDatabase().execSQL("PRAGMA foreign_key=on ");
         Cursor cursor = null;
        switch (task){
            case "note":
                switch (mode){
                    case "subtopic":
                        cursor = getReadableDatabase().rawQuery(ContractSchema.NOTE.FETCH_NOTES, args);
                        return cursor;
                    case "topic":
                        cursor = getReadableDatabase().rawQuery(ContractSchema.NOTE.FETCH_TOPIC, args);
                        return cursor;
                    case "book":
                        cursor = getReadableDatabase().rawQuery(ContractSchema.NOTE.FETCH_BOOK, args);
                        return cursor;
                    default:
                }

            case "subtopic":
                if(mode.equals("book"))
                    cursor = getReadableDatabase().rawQuery(ContractSchema.SUBTOPIC.FETCH_BOOK, args);
                else
                    cursor = getReadableDatabase().rawQuery(ContractSchema.SUBTOPIC.FETCH, args);
                return cursor;
            case "topic":
                cursor = getReadableDatabase().rawQuery(ContractSchema.TOPIC.FETCH, args);
                return cursor;
            case "book":
                cursor = getReadableDatabase().rawQuery(ContractSchema.BOOK.FETCH, args);
                return cursor;
            default:
                return null;
        }
    }


}
