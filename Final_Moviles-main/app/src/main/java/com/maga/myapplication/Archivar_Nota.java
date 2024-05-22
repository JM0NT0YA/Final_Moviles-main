package com.maga.myapplication;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.maga.myapplication.R;

public class Archivar_Nota extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Habilita el ajuste de borde a borde para la actividad
        EdgeToEdge.enable(this);

        // Establece el diseño de la actividad
        setContentView(R.layout.activity_archivar_nota);

        // Aplica el ajuste de los márgenes para la vista principal
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            // Obtiene los márgenes del sistema y los aplica a la vista principal
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets; // Devuelve los márgenes actualizados
        });
    }
}
