package ed.maevski.androidpraktika.adapter

import android.widget.AdapterView
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import ed.maevski.androidpraktika.data.Deviant_picture
import ed.maevski.androidpraktika.data.Item

class PictureRecyclerAdapter(private val clickListener: OnItemClickListener) : ListDelegationAdapter<List<Item>>() {

    init {
        delegatesManager.addDelegate(PictureDelegateAdapter())
        delegatesManager.addDelegate(AdDelegateAdapter())
    }

    override fun setItems(items: List<Item>?) {
        super.setItems(items)
        notifyDataSetChanged()
    }

    //Интерфейс для обработки кликов
    interface OnItemClickListener {
        fun click(picture: Deviant_picture)
    }
}