package com.example.informaticapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.text.InputType;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvGoToRegister;
    private FirebaseAuth mAuth;
    private ImageView togglePassword;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.textoCorreo);
        etPassword = findViewById(R.id.textoContrasena);
        btnLogin = findViewById(R.id.inicio1);
        tvGoToRegister = findViewById(R.id.tv_go_to_register);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();



                if (password.length() < 8) {
                    Toast.makeText(MainActivity.this, "La contraseña debe tener al menos 8 caracteres", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Verificar si los campos están vacíos
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Llenar los campos completos por favor", Toast.LENGTH_SHORT).show();
                } else {
                    // Iniciar sesión del usuario con Firebase Authentication
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Inicio de sesión exitoso
                                        Toast.makeText(MainActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                                        // Redirigir a la actividad principal del usuario
                                        Intent intent = new Intent(MainActivity.this, MenuPrincipal.class);
                                        startActivity(intent);
                                        finish();

                                    } else {
                                        // Error en el inicio de sesión
                                        Toast.makeText(MainActivity.this, "Error en el inicio de sesión", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        tvGoToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Redirigir a la actividad de registro
                Intent intent = new Intent(MainActivity.this, Registro.class);
                startActivity(intent);
            }
        });
    }
}