package com.maga.myapplication;

import android.content.Context;
import android.widget.Toast;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.SimpleDateFormat;

public class Utilidad {

    /**
     * Muestra un mensaje Toast en la pantalla.
     *
     * @param context El contexto de la aplicación.
     * @param message El mensaje a mostrar en el Toast.
     */
    public static void verToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Clase interna que maneja la referencia a una colección de Firestore.
     */
    public static class ReferenciaDeColeccion {
        public CollectionReference collectionReference;

        /**
         * Constructor que inicializa la referencia de la colección.
         *
         * @param collectionReference La referencia de la colección de Firestore.
         */
        public ReferenciaDeColeccion(CollectionReference collectionReference) {
            this.collectionReference = collectionReference;
        }

        /**
         * Obtiene una referencia a un nuevo documento dentro de la colección.
         *
         * @return La referencia del nuevo documento.
         */
        public DocumentReference document() {
            return collectionReference.document();
        }
    }

    /**
     * Obtiene una referencia a la colección de notas del usuario actual.
     *
     * @return Una instancia de ReferenciaDeColeccion que contiene la referencia de la colección.
     */
    public static ReferenciaDeColeccion getReferenciaDeColeccion() {
        FirebaseUser UsuarioActual = FirebaseAuth.getInstance().getCurrentUser();
        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("notas")
                .document(UsuarioActual.getUid()).collection("Mis_Notas");
        return new ReferenciaDeColeccion(collectionReference);
    }

    /**
     * Obtiene una referencia a un documento específico dentro de la colección de notas del usuario actual.
     *
     * @param docId El ID del documento. Si es nulo o vacío, se crea un nuevo documento.
     * @return La referencia del documento.
     */
    public static DocumentReference getDocumentReference(String docId) {
        FirebaseUser UsuarioActual = FirebaseAuth.getInstance().getCurrentUser();
        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("notas")
                .document(UsuarioActual.getUid()).collection("Mis_Notas");
        ReferenciaDeColeccion referenciaDeColeccion = new ReferenciaDeColeccion(collectionReference);

        if (docId != null && !docId.isEmpty()) {
            return referenciaDeColeccion.collectionReference.document(docId);
        } else {
            return referenciaDeColeccion.document();
        }
    }

    /**
     * Convierte un objeto Timestamp de Firestore a una cadena de texto con formato "MM/dd/yyyy".
     *
     * @param timestamp El objeto Timestamp de Firestore.
     * @return La representación en cadena del Timestamp.
     */
    public static String timestampToString(Timestamp timestamp) {
        return new SimpleDateFormat("MM/dd/yyyy").format(timestamp.toDate());
    }
}