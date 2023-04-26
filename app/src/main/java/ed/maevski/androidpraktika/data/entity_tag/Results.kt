package ed.maevski.androidpraktika.data.entity_tag

import com.google.gson.annotations.SerializedName
import ed.maevski.androidpraktika.data.entity.*

data class Results(
    @SerializedName("tag_name" ) var tagName : String
)