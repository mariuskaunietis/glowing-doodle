package lt.paysera.marius.currencyconverter.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import lt.paysera.marius.currencyconverter.R
import lt.paysera.marius.currencyconverter.fragment.ConverterFragment
import lt.paysera.marius.currencyconverter.fragment.InformationFragment

/**
 * Created by marius on 17.3.31.
 */
class ViewPagerAdapter(fragmentManager: FragmentManager,
                       val context: Context) : FragmentPagerAdapter(fragmentManager) {
	override fun getItem(position: Int): Fragment {
		return when (position) {
			0    -> {
				ConverterFragment()
			}
			1    -> {
				InformationFragment()
			}
			else -> {
				throw RuntimeException("Should never happen")
			}

		}
	}

	override fun getPageTitle(position: Int): CharSequence {
		return when (position) {
			0    -> {
				context.getString(R.string.title_converter)
			}
			1    -> {
				context.getString(R.string.title_info)
			}
			else -> {
				""
			}

		}
	}

	override fun getCount() = 2


}