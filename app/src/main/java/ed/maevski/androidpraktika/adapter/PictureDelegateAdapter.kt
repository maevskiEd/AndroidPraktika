package ed.maevski.androidpraktika.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import ed.maevski.androidpraktika.data.Deviant_picture
import ed.maevski.androidpraktika.data.Item
import ed.maevski.androidpraktika.databinding.ItemPictureBinding

class PictureDelegateAdapter :
    AbsListItemAdapterDelegate<Deviant_picture, Item, PictureDelegateAdapter.ViewHolder>() {
    class ViewHolder(binding: ItemPictureBinding) : RecyclerView.ViewHolder(binding.root) {
        val picture = binding.poster
        val title = binding.title
        val author = binding.author
        val description = binding.description
    }

    override fun isForViewType(item: Item, items: MutableList<Item>, position: Int): Boolean {
        return item is Deviant_picture
    }

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(ItemPictureBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(item: Deviant_picture, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.picture.setImageResource(item.picture)
        holder.title.text = item.title
        holder.author.text = item.author
        holder.description.text = item.description
    }
}