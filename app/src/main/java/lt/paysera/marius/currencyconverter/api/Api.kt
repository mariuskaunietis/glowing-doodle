package lt.paysera.marius.currencyconverter.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by marius on 17.3.31.
 */
object Api {
	private val BASE_API_URL = "http://api.evp.lt"

	val gson: Gson by lazy {
		val builder = GsonBuilder()
		builder.create()
	}
	val appService: AppService  by lazy {
		retrofit.create(AppService::class.java)
	}

	private val retrofit by lazy {
		val retrofitBuilder = Retrofit.Builder()
		retrofitBuilder.addConverterFactory(GsonConverterFactory.create(gson))
		retrofitBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
		retrofitBuilder.client(okClient)
		retrofitBuilder.baseUrl(BASE_API_URL)
		retrofitBuilder.build()
	}
	val okClient: OkHttpClient by lazy {
		val okBuilder = OkHttpClient.Builder()
		val loggingInterceptor = HttpLoggingInterceptor()
		loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
		okBuilder.interceptors().add(loggingInterceptor)


		okBuilder.build()
	}


}

