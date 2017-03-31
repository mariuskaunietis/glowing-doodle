package lt.paysera.marius.currencyconverter.api

import io.reactivex.Observable
import lt.paysera.marius.currencyconverter.api.model.ConversionResponse
import lt.paysera.marius.currencyconverter.api.model.Currency
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by marius on 17.3.31.
 */
interface AppService {
	@GET("currency/commercial/exchange/{fromAmount}-{fromCurrency}/{toCurrency}/latest")
	fun convertCurrency(@Path("fromAmount") fromAmount: Double,
	                    @Path("fromCurrency") fromCurrency: Currency,
	                    @Path("toCurrency") toCurrency: Currency): Observable<ConversionResponse>


}

