package com.chiclaim.rxjava.operator.create;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func0;
import rx.functions.Func1;

public class RxUtils {

    public static <T> Observable<T> createObservable(final Callable<T> callable) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                try {
                    T result = callable.call();
                    subscriber.onNext(result);
                } catch (Exception e) {
                    subscriber.onError(e);
                }
                subscriber.onCompleted();
            }
        });
    }

    public static <T> Observable<T> deferObservable(final Callable<T> callable) {
        return Observable.defer(new Func0<Observable<T>>() {

            @Override
            public Observable<T> call() {
                T result;
                try {
                    result = callable.call();
                } catch (Exception e) {
                    return Observable.error(e);
                }
                return Observable.just(result);
            }
        });
    }

    public static <T> Observable.Transformer<T, T> handlerResult() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> baseModelObservable) {
                return baseModelObservable.flatMap(new Func1<T, Observable<T>>() {
                    @Override
                    public Observable<T> call(T result) {
                        if (true/*result.getCode() == HttpResult.SUCCESS*/) {
                            return emitData(result);
                        }
                        return Observable.error(new ServerException("your error code", "your error image"));
                    }
                });
            }
        };
    }

    public static <T> Observable<T> wrapCallable(final Callable<T> callable) {
        return RxUtils.deferObservable(callable).compose(RxUtils.<T>handlerResult());
    }

    public static <T> Observable<T> emitData(final T data) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                try {
                    subscriber.onNext(data);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

}
