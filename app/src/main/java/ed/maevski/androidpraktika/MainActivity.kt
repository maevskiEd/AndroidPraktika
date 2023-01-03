package ed.maevski.androidpraktika

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import ed.maevski.androidpraktika.data.Ad
import ed.maevski.androidpraktika.data.DeviantPicture
import ed.maevski.androidpraktika.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var backPressed = 0L
    private lateinit var binding: ActivityMainBinding

    private val itemsRV = listOf(
        DeviantPicture(
            0,
            "Casual Mulan",
            "Zarory",
            R.drawable.poster1,
            "Casual Mulan from Wreck it Ralph 2 Love  I hope I’m not too late to the party, but better late than never :D Hope you like it ^^",
            "https://www.deviantart.com/zarory/art/Casual-Mulan-762500114"
        ),
        Ad(1001, "!--AD--!", "Поддержите наших авторов на Patreon"),
        DeviantPicture(
            1,
            "Katara Sketch",
            "Zarory",
            R.drawable.poster2,
            "Had to draw my favorite Avatar character, Katara Slowly working my way through the gaang",
            "https://www.deviantart.com/zarory/art/Katara-Sketch-935980566"
        ),
        DeviantPicture(
            2,
            "Frozen Fever - Anna",
            "imdrunkonTea",
            R.drawable.poster3,
            "Welcoming Spring with some Frozen Fever fanart :) such a lovely short film. And the Snowgies are adorable! I want one XD",
            "https://www.deviantart.com/imdrunkontea/art/Frozen-Fever-Anna-523899149"
        ),
        DeviantPicture(
            3,
            "Anna",
            "imdrunkonTea",
            R.drawable.poster4,
            "Anna in her Frozen 2 garb!",
            "https://www.deviantart.com/imdrunkontea/art/Anna-801277994"
        ),
        DeviantPicture(
            4,
            "Spirit Blossom Ahri",
            "imdrunkonTea",
            R.drawable.poster5,
            "lil' doodle of Ahri's latest skin!",
            "https://www.deviantart.com/imdrunkontea/art/Spirit-Blossom-Ahri-850236572"
        ),
        DeviantPicture(
            5,
            "KDA - The Dancer",
            "imdrunkonTea",
            R.drawable.poster6,
            "Kai'sa's new KDA outfit is tres parfait",
            "https://www.deviantart.com/imdrunkontea/art/KDA-The-Dancer-853644443"
        ),
        DeviantPicture(
            6,
            "Commission : Crystal-Cat44",
            "Sa-Dui",
            R.drawable.poster7,
            "Commission for :iconcrystal-cat44: Thank you so much!! ",
            "https://www.deviantart.com/sa-dui/art/Commission-Crystal-Cat44-932961567"
        ),
        DeviantPicture(
            7,
            "Commission - Magician",
            "NKSTUDIODIGITAL",
            R.drawable.poster8,
            "- Project Name: Magician; - Project: Commission for client; - Director: NK; - Artist: ToothBrushNH",
            "https://www.deviantart.com/nkstudiodigital/art/Commission-Magician-931227836"
        ),
        DeviantPicture(
            8,
            "Triss Merigold",
            "MilliganVick",
            R.drawable.poster9,
            "CHARACTER  Triss Merigold - The Witcher 3; Torie as Triss",
            "https://www.deviantart.com/milliganvick/art/Triss-Merigold-933869032"
        ),
        DeviantPicture(
            9,
            "Yuna / Final Fantasy X - Summoner",
            "ADSouto",
            R.drawable.poster10,
            "CHARACTER  Final Fantasy X. Daughter of High Summoner Braska. Honest and determined, Yuna embarks on a pilgrimage to obtain the Final Aeon and defeat Sin. Yuna is learning the mystical art of summoning aeons—powerful spirits of yore.",
            "https://www.deviantart.com/adsouto/art/Yuna-Final-Fantasy-X-Summoner-932424050"
        )
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_placeholder, HomeFragment(itemsRV))
            .addToBackStack(null)
            .commit()

        initMenu()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            if (backPressed + TIME_INTERVAL > System.currentTimeMillis()) {
                super.onBackPressed()
                finish()
            } else {
                Toast.makeText(this, R.string.textToExit, Toast.LENGTH_SHORT).show()
            }

            backPressed = System.currentTimeMillis()
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        const val TIME_INTERVAL = 2000
    }

    //Ищем фрагмент по тегу, если он есть то возвращаем его, если нет, то null
    private fun checkFragmentExistence(tag: String): Fragment? = supportFragmentManager.findFragmentByTag(tag)

    private fun changeFragment(fragment: Fragment, tag: String) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_placeholder, fragment, tag)
            .addToBackStack(null)
            .commit()
    }

    fun launchDetailsFragment(picture: DeviantPicture) {
        //Создаем "посылку"
        val bundle = Bundle()
        //Кладем в "посылку"
        bundle.putParcelable("dev", picture)
        //Кладем фрагмент с деталями в перменную
        val fragment = DetailsFragment()
        //Прикрепляем нашу "посылку" к фрагменту
        fragment.arguments = bundle

        //Запускаем фрагмент
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_placeholder, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun initMenu() {
        binding.bottomNavigation.setOnItemSelectedListener {

            when (it.itemId) {
                R.id.home -> {
                    val tag = "home"
                    val fragment = checkFragmentExistence(tag)
                    //В первом параметре, если фрагмент не найден и метод вернул null, то с помощью
                    //элвиса мы вызываем создание нового фрагмента
                    changeFragment( fragment?: HomeFragment(itemsRV), tag)
                    true
                }
                R.id.favorites -> {
                    val tag = "favorites"
                    val fragment = checkFragmentExistence(tag)
                    changeFragment( fragment?: FavoritesFragment(itemsRV), tag)
                    true
                }
                R.id.watch_later -> {
                    val tag = "watch_later"
                    val fragment = checkFragmentExistence(tag)
                    changeFragment( fragment?: WatchLaterFragment(), tag)
                    true
                }
                R.id.selections -> {
                    val tag = "selections"
                    val fragment = checkFragmentExistence(tag)
                    changeFragment( fragment?: SelectionsFragment(), tag)
                    true
                }
                else -> false
            }
        }
    }
}