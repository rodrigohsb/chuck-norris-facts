package br.com.chucknorrisfacts.home.ui

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.View
import br.com.chucknorrisfacts.R
import br.com.chucknorrisfacts.base.BaseActivity
import br.com.chucknorrisfacts.webservice.exceptions.*
import br.com.chucknorrisfacts.home.adapter.HomeAdapter
import br.com.chucknorrisfacts.home.status.State
import br.com.chucknorrisfacts.home.viewmodel.HomeViewModel
import com.github.salomonbrys.kodein.LazyKodein
import com.github.salomonbrys.kodein.android.appKodein
import com.github.salomonbrys.kodein.instance
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @rodrigohsb
 */
class HomeActivity : BaseActivity() {

    val kodein by lazy { LazyKodein(appKodein) }

    private val homeAdapter = HomeAdapter()

    private val lManager by lazy { LinearLayoutManager(this) }

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>) =
                    kodein.value.instance<HomeViewModel>() as T
        }
        ViewModelProviders.of(this, factory).get(HomeViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainFAB.setOnClickListener {
            searchView.visibility = View.VISIBLE
        }

        searchView.setOnCloseListener {
            searchView.visibility = View.GONE
            true
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                initObserver()
                viewModel.searchContent(query)
                return false
            }

            override fun onQueryTextChange(newText: String) = false
        })

        showLoadingView()
    }

    private fun initObserver() {

        viewModel.fetchState()
                .subscribe{ status: State ->
                    clearView()
                    when (status) {
                        is State.Loading -> showLoadingView()
                        is State.WaitingForInput -> { }
                        is State.Error -> processError(status)
                        is State.Sucess -> handleContent(status)
                    }
                }
    }

    private fun handleContent(status: State.Sucess) {
        showSize(status.searchFactsEntryModel.size,
                status.searchFactsEntryModel.query)

        homeAdapter.clear()
        homeAdapter.addData(status.searchFactsEntryModel.homeEntryModel)
        recyclerView.apply {
            layoutManager = lManager
            adapter = homeAdapter
            setHasFixedSize(true)
        }

        recyclerView.visibility = View.VISIBLE
    }

    private fun processError(status: State.Error) {
        showEmptyView()
        when (status.exception) {
            is TimeoutException -> textView.text = "TimeoutException"
            is Error4XXException -> textView.text = "Ops,\n não é possível fazer pesquisa com termo."
            is Error5XXException -> textView.text = "Ops!\n Por favor, tente mais tarde."
            is NoNetworkException -> textView.text = "NoNetworkConnection"
            is BadRequestException -> textView.text = "NoNetworkConnection"
            is NoDataException -> textView.text = "não há resultados para o termo."
            else -> textView.text = "GenericException"
        }
    }

    private fun showSize(size: Int, query: String) {
        Snackbar.make(root,
                "$size resultados encontrados para a busca: ${query.toUpperCase()}",
                Snackbar.LENGTH_LONG)
                .show()
    }

    override fun showEmptyView() {
        textView.visibility = View.VISIBLE
    }

    override fun hideEmptyView() {
        textView.visibility = View.GONE
    }

    override fun showLoadingView() {
        loading.visibility = View.VISIBLE
    }

    override fun hideLoadingView() {
        loading.visibility = View.GONE
    }

    override fun showTimeoutView() {
        textView.visibility = View.VISIBLE
    }

    override fun hideTimeoutView() {
        textView.visibility = View.GONE
    }

    override fun showGenericErrorView() {
        textView.visibility = View.VISIBLE
    }

    override fun hideGenericErrorView() {
        textView.visibility = View.GONE
    }

    override fun showNoConnectionView() {
        textView.visibility = View.VISIBLE
    }

    override fun hideNoConnectionView() {
        textView.visibility = View.GONE
    }
}