package com.maga.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;

public class Registro extends AppCompatActivity {

    // Referencia a la base de datos de Firebase
    DatabaseReference quotesRef;
    // Variables para los elementos de la interfaz de usuario
    private TextView tengoCuenta;
    private EditText nombreEditText;
    private EditText mailEditText;
    private EditText passEditText;
    private EditText confpassEditText;
    private Button btnVolver, btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Vincula las vistas con los elementos del layout
        tengoCuenta = findViewById(R.id.Tengocuenta);
        nombreEditText = findViewById(R.id.nombre);
        mailEditText = findViewById(R.id.mail);
        passEditText = findViewById(R.id.contrasena);
        confpassEditText = findViewById(R.id.confirmarcontrasena);
        btnRegistrar = findViewById(R.id.btnregistro);
        btnVolver = findViewById(R.id.btnvolver);

        // Configura el click listener para el botón de registro
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtiene el texto de los campos de entrada
                String Nombres = nombreEditText.getText().toString();
                String Mails = mailEditText.getText().toString();
                String Contrasena = passEditText.getText().toString();
                String ConfContrasena = confpassEditText.getText().toString();

                // Verifica si los campos están vacíos o si los datos son válidos
                if (TextUtils.isEmpty(Nombres)) {
                    nombreEditText.setError("Nombre no puede estar vacío");
                    return;
                }
                if (TextUtils.isEmpty(Mails)) {
                    mailEditText.setError("Correo no puede estar vacío");
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(Mails).matches()) {
                    mailEditText.setError("Ingrese un correo válido");
                    return;
                }
                if (TextUtils.isEmpty(Contrasena)) {
                    passEditText.setError("Contraseña no puede estar vacía");
                    return;
                }
                if (TextUtils.isEmpty(ConfContrasena)) {
                    confpassEditText.setError("Confirmación de contraseña no puede estar vacía");
                    return;
                }
                if (!Contrasena.equals(ConfContrasena)) {
                    confpassEditText.setError("Las contraseñas no coinciden");
                    return;
                }

                // Agrega el usuario a la base de datos
                addQuoteToDB(Nombres, Mails, Contrasena);
            }
        });

        // Configura el click listener para el botón de volver
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Llama al método onBackPressed() para volver a la actividad anterior
            }
        });

        // Configura el click listener para el texto de "tengo cuenta"
        tengoCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registro.this, Login.class)); // Navega a la actividad de inicio de sesión
            }
        });
    }

    /**
     * Agrega el usuario a la base de datos de Firebase Authentication y Realtime Database.
     *
     * @param Nombres    El nombre del usuario.
     * @param Mails      El correo electrónico del usuario.
     * @param Contrasena La contraseña del usuario.
     */
    private void addQuoteToDB(String Nombres, String Mails, String Contrasena) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(Mails, Contrasena)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Si la creación del usuario es exitosa
                            FirebaseUser user = auth.getCurrentUser();
                            // Guarda información adicional del usuario en la base de datos
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference userRef = database.getReference("Usuarios").child(user.getUid());
                            HashMap<String, Object> userInfo = new HashMap<>();
                            userInfo.put("nombre", Nombres);
                            userInfo.put("mail", Mails);
                            // No es seguro almacenar la contraseña en la base de datos
                            // userInfo.put("contrasena", Contrasena);
                            userRef.setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Registro.this, "Usuario Creado y Guardado en la Base de Datos :)", Toast.LENGTH_SHORT).show();
                                        // Limpia los campos de entrada
                                        nombreEditText.getText().clear();
                                        mailEditText.getText().clear();
                                        passEditText.getText().clear();
                                        confpassEditText.getText().clear();
                                    } else {
                                        Toast.makeText(Registro.this, "Error al Guardar Usuario en la Base de Datos :(" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            // Si la creación del usuario falla, muestra un mensaje de error
                            Toast.makeText(Registro.this, "Error al Crear Usuario :(: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}