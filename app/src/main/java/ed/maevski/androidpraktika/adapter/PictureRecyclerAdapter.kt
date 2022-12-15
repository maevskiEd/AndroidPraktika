package ed.maevski.androidpraktika.adapter

import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import ed.maevski.androidpraktika.data.DeviantPicture
import ed.maevski.androidpraktika.data.Item

class PictureRecyclerAdapter(private val clickListener: OnItemClickListener) : ListDelegationAdapter<List<Item>>() {

    init {
        delegatesManager.addDelegate(PictureDelegateAdapter(clickListener))
        delegatesManager.addDelegate(AdDelegateAdapter())
    }

    override fun setItems(items: List<Item>?) {
        super.setItems(items)
        notifyDataSetChanged()
    }

    //Интерфейс для обработки кликов
    interface OnItemClickListener {
        fun click(picture: DeviantPicture)
    }
}