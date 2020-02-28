package com.nablanet.aula31.repo.query;

import androidx.annotation.NonNull;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class WriteFuture implements Future<DatabaseError> {

    private DatabaseError error;
    private Semaphore semaphore;
    private boolean done = false;

    public WriteFuture(DatabaseReference ref, Object value) throws DatabaseException {
        semaphore = new Semaphore(0);
        ref.setValue(
                value,
                new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError error, @NonNull DatabaseReference ref) {
                        WriteFuture.this.error = error;
                        finish();
                    }
                });
    }

    public WriteFuture(DatabaseReference ref, Object value, double priority)
            throws DatabaseException {
        semaphore = new Semaphore(0);
        ref.setValue(
                value,
                priority,
                new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError error, @NonNull DatabaseReference ref) {
                        WriteFuture.this.error = error;
                        finish();
                    }
                });
    }

    public WriteFuture(DatabaseReference ref, Object value, String priority)
            throws DatabaseException {
        semaphore = new Semaphore(0);
        ref.setValue(
                value,
                priority,
                new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError error, @NonNull DatabaseReference ref) {
                        WriteFuture.this.error = error;
                        finish();
                    }
                });
    }

    private void finish() {
        done = true;
        semaphore.release(1);
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return error != null;
    }

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public DatabaseError get() throws InterruptedException, ExecutionException {
        semaphore.acquire(1);
        return error;
    }

    @Override
    public DatabaseError get(long timeout, TimeUnit unit)
            throws InterruptedException, TimeoutException {
        boolean success = semaphore.tryAcquire(1, timeout, unit);
        if (!success) {
            //IntegrationTestHelpers.failOnFirstUncaughtException();
            throw new TimeoutException();
        }
        return error;
    }

    public DatabaseError timedGet() throws Exception {
        if (error != null) {
            throw new Exception(error.getMessage());
        }
        return get(TestValues.TEST_TIMEOUT, TimeUnit.MILLISECONDS);
    }

}
