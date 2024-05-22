package com.maga.myapplication;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;

public class Ver_Nota extends AppCompatActivity {

    // Declaración de variables de instancia
    RecyclerView recyclerView;
    ExtendedFloatingActionButton AddNota, BtnVolver, BtnBuscar;
    ImageButton menubtn;
    NotaAdapter notaAdapter;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ver_nota);

        // Ajuste de márgenes para adaptarse a las barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialización de los elementos UI
        AddNota = findViewById(R.id.btnAdd);
        recyclerView = findViewById(R.id.recycler_view_notas);
        menubtn = findViewById(R.id.btnMenu);
        BtnVolver = findViewById(R.id.btnVolver);
        BtnBuscar = findViewById(R.id.btnBuscar);

        // Inicialización de Firebase Authentication y obtención del usuario actual
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        // Configuración de listeners para los botones
        BtnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegación a la actividad MenuPrincipal
                startActivity(new Intent(Ver_Nota.this, MenuPrincipal.class));
            }
        });
        AddNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegación a la actividad Agregar_Nota
                startActivity(new Intent(Ver_Nota.this, Agregar_Nota.class));
            }
        });
        BtnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegación a la actividad filtrar_Nota
                startActivity(new Intent(Ver_Nota.this, filtrar_Nota.class));
            }
        });

        // Configuración del botón de menú
        menubtn.setOnClickListener((v) -> ShowMenu());

        // Configuración del RecyclerView
        configurarRecyclerView();
    }

    /**
     * Configura el RecyclerView para mostrar las notas en orden descendente por marca de tiempo.
     */
    public void configurarRecyclerView() {
        // Obtención de la referencia de la colección desde una clase de utilidad
        Utilidad.ReferenciaDeColeccion referenciaDeColeccion = Utilidad.getReferenciaDeColeccion();
        CollectionReference collectionRef = referenciaDeColeccion.collectionReference;

        // Consulta para ordenar las notas por marca de tiempo en orden descendente
        Query query = collectionRef.orderBy("timestamp", Query.Direction.DESCENDING);

        // Configuración de opciones para el adaptador FirestoreRecyclerOptions
        FirestoreRecyclerOptions<Nota> options = new FirestoreRecyclerOptions.Builder<Nota>()
                .setQuery(query, Nota.class).build();

        // Inicialización del adaptador de notas con las opciones configuradas
        notaAdapter = new NotaAdapter(options, this);

        // Configuración del RecyclerView con un LinearLayoutManager y el adaptador
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(notaAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Inicia la escucha de cambios en los datos de Firestore cuando la actividad empieza
        if (notaAdapter != null) {
            notaAdapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Detiene la escucha de cambios en los datos de Firestore cuando la actividad se detiene
        if (notaAdapter != null) {
            notaAdapter.stopListening();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // Notifica al adaptador de que los datos han cambiado cuando la actividad se reinicia
        if (notaAdapter != null) {
            notaAdapter.notifyDataSetChanged();
        }
    }
    /**
     * Muestra un menú emergente con opciones.
     */
    public void ShowMenu() {
        PopupMenu popupMenu = new PopupMenu(Ver_Nota.this, menubtn);
        popupMenu.getMenu().add("Mi Perfil");
        popupMenu.getMenu().add("Cerrar Sesion");
        popupMenu.show();

        // Maneja la selección de elementos del menú
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle().equals("Cerrar Sesion")) {
                    // Cierra sesión y vuelve a la actividad principal
                    SalirAplicacion();
                } else if (item.getTitle().equals("Mi Perfil")) {
                    // Navega a la actividad MiPerfil
                    startActivity(new Intent(Ver_Nota.this, MiPerfil.class));
                }
                return false;
            }
        });
    }
    /**
     * Cierra la sesión del usuario y navega a la actividad principal.
     */
    private void SalirAplicacion() {
        firebaseAuth.signOut();
        startActivity(new Intent(Ver_Nota.this, MainActivity.class));
        finish();
    }
}
