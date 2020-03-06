package com.nablanet.aula31.core.viewmodel;

import android.util.Log;

import org.junit.Test;
import org.reactivestreams.Subscription;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static org.junit.Assert.*;

public class UserViewModelTest {

    @Test
    public void saveImageProfile() {

        Observer<Integer> observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("onSubscribe");
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("onNext: " + integer.toString());
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("Throwable: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        };

        Observable.just(1, 2, 3, 4).subscribe(observer);

        getFlowable().subscribe(getObserver());





        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    private FlowableSubscriber<Integer> getObserver() {
        return new FlowableSubscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                System.out.println("FonSubscribe");
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("FonNext: " + integer.toString());
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("FonError: " + t.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("FonComplete");
            }
        };
    }

    private Flowable<Integer> getFlowable() {
        return Flowable.just(1, 2, 3, 4);
    }

}