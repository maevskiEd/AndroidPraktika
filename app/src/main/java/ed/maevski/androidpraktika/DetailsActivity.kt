package ed.maevski.androidpraktika

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ed.maevski.androidpraktika.data.DeviantPicture
import ed.maevski.androidpraktika.databinding.ActivityDetailsBinding
import ed.maevski.androidpraktika.databinding.ActivityMainBinding

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pic = intent.extras?.get("dev") as DeviantPicture

        //Устанавливаем заголовок
        binding.detailsToolbar.title = pic.title
        //Устанавливаем картинку
        binding.detailsPoster.setImageResource(pic.picture)
        //Устанавливаем описание
        binding.detailsDescription.text = pic.description
    }
}