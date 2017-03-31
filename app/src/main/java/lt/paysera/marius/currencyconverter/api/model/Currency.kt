package lt.paysera.marius.currencyconverter.api.model

import com.google.gson.annotations.SerializedName

/**
 * Created by marius on 17.3.31.
 */
enum class Currency(val readableName: String, val symbol: String) {
	@SerializedName("EUR") EUR("EUR", "€"),
	@SerializedName("USD") USD("USD", "$"),
	@SerializedName("JPY") JPY("JPY", "¥");

	override fun toString(): String {
		return readableName
	}

}