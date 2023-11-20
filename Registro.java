package com.example.informaticapp;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Registro extends AppCompatActivity {

    private EditText etEmail, etNombre, etContraseña,etOrganizacion,etApellido;
    private Button btnRegistrar;
    private TextView tvGoToLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Inicializar FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.editTextEmail);
        etNombre = findViewById(R.id.editTextNombre);
        etApellido = findViewById(R.id.editTextApellidos);
        etOrganizacion=findViewById(R.id.editTextOrg);
        etContraseña = findViewById(R.id.editTextContraseña);
        btnRegistrar = findViewById(R.id.buttonRegistrar);
        tvGoToLogin = findViewById(R.id.tv_go_to_login);


        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString();
                String password = etContraseña.getText().toString();
                String nombre = etNombre.getText().toString();
                String organizacion = etOrganizacion.getText().toString();
                String apellido = etApellido.getText().toString();


                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(nombre) || TextUtils.isEmpty(apellido)|| TextUtils.isEmpty(organizacion)) {
                    Toast.makeText(Registro.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 8) {
                    Toast.makeText(Registro.this, "La contraseña debe tener al menos 8 caracteres", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Registrar el usuario con Firebase Authentication
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Registro exitoso

                                    // Obtener el ID del usuario recién registrado
                                    String userId = mAuth.getCurrentUser().getUid();

                                    // Obtener una referencia a la base de datos de Firebase Realtime
                                    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

                                    // Registrar datos adicionales en la base de datos
                                    databaseRef.child("usuarios").child(userId).child("nombre").setValue(etNombre.getText().toString());
                                    databaseRef.child("usuarios").child(userId).child("apellido").setValue(etApellido.getText().toString());
                                    databaseRef.child("usuarios").child(userId).child("organizacion").setValue(etOrganizacion.getText().toString());
                                    databaseRef.child("usuarios").child(userId).child("email").setValue(email);

                                    Toast.makeText(Registro.this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Error en el registro
                                    Toast.makeText(Registro.this, "Error al registrar usuario", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        tvGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Registro.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
