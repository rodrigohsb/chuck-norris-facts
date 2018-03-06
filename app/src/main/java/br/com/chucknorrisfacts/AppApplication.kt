package br.com.chucknorrisfacts

import android.app.Application
import br.com.chucknorrisfacts.di.Injector
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.KodeinAware
import com.github.salomonbrys.kodein.conf.ConfigurableKodein

/**
 * @rodrigohsb
 */
class AppApplication: Application(), KodeinAware {

    override val kodein = ConfigurableKodein(mutable = true)

    override fun onCreate() {
        super.onCreate()
        resetInjection()
    }

    fun addModule(activityModules: Kodein.Module) {
        kodein.addImport(activityModules, true)
    }

    fun resetInjection() {
        kodein.clear()
        kodein.addImport(appDependencies(), true)
    }

    private fun appDependencies() = Injector(this).dependencies
}