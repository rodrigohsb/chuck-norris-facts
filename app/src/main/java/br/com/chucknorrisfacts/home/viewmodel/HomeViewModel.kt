package br.com.chucknorrisfacts.home.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import br.com.chucknorrisfacts.home.handler.FactHandler
import br.com.chucknorrisfacts.home.service.HomeService
import br.com.chucknorrisfacts.home.status.HomeStatus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @rodrigohsb
 */
class HomeViewModel(private val homeService: HomeService,
                    private val factHandler: FactHandler): ViewModel() {

    var homeStatus: MutableLiveData<HomeStatus> = MutableLiveData()

    init { homeStatus.value = HomeStatus.WaitingForInput() }

    fun searchContent(query: String){

        homeStatus.value = HomeStatus.Loading()

        homeService
            .searchFacts(query)
            .map { factHandler.convert(it,query) }
            .subscribe({
                homeStatus.value = HomeStatus.Content(it)
            }, { t ->
                homeStatus.value = HomeStatus.Error(t as Exception)
            })
    }
}