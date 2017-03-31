package lt.paysera.marius.currencyconverter.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import kotlinx.android.synthetic.main.fragment_converter.*
import lt.paysera.marius.currencyconverter.Config
import lt.paysera.marius.currencyconverter.R
import lt.paysera.marius.currencyconverter.api.model.Balance
import lt.paysera.marius.currencyconverter.api.model.Currency
import lt.paysera.marius.currencyconverter.data.CurrencyConversion
import lt.paysera.marius.currencyconverter.utilities.addTo

/**
 * Created by marius on 17.3.31.
 */
class ConverterFragment : BaseRxFragment() {
	val progressBar by lazy {
		val progress = ProgressDialog(context)
		progress.setMessage(getString(R.string.info_loading))
		progress
	}
	val currentBalanceAdapter by lazy {
		ArrayAdapter<Balance>(context, android.R.layout.simple_list_item_1)
	}
	val toCurrencyAdapter by lazy {
		val items = Currency.values()
		ArrayAdapter<Currency>(context, android.R.layout.simple_list_item_1, items)
	}

	override fun onCreateView(inflater: LayoutInflater,
	                          container: ViewGroup?,
	                          savedInstanceState: Bundle?): View {
		return inflater.inflate(R.layout.fragment_converter, container, false)
	}

	override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		converterToCurrency.adapter = toCurrencyAdapter
		converterCurrentBalance.adapter = currentBalanceAdapter
		converterCurrentBalance.onItemSelectedListener = getOnAccountSelectedListener()


		converterDoConversion.setOnClickListener {
			CurrencyConversion.currentBalance
					.observeOn(AndroidSchedulers.mainThread())
					.subscribe {
						val currentAccount = it.find { it.currency == converterFromCurrency.tag }
						validateAndConvert(currentAccount)
					}
					.addTo(disposable)

		}
	}

	override fun onStart() {
		super.onStart()
		CurrencyConversion.currentBalance
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe {
					currentBalanceAdapter.clear()
					currentBalanceAdapter.addAll(it)
					currentBalanceAdapter.notifyDataSetChanged()


					if (shouldApplyTaxes()) {
						converterInfo.setText(R.string.info_taxAfterFive)
					} else {
						converterInfo.setText(R.string.info_noTaxFirstFive)
					}
				}
				.addTo(disposable)
		val combiner = BiFunction <Any, List<Balance>, Balance> {
			view, accounts ->
			accounts.find { it.currency == converterFromCurrency.tag }

		}
		RxView.clicks(converterDoConversion)
				.withLatestFrom(CurrencyConversion.currentBalance, combiner)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe {
					validateAndConvert(it)
				}
				.addTo(disposable)

		RxTextView.textChanges(converterFromValue)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe {
					converterFromValueContainer.error = null
				}
	}

	private fun shouldApplyTaxes() = CurrencyConversion.history.values.size >= 5

	private fun validateAndConvert(currentAccount: Balance?) {
		if (currentAccount == null) {
			Toast.makeText(context, R.string.error_selectAccount, Toast.LENGTH_LONG).show()
			return
		}
		if (converterFromValue.text.isEmpty()) {
			converterFromValueContainer.error = getString(R.string.error_empty)
			return
		}


		val valueFrom = try {
			converterFromValue.text.toString().toDouble()
		} catch (e: NumberFormatException) {
			converterFromValueContainer.error = getString(R.string.error_wrongFormat)
			return
		}

		val taxes = if (shouldApplyTaxes()) {
			valueFrom * 0.007
		} else {
			0.0
		}

		if (currentAccount.amount < (valueFrom + taxes)) {
			converterFromValueContainer.error = getString(R.string.error_notEnoughMoney)
			return
		}

		val toCurrency = converterToCurrency.selectedItem as Currency
		if (currentAccount.currency == toCurrency) {
			Toast.makeText(context, R.string.error_mustNotMatch, Toast.LENGTH_LONG).show()
			return
		}

		CurrencyConversion.convert(fromAmount = valueFrom,
		                           fromCurrency = currentAccount.currency,
		                           toCurrency = toCurrency,
		                           tax = taxes)
				.doOnSubscribe {
					progressBar.show()
				}
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe({
					           val message = getString(R.string.info_successMessage,
					                                   valueFrom,
					                                   currentAccount.currency,
					                                   it.amount,
					                                   it.currency,
					                                   taxes,
					                                   Config.taxCurrency)
					           showMessage(title = getString(R.string.info_conversionSuccess),
					                       message = message)
				           }, {
					           showMessage(title = getString(R.string.info_error),
					                       message = getString(R.string.info_checkInternetSettings))
				           }, {
					           progressBar.hide()
				           }
				)
	}


	private fun showMessage(title: String, message: String) {
		val adb = AlertDialog.Builder(context)
		adb.setTitle(title)
		adb.setMessage(message)
		adb.setPositiveButton(android.R.string.ok, { dialog, which ->
			//do nothing, just dismiss
		})
		adb.show()
	}


	private fun getOnAccountSelectedListener(): AdapterView.OnItemSelectedListener? {
		return object : AdapterView.OnItemSelectedListener {
			override fun onNothingSelected(parent: AdapterView<*>?) {
			}

			override fun onItemSelected(parent: AdapterView<*>?,
			                            view: View?,
			                            position: Int,
			                            id: Long) {
				val selectedCurrency = currentBalanceAdapter.getItem(position).currency
				converterFromCurrency.text = selectedCurrency.toString()
				converterFromCurrency.tag = selectedCurrency
			}
		}
	}
}
