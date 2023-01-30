package ed.maevski.androidpraktika.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import ed.maevski.androidpraktika.domain.DeviantPicture
import ed.maevski.androidpraktika.domain.Item
import ed.maevski.androidpraktika.databinding.ItemPictureBinding
import ed.maevski.androidpraktika.view.rv_adapters.PictureRecyclerAdapter

class PictureDelegateAdapter(private val clickListener: PictureRecyclerAdapter.OnItemClickListener) :
    AbsListItemAdapterDelegate<DeviantPicture, Item, PictureDelegateAdapter.ViewHolder>() {
    class ViewHolder(binding: ItemPictureBinding) : RecyclerView.ViewHolder(binding.root) {
        val picture = binding.poster
        val title = binding.title
        val author = binding.author
        val description = binding.description
        val item_container = binding.itemContainer
        val donut_view = binding.donutView
    }

    override fun isForViewType(item: Item, items: MutableList<Item>, position: Int): Boolean {
        return item is DeviantPicture
    }

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(ItemPictureBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(item: DeviantPicture, holder: ViewHolder, payloads: MutableList<Any>) {
//        holder.picture.setImageResource(item.picture)

        //Указываем контейнер, в котором будет "жить" наша картинка
        Glide.with(holder.item_container)
            //Загружаем сам ресурс
            .load(item.picture)
            //Центруем изображение
            .centerCrop()
            //Указываем ImageView, куда будем загружать изображение
            .into(holder.picture)

        holder.title.text = item.title
        holder.author.text = item.author
        holder.description.text = item.description
        holder.donut_view.setProgress(item.countViews)

        holder.item_container.setOnClickListener{
            clickListener.click(item)
        }
    }
}