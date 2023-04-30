package ed.maevski.androidpraktika.view.fragments

import android.R
import android.annotation.SuppressLint
import android.database.Cursor
import android.database.MatrixCursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import ed.maevski.androidpraktika.data.entity.DeviantPicture
import ed.maevski.androidpraktika.databinding.FragmentHomeBinding
import ed.maevski.androidpraktika.domain.Item
import ed.maevski.androidpraktika.utils.AnimationHelper
import ed.maevski.androidpraktika.utils.AutoDisposable
import ed.maevski.androidpraktika.utils.addTo
import ed.maevski.androidpraktika.view.MainActivity
import ed.maevski.androidpraktika.view.decoration.TopSpacingItemDecoration
import ed.maevski.androidpraktika.view.rv_adapters.ArtRecyclerAdapter
import ed.maevski.androidpraktika.viewmodel.HomeFragmentViewModel

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit


class HomeFragment() : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ArtRecyclerAdapter

    private val autoDisposable = AutoDisposable()

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

        autoDisposable.bindTo(lifecycle)

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

    @SuppressLint("CheckResult")
    private fun initSearchView() {

        binding.searchView.setOnClickListener {
            binding.searchView.isIconified = false
        }

        // Определяем имена столбцов таблицы
        val FROM_COLUMNS = arrayOf("tag")

        // Определяем массив с ID элементов View, в которые будут выводиться данные
        val TO_IDS = intArrayOf(android.R.id.text1)

        //        готовая разметка android.R.layout.simple_list_item_1, которая состоит из одного TextView
        val cursorAdapter = SimpleCursorAdapter(
            requireContext(),
            R.layout.simple_list_item_1,
            null,
            FROM_COLUMNS,
            TO_IDS,
            0
        )

        binding.searchView.findViewById<AutoCompleteTextView>(androidx.appcompat.R.id.search_src_text).threshold =
            MIN_THRESHOLD_SEARCH_TAG

        binding.searchView.suggestionsAdapter = cursorAdapter


        //Вешаем слушатель на клавиатуру
        binding.searchView.setOnQueryTextListener(object :
        //Вызывается на ввод символов
            SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                val cursor = MatrixCursor(arrayOf("_id", "tag"))

//                    filmsAdapter.items.clear()

                Observable.create { subscriber ->

                    if (newText.length >= MIN_THRESHOLD_SEARCH_TAG) {
                        println("onQueryTextChange -> $newText")
                        subscriber.onNext(newText)
                    }
                }
                    .subscribeOn(Schedulers.io())
                    .map {
                        println("Внутри Observable -> it: $it")
                        it.toLowerCase(Locale.getDefault()).trim()
                    }
                    .debounce(800, TimeUnit.MILLISECONDS)
                    .filter {
                        //Если в поиске пустое поле, возвращаем список фильмов по умолчанию
//                viewModel.getFilms()
                        it.isNotBlank()
                    }
                    .flatMap {
                        homeFragmentViewModel.getTagSuggestions(it)
                    }
                    .map {
                        println("2level^ Внутри Observable -> it: ${it}")
                        var index = 0
                        it.results.forEach { ress ->
                            cursor.addRow(arrayOf(index++, ress.tagName))
                        }
                        it
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(
                        onError = {
                            Toast.makeText(
                                requireContext(),
                                "Что-то пошло не так",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        },
                        onNext = {
                            it.results.forEach { ress ->
                                println(ress.tagName)
                            }
                            println("it = ${it.results}")
                            cursorAdapter.changeCursor(cursor)
                            cursorAdapter.notifyDataSetChanged()
                            cursor.close()
                        }
                    )
                    .addTo(autoDisposable)

                return true
            }

            //Вызывается по нажатию кнопки "Поиск"
            override fun onQueryTextSubmit(query: String): Boolean {
//                    subscriber.onNext(query)
                println("onQueryTextSubmit: $query")
                return false
            }
        })

        binding.searchView.setOnSuggestionListener(object : SearchView.OnSuggestionListener {
            override fun onSuggestionSelect(position: Int): Boolean {
                return false
            }

            @SuppressLint("Range")
            override fun onSuggestionClick(position: Int): Boolean {
                val posCursor = binding.searchView.suggestionsAdapter.getItem(position) as Cursor
//                val selection = cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1))
                val selection = posCursor.getString(posCursor.getColumnIndex("tag"))
                binding.searchView.setQuery(selection, false)
                Observable.create { subscriber ->
                    subscriber.onNext(selection)
                }.subscribeOn(Schedulers.io())
                    .flatMap {
                        homeFragmentViewModel.getTagBrowseResult(it)
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map {
                        val dvrList = mutableListOf<Item>()
                        it.results.forEach { ress ->
                            dvrList.add(
                                DeviantPicture(
                                    id = ress.deviationid,
                                    title = ress.title,
                                    author = ress.author.username,
                                    picture = 0,
                                    description = "",
                                    url = ress.preview.src,
                                    urlThumb150 = ress.thumbs[0].src,
                                    countFavorites = ress.stats.favourites,
                                    comments = ress.stats.comments,
                                    countViews = 100000,
                                    isInFavorites = false,
                                    setting = ""
                                )
                            )
                        }
                        dvrList
                    }
                    .subscribe {
                        adapter.items = it
                        binding.mainRecycler.adapter = adapter
                        (binding.mainRecycler.adapter as ArtRecyclerAdapter).notifyDataSetChanged()
                    }.addTo(autoDisposable)

                return true
            }

        })
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

        //        Минимальное количество символов для поиска tag на сайте DeviantArt
        private const val MIN_THRESHOLD_SEARCH_TAG = 3
    }
}