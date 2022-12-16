package ed.maevski.androidpraktika.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import ed.maevski.androidpraktika.data.DeviantPicture
import ed.maevski.androidpraktika.data.Item
import ed.maevski.androidpraktika.databinding.ItemPictureBinding

class PictureDelegateAdapter(private val clickListener: PictureRecyclerAdapter.OnItemClickListener) :
    AbsListItemAdapterDelegate<DeviantPicture, Item, PictureDelegateAdapter.ViewHolder>() {
    class ViewHolder(binding: ItemPictureBinding) : RecyclerView.ViewHolder(binding.root) {
        val picture = binding.poster
        val title = binding.title
        val author = binding.author
        val description = binding.description
    }

    override fun isForViewType(item: Item, items: MutableList<Item>, position: Int): Boolean {
        return item is DeviantPicture
    }

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(ItemPictureBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(item: DeviantPicture, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.picture.setImageResource(item.picture)
        holder.title.text = item.title
        holder.author.text = item.author
        holder.description.text = item.description

        //Обрабатываем нажатие на весь элемент целиком - Уточнить????  (можно сделать на отдельный элемент
        //например, картинку) и вызываем метод нашего листенера, который мы получаем из
        //конструктора адаптера
        holder.picture.setOnClickListener{
            clickListener.click(item)
        }
    }
}