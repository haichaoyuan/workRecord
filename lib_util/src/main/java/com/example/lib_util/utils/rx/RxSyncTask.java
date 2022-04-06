package com.example.lib_util.utils.rx;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 封装的一种提供主线程运行，异步线程运行的方式
 */
public class RxSyncTask {

    private static RxSyncTask INSTANCE;

    public static RxSyncTask getInstance() {
        if (INSTANCE == null) {
            synchronized (RxSyncTask.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RxSyncTask();
                }
            }
        }
        return INSTANCE;
    }

    private RxSyncTask() {
    }

    public CompositeDisposable execute(final Action01 action) {
        final CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(Observable.just(1)
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Integer, Object>() {
                    @Override
                    public Object apply(Integer integer) throws Exception {
                        action.onPreExecute();
                        return 1;
                    }
                })
                .observeOn(Schedulers.io())
                .map(new Function<Object, Object>() {
                    @Override
                    public Object apply(Object integer) throws Exception {
                        action.doInBackground();
                        return 1;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        action.onPostExecute();
                        if (!compositeDisposable.isDisposed()) {
                            compositeDisposable.clear();
                        }
                    }
                }));
        return compositeDisposable;
    }

    public CompositeDisposable executeOnMainThread(final Action02 action) {
        final CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(Observable.just(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        action.onTask();
                        if (!compositeDisposable.isDisposed()) {
                            compositeDisposable.clear();
                        }
                    }
                }));
        return compositeDisposable;
    }

    public CompositeDisposable executeOnWorkThread(final Action02 action) {
        final CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(Observable.just(1)
                .observeOn(Schedulers.newThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        action.onTask();
                        if (!compositeDisposable.isDisposed()) {
                            compositeDisposable.clear();
                        }
                    }
                }));
        return compositeDisposable;
    }

    public interface Action01 {
        void onPreExecute();

        void doInBackground();

        void onPostExecute();
    }

    public interface Action02 {
        void onTask();
    }
}

