package ed.maevski.androidpraktika.domain

import ed.maevski.androidpraktika.data.MainRepository

class Interactor(val repo: MainRepository) {
    fun getPicturesDB(): List<Item> = repo.itemsRV
}