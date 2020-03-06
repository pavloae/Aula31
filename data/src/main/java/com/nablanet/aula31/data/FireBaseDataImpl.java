package com.nablanet.aula31.data;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nablanet.aula31.core.repository.FireBase.FireBaseData;
import com.nablanet.aula31.core.utils.UriConverter;
import com.nablanet.aula31.domain.RequestUploadFile;
import com.nablanet.aula31.domain.ResultUploadFile;
import com.nablanet.aula31.domain.model.Phone;
import com.nablanet.aula31.domain.model.User;

import java.io.File;
import java.net.MalformedURLException;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.subjects.PublishSubject;

public class FireBaseDataImpl implements FireBaseData {

    public final FirebaseDatabase firebaseDatabase;
    public final FirebaseStorage firebaseStorage;
    public DatabaseReference phoneReference;

    @Inject
    public FireBaseDataImpl(FirebaseDatabase firebaseDatabase, FirebaseStorage firebaseStorage) {
        this.firebaseDatabase = firebaseDatabase;
        this.firebaseStorage = firebaseStorage;
    }

    @Override
    public Single<User> getUser() {

        DatabaseReference userReference = firebaseDatabase.getReference("users").child(getUid());
        userReference.keepSynced(true);

        final PublishSubject<User> publishSubject = PublishSubject.create();

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = (new SnapshotFactory<>(User.class)).toValue(dataSnapshot);
                if (user != null)
                    publishSubject.onNext(user);
                else
                    publishSubject.onError(new Throwable("No se encontró usuario"));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                publishSubject.onError(new Throwable(databaseError.getMessage()));
            }
        });

        return publishSubject.firstOrError();

    }

    @Override
    public Single<Phone> getPhone() {
        if (phoneReference == null) {
            phoneReference = firebaseDatabase.getReference("phones").child(getOwnPhoneId());
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
                firebaseDatabase.getReference("users").child(getUid())
                        .setValue(user);
            }
        });
    }

    @Override
    public Completable savePhone(@NonNull final Phone phone) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {
                firebaseDatabase.getReference("phones").child(phone.key).child("share")
                        .setValue(phone.share);
            }
        });
    }

    @Override
    public Observable<ResultUploadFile> saveFile(RequestUploadFile requestUploadFile) {

        final PublishSubject<ResultUploadFile> publishSubject = PublishSubject.create();

        String uid = getUid();
        if (uid == null) {
            publishSubject.onError(new Throwable("No hay un id para el usuario"));
            return publishSubject;
        }

        final StorageReference storageReference = firebaseStorage.getReference()
                .child(uid + File.separator + requestUploadFile.path);

        UploadTask uploadTask = storageReference.putFile(UriConverter.getUri(requestUploadFile.uri));

        final OnSuccessListener<Uri> onSuccessListener = new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                try {
                    publishSubject.onNext(
                            new ResultUploadFile(true, UriConverter.getURL(uri))
                    );
                    publishSubject.onComplete();
                } catch (MalformedURLException | NullPointerException e) {
                    e.printStackTrace();
                    publishSubject.onError(new Throwable("No se pudo obtener una URL"));
                }
            }
        };

        uploadTask
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl()
                                .addOnSuccessListener(onSuccessListener)
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        publishSubject.onError(e);
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        publishSubject.onError(e);
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        publishSubject.onNext(
                                new ResultUploadFile(
                                        taskSnapshot.getBytesTransferred(),
                                        taskSnapshot.getTotalByteCount()
                                )
                        );
                    }
                });

        return publishSubject;
    }


    @Nullable
    private String getUid(){
        return FirebaseAuth.getInstance().getUid();
    }

    @Nullable
    private String getOwnPhoneId() {
        return  (FirebaseAuth.getInstance().getCurrentUser() == null) ?
                null : FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
    }

}
