package page.smirnov.wallestertest.domain.interactor

import page.smirnov.wallester.core_persistence.data.model.Beer
import page.smirnov.wallestertest.data.model.SortingMode

interface SortBeers {
    fun exec(beers: List<Beer>, sortingMode: SortingMode): List<Beer>
}

class SortBeersImpl : SortBeers {

    /**
     * In fact, we can also sort in-place, but it'll require a mutable list.
     * It would give higher performance (less GC), but it's not kotlin-idiomatic
     */
    override fun exec(beers: List<Beer>, sortingMode: SortingMode): List<Beer> {
        return when (sortingMode) {
            // Unfortunately «when» can't be placed in sortedBy as the compiler can't resolve returning type
            SortingMode.NAME -> beers.sortedBy { it.name }
            SortingMode.ABV -> beers.sortedBy { it.abv }
            SortingMode.EBC -> beers.sortedBy { it.ebc }
            SortingMode.IBU -> beers.sortedBy { it.ibu }
        }
    }
}
