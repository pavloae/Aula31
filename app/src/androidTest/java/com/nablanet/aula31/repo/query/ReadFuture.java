package com.nablanet.aula31.repo.query;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.view.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ReadFuture implements Future<List<EventRecord>> {

    public interface CompletionCondition {
        public boolean isComplete(List<EventRecord> events);
    }

    private List<EventRecord> events = new ArrayList<EventRecord>();
    private Semaphore semaphore;
    private boolean wasCancelled = false;
    private final ValueEventListener valueEventListener;
    private boolean done = false;
    private Exception exception;

    public ReadFuture(final Query ref, final CompletionCondition condition) {
        this(ref, condition, false);
    }

    private ReadFuture(
            final Query ref, final CompletionCondition condition, final boolean ignoreFirstNull) {
        semaphore = new Semaphore(0);
        this.valueEventListener =
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (ignoreFirstNull && events.size() == 0 && snapshot.getValue() == null) {
                            return;
                        }
                        events.add(new EventRecord(snapshot, Event.EventType.VALUE, null));
                        try {
                            if (condition.isComplete(events)) {
                                ref.removeEventListener(valueEventListener);
                                finish();
                            }
                        } catch (Exception e) {
                            exception = e;
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        wasCancelled = true;
                        finish();
                    }
                };
        ref.addValueEventListener(this.valueEventListener);
    }

    // Completes on the first fired event
    public ReadFuture(final Query ref) {
        this(
                ref,
                new CompletionCondition() {
                    @Override
                    public boolean isComplete(List<EventRecord> events) {
                        return true;
                    }
                });
    }

    // Factory helper - completes on first non-null value returned.
    public static ReadFuture untilNonNull(Query ref) {
        return new ReadFuture(
                ref,
                new CompletionCondition() {
                    @Override
                    public boolean isComplete(List<EventRecord> events) {
                        return events.get(events.size() - 1).getSnapshot().getValue() != null;
                    }
                });
    }

    // Factory helper - completes when equal to provided value.
    public static ReadFuture untilEquals(Query ref, final Object value) {
        return untilEquals(ref, value, false);
    }

    public static ReadFuture untilEquals(Query ref, final Object value, boolean ignoreFirstNull) {
        return new ReadFuture(
                ref,
                new CompletionCondition() {
                    @Override
                    public boolean isComplete(List<EventRecord> events) {
                        Object eventValue = events.get(events.size() - 1).getSnapshot().getValue();
                        return eventValue != null && eventValue.equals(value);
                    }
                },
                ignoreFirstNull);
    }

    // Factory helper - completes after count events, ignoring the first null
    public static ReadFuture untilCountAfterNull(Query ref, final int count) {
        return new ReadFuture(
                ref,
                new CompletionCondition() {
                    @Override
                    public boolean isComplete(List<EventRecord> events) {
                        return events.size() == count;
                    }
                },
                true);
    }

    // Factory helper - completes after count events.
    public static ReadFuture untilCount(Query ref, final int count) {
        return new ReadFuture(
                ref,
                new CompletionCondition() {
                    @Override
                    public boolean isComplete(List<EventRecord> events) {
                        return events.size() == count;
                    }
                });
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        // Can't cancel this
        return false;
    }

    @Override
    public boolean isCancelled() {
        return wasCancelled;
    }

    @Override
    public boolean isDone() {
        return done; // To change body of implemented methods use File | Settings | File Templates.
    }

    public Object lastValue() {
        return events.get(events.size() - 1).getSnapshot().getValue();
    }

    @Override
    public List<EventRecord> get() throws InterruptedException, ExecutionException {
        semaphore.acquire(1);
        return events;
    }

    @Override
    public List<EventRecord> get(long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        return null; // To change body of implemented methods use File | Settings | File Templates.
    }

    public Object waitForLastValue() throws Exception {
        timedWait();
        return lastValue();
    }

    public List<EventRecord> timedGet() throws Exception {
        return timedGet(TestValues.TEST_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    public List<EventRecord> timedGet(long timeout, TimeUnit timeoutUnit) throws Exception {
        timedWait(timeout, timeoutUnit);
        return events;
    }

    public void timedWait() throws Exception {
        timedWait(TestValues.TEST_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    public void timedWait(long timeout, TimeUnit timeoutUnit)
            throws Exception {
        if (!semaphore.tryAcquire(1, timeout, timeoutUnit)) {
            //IntegrationTestHelpers.failOnFirstUncaughtException();
            throw new TimeoutException();
        }
        if (exception != null)
            throw new Exception(exception);
    }

    private void finish() {
        done = true;
        semaphore.release(1);
    }

}