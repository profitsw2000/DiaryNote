package diarynote.core.viewmodel

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

abstract class CoreViewModel : ViewModel(), DefaultLifecycleObserver {
    protected val fullLifeCycleCompositeDisposable = CompositeDisposable()
    protected val viewLifeCycleCompositeDisposable = CompositeDisposable()

    override fun onDestroy(owner: LifecycleOwner) {
        viewLifeCycleCompositeDisposable.clear()
    }

    override fun onCleared() {
        fullLifeCycleCompositeDisposable.dispose()
    }

    protected fun Disposable.addViewLifeCycle(): Disposable {
        viewLifeCycleCompositeDisposable.add(this)
        return this
    }

    protected fun Disposable.addFullLifeCycle(): Disposable {
        fullLifeCycleCompositeDisposable.add(this)
        return this
    }
}