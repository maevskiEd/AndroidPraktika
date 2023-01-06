package ed.maevski.androidpraktika

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.airbnb.lottie.LottieAnimationView
import ed.maevski.androidpraktika.databinding.ActivitySplashScreenBinding

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.splashScreenRoot.setOnClickListener {
            val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
}