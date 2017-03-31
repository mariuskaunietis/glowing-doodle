package lt.paysera.marius.currencyconverter.utilities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by marius on 17.3.31.
 */
fun Disposable.addTo(composite: CompositeDisposable) {
	composite.add(this)
}

fun Any.loge(message: Any?) {
	try {

		var tag: String? = if (this is String) {
			this
		} else {
			this.javaClass.simpleName
		}
		if (tag == null || tag.isEmpty()) {
			if (this.javaClass.enclosingClass != null) {
				tag = this.javaClass.enclosingClass.simpleName
			}
		}
		if (message is String) {
			Log.e(tag, message)
			return
		}
		if (message is Collection<*>) {
			Log.e(tag, "---print collection---")
			for (o in message) {
				loge(o)
			}
			Log.e(tag, "---end print collection---")
			return
		}
		if (message is Throwable) {
			Log.e(tag, message.toString())
			message.printStackTrace()
			return
		}
		if (message is Intent) {
			if (message.action.isNotBlank()) {
				Log.e(tag, message.action)
			}
			loge(message.data)
			loge(message.extras)
			return
		}
		if (message is Bundle) {
			message.keySet().forEach {
				loge(message.get(it))
			}
			return
		}
		Log.e(tag, message.toString())
	} catch (ignore: Exception) {
		//would be a shame if app crashed only due to logging :)
	}
}

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
	return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}
