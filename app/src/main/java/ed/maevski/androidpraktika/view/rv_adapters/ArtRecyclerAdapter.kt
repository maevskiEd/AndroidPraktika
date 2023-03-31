package ed.maevski.androidpraktika.view.rv_adapters

import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import ed.maevski.androidpraktika.data.entity.DeviantPicture
import ed.maevski.androidpraktika.domain.Item
import ed.maevski.androidpraktika.view.adapters.ArtDelegateAdapter

class ArtRecyclerAdapter (private val clickListener: OnItemClickListener) : ListDelegationAdapter<List<Item>>() {

    init {
        delegatesManager.addDelegate(ArtDelegateAdapter(clickListener))
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