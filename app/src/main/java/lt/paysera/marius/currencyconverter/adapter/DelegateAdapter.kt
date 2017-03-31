package lt.paysera.marius.currencyconverter.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import lt.paysera.marius.currencyconverter.utilities.inflate

/**
 * Created by marius on 17.3.3.
 */
open class DelegateAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		return BasicViewHolder(parent.inflate(ViewType.values()[viewType].layoutId))
	}


	protected val items = mutableListOf<DisplayableItem>()

	override fun getItemCount(): Int {
		return items.size
	}

	override fun getItemViewType(position: Int): Int {
		return items[position].getViewType().ordinal
	}

	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		val item = items[position]
		item.bind(holder)
	}


	fun add(newItem: DisplayableItem) {
		items.add(newItem)
		notifyItemInserted(items.size)
	}

	fun clear() {
		items.clear()
		notifyDataSetChanged()
	}
}