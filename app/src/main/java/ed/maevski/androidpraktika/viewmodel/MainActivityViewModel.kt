package ed.maevski.androidpraktika.viewmodel

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import ed.maevski.androidpraktika.App
import ed.maevski.androidpraktika.domain.Interactor
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

class MainActivityViewModel : ViewModel() {
    val flagToken: MutableLiveData<Boolean> = MutableLiveData()

    var errorEvent = SingleLiveEvent<String>()

    private var access_token: String

    //Инициализируем интерактор
    @Inject
    lateinit var interactor: Interactor

    init {
        println("MainActivityViewModel: Init")

        App.instance.dagger.inject(this)
        access_token = interactor.getAccessTokenFromPreferences()
        flagToken.postValue(true)

        initToken()
    }

    fun initToken() {
        if (access_token.isEmpty()) {
            println("initToken: then")
            interactor.getTokenFromApi(flagToken, errorEvent)
        } else {
            println("initToken: else")
            interactor.checkToken(access_token, flagToken, errorEvent)
        }
    }

    class SingleLiveEvent<T> : MutableLiveData<T>() {
        private val mPending = AtomicBoolean(false)
        @MainThread
        override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
            if (hasActiveObservers()) {
                Log.w(TAG, "Multiple observers registered but only one will be notified of changes.")
            }
            // Observe the internal MutableLiveData
            super.observe(owner, object : Observer<T> {
                override fun onChanged(t: T?) {
                    if (mPending.compareAndSet(true, false)) {
                        observer.onChanged(t)
                    }
                }
            })
        }
        @MainThread
        override fun setValue(t: T?) {
            mPending.set(true)
            super.setValue(t)
        }
        /**
         * Used for cases where T is Void, to make calls cleaner.
         */
        @MainThread
        fun call() {
            setValue(null)
        }
        companion object {
            private val TAG = "SingleLiveEvent"
        }
    }
}