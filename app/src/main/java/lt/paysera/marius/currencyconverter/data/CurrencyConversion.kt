package lt.paysera.marius.currencyconverter.data

import android.content.Context
import android.preference.PreferenceManager
import com.google.gson.reflect.TypeToken
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.ReplaySubject
import lt.paysera.marius.currencyconverter.api.Api
import lt.paysera.marius.currencyconverter.api.model.Balance
import lt.paysera.marius.currencyconverter.api.model.ConversionResponse
import lt.paysera.marius.currencyconverter.api.model.Currency
import lt.paysera.marius.currencyconverter.api.model.History
import lt.paysera.marius.currencyconverter.utilities.loge

/**
 * Created by marius on 17.3.31.
 */
object CurrencyConversion {
	val KEY_CURRENT_BALANCE = "currentBalances"
	val KEY_HISTORY = "history"
	val currentBalance: BehaviorSubject<List<Balance>> by lazy {
		val subject = BehaviorSubject.create<List<Balance>>()
		subject
	}
	val history: ReplaySubject<History> by lazy {
		val subject = ReplaySubject.create<History>()
		subject
	}

	fun save(context: Context) {
		val prefsEditor = PreferenceManager.getDefaultSharedPreferences(context).edit()

		val currentBalances = currentBalance.value
		val historyValues = history.values

		prefsEditor.putString(KEY_CURRENT_BALANCE, Api.gson.toJson(currentBalances))
		prefsEditor.putString(KEY_HISTORY, Api.gson.toJson(historyValues))

		prefsEditor.apply()

	}

	fun restore(context: Context) {
		val prefs = PreferenceManager.getDefaultSharedPreferences(context)

		val balancesJson = prefs.getString(KEY_CURRENT_BALANCE, null)

		if (balancesJson == null) {
			val startingBalance = listOf(Balance(1000.0, Currency.EUR),
			                             Balance(0.0, Currency.USD),
			                             Balance(0.0, Currency.JPY))
			currentBalance.onNext(startingBalance)
		} else {
			val balancesTypeToken = object : TypeToken<List<Balance>>() {}.type
			val currentBalances: List<Balance> = Api.gson.fromJson(
					balancesJson, balancesTypeToken)
			currentBalance.onNext(currentBalances)
		}

		val historyJson = prefs.getString(KEY_HISTORY, null)
		if (historyJson != null) {
			val historyTypeToken = object : TypeToken<List<History>>() {}.type
			val historyValues: List<History> = Api.gson.fromJson(
					historyJson, historyTypeToken)
			historyValues.forEach {
				history.onNext(it)
			}
		}
	}


	fun convert(fromAmount: Double,
	            fromCurrency: Currency,
	            toCurrency: Currency,
	            tax: Double): Observable<ConversionResponse> {

		val combiner = BiFunction<ConversionResponse, List<Balance>, ConversionResponse> {
			conversionResponse, balance ->

			val newBalance = balance.map {
				when (it.currency) {
					fromCurrency                -> {
						Balance(it.amount - fromAmount - tax, it.currency)
					}
					conversionResponse.currency -> {
						Balance(it.amount + conversionResponse.amount, it.currency)
					}
					else                        -> {
						it
					}

				}
			}
			currentBalance.onNext(newBalance)
			val newHistory = History(fromCurrency = fromCurrency,
			                         toCurrency = conversionResponse.currency,
			                         fromAmount = fromAmount,
			                         toAmount = conversionResponse.amount,
			                         tax = tax)
			history.onNext(newHistory)

			conversionResponse

		}
		return Api.appService.convertCurrency(fromAmount = fromAmount,
		                                      fromCurrency = fromCurrency,
		                                      toCurrency = toCurrency)
				.withLatestFrom(currentBalance, combiner)
				.subscribeOn(Schedulers.io())
				.observeOn(Schedulers.io())
				.doOnError {
					loge("error")
				}
	}
}



