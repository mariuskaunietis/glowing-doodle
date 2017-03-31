package lt.paysera.marius.currencyconverter.api.model

import lt.paysera.marius.currencyconverter.api.model.Currency

/**
 * Created by marius on 17.3.31.
 */
class Balance(val amount: Double, val currency: Currency) {
	override fun toString(): String {
		return when (currency) {
			Currency.USD,
			Currency.JPY -> {
				"${currency.symbol}$amount"
			}
			else         -> {
				"$amount${currency.symbol}"
			}
		}
	}
}

