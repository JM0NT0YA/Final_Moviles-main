package com.maga.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class filtrar_Nota extends AppCompatActivity {

    ImageButton BtnMenu;
    EditText BuscarTexto;
    FirebaseAuth firebaseAuth;
    NotaAdapter notaAdapter;
    RecyclerView recyclerView;
    ExtendedFloatingActionButton btnVolver, btnAgregar, btnPerfil;
    CollectionReference collectionRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_filtrar_nota);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        BtnMenu = findViewById(R.id.btnMenu);
        btnVolver = findViewById(R.id.btnVolver);
        btnAgregar = findViewById(R.id.add_note);
        btnPerfil = findViewById(R.id.perfilbtn);
        BuscarTexto = findViewById(R.id.ETbuscar);
        recyclerView = findViewById(R.id.recycler_view_filtrar);

        firebaseAuth = FirebaseAuth.getInstance();


        Utilidad.ReferenciaDeColeccion referenciaDeColeccion = Utilidad.getReferenciaDeColeccion();
        collectionRef = referenciaDeColeccion.collectionReference; // Asegúrate de que collectionRef esté inicializado antes de llamar a configurarRecyclerView()

        configurarRecyclerView();

        BuscarTexto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterNotes(s.toString()); // Llama a la función de filtro con el texto ingresado
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        BtnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowMenu();
            }
        });
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(filtrar_Nota.this,MenuPrincipal.class));
            }
        });
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(filtrar_Nota.this, Agregar_Nota.class));
            }
        });
        btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(filtrar_Nota.this, MiPerfil.class));
            }
        });

    }
    private void filterNotes(String query) {

        Query queryRef = collectionRef.whereEqualTo("titulo", query).orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Nota> options = new FirestoreRecyclerOptions.Builder<Nota>()
                .setQuery(queryRef, Nota.class)
                .build();

        notaAdapter = new NotaAdapter(options, this);
        recyclerView.setAdapter(notaAdapter);
        notaAdapter.startListening();
    }

    public void configurarRecyclerView() {
        // Tu lógica para mostrar el menú
        // Elimina la redeclaración de collectionRef aquí
        Query query = collectionRef.orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Nota> options = new FirestoreRecyclerOptions.Builder<Nota>()
                .setQuery(query, Nota.class).build();
        notaAdapter = new NotaAdapter(options, this); // Inicializa notaAdapter con los datos reales
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(notaAdapter);
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (notaAdapter!= null) {
            notaAdapter.startListening();
        }
    }
    public void ShowMenu(){
        PopupMenu popupMenu = new PopupMenu(filtrar_Nota.this,BtnMenu);
        popupMenu.getMenu().add("Agregar Nota");
        popupMenu.getMenu().add("Ver Mis Notas");
        popupMenu.getMenu().add("Mi Perfil");
        popupMenu.getMenu().add("Cerrar Sesion");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle()=="Cerrar Sesion"){
                    firebaseAuth.getInstance().signOut();
                    startActivity(new Intent(filtrar_Nota.this, MainActivity.class));
                    finish();
                } else if (item.getTitle()=="Agregar Nota") {
                    startActivity(new Intent(filtrar_Nota.this, Agregar_Nota.class));
                } else if (item.getTitle()=="Ver Mis Notas") {
                    startActivity(new Intent(filtrar_Nota.this, Ver_Nota.class));
                } else if (item.getTitle()=="Mi Perfil") {
                    startActivity(new Intent(filtrar_Nota.this, MiPerfil.class));
                }
                return false;
            }
        });
    }
}