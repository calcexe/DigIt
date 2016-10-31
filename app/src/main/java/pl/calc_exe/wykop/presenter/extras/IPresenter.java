package pl.calc_exe.wykop.presenter.extras;

import pl.calc_exe.wykop.view.extras.IView;

/**
 * Created by Mateusz on 2016-08-26.
 */
public interface IPresenter<T extends IView> {
    void onTakeView(T view);
}
