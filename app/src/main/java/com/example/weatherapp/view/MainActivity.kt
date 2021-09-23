package com.example.weatherapp.view


import android.os.AsyncTask
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.R
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.net.URL
import android.text.format.Time
import java.text.SimpleDateFormat
import java.util.*
import java.util.Arrays.copyOfRange


class MainActivity : AppCompatActivity() {

    private val BASE_URL = "https://api.openweathermap.org/data/2.5/weather?q=Ak%C5%9Fehir,TR&appid=241d9a0ab86e84b7a7ecc79f7d4a17fa"

    //URL : https://api.openweathermap.org/data/2.5/onecall?lat=38.357498&lon=31.416389&exclude=daily&appid=241d9a0ab86e84b7a7ecc79f7d4a17fa

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weatherTask().execute()


    }

    inner class weatherTask():AsyncTask<String,Void,String>(){
        override fun doInBackground(vararg params: String?): String? {
            var response:String?
            try {
                response = URL("https://api.openweathermap.org/data/2.5/weather?q=Ak%C5%9Fehir,TR&appid=241d9a0ab86e84b7a7ecc79f7d4a17fa").readText(Charsets.UTF_8)

            }
            catch (e:Exception){
                println(e)
                response = null
            }

            return response

        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jsonObj = JSONObject(result)
                val Main = jsonObj.getJSONObject("main")
                val cityname = jsonObj.get("name")
                val Coord = jsonObj.getJSONObject("coord")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
                val sys = jsonObj.getJSONObject("sys")
                val updateAt:Long = jsonObj.getLong("dt")
                val wind = jsonObj.getJSONObject("wind")


                val windDeg=wind.getString("deg")
                val country = sys.getString("country")
                val id = weather.getString("id")
                val temp = Main.getString("temp")
                val sunRise:Long = sys.getLong("sunrise")
                val sunSet:Long = sys.getLong("sunset")
                val lon = Coord.getString("lon")

                descriptionTextView.text = weather.getString("description")
                mainTextView.text = weather.getString("main")
                lastUpdateText.text  = "Son Güncelleme:   "+ SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(Date(updateAt*1000))
                //lastUpdateText.text  = "Son Güncelleme:   "+ SimpleDateFormat("dd/MM/yyyy hh:mm a").format(Date(updateAt*1000))
                sunriseTextView.text =SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunRise*1000))
                sunsetTextView.text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunSet*1000))
                //sunsetTextView.text = SimpleDateFormat("hh:mm a").format(Date(sunSet*1000))
                countryTextView.text = "${cityname},${country}"
                tempretureTextView.text = "${kelvinConvertTocelcius(temp.toDouble()).toInt()}"
                tempMinTextView.text = "temp Min: "+"${kelvinConvertTocelcius((Main.getString("temp_min").toDouble())).toInt()}"+" °"
                tempMaxTextView.text = "temp Max: "+"${kelvinConvertTocelcius((Main.getString("temp_max").toDouble())).toInt()}"+" °"




                if(weather.getString("main") == "Clear"){

                }

                if(windDeg.toInt() in 270..360){
                    windspeedTextView.text = "Wind:Northwest "+wind.getString("speed")+" km/h"
                }

                if(windDeg.toInt() in 180..270){
                    windspeedTextView.text = "Wind:Southwest "+wind.getString("speed")+" km/h"
                }

                if(windDeg.toInt() in 90..180){
                    windspeedTextView.text = "Wind:Southeast "+wind.getString("speed")+" km/h"
                }
                if(windDeg.toInt() in 0..90){
                    windspeedTextView.text = "Wind:Northeast "+wind.getString("speed")+" km/h"
                }



            }
            catch (e:Exception){
                println(e)
            }

        }
        fun kelvinConvertTocelcius(kelvin:Double):Double{
            val celcius:Double = kelvin - 273
            return celcius
        }

        fun Animation(imageView: ImageView){
            imageView.animate().apply {
                duration = 1000
                rotation(45f)
            }

        }

    }

}