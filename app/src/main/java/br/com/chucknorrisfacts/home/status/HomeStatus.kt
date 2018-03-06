package br.com.chucknorrisfacts.home.status

import br.com.chucknorrisfacts.home.entry.SearchFactsEntryModel

/**
 * @rodrigohsb
 */
sealed class HomeStatus {

    class Loading : HomeStatus()
    class WaitingForInput : HomeStatus()
    class Error(val exception: Exception) : HomeStatus()
    data class Content(val searchFactsEntryModel: SearchFactsEntryModel) : HomeStatus()
}