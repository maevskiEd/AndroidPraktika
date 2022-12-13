package ed.maevski.androidpraktika.adapter

import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import ed.maevski.androidpraktika.data.Item

class PictureRecyclerAdapter: ListDelegationAdapter<List<Item>>() {

    init {
        delegatesManager.addDelegate(PictureDelegateAdapter())
        delegatesManager.addDelegate(AdDelegateAdapter())
    }

    override fun setItems(items: List<Item>?) {
        super.setItems(items)
        notifyDataSetChanged()
    }
}