package com.example.mariai.meuslivros;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class BancoHelper extends SQLiteOpenHelper {

    private static final String TAG = "sql";
    private static final String TEXT_TYPE = " TEXT";
    private static final String NUMBER_TYPE = " INTEGER";
    private static final String VIRGULA = ",";
    private static final String DATABASE_NAME = "banco_meuslivros.sqlite";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE = ("CREATE TABLE " + LivroContrato.LivroEntry.TABLE_NAME + "(" +
            LivroContrato.LivroEntry._ID + NUMBER_TYPE + " PRIMARY KEY " + VIRGULA +
            LivroContrato.LivroEntry.TITULO + TEXT_TYPE + VIRGULA +
            LivroContrato.LivroEntry.AUTOR + TEXT_TYPE + VIRGULA +
            LivroContrato.LivroEntry.ANO + NUMBER_TYPE + VIRGULA +
            LivroContrato.LivroEntry.NOTA + NUMBER_TYPE + ");"
    );


    private static final String SQL_DROP_TABLE = ("DROP TABLE " + LivroContrato.LivroEntry.TABLE_NAME + ";");


    public BancoHelper(Context context) {
        // context, nome do banco, factory, versão
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Não foi possível acessar o banco, criando um novo...");

        db.execSQL(SQL_CREATE_TABLE);

        Log.d(TAG, "Banco criado com sucesso.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Caso mude a versãoo do banco de dados, podemos executar um SQL aqui
        if (oldVersion != newVersion) {
            // Execute o script para atualizar a versão...
            Log.d(TAG, "Foi detectada uma nova versão do banco, aqui deverão ser executados os scripts de update.");
            db.execSQL(SQL_DROP_TABLE);
            this.onCreate(db);
        }

    }


    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Execute o script para atualizar a versão...
            Log.d(TAG, "Foi detectada uma nova versão do banco, aqui deverão ser executados os scripts de downgrade.");
            db.execSQL(SQL_DROP_TABLE);
            this.onCreate(db);
        }
    }

    // Insere um novo livro, ou atualiza se já existe.
    public long save(Livro livro) {
        long id = livro.getId();
        SQLiteDatabase db = getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(LivroContrato.LivroEntry.TITULO, livro.getTitulo());
            values.put(LivroContrato.LivroEntry.AUTOR, livro.getAutor());
            values.put(LivroContrato.LivroEntry.ANO, livro.getAno());
            values.put(LivroContrato.LivroEntry.NOTA, livro.getNota());

            if (id != 0) {

                String selection = LivroContrato.LivroEntry._ID + "= ?";
                String[] whereArgs = new String[]{String.valueOf(id)};

                // update livro set values = ... where _id=?
                int count = db.update(LivroContrato.LivroEntry.TABLE_NAME, values, selection, whereArgs);
                Log.i(TAG, "Atualizou id [" + id + "] no banco.");
                return count;

            } else {
                // insert into livro values (...)-------------------alterei de "" para null
                id = db.insert(LivroContrato.LivroEntry.TABLE_NAME, null, values);
                Log.i(TAG, "Inseriu id [" + id + "] no banco.");
                return id;
            }
        } finally {
            db.close();
        }
    }


    // Consulta a lista com todos os livros
    public List<Livro> findAll() {
        SQLiteDatabase db = getReadableDatabase();
        try {
            // select * from carro
            Cursor c = db.query(LivroContrato.LivroEntry.TABLE_NAME, null, null, null, null, null, null, null);
            Log.i(TAG, "Listou todos os registros");
            return toList(c);
        } finally {
            db.close();
        }
    }


    // Lê o cursor e cria a lista de livros
    private List<Livro> toList(Cursor c) {

        List<Livro> livros = new ArrayList<>();

        if (c.moveToFirst()) {
            do {
                Livro livro = new Livro();
                livros.add(livro);

                // recupera os atributos de livro
                livro.setId(c.getInt(c.getColumnIndex(LivroContrato.LivroEntry._ID)));
                livro.setTitulo(c.getString(c.getColumnIndex(LivroContrato.LivroEntry.TITULO)));
                livro.setAutor(c.getString(c.getColumnIndex(LivroContrato.LivroEntry.AUTOR)));
                livro.setAno(c.getInt(c.getColumnIndex(LivroContrato.LivroEntry.ANO)));
                livro.setNota(c.getFloat(c.getColumnIndex(LivroContrato.LivroEntry.NOTA)));

            } while (c.moveToNext());
        }

        return livros;
    }

    // Executa um SQL
    public void execSQL(String sql) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.execSQL(sql);
        } finally {
            db.close();
        }
    }

    // Executa um SQL
    public void execSQL(String sql, Object[] args) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.execSQL(sql, args);
        } finally {
            db.close();
        }
    }
}
