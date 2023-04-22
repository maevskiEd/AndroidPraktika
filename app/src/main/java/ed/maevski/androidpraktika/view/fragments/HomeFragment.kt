package ed.maevski.androidpraktika.view.fragments

import android.R
import android.annotation.SuppressLint
import android.database.Cursor
import android.database.MatrixCursor
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import ed.maevski.androidpraktika.view.MainActivity
import ed.maevski.androidpraktika.data.entity.DeviantPicture
import ed.maevski.androidpraktika.domain.Item
import ed.maevski.androidpraktika.databinding.FragmentHomeBinding
import ed.maevski.androidpraktika.view.decoration.TopSpacingItemDecoration
import ed.maevski.androidpraktika.utils.AnimationHelper
import ed.maevski.androidpraktika.view.rv_adapters.ArtRecyclerAdapter
import ed.maevski.androidpraktika.viewmodel.HomeFragmentViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.core.Observable
import java.util.*
import java.util.concurrent.TimeUnit

class HomeFragment() : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ArtRecyclerAdapter

    private val homeFragmentViewModel: HomeFragmentViewModel by viewModels()

    private var picturesDataBase = listOf<Item>()
        //Используем backing field
        set(value) {
            //Если придет такое же значение, то мы выходим из метода
            if (field == value) return
            //Если пришло другое значение, то кладем его в переменную
            field = value
            //Обновляем RV адаптер
            adapter.items = picturesDataBase
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        println("HomeFragment: onCreateView")

        return binding.root
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        println("HomeFragment: onViewCreated")

        homeFragmentViewModel.getDeviantArts()
        homeFragmentViewModel.picturesListData =
            homeFragmentViewModel.interactor.getDeviantPicturesFromDBWithCategory()

        AnimationHelper.performFragmentCircularRevealAnimation(
            binding.homeFragmentRoot,
            requireActivity(),
            POSITION_ONE
        )

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {}
            }
        )

        homeFragmentViewModel.picturesListData
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { list ->
//                filmsAdapter.addItems(list)
                picturesDataBase = list
            }

        initSearchView()
        initPullToRefresh()
        initRecyckler()
    }

    private fun initRecyckler() {
        adapter = ArtRecyclerAdapter(object : ArtRecyclerAdapter.OnItemClickListener {
            override fun click(picture: DeviantPicture) {
                Toast.makeText(requireContext(), picture.title, Toast.LENGTH_SHORT).show()
                (requireActivity() as MainActivity).launchDetailsFragment(picture)
            }
        })

        adapter.items = picturesDataBase
//        binding.mainRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.mainRecycler.layoutManager = GridLayoutManager(requireContext(), 3)
        val decorator = TopSpacingItemDecoration(8)
        binding.mainRecycler.addItemDecoration(decorator)
        binding.mainRecycler.adapter = adapter
    }

    private fun initSearchView() {
        binding.searchView.setOnClickListener {
            binding.searchView.isIconified = false
        }

        // Настраиваем автозаполнение
        val suggestions = listOf("apple", "banana", "cherry", "date", "elderberry")
        // Определяем имена столбцов таблицы
        val FROM_COLUMNS = arrayOf("tag")

        // Определяем массив с ID элементов View, в которые будут выводиться данные
        val TO_IDS = intArrayOf(R.id.text1)

        //        готовая разметка android.R.layout.simple_list_item_1, которая состоит из одного TextView
        val cursorAdapter = SimpleCursorAdapter(requireContext(), R.layout.simple_list_item_1, null, FROM_COLUMNS, TO_IDS, 0)

        binding.searchView.suggestionsAdapter = cursorAdapter

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Вызывается, когда пользователь отправляет запрос
                // Обрабатываем запрос и выполняем поиск
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Вызывается, когда пользователь вводит текст в SearchView
                // Обновляем автозаполнение
                //        adapter.filter.filter(newText)

                val cursor = MatrixCursor(arrayOf("_id", "tag"))
                newText?.let {
                    suggestions.forEachIndexed { index, suggestion ->
                        if (suggestion.contains(newText, true))
                            cursor.addRow(arrayOf(index, suggestion))
                    }
                }

                cursorAdapter.changeCursor(cursor)
                return true
            }

        })

        binding.searchView.setOnSuggestionListener(object: SearchView.OnSuggestionListener {
            override fun onSuggestionSelect(position: Int): Boolean {
                return false
            }

            @SuppressLint("Range")
            override fun onSuggestionClick(position: Int): Boolean {
                val cursor = binding.searchView.suggestionsAdapter.getItem(position) as Cursor
//                val selection = cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1))
                val selection = cursor.getString(cursor.getColumnIndex("tag"))
                binding.searchView.setQuery(selection, false)

                // Do something with selection
                return true
            }

        })

/*        val intStream = Observable.create<String> { subscriber ->
            binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String): Boolean {
//                    adapter.items.clear()
                    subscriber.onNext(newText)
                    return false
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    subscriber.onNext(query)
                    return false
                }
            })
        }*/


/*            .subscribeOn(Schedulers.io())
            .map {
            it.lowercase(Locale.getDefault()).trim()
        }.debounce(800, TimeUnit.MILLISECONDS)
            .filter {
                //Если в поиске пустое поле, возвращаем список фильмов по умолчанию
                viewModel.getFilms()
                it.isNotBlank()
            }            .flatMap {
                viewModel.getSearchResult(it)
            }*/





/*        //Подключаем слушателя изменений введенного текста в поиска
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            //Этот метод отрабатывает при нажатии кнопки "поиск" на софт клавиатуре
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            //Этот метод отрабатывает на каждое изменения текста
            override fun onQueryTextChange(newText: String): Boolean {
                val deviantPictures: List<DeviantPicture> =
                    picturesDataBase.filter { it is DeviantPicture } as List<DeviantPicture>
                //Если ввод пуст то вставляем в адаптер всю БД
                if (newText.isEmpty()) {
                    adapter.items = picturesDataBase
                    binding.mainRecycler.adapter = adapter
                    return true
                }
                //Фильтруем список на поиск подходящих сочетаний
                val result = deviantPictures.filter {
                    //Чтобы все работало правильно, нужно и запрос, и имя фильма приводить к нижнему регистру
                    it.title.toLowerCase(Locale.getDefault())
                        .contains(newText.toLowerCase(Locale.getDefault()))
                }
                //Добавляем в адаптер
                adapter.items = result
                binding.mainRecycler.adapter = adapter
                return true
            }
        })*/
    }

    private fun initPullToRefresh() {
        //Вешаем слушатель, чтобы вызвался pull to refresh
        binding.pullToRefresh.setOnRefreshListener {
            //Чистим адаптер(items нужно будет сделать паблик или создать для этого публичный метод)
//            adapter.items.
/*            filmsAdapter.items.clear()*/
            //Делаем новый запрос фильмов на сервер
            homeFragmentViewModel.getDeviantArts()
            //Убираем крутящееся колечко
            binding.pullToRefresh.isRefreshing = false
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        private const val POSITION_ONE = 1
    }
}