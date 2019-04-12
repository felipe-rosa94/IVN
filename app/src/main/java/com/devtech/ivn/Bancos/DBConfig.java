package com.devtech.ivn.Bancos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBConfig {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "ivn.sqb";

    private static final String DATABASE_TABLE = "ivn";

    private static final String _ID = "Id";
    private static final String _ID_PG = "IdPg";
    private static final String _SENHA = "Senha";
    private static final String _BKP_CONVERSA = "BkpConversa";

    private static final String DATABASE_CREATE =
            "create table " + DATABASE_TABLE + " ("
                    + _ID + " integer primary key autoincrement, "
                    + _ID_PG + " text, "
                    + _SENHA + " text, "
                    + _BKP_CONVERSA + " text "
                    + ");";

    public class Struc {
        public int Id = 1;
        public String IdPg = "";
        public String Senha = "";
        public String BkpConversa = "";
    }

    public final Struc Fields = new Struc();

    private final Context context;

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBConfig(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    public boolean emtpyTable() {
        return db.delete(DATABASE_TABLE, null, null) > 0;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
            db.execSQL("insert into ivn (Id,IdPg,Senha,BkpConversa) values (1,\"\",\"\",\"\");");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w("Database", "Atualizando da versão " + oldVersion
                    + " para "
                    + newVersion + ". Isto destruirá todos os dados.");
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE + ";");
            onCreate(db);
        }
    }

    public void open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        getId(1);
    }

    public void close() {
        DBHelper.close();
    }

    private ContentValues getArgs() {
        ContentValues args = new ContentValues();
        args.put(_ID, Fields.Id);
        args.put(_ID_PG, Fields.IdPg);
        args.put(_SENHA, Fields.Senha);
        args.put(_BKP_CONVERSA, Fields.BkpConversa);
        return args;
    }

    public void getFields(Cursor c) {
        if (!c.isAfterLast() && !c.isBeforeFirst()) {
            int i = 0;
            Fields.Id = c.getInt(i++);
            Fields.IdPg = c.getString(i++);
            Fields.Senha = c.getString(i++);
            Fields.BkpConversa = c.getString(i++);
        }
    }

    public long insert() {
        return db.insert(DATABASE_TABLE, null, getArgs());
    }

    public boolean update(long Id) {
        return db.update(DATABASE_TABLE, getArgs(), _ID + "=" + Id, null) > 0;
    }

    public boolean delete(long Id) {
        return db.delete(DATABASE_TABLE, _ID + "=" + Id, null) > 0;
    }

    public Cursor getAll() {
        return getWhere(null);
    }

    public Cursor getId(long Id) throws SQLException {
        Cursor c = getWhere(_ID + "=" + Id);
        return c;
    }

    public Cursor getWhere(String Where) throws SQLException {
        Cursor c =
                db.query(DATABASE_TABLE, new String[]{
                                _ID,
                                _ID_PG,
                                _SENHA,
                                _BKP_CONVERSA},
                        Where,
                        null,
                        null,
                        null,
                        _ID);

        if (c != null) {
            c.moveToFirst();
            getFields(c);
        }
        return c;
    }
}
