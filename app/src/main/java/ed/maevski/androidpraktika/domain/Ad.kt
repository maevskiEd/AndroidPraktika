package ed.maevski.androidpraktika.domain

class Ad(override val id: Int, val title: String, val content : String, override var isInFavorites: Boolean = false) :
    Item