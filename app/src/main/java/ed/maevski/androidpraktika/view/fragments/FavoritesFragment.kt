package ed.maevski.androidpraktika.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import ed.maevski.androidpraktika.view.rv_adapters.FavoriteRecyclerAdapter
import ed.maevski.androidpraktika.domain.Item
import ed.maevski.androidpraktika.databinding.FragmentFavoritesBinding
import ed.maevski.androidpraktika.view.decoration.TopSpacingItemDecoration
import ed.maevski.androidpraktika.utils.AnimationHelper

class FavoritesFragment(val devPictures: List<Item>) : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val favPadding = 8

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

        val adapter = FavoriteRecyclerAdapter()

        adapter.items = favPictures
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