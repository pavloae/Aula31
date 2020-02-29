package com.nablanet.aula31.data;

import android.service.carrier.CarrierMessagingService;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
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

    @Inject
    public FireBaseDataImpl(FirebaseDatabase dbInstance) {
        this.dbInstance = dbInstance;
    }

    @Override
    public Single<User> getUser() {
        return Single.create(new SingleOnSubscribe<User>() {
            @Override
            public void subscribe(final SingleEmitter<User> emitter) throws Exception {
                dbInstance.getReference("users").child(getUid())
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
        return Single.create(new SingleOnSubscribe<Phone>() {
            @Override
            public void subscribe(final SingleEmitter<Phone> emitter) throws Exception {
                dbInstance.getReference("phones").child(getOwnPhoneId())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Phone phone = new SnapshotFactory<>(Phone.class).toValue(dataSnapshot);
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
    public Completable saveUser(User user) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {
                emitter.onError(new Throwable("No implementado aún"));
            }
        });
    }

    @Override
    public Completable savePhone(Phone phone) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {
                emitter.onError(new Throwable("No implementado aún"));
            }
        });
    }


    public String getUid(){
        return FirebaseAuth.getInstance().getUid();
    }

    public String getOwnPhoneId() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null)
            return null;
        return FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
    }
}
