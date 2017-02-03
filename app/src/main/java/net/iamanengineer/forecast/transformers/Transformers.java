package net.iamanengineer.forecast.transformers;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *  Transformers allow us to reuse the subscribeOn/observeOn combos that are most commonly used.
 *  i.e. subscribeOn io(), observe on UI Thread
 * Created by julien on 01-20-17.
 */

public class Transformers {

    public static <T> Observable.Transformer<T, T> io() {
        return
                observable -> observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
    }

}
