package lt.paysera.marius.currencyconverter

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import lt.paysera.marius.currencyconverter.adapter.ViewPagerAdapter
import lt.paysera.marius.currencyconverter.data.CurrencyConversion

class MainActivity : AppCompatActivity() {

	private val pagerAdapter by lazy {
		ViewPagerAdapter(fragmentManager = supportFragmentManager, context = this@MainActivity)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		mainTabs.setupWithViewPager(mainViewPager)
		mainViewPager.adapter = pagerAdapter
		setSupportActionBar(mainToolbar)
	}

	override fun onStart() {
		super.onStart()
		CurrencyConversion.restore(this)
	}

	override fun onStop() {
		super.onStop()
		CurrencyConversion.save(this)
	}

}


