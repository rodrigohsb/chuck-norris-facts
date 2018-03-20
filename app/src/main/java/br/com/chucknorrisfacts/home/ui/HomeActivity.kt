package br.com.chucknorrisfacts.home.ui

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.View
import br.com.chucknorrisfacts.R
import br.com.chucknorrisfacts.home.adapter.HomeAdapter
import br.com.chucknorrisfacts.home.entry.SearchFactsEntryModel
import br.com.chucknorrisfacts.home.status.State
import br.com.chucknorrisfacts.home.viewmodel.HomeViewModel
import br.com.chucknorrisfacts.webservice.exceptions.*
import com.github.salomonbrys.kodein.LazyKodein
import com.github.salomonbrys.kodein.android.appKodein
import com.github.salomonbrys.kodein.instance
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.error_state.*
import kotlinx.android.synthetic.main.loading_state.*
import kotlinx.android.synthetic.main.waiting_for_input_state.*

/**
 * @rodrigohsb
 */
class HomeActivity : AppCompatActivity() {

    private lateinit var query: String

    val kodein by lazy { LazyKodein(appKodein) }

    private val homeAdapter = HomeAdapter()

    private val lManager by lazy { LinearLayoutManager(this) }

    private val composible = CompositeDisposable()

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

        showWaitingForInputView()

        initListeners()
    }

    private fun initListeners() {

        mainFAB.setOnClickListener { searchView.visibility = View.VISIBLE }

        searchView.setOnCloseListener {
            searchView.visibility = View.GONE
            true
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(queryText: String): Boolean {

                this@HomeActivity.query = queryText

                val disp = search(queryText)

                composible += disp

                return false
            }

            override fun onQueryTextChange(newText: String) = false
        })
    }

    private fun search(queryText: String): Disposable {
        return viewModel.searchContent(queryText)
                .subscribe({ status: State ->
                    handleStatus(status)
                }, { t ->
                    processError(t as Exception)
                })
    }

    override fun onResume() {
        super.onResume()
        composible += viewModel.fetchState()
                        .subscribe({
                            status: State -> handleStatus(status)
                        }, {
                            t -> processError(t as Exception)
                        })
    }

    private fun handleStatus(status: State) {
        clearView()
        when (status) {
            is State.WaitingForInput -> {
                clearView()
                showWaitingForInputView()
            }
            is State.Loading -> showLoadingView()
            is State.Sucess -> handleContent(status)
            is State.Error -> processError(status.exception)

        }
    }

    private fun handleContent(status: State.Sucess) {
        showSnackbar(status.searchFactsEntryModel)

        homeAdapter.clear()
        homeAdapter.addData(status.searchFactsEntryModel.homeEntryModel)
        recyclerView.apply {
            layoutManager = lManager
            adapter = homeAdapter
            setHasFixedSize(true)
        }

        recyclerView.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        composible.clear()
    }

    private fun showSnackbar(searchFactsEntryModel: SearchFactsEntryModel) {

        val size = searchFactsEntryModel.size
        val query = searchFactsEntryModel.query

        Snackbar.make(root,
                "$size resultados encontrados para a busca: ${query.toUpperCase()}",
                Snackbar.LENGTH_LONG).show()
    }

    private fun clearView() {
        hideErrorView()
        hideLoadingView()
        hideWaitingForInputView()
    }

    private fun hideErrorView() { errorRoot.visibility = View.GONE }

    private fun hideLoadingView() { loadingState.visibility = View.GONE }
    private fun showLoadingView() { loadingState.visibility = View.VISIBLE }

    private fun hideWaitingForInputView() { waitingForInputState.visibility = View.GONE }
    private fun showWaitingForInputView() { waitingForInputState.visibility = View.VISIBLE }

    private fun showErrorView(msgId: Int) {
        errorState.setText(msgId)
        retry()
        errorRoot.visibility = View.VISIBLE
    }

    private fun retry() = errorButton.setOnClickListener(RetryListener())

    private fun processError(t: Throwable) {
        clearView()
        when (t) {
            is TimeoutException -> showErrorView(R.string.timeout_error_message)
            is Error4XXException -> showErrorView(R.string.client_error_message)
            is NoNetworkException -> showErrorView(R.string.no_connection_error_message)
            is BadRequestException -> showErrorView(R.string.bad_request_error_message)
            is NoDataException -> showErrorView(R.string.empty_error_message)
            is Error5XXException -> {
                showErrorView(R.string.server_error_message)
                errorButton.visibility = View.GONE
            }
            else -> showErrorView(R.string.generic_error_message)
        }
    }

    inner class RetryListener: View.OnClickListener {
        override fun onClick(v: View) {
            clearView()
            composible += search(query)
        }
    }

}