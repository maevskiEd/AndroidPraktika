package ed.maevski.androidpraktika

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import ed.maevski.androidpraktika.adapter.PictureRecyclerAdapter
import ed.maevski.androidpraktika.data.Ad
import ed.maevski.androidpraktika.data.DeviantPicture
import ed.maevski.androidpraktika.data.Item
import ed.maevski.androidpraktika.databinding.FragmentHomeBinding
import ed.maevski.androidpraktika.decoration.TopSpacingItemDecoration
import java.util.*

class HomeFragment(val items: List<Item>) : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val adapter = PictureRecyclerAdapter(object : PictureRecyclerAdapter.OnItemClickListener {
            override fun click(picture: DeviantPicture) {
                Toast.makeText(requireContext(), picture.title, Toast.LENGTH_SHORT).show()
                (requireActivity() as MainActivity).launchDetailsFragment(picture)
            }
        })

        adapter.items = items
        binding.mainRecycler.layoutManager = LinearLayoutManager(requireContext())
        val decorator = TopSpacingItemDecoration(8)
        binding.mainRecycler.addItemDecoration(decorator)
        binding.mainRecycler.adapter = adapter

        AnimationHelper.performFragmentCircularRevealAnimation(binding.homeFragmentRoot, requireActivity(), 1)

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {}
            }
        )

        binding.searchView.setOnClickListener {
            binding.searchView.isIconified = false
        }

        //Подключаем слушателя изменений введенного текста в поиска
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            //Этот метод отрабатывает при нажатии кнопки "поиск" на софт клавиатуре
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }
            //Этот метод отрабатывает на каждое изменения текста
            override fun onQueryTextChange(newText: String): Boolean {
                val deviantPictures: List<DeviantPicture> = items.filter { it is DeviantPicture} as List<DeviantPicture>
                //Если ввод пуст то вставляем в адаптер всю БД
                if (newText.isEmpty()) {
                    adapter.items = items
                    binding.mainRecycler.adapter = adapter
                    return true
                }
                //Фильтруем список на поиск подходящих сочетаний
                val result = deviantPictures.filter {
                    //Чтобы все работало правильно, нужно и запрос, и имя фильма приводить к нижнему регистру
                    it.title.toLowerCase(Locale.getDefault()).contains(newText.toLowerCase(Locale.getDefault()))
                }
                //Добавляем в адаптер
                adapter.items = result
                binding.mainRecycler.adapter = adapter
                return true
            }
        })
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}