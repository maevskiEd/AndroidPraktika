package ed.maevski.androidpraktika.data.entity_tag

import com.google.gson.annotations.SerializedName

data class TagSuggestionsResponse (
    @SerializedName("results") val results: List<Results>
)