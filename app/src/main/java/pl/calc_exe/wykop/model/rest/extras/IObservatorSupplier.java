package pl.calc_exe.wykop.model.rest.extras;

import rx.Observable;

public interface IObservatorSupplier<T> {
    Observable<T> getObservator();
}
