package ed.maevski.androidpraktika.data

import ed.maevski.androidpraktika.R
import ed.maevski.androidpraktika.domain.Ad
import ed.maevski.androidpraktika.domain.DeviantPicture

class MainRepository {
    val itemsRV = listOf(
        DeviantPicture(
            0,
            "Casual Mulan",
            "Zarory",
            R.drawable.poster1,
            "Casual Mulan from Wreck it Ralph 2 Love  I hope I’m not too late to the party, but better late than never :D Hope you like it ^^",
            "https://www.deviantart.com/zarory/art/Casual-Mulan-762500114",
            1917,
            44,
            60000
        ),
        Ad(1001, "!--AD--!", "Поддержите наших авторов на Patreon"),
        DeviantPicture(
            1,
            "Katara Sketch",
            "Zarory",
            R.drawable.poster2,
            "Had to draw my favorite Avatar character, Katara Slowly working my way through the gaang",
            "https://www.deviantart.com/zarory/art/Katara-Sketch-935980566",
            1683,
            28,
            199000
        ),
        DeviantPicture(
            2,
            "Frozen Fever - Anna",
            "imdrunkonTea",
            R.drawable.poster3,
            "Welcoming Spring with some Frozen Fever fanart :) such a lovely short film. And the Snowgies are adorable! I want one XD",
            "https://www.deviantart.com/imdrunkontea/art/Frozen-Fever-Anna-523899149",
            2062,
            180,
            64000
        ),
        DeviantPicture(
            3,
            "Anna",
            "imdrunkonTea",
            R.drawable.poster4,
            "Anna in her Frozen 2 garb!",
            "https://www.deviantart.com/imdrunkontea/art/Anna-801277994",
            1559,
            97,
            64000
        ),
        DeviantPicture(
            4,
            "Spirit Blossom Ahri",
            "imdrunkonTea",
            R.drawable.poster5,
            "lil' doodle of Ahri's latest skin!",
            "https://www.deviantart.com/imdrunkontea/art/Spirit-Blossom-Ahri-850236572",
            286,
            10,
            6000
        ),
        DeviantPicture(
            5,
            "KDA - The Dancer",
            "imdrunkonTea",
            R.drawable.poster6,
            "Kai'sa's new KDA outfit is tres parfait",
            "https://www.deviantart.com/imdrunkontea/art/KDA-The-Dancer-853644443",
            639,
            11,
            14000
        ),
        DeviantPicture(
            6,
            "Commission : Crystal-Cat44",
            "Sa-Dui",
            R.drawable.poster7,
            "Commission for :iconcrystal-cat44: Thank you so much!! ",
            "https://www.deviantart.com/sa-dui/art/Commission-Crystal-Cat44-932961567",
            937,
            6,
            473000
        ),
        DeviantPicture(
            7,
            "Commission - Magician",
            "NKSTUDIODIGITAL",
            R.drawable.poster8,
            "- Project Name: Magician; - Project: Commission for client; - Director: NK; - Artist: ToothBrushNH",
            "https://www.deviantart.com/nkstudiodigital/art/Commission-Magician-931227836",
            1341,
            38,
            517000
        ),
        DeviantPicture(
            8,
            "Triss Merigold",
            "MilliganVick",
            R.drawable.poster9,
            "CHARACTER  Triss Merigold - The Witcher 3; Torie as Triss",
            "https://www.deviantart.com/milliganvick/art/Triss-Merigold-933869032",
            329,
            4,
            28000
        ),
        DeviantPicture(
            9,
            "Yuna / Final Fantasy X - Summoner",
            "ADSouto",
            R.drawable.poster10,
            "CHARACTER  Final Fantasy X. Daughter of High Summoner Braska. Honest and determined, Yuna embarks on a pilgrimage to obtain the Final Aeon and defeat Sin. Yuna is learning the mystical art of summoning aeons—powerful spirits of yore.",
            "https://www.deviantart.com/adsouto/art/Yuna-Final-Fantasy-X-Summoner-932424050",
            733,
            11,
            65000
        )
    )
}