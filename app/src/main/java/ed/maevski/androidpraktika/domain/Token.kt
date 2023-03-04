package ed.maevski.androidpraktika.domain

import javax.inject.Inject

data class Token @Inject constructor(
    override var tokenKey: String,
    override var status: Boolean = false) : AbstractToken

interface AbstractToken {
    var tokenKey: String
    var status: Boolean
}