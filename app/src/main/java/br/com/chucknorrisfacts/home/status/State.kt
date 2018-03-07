package br.com.chucknorrisfacts.home.status

import br.com.chucknorrisfacts.home.entry.SearchFactsEntryModel

/**
 * @rodrigohsb
 */
sealed class State {

    class Loading : State()
    class WaitingForInput : State()
    class Error(val exception: Exception) : State()
    data class Sucess(val searchFactsEntryModel: SearchFactsEntryModel) : State()
}