package lt.paysera.marius.currencyconverter.fragment

import android.support.v4.app.Fragment
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by marius on 17.3.31.
 */
abstract class BaseRxFragment : Fragment() {
	val disposable by lazy {
		CompositeDisposable()
	}

	override fun onStop() {
		super.onStop()
		disposable.clear()
	}
}
