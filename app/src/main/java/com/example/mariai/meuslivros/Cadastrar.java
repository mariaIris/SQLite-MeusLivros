package com.example.mariai.meuslivros;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

public class Cadastrar extends AppCompatActivity {

    EditText titulo;
    EditText autor;
    EditText ano;
    RatingBar nota;
    BancoHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

        db = new BancoHelper(this);

        Button CadastrarLivro = findViewById(R.id.cadastrarLivro);
        Button Cancelar = findViewById(R.id.cancelar);
        titulo = findViewById(R.id.titulo);
        autor = findViewById(R.id.autor);
        ano = findViewById(R.id.ano);
        nota = findViewById(R.id.nota);

        CadastrarLivro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                float avaliacao = nota.getRating();
                int lancamento = Integer.parseInt(ano.getText().toString());

                Livro livro = new Livro(titulo.getText().toString(), autor.getText().toString(), lancamento, avaliacao);
                db.save(livro);

                Log.i("CADASTRO", "Nota:" + avaliacao);

                setResult(RESULT_OK);
                finish();
            }
        });

        Cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setResult(RESULT_CANCELED);
                finish();
            }
        });


    }
}
