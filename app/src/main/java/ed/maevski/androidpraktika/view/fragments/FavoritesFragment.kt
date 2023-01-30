package ed.maevski.androidpraktika.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ed.maevski.androidpraktika.view.rv_adapters.FavoriteRecyclerAdapter
import ed.maevski.androidpraktika.domain.Item
import ed.maevski.androidpraktika.databinding.FragmentFavoritesBinding
import ed.maevski.androidpraktika.view.decoration.TopSpacingItemDecoration
import ed.maevski.androidpraktika.utils.AnimationHelper
import ed.maevski.androidpraktika.viewmodel.FavoritesFragmentViewModel

class FavoritesFragment() : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val favPadding = 8
    private lateinit var adapter: FavoriteRecyclerAdapter

    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(FavoritesFragmentViewModel::class.java)
    }

    private var devPictures = listOf<Item>()
        //Используем backing field
        set(value) {
            //Если придет такое же значение, то мы выходим из метода
            if (field == value) return
            //Если пришло другое значение, то кладем его в переменную
            field = value
            //Обновляем RV адаптер
            adapter.items = devPictures.filter { it.isInFavorites }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.picturesListLiveData.observe(viewLifecycleOwner, Observer<List<Item>> {
            devPictures  = it
        })

        //Получаем список при транзакции фрагмента
        adapter = FavoriteRecyclerAdapter()

        adapter.items = devPictures.filter { it.isInFavorites }
        val decorator = TopSpacingItemDecoration(favPadding )
        binding.favoritesRecycler.addItemDecoration(decorator)
        binding.favoritesRecycler.adapter = adapter

        AnimationHelper.performFragmentCircularRevealAnimation(binding.favoritesFragmentRoot, requireActivity(), 1)

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {}
            }
        )

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}