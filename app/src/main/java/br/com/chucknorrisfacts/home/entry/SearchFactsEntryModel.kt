package br.com.chucknorrisfacts.home.entry

/**
 * @rodrigohsb
 */
data class SearchFactsEntryModel(val size: Int, val query: String,
                                 val homeEntryModel: List<HomeEntryModel>)