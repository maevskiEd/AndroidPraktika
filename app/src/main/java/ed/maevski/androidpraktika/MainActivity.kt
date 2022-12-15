package ed.maevski.androidpraktika

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import ed.maevski.androidpraktika.adapter.PictureRecyclerAdapter
import ed.maevski.androidpraktika.data.Ad
import ed.maevski.androidpraktika.data.DeviantPicture
import ed.maevski.androidpraktika.data.Item
import ed.maevski.androidpraktika.databinding.ActivityMainBinding
import ed.maevski.androidpraktika.decoration.TopSpacingItemDecoration

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = PictureRecyclerAdapter(object : PictureRecyclerAdapter.OnItemClickListener{
            override fun click(picture: DeviantPicture) {
                Toast.makeText(this@MainActivity,picture.title,Toast.LENGTH_SHORT).show()
                //Создаем бандл и кладем туда объект с данными фильма
                val bundle = Bundle()
                //Первым параметром указывается ключ, по которому потом будем искать, вторым сам
                //передаваемый объект
                bundle.putParcelable("dev", picture)
                //Запускаем наше активити
                val intent = Intent(this@MainActivity, DetailsActivity::class.java)
                //Прикрепляем бандл к интенту
                intent.putExtras(bundle)
                //Запускаем активити через интент
                startActivity(intent)
            }
        })

        fun getItems(): List<Item> {
            return listOf(
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
        }

        adapter.items = getItems()
        binding.mainRecycler.adapter = adapter

        binding.mainRecycler.layoutManager = LinearLayoutManager(this@MainActivity)
        val decorator = TopSpacingItemDecoration(8)
        binding.mainRecycler.addItemDecoration(decorator)

        initMenu()
    }

    private fun initMenu() {
        binding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.settings -> {
                    Toast.makeText(this, R.string.menu_settings_title, Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }


        binding.bottomNavigation.setOnItemSelectedListener {

            when (it.itemId) {
                R.id.favorites -> {
                    Toast.makeText(this, R.string.menu_favorites_title, Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.watch_later -> {
                    Toast.makeText(this, R.string.menu_watch_later_title, Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.selections -> {
                    Toast.makeText(this, R.string.menu_selections_title, Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }
}