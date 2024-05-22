package com.maga.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
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
 * Clase que representa la actividad del menú principal de la aplicación.
 * Aquí el usuario puede navegar a diferentes secciones como agregar, listar, buscar notas y ver su perfil.
 */
public class MenuPrincipal extends AppCompatActivity {

    // Declaración de variables de la interfaz de usuario
    private Button CerrarSesion;
    private ImageButton btnmenu;
    private LinearLayout CrearNota, ListarNota, BuscarNota, MiPerfil;
    private TextView NombrePrincipal, CorreoPrincipal;
    private ProgressBar progressBarDatos;

    // Referencia a la base de datos de Firebase y autenticación
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DatabaseReference Usuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_principal);

        // Configuración de los insets del sistema para vistas adecuadas
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Configuración de la ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Agenda");
        }

        // Inicialización de vistas
        NombrePrincipal = findViewById(R.id.txtnombrePrincipal);
        CorreoPrincipal = findViewById(R.id.txtcorreoPrincipal);
        progressBarDatos = findViewById(R.id.progressdatos);
        CerrarSesion = findViewById(R.id.cerrarSesion);
        btnmenu = findViewById(R.id.btnmenu);
        CrearNota = findViewById(R.id.linearLayoutAgregarNota);
        ListarNota = findViewById(R.id.linearLayoutListarNota);
        BuscarNota = findViewById(R.id.linearLayoutBuscarNota);
        MiPerfil = findViewById(R.id.linearLayoutPerfil);

        // Inicialización de Firebase Auth y DatabaseReference
        firebaseAuth = FirebaseAuth.getInstance();
        Usuarios = FirebaseDatabase.getInstance().getReference("Usuarios");
        user = firebaseAuth.getCurrentUser();

        // Configuración de listeners para los botones
        CrearNota.setOnClickListener(v -> startActivity(new Intent(MenuPrincipal.this, Agregar_Nota.class)));
        ListarNota.setOnClickListener(v -> startActivity(new Intent(MenuPrincipal.this, Ver_Nota.class)));
        BuscarNota.setOnClickListener(v -> startActivity(new Intent(MenuPrincipal.this, filtrar_Nota.class)));
        MiPerfil.setOnClickListener(v -> startActivity(new Intent(MenuPrincipal.this, com.maga.myapplication.MiPerfil.class)));
        CerrarSesion.setOnClickListener(v -> SalirAplicacion());
        btnmenu.setOnClickListener(v -> ShowMenu());
    }

    /**
     * Muestra un menú emergente con la opción de cerrar sesión.
     */
    public void ShowMenu() {
        PopupMenu popupMenu = new PopupMenu(MenuPrincipal.this, btnmenu);
        popupMenu.getMenu().add("Cerrar Sesion");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(item -> {
            if ("Cerrar Sesion".equals(item.getTitle())) {
                firebaseAuth.signOut();
                startActivity(new Intent(MenuPrincipal.this, MainActivity.class));
                finish();
            }
            return false;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        ComprobarInicioSesion();
    }

    /**
     * Comprueba si el usuario ha iniciado sesión.
     * Si no ha iniciado sesión, redirige al usuario a la actividad principal.
     */
    private void ComprobarInicioSesion() {
        if (user != null) {
            CargarDatos();
        } else {
            startActivity(new Intent(MenuPrincipal.this, MainActivity.class));
            finish();
        }
    }

    /**
     * Carga los datos del usuario desde la base de datos y los muestra en la interfaz de usuario.
     */
    private void CargarDatos() {
        Usuarios.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Oculta el ProgressBar y muestra los datos
                    progressBarDatos.setVisibility(View.GONE);
                    NombrePrincipal.setVisibility(View.VISIBLE);
                    // CorreoPrincipal.setVisibility(View.VISIBLE);

                    // Obtiene los datos del usuario desde el snapshot
                    String nombres = snapshot.child("nombre").getValue(String.class);

                    // Asigna los datos obtenidos a los TextViews
                    NombrePrincipal.setText(nombres);
                    // CorreoPrincipal.setText(correo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Maneja el error
            }
        });
    }

    /**
     * Cierra la sesión del usuario y redirige a la actividad principal.
     */
    private void SalirAplicacion() {
        firebaseAuth.signOut();
        startActivity(new Intent(MenuPrincipal.this, MainActivity.class));
        Toast.makeText(this, "Cerraste sesion", Toast.LENGTH_SHORT).show();
    }
}