package ed.maevski.androidpraktika.data.entity

data class DeviantartResponse(
    val has_more: Boolean,
    val results: List<Results>
)