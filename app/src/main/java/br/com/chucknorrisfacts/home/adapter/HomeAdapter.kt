package br.com.chucknorrisfacts.home.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.chucknorrisfacts.R
import br.com.chucknorrisfacts.home.entry.HomeEntryModel
import kotlinx.android.synthetic.main.layout_item.view.*

/**
 * @rodrigohsb
 */
class HomeAdapter : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    private var repositories: MutableList<HomeEntryModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val root = layoutInflater.inflate(R.layout.layout_item, parent, false)
        return ViewHolder(root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repository = repositories[position]
        holder.bind(repository)
    }

    override fun getItemCount(): Int = repositories.size

    class ViewHolder(private val rootView: View) : RecyclerView.ViewHolder(rootView) {

        fun bind(content: HomeEntryModel) {
            rootView.text.text = content.text
            rootView.category.text = content.category
        }
    }

    fun addData(list: List<HomeEntryModel>) {
        repositories.addAll(list)
        notifyDataSetChanged()
    }

    fun clear() = repositories.clear()
}