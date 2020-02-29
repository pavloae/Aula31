package com.nablanet.aula31.data;

import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FirebaseQueryLiveData<T> extends MutableLiveData<DataResult<T>> {

    private boolean listenerRemovePending = false;
    private final Handler handler = new Handler();
    private final Runnable removeListener = new Runnable() {
        @Override
        public void run() {
            query.removeEventListener(listener);
            listenerRemovePending = false;
        }
    };

    private final Query query;
    private final MyValueEventListener listener = new MyValueEventListener();
    private final Class<T> clazz;

    public FirebaseQueryLiveData(@NonNull Query query, @NonNull Class<T> clazz) {
        this.query = query;
        this.clazz = clazz;
    }

    public FirebaseQueryLiveData(@NonNull DatabaseReference ref, @NonNull Class<T> clazz) {
        this.query = ref;
        this.clazz = clazz;
    }

    @Override
    protected void onActive() {
        if (listenerRemovePending)
            handler.removeCallbacks(removeListener);
        else
            query.addValueEventListener(listener);

        listenerRemovePending = false;
    }

    @Override
    protected void onInactive() {
        // Esperamos dos segundos antes de remover el listener por si es necesario utilizarlo otra vez
        // Con esto evitamos tener que hacer otra consulta a la base de datos en un cambio de
        // configuración de pantalla
        handler.postDelayed(removeListener, 2000);
        listenerRemovePending = true;
    }

    private class MyValueEventListener implements ValueEventListener {

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (!listenerRemovePending)
                setValue(new DataResult<>(dataSnapshot, new SnapshotFactory<>(clazz)));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            setValue(new DataResult<T>(databaseError));
        }

    }

}
