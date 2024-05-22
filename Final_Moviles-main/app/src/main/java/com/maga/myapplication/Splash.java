package com.maga.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Splash extends AppCompatActivity {

    // Variable para la autenticación de Firebase
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Habilita el modo EdgeToEdge
        setContentView(R.layout.activity_splash);

        // Configura el listener para los insets del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializa la autenticación de Firebase
        firebaseAuth = FirebaseAuth.getInstance();

        // Agrega el retraso para iniciar la siguiente actividad
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Verifica el estado del usuario de Firebase
                VerificaUsuario();
            }
        }, 2000); // Retraso de 2 segundos
    }

    /**
     * Verifica el estado de autenticación del usuario.
     * Si el usuario no está autenticado, se redirige a la actividad principal (MainActivity).
     * Si el usuario está autenticado, se redirige a la actividad del menú principal (MenuPrincipal).
     */
    private void VerificaUsuario() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            // El usuario no está autenticado, redirige a MainActivity
            startActivity(new Intent(Splash.this, MainActivity.class));
            finish();
        } else {
            // El usuario está autenticado, redirige a MenuPrincipal
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(Splash.this, MenuPrincipal.class));
                    finish();
                }
            }, 0); // No hay retraso adicional
        }
    }
}