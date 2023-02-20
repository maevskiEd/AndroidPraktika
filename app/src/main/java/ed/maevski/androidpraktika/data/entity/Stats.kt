package ed.maevski.androidpraktika.data.entity

import com.google.gson.annotations.SerializedName

data class Stats(
    @SerializedName("comments") val comments: Int,
    @SerializedName("favourites") val favourites: Int
)