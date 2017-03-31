package lt.paysera.marius.currencyconverter.adapter

import android.support.v7.widget.RecyclerView

/**
 * Created by marius on 17.3.31.
 */
interface DisplayableItem {
	fun getViewType(): ViewType
	fun bind(holder: RecyclerView.ViewHolder)
}