package ed.maevski.androidpraktika.data

class Deviant_picture(
    override val id: Int,
    val title: String,
    val author: String,
    val picture: Int,
    val description: String,
    val http: String
) : Item

