package com.example.mariai.meuslivros;

import android.provider.BaseColumns;

public final class LivroContrato {

    public LivroContrato() {
    }


    public static class LivroEntry implements BaseColumns {

        public static final String TABLE_NAME = "Livro";
        public static final String TITULO = "Titulo";
        public static final String AUTOR = "Autor";
        public static final String ANO = "Ano";
        public static final String NOTA = "Nota";


    }
}
