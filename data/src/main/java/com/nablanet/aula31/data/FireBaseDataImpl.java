package com.nablanet.aula31.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nablanet.aula31.core.repository.FireBase.FireBaseData;
import com.nablanet.aula31.domain.model.Phone;
import com.nablanet.aula31.domain.model.User;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

public class FireBaseDataImpl implements FireBaseData {

    public final FirebaseDatabase dbInstance;
    public DatabaseReference userReference;
    public DatabaseReference phoneReference;

    @Inject
    public FireBaseDataImpl(FirebaseDatabase dbInstance) {
        this.dbInstance = dbInstance;
    }

    @Override
    public Single<User> getUser() {

        if (userReference == null) {
            userReference = dbInstance.getReference("users").child(getUid());
            userReference.keepSynced(true);
        }

        return Single.create(new SingleOnSubscribe<User>() {
            @Override
            public void subscribe(final SingleEmitter<User> emitter) throws Exception {
                userReference
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                User user = (new SnapshotFactory<>(User.class)).toValue(dataSnapshot);
                                emitter.onSuccess(user);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                emitter.onError(new Throwable(databaseError.getMessage()));
                            }
                        });
            }
        });
    }

    @Override
    public Single<Phone> getPhone() {
        if (phoneReference == null) {
            phoneReference = dbInstance.getReference("phones").child(getOwnPhoneId());
            phoneReference.keepSynced(true);
        }
        return Single.create(new SingleOnSubscribe<Phone>() {
            @Override
            public void subscribe(final SingleEmitter<Phone> emitter) throws Exception {
                phoneReference
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Phone phone = new SnapshotFactory<>(Phone.class).toValue(dataSnapshot);
                                if (phone != null)
                                    phone.key = dataSnapshot.getKey();
                                emitter.onSuccess(phone);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                emitter.onError(new Throwable(databaseError.getMessage()));
                            }
                        });
            }
        });
    }

    @Override
    public Completable saveUser(@NonNull final User user) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {
                dbInstance.getReference("users").child(getUid())
                        .setValue(user);
            }
        });
    }

    @Override
    public Completable savePhone(@NonNull final Phone phone) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {
                dbInstance.getReference("phones").child(phone.key).child("share")
                        .setValue(phone.share);
            }
        });
    }


    private String getUid(){
        return FirebaseAuth.getInstance().getUid();
    }

    private String getOwnPhoneId() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null)
            return null;
        return FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
    }
}
