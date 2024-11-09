package dijairdev.com.br.dao.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    public DatabaseOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String scriptCreate = "create table Contato (" +
                "id integer primary key autoincrement," +
                "nome text not null," +
                "endereco text," +
                "cep text," +
                "email text, " +
                "telefone text, " +
                "foto blob);";

        db.execSQL(scriptCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // usar para mudança de versão no DB
    }
}
