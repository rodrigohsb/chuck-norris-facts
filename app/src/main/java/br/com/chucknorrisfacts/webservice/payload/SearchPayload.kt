package br.com.chucknorrisfacts.webservice.payload

import com.google.gson.annotations.SerializedName

/**
 * @rodrigohsb
 */
data class SearchPayload(val total: Int?,
                         @SerializedName("result") val searchList: List<SearchList>?)

data class SearchList(val id : String?,
                      val url : String?,
                      @SerializedName("value") val text : String?,
                      @SerializedName("icon_url") val iconPath : String?,
                      @SerializedName("category") val categories : List<String>?)