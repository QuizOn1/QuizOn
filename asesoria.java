package com.example.informaticapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class asesoria extends AppCompatActivity {

    private EditText editTextNombre;
    private EditText editTextCorreo;
    // Otros EditText y variables necesarias
    private CalendarView calendarView;

    private EditText editTextMateria;

    private EditText editTextTemaEspecifico;
    private long fechaSeleccionada; // Variable de instancia para almacenar la fecha seleccionada

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asesoria);

        editTextNombre = findViewById(R.id.editTextNombre);
        editTextCorreo = findViewById(R.id.editTextCorreo);
        calendarView = findViewById(R.id.calendarView);

        // Agrega el listener para capturar la fecha seleccionada
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                Calendar selectedCalendar = Calendar.getInstance();
                selectedCalendar.set(year, month, dayOfMonth);
                fechaSeleccionada = selectedCalendar.getTimeInMillis();
            }
        });

        // Inicializa otros EditText y variables
        calendarView.setDate(System.currentTimeMillis());

        Button btnEnviar = findViewById(R.id.btnEnviar);
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Aquí puedes obtener los valores de los EditText y realizar la lógica de envío
                agendarAsesoria();
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference("asesorias");
    }

    private void agendarAsesoria() {
        // Obtén la información de la asesoría
        String nombre = editTextNombre.getText().toString();
        String correo = editTextCorreo.getText().toString();
        String materia = editTextMateria.getText().toString();
        String tema = editTextTemaEspecifico.getText().toString();

        // Verifica que no esten vacios
        if (!TextUtils.isEmpty(nombre) && !TextUtils.isEmpty(correo) && !TextUtils.isEmpty(materia) && !TextUtils.isEmpty(tema)) {

            // Convierte la fecha a un formato legible
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            String fechaFormateada = dateFormat.format(new Date(fechaSeleccionada));

            // Crea un objeto Asesoria para almacenar en la base de datos
            AsesoriaItem asesoria = new AsesoriaItem(nombre, correo, materia, tema, fechaFormateada);

            // Genera una nueva clave única para cada asesoría identificacion unica
            String nuevaClave = databaseReference.push().getKey();

            // Guarda la asesoría en la base de datos bajo la clave generada
            databaseReference.child(nuevaClave).setValue(asesoria)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Diálogo de confirmación
                                AlertDialog.Builder builder = new AlertDialog.Builder(asesoria.this);
                                builder.setTitle("Confirmación");
                                builder.setMessage("¿Acepta la asesoría? Nos pondremos en contacto a tu correo en menos de 24 horas");
                                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // Redirigir al usuario a la pantalla de inicio de sesión
                                        String mensajeExito = "Asesoría agendada con éxito.\nNombre: " + nombre +
                                                "\nCorreo: " + correo + "\nFecha: " + fechaFormateada;
                                        Toast.makeText(asesoria.this, mensajeExito, Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(asesoria.this, MainActivity.class);
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

                                builder.show(); // Mostrar el diálogo
                            } else {
                                // Manejar errores al guardar en Firebase
                                Toast.makeText(asesoria.this, "Error al agendar la asesoría.", Toast.LENGTH_SHORT).show();
                                Log.e("Firebase", "Error al escribir datos en la base de datos", task.getException());
                            }
                        }
                    });
        } else {
            // Mostrar mensaje de error porque alguno de los campos está vacío
            Toast.makeText(asesoria.this, "Ingrese todos los campos requeridos.", Toast.LENGTH_SHORT).show();
        }
    }
}

