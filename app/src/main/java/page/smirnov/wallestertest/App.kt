package page.smirnov.wallestertest

import android.app.Application
import page.smirnov.wallester.core_persistence.data.repository.FavoritesRepositoryHolder

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        initFavoritesDatabase()
    }

    /**
     * We should never do this in production — it must be done with DI on demand
     */
    private fun initFavoritesDatabase() {
        FavoritesRepositoryHolder.initialize(this)
    }
}
