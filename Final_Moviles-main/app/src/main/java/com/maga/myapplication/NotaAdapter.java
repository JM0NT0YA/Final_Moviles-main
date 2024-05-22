package com.maga.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;

public class NotaAdapter extends FirestoreRecyclerAdapter<Nota, NotaAdapter.NotaViewHolder> {
    private Context context;

    /**
     * Constructor de NotaAdapter.
     * @param options Opciones para configurar el adaptador FirestoreRecyclerAdapter.
     * @param context Contexto de la aplicación para iniciar actividades.
     */
    public NotaAdapter(@NonNull FirestoreRecyclerOptions<Nota> options, Context context) {
        super(options);
        this.context = context;
    }

    /**
     * Vincula los datos de la nota a las vistas en el ViewHolder.
     * @param notaViewHolder El ViewHolder que debe ser actualizado con el contenido de la nota.
     * @param position La posición del elemento dentro del adaptador.
     * @param nota La instancia de la nota que contiene los datos.
     */
    @Override
    protected void onBindViewHolder(@NonNull NotaViewHolder notaViewHolder, int position, @NonNull Nota nota) {
        // Asigna los datos de la nota a los TextView correspondientes
        notaViewHolder.TVTitulo.setText(nota.titulo);
        notaViewHolder.TVDescripcion.setText(nota.descripcion);
        notaViewHolder.TVFecha.setText(nota.fecha);
        notaViewHolder.TVTimestamp.setText(Utilidad.timestampToString(nota.timestamp));

        // Configura un listener para el evento de clic en el elemento de la lista
        notaViewHolder.itemView.setOnClickListener((v) -> {
            Intent intent = new Intent(context, Agregar_Nota.class);
            intent.putExtra("titulo", nota.titulo);
            intent.putExtra("descripcion", nota.descripcion);
            String docID = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId", docID);
            context.startActivity(intent);
        });
    }

    /**
     * Crea nuevos ViewHolder para el RecyclerView.
     * @param parent El ViewGroup al que se añadirá la nueva vista.
     * @param viewType El tipo de vista de la nueva vista.
     * @return Un nuevo NotaViewHolder que contiene la vista para cada elemento.
     */
    @NonNull
    @Override
    public NotaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla el layout para el elemento del RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_nota_item, parent, false);
        return new NotaViewHolder(view);
    }

    /**
     * ViewHolder para los elementos del RecyclerView que muestra las notas.
     */
    class NotaViewHolder extends RecyclerView.ViewHolder {
        TextView TVTitulo, TVDescripcion, TVFecha, TVTimestamp;

        /**
         * Constructor de NotaViewHolder.
         * @param itemView La vista que representa un elemento del RecyclerView.
         */
        public NotaViewHolder(@NonNull View itemView) {
            super(itemView);
            // Vincula los TextView con los elementos del layout
            TVTitulo = itemView.findViewById(R.id.nota_titulo);
            TVDescripcion = itemView.findViewById(R.id.descripcio_nota);
            TVFecha = itemView.findViewById(R.id.fecha);
            TVTimestamp = itemView.findViewById(R.id.timestamp);
        }
    }
}