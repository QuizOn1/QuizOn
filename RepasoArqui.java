package com.example.informaticapp;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.List;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class RepasoArqui extends AppCompatActivity {

    private ImageView BotonAtras;
    private Button BotonIniciarQuiz;


    // Define la clase Tema como una clase interna estática para facilitar la serialización.
    public static class Tema {
        private String titulo;
        private String contenido;


        public Tema() {
            // Constructor vacío requerido para Firebase
        }


        public Tema(String titulo, String contenido) {
            this.titulo = titulo;
            this.contenido = contenido;
        }


        public String getTitulo() {
            return titulo;
        }


        public void setTitulo(String titulo) {
            this.titulo = titulo;
        }


        public String getContenido() {
            return contenido;
        }


        public void setContenido(String contenido) {
            this.contenido = contenido;
        }
    }


    // Adaptador para RecyclerView
    public static class TemaAdapter extends RecyclerView.Adapter<TemaAdapter.TemaViewHolder> {
        private List<Tema> listaTemas;


        public TemaAdapter(List<Tema> listaTemas) {
            this.listaTemas = listaTemas;
        }


        @NonNull
        @Override
        public TemaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tema, parent, false);
            return new TemaViewHolder(itemView);
        }


        @Override
        public void onBindViewHolder(@NonNull TemaViewHolder holder, int position) {
            Tema tema = listaTemas.get(position);
            holder.tituloTextView.setText(tema.getTitulo());
            holder.contenidoTextView.setText(tema.getContenido());
        }


        @Override
        public int getItemCount() {
            return listaTemas.size();
        }


        public static class TemaViewHolder extends RecyclerView.ViewHolder {
            public TextView tituloTextView;
            public TextView contenidoTextView;


            public TemaViewHolder(View view) {
                super(view);
                tituloTextView = view.findViewById(R.id.tituloTextView);
                contenidoTextView = view.findViewById(R.id.contenidoTextView);
            }
        }
    }


    private List<Tema> listaTemas = new ArrayList<>();
    private TemaAdapter temaAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repaso_arqui);


        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        temaAdapter = new TemaAdapter(listaTemas);
        recyclerView.setAdapter(temaAdapter);



        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("RepasoArqui");


        // Escucha cambios en la base de datos
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaTemas.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Utiliza la clase Tema para deserializar los datos
                    Tema tema = snapshot.getValue(Tema.class);
                    if (tema != null) {
                        listaTemas.add(tema);
                    }
                }
                temaAdapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error en la obtención de datos desde Firebase: " + databaseError.getMessage());
            }
        });

        BotonAtras= findViewById(R.id.imageAtras);

        BotonAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RepasoArqui.this, MenuRepaso.class);
                startActivity(intent);
            }

        });

        BotonIniciarQuiz= findViewById(R.id.startQuizBtn);

        BotonIniciarQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RepasoArqui.this, StartActivity.class);
                startActivity(intent);
            }

        });


    }
}


