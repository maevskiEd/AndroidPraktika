package ed.maevski.androidpraktika.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DeviantPicture(
    override val id: Int,
    val title: String,
    val author: String,
    val picture: Int,
    val description: String,
    val http: String,
    val countFavorites: Int,
    val comments: Int,
    val countViews: Int,
    override var isInFavorites: Boolean = false
) : Item, Parcelable

