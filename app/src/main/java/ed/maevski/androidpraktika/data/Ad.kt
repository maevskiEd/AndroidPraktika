package ed.maevski.androidpraktika.data

class Ad(override val id: Int, val title: String, val content : String, override var isInFavorites: Boolean = false) : Item