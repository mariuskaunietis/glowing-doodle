package lt.paysera.marius.currencyconverter.api.model

import lt.paysera.marius.currencyconverter.api.model.Currency

/**
 * Created by marius on 17.3.31.
 */
class History(val fromCurrency: Currency,
              val toCurrency: Currency,
              val fromAmount: Double,
              val toAmount: Double,
              val tax: Double)