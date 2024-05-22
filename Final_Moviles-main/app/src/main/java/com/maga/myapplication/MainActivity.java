package com.maga.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    // Declaración de botones
    Button btn_Login, btn_Registro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Habilita EdgeToEdge para ocupar toda la pantalla
        EdgeToEdge.enable(this);
        
        // Establece el diseño de la actividad
        setContentView(R.layout.activity_main);
        
        // Aplica los insets del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialización de botones
        btn_Login = findViewById(R.id.btnlogin);
        btn_Registro = findViewById(R.id.btnregistro);

        // Configuración de listeners para los botones
        btn_Login.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, Login.class)));
        btn_Registro.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, Registro.class)));
    }
}
