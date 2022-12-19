package ed.maevski.androidpraktika.adapter

import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import ed.maevski.androidpraktika.data.Item

class FavoriteRecyclerAfapter() : ListDelegationAdapter<List<Item>>() {

    init {
        delegatesManager.addDelegate(AdDelegateAdapter())
        delegatesManager.addDelegate(FavoriteDelegateAdapter())
    }

    override fun setItems(items: List<Item>?) {
        super.setItems(items)
        notifyDataSetChanged()
    }
}