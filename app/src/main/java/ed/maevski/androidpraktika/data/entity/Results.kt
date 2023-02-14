package ed.maevski.androidpraktika.data.entity

data class Results(
    val allows_comments: Boolean,
    val author: Author,
    val category: String,
    val category_path: String,
    val content: Content,
    val daily_deviation: DailyDeviation,
    val deviationid: String,
    val download_filesize: Int,
    val is_blocked: Boolean,
    val is_deleted: Boolean,
    val is_downloadable: Boolean,
    val is_favourited: Boolean,
    val is_mature: Boolean,
    val is_published: Boolean,
    val preview: Preview,
    val printid: Any,
    val published_time: String,
    val stats: Stats,
    val thumbs: List<Thumb>,
    val title: String,
    val url: String
)