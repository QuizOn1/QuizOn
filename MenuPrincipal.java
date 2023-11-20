package com.example.informaticapp;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.TextView;

public class MenuPrincipal extends AppCompatActivity {

    private ImageView imageRepaso;
    private ImageView imagenAtras;
    private ImageView imageQuiz;
    private TextView Saludo;
    private Button btnCerrarSesion;

    private Button btnAsesoria;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        // Inicia Firebase
        mAuth = FirebaseAuth.getInstance();

        imageRepaso = findViewById(R.id.imageView);
        imageQuiz = findViewById(R.id.imageView1);
        Saludo = findViewById(R.id.textSaludo);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion); // Obtener referencias y id de xml
        btnAsesoria = findViewById(R.id.btnAsesoria);

        // El clic del botón de cierre de sesión
        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Diálogo de confirmación
                AlertDialog.Builder builder = new AlertDialog.Builder(MenuPrincipal.this);
                builder.setTitle("Confirmación");
                builder.setMessage("¿Estás seguro de que quieres cerrar la sesión?");
                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Cerrar la sesión actual
                        mAuth.signOut();

                        // Redirigir al usuario a la pantalla de inicio de sesión
                        Intent intent = new Intent(MenuPrincipal.this, MainActivity.class);
                        startActivity(intent);
                        finish(); // Cierra la actividad actual
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // No hacer nada si el usuario elige "No"
                    }
                });

                // Mostrar el diálogo
                builder.show();
            }
        });

        imageQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuPrincipal.this, MenuQuiz.class);
                startActivity(intent);
            }
        });
        imageRepaso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuPrincipal.this, MenuRepaso.class);
                startActivity(intent);
            }
        });
        btnAsesoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuPrincipal.this, asesoria.class);
                startActivity(intent);
            }
        });


        // Llamar a la función para obtener y mostrar el nombre del usuario
        obtenerYMostrarNombreUsuario();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Llamar a la función para obtener y mostrar el nombre del usuario
        obtenerYMostrarNombreUsuario();
    }

    private void obtenerYMostrarNombreUsuario() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userID = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("usuarios").child(userID).child("nombre");
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String nombreUsuario = dataSnapshot.getValue(String.class);
                        Saludo.setText("Hola " + nombreUsuario);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Manejo de errores
                }
            });
        }
    }
}