package com.maga.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    private EditText mailEditText;
    private EditText passEditText;
    private TextView newusuario;
    private Button btnLogin, btnVolver;

    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicialización de Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        // Enlazar las vistas
        mailEditText = findViewById(R.id.email);
        passEditText = findViewById(R.id.password);
        btnLogin = findViewById(R.id.login);
        btnVolver = findViewById(R.id.btnvolver);
        newusuario = findViewById(R.id.usuarioNuevo);

        progressDialog = new ProgressDialog(Login.this);
        progressDialog.setTitle("Espere Por Favor");
        progressDialog.setCanceledOnTouchOutside(false);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtener los valores ingresados por el usuario
                String email = mailEditText.getText().toString();
                String password = passEditText.getText().toString();
                
                // Validar el correo electrónico y la contraseña
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(Login.this, "Correo Inválido", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Login.this, "Ingrese una Contraseña", Toast.LENGTH_SHORT).show();
                } else {
                    // Iniciar sesión
                    loginUser(email, password);
                }
            }
        });

        newusuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirigir a la actividad de registro
                startActivity(new Intent(Login.this, Registro.class));
            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Volver a la actividad principal
                startActivity(new Intent(Login.this, MainActivity.class));
            }
        });
    }

    private void loginUser(String email, String password) {
        progressDialog.setMessage("Iniciando Sesión...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            // Inicio de sesión exitoso, redirigir al menú principal
                            startActivity(new Intent(Login.this, MenuPrincipal.class));
                            finish();
                        } else {
                            // Error en el inicio de sesión
                            Toast.makeText(Login.this, "Verifique el Correo y la Contraseña", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Error en el inicio de sesión
                Toast.makeText(Login.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
}