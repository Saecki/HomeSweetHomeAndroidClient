package bedbrains.platform

import java.util.*

object UIDProvider {

    val newUID: String
        get() = UUID.randomUUID().toString()

}