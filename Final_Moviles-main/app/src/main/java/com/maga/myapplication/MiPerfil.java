package com.maga.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Clase que representa la actividad de perfil del usuario, donde se pueden ver y editar detalles del perfil.
 */
public class MiPerfil extends AppCompatActivity {

    // Declaración de variables de la interfaz de usuario
    private TextView txtNombrePerfil, txtCorreoPerfil, txtContraseña;
    private ImageButton menubtn;
    private EditText etContrasena;
    private Button EditarContrasena;

    // Referencia a la base de datos de Firebase y autenticación
    private DatabaseReference Usuarios;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user; // Variable de instancia para el usuario actual

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Configuración para hacer la actividad de borde a borde
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mi_perfil);

        // Configuración de los insets del sistema para vistas adecuadas
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialización de vistas
        txtNombrePerfil = findViewById(R.id.txtNombrePerfil);
        txtCorreoPerfil = findViewById(R.id.txtCorreoPerfil);
        txtContraseña = findViewById(R.id.txtContrasena);
        etContrasena = findViewById(R.id.etContrasena);
        EditarContrasena = findViewById(R.id.btnRestablecerContrasena);
        menubtn = findViewById(R.id.btnMenu);

        // Inicialización de Firebase Auth y DatabaseReference
        firebaseAuth = FirebaseAuth.getInstance();
        Usuarios = FirebaseDatabase.getInstance().getReference("Usuarios");
        user = firebaseAuth.getCurrentUser(); // Obtener el usuario actual

        // Cargar datos del usuario si está autenticado
        if (user != null) {
            CargarDatos();
        } else {
            // Manejar el caso en que el usuario no esté autenticado
        }

        // Configuración del botón de restablecimiento de contraseña
        EditarContrasena.setOnClickListener(v -> {
            String email = etContrasena.getText().toString(); // Asumiendo que el usuario ingresa su correo electrónico en etContrasena
            if (!email.isEmpty()) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(MiPerfil.this, "Se ha enviado un correo electrónico para restablecer tu contraseña.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(MiPerfil.this, "Error al enviar el correo electrónico de restablecimiento de contraseña.", Toast.LENGTH_LONG).show();
                            }
                        });
            } else {
                Toast.makeText(MiPerfil.this, "Por favor, ingresa tu correo electrónico.", Toast.LENGTH_SHORT).show();
            }
        });

        // Configuración del botón de menú
        menubtn.setOnClickListener(v -> ShowMenu());
    }

    /**
     * Muestra un menú emergente con opciones para agregar, ver, buscar notas y cerrar sesión.
     */
    public void ShowMenu() {
        PopupMenu popupMenu = new PopupMenu(MiPerfil.this, menubtn);
        popupMenu.getMenu().add("Agregar Nota");
        popupMenu.getMenu().add("Ver Mis Notas");
        popupMenu.getMenu().add("Buscar Nota");
        popupMenu.getMenu().add("Cerrar Sesion");
        popupMenu.show();

        // Configuración de acciones para los ítems del menú
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getTitle().toString()) {
                case "Cerrar Sesion":
                    firebaseAuth.signOut();
                    startActivity(new Intent(MiPerfil.this, Registro.class));
                    finish();
                    return true;
                case "Agregar Nota":
                    startActivity(new Intent(MiPerfil.this, Agregar_Nota.class));
                    return true;
                case "Ver Mis Notas":
                    startActivity(new Intent(MiPerfil.this, Ver_Nota.class));
                    return true;
                case "Buscar Nota":
                    startActivity(new Intent(MiPerfil.this, filtrar_Nota.class));
                    return true;
                default:
                    return false;
            }
        });
    }

    /**
     * Carga los datos del usuario desde la base de datos y los muestra en la interfaz de usuario.
     */
    private void CargarDatos() {
        Usuarios.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String nombres = snapshot.child("nombre").getValue(String.class);
                    String correo = snapshot.child("mail").getValue(String.class);
                    String contrasena = snapshot.child("contrasena").getValue(String.class);

                    if (nombres != null && correo != null && contrasena != null) {
                        txtNombrePerfil.setText("Usuario: " + nombres);
                        txtCorreoPerfil.setText(correo);
                        txtContraseña.setText("Contraseña: " + contrasena);
                        etContrasena.setText(correo);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar el error
            }
        });
    }
}