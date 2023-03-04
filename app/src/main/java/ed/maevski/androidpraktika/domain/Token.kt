package ed.maevski.androidpraktika.domain

import javax.inject.Inject

data class Token (
    override var tokenKey: String,
    override var status: Boolean = false) : AbstractToken {
    @Inject constructor() : this("",false)
}

interface AbstractToken {
    var tokenKey: String
    var status: Boolean
}