package lt.paysera.marius.currencyconverter.fragment

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_information.*
import lt.paysera.marius.currencyconverter.Config
import lt.paysera.marius.currencyconverter.R
import lt.paysera.marius.currencyconverter.adapter.BalanceHistoryDisplayableItem
import lt.paysera.marius.currencyconverter.adapter.DelegateAdapter
import lt.paysera.marius.currencyconverter.data.CurrencyConversion
import lt.paysera.marius.currencyconverter.utilities.addTo

/**
 * Created by marius on 17.3.31.
 */
class InformationFragment : BaseRxFragment() {

	var taxTotals = 0.0

	val adapter by lazy {
		DelegateAdapter()
	}

	override fun onCreateView(inflater: LayoutInflater,
	                          container: ViewGroup?,
	                          savedInstanceState: Bundle?): View {
		return inflater.inflate(R.layout.fragment_information, container, false)
	}

	override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		informationHistoryRecycler.adapter = adapter
		informationHistoryRecycler.layoutManager = LinearLayoutManager(context)
		informationHistoryRecycler.addItemDecoration(DividerItemDecoration(context,
		                                                                   DividerItemDecoration.VERTICAL))
	}

	override fun onStart() {
		super.onStart()
		CurrencyConversion.history
				.observeOn(AndroidSchedulers.mainThread())
				.doOnDispose {
					adapter.clear()
				}
				.subscribe {
					taxTotals += it.tax
					informationTotalTax.text = getString(R.string.info_totalTax,
					                                     taxTotals,
					                                     Config.taxCurrency)
					adapter.add(BalanceHistoryDisplayableItem(it))
				}
				.addTo(disposable)
	}
}
