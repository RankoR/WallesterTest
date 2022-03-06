package page.smirnov.wallester.core_network.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Should be in a separate feature module like «:beers», but it's too complicated for a test project
 * Should use KotlinX serialization, but we can't use it
 */
@Parcelize
data class Beer(
    val id: Long,
    val name: String,
    val abv: Float,
    val ebc: Float,
    val ibu: Float
) : Parcelable
