package com.sirius.sample.ui.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sirius.sample.R
import com.sirius.sample.base.ui.SimpleBaseRecyclerViewAdapter
import com.sirius.sample.databinding.ViewItemsTagsBinding
import com.sirius.sample.models.ui.ItemTags


class TagsListAdapter(private val tagsClickAction:  (ItemTags) -> Unit) :
    SimpleBaseRecyclerViewAdapter<ItemTags, TagsListAdapter.TagsViewHolder>() {


    override fun onBind(holder: TagsViewHolder?, position: Int) {
        val item = getItem(position)
        holder?.bind(item,tagsClickAction)
    }


    class TagsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: ViewItemsTagsBinding? = DataBindingUtil.bind<ViewItemsTagsBinding>(itemView)
        fun bind(item: ItemTags,
                 tagsClickAction: (ItemTags) -> Unit) {
            binding?.model = item
            binding?.root?.setOnClickListener {
                tagsClickAction .invoke(item)
            }
        }
    }

    override fun getLayoutRes(): Int {
        return R.layout.view_items_tags
    }

    override fun getViewHolder(
        parent: ViewGroup?,
        layoutRes: Int,
        viewType: Int
    ): TagsViewHolder {
        val view =
            LayoutInflater.from(parent?.context).inflate(getLayoutRes(), parent, false)
        return TagsViewHolder(view)
    }


}