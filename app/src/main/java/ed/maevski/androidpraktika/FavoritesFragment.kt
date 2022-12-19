package ed.maevski.androidpraktika

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ed.maevski.androidpraktika.data.Item
import ed.maevski.androidpraktika.databinding.FragmentFavoritesBinding
import ed.maevski.androidpraktika.databinding.FragmentHomeBinding

class FavoritesFragment(val devPictures: List<Item>) : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val favPictures = mutableListOf<Item>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Получаем список при транзакции фрагмента
        val favPictures: List<Item> = devPictures.filter { it.isInFavorites }

/*         favorites_recycler
            .apply {
                filmsAdapter =
                    FilmListRecyclerAdapter(object : FilmListRecyclerAdapter.OnItemClickListener {
                        override fun click(film: Film) {
                            (requireActivity() as MainActivity).launchDetailsFragment(film)
                        }
                    })
                //Присваиваем адаптер
                adapter = filmsAdapter
                //Присвои layoutmanager
                layoutManager = LinearLayoutManager(requireContext())
                //Применяем декоратор для отступов
                val decorator = TopSpacingItemDecoration(8)
                addItemDecoration(decorator)
            }
        //Кладем нашу БД в RV
        filmsAdapter.add*/
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}