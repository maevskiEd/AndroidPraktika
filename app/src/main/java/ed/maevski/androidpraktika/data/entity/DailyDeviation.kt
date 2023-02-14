package ed.maevski.androidpraktika.data.entity

data class DailyDeviation(
    val body: String,
    val giver: Giver,
    val suggester: Suggester,
    val time: String
)