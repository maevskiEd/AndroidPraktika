package ed.maevski.androidpraktika.view.rv_adapters

import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import ed.maevski.androidpraktika.domain.Item
import ed.maevski.androidpraktika.view.adapters.AdDelegateAdapter
import ed.maevski.androidpraktika.view.adapters.FavoriteDelegateAdapter

class FavoriteRecyclerAdapter() : ListDelegationAdapter<List<Item>>() {

    init {
        delegatesManager.addDelegate(AdDelegateAdapter())
        delegatesManager.addDelegate(FavoriteDelegateAdapter())
    }

    override fun setItems(items: List<Item>?) {
        super.setItems(items)
        notifyDataSetChanged()
    }
}