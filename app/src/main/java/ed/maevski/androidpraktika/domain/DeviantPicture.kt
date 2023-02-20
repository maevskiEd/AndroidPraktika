package ed.maevski.androidpraktika.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DeviantPicture(
    override val id: String,
    val title: String,
    val author: String,
    val picture: Int,
    val description: String,
    val url: String,
    val urlThumb150: String,
    val countFavorites: Int,
    val comments: Int,
    val countViews: Int,
    override var isInFavorites: Boolean = false
) : Item, Parcelable

