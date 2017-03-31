package lt.paysera.marius.currencyconverter.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.list_item_balance_history.view.*
import lt.paysera.marius.currencyconverter.Config
import lt.paysera.marius.currencyconverter.R
import lt.paysera.marius.currencyconverter.api.model.History

/**
 * Created by marius on 17.3.31.
 */
class BalanceHistoryDisplayableItem(val history: History) : DisplayableItem {
	override fun getViewType() = ViewType.BALANCE_HISTORY
	override fun bind(holder: RecyclerView.ViewHolder) {
		holder.itemView.apply {
			if (history.tax > 0.0) {
				balanceHistoryItemTax.visibility = View.VISIBLE
				balanceHistoryItemTax.text = context.getString(R.string.history_paidTaxes,
				                                               history.tax,
				                                               Config.taxCurrency)
			} else {
				balanceHistoryItemTax.visibility = View.GONE
				balanceHistoryItemTax.text = ""
			}
			balanceHistoryItemFrom.text = context.getString(R.string.history_from,
			                                                history.fromAmount,
			                                                history.fromCurrency)
			balanceHistoryItemTo.text = context.getString(R.string.history_to,
			                                              history.toAmount,
			                                              history.toCurrency)

		}
	}
}
