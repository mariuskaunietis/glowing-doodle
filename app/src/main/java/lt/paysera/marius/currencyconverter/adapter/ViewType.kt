package lt.paysera.marius.currencyconverter.adapter

import android.support.annotation.LayoutRes
import lt.paysera.marius.currencyconverter.R

/**
 * Created by marius on 17.3.31.
 */
enum class ViewType(@LayoutRes val layoutId: Int) {
	BALANCE_HISTORY(R.layout.list_item_balance_history)
}