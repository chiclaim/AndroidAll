package com.example.rxjava;

import org.junit.Assert;
import org.junit.Test;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicReference;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.internal.operators.OnSubscribeRange;
import rx.observers.TestSubscriber;

public class OnSubscribeDetachTest {

    Object o;

    @Test
    public void just() throws Exception {
        o = new Object();

        WeakReference<Object> wr = new WeakReference<Object>(o);

        TestSubscriber<Object> ts = new TestSubscriber<Object>();

        Observable.just(o).count().onTerminateDetach().subscribe(ts);

        ts.assertValue(1);
        ts.assertCompleted();
        ts.assertNoErrors();

        o = null;

        System.gc();
        Thread.sleep(200);

        Assert.assertNull("Object retained!", wr.get());

    }

    @Test
    public void error() {
        TestSubscriber<Object> ts = new TestSubscriber<Object>();

        Observable.error(new TestException()).onTerminateDetach().subscribe(ts);

        ts.assertNoValues();
        ts.assertError(TestException.class);
        ts.assertNotCompleted();
    }

    @Test
    public void empty() {
        TestSubscriber<Object> ts = new TestSubscriber<Object>();

        Observable.empty().onTerminateDetach().subscribe(ts);

        ts.assertNoValues();
        ts.assertNoErrors();
        ts.assertCompleted();
    }

    @Test
    public void range() {
        TestSubscriber<Object> ts = new TestSubscriber<Object>();

        Observable.range(1, 1000).onTerminateDetach().subscribe(ts);

        ts.assertValueCount(1000);
        ts.assertNoErrors();
        ts.assertCompleted();
    }


    @Test
    public void backpressured() throws Exception {
        o = new Object();

        WeakReference<Object> wr = new WeakReference<Object>(o);

        TestSubscriber<Object> ts = new TestSubscriber<Object>(0L);

        Observable.just(o).count().onTerminateDetach().subscribe(ts);

        ts.assertNoValues();

        ts.requestMore(1);

        ts.assertValue(1);
        ts.assertCompleted();
        ts.assertNoErrors();

        o = null;

        System.gc();
        Thread.sleep(200);

        Assert.assertNull("Object retained!", wr.get());
    }

    @Test
    public void justUnsubscribed() throws Exception {
        o = new Object();

        WeakReference<Object> wr = new WeakReference<Object>(o);

        TestSubscriber<Object> ts = new TestSubscriber<Object>(0);

        Observable.just(o).count().onTerminateDetach().subscribe(ts);

        ts.unsubscribe();
        o = null;

        System.gc();
        Thread.sleep(200);

        Assert.assertNull("Object retained!", wr.get());

    }

    @Test
    public void deferredUpstreamProducer() {
        final AtomicReference<Subscriber<? super Object>> subscriber = new AtomicReference<Subscriber<? super Object>>();

        TestSubscriber<Object> ts = new TestSubscriber<Object>(0);

        Observable.create(new OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> t) {
                subscriber.set(t);
            }
        }).onTerminateDetach().subscribe(ts);

        ts.requestMore(2);

        new OnSubscribeRange(1, 3).call(subscriber.get());

        ts.assertValues(1, 2);

        ts.requestMore(1);

        ts.assertValues(1, 2, 3);
        ts.assertCompleted();
        ts.assertNoErrors();
    }
}