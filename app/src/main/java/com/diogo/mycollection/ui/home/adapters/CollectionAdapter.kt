package com.diogo.mycollection.ui.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.diogo.mycollection.R
import com.diogo.mycollection.data.model.CollectionItem
import com.diogo.mycollection.databinding.ItemCollectionHomeBinding
import com.diogo.mycollection.core.extensions.toDisplayName

class CollectionAdapter(
    private val items: List<CollectionItem>,
    private val onItemClick: (CollectionItem) -> Unit
) : RecyclerView.Adapter<CollectionAdapter.CollectionViewHolder>() {

    inner class CollectionViewHolder(
        val binding: ItemCollectionHomeBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CollectionViewHolder {
        val binding = ItemCollectionHomeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CollectionViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: CollectionViewHolder,
        position: Int
    ) {
        val item = items[position]
        with(holder.binding) {
            itemTitle.text = item.title
            itemAuthor.text = item.author
            itemTag.text = item.type.toDisplayName(root.context)
            itemRating.text = item.rating.toString()

            val hasImage = !item.imageUrl.isNullOrBlank()

            if (hasImage) {
                itemImage.visibility = View.VISIBLE
                itemPlaceholderText.visibility = View.GONE

                itemImage.load(item.imageUrl) {
                    crossfade(true)
                    placeholder(R.drawable.bg_image_placeholder)
                    error(R.drawable.bg_image_placeholder)
                }
            } else {
                itemImage.visibility = View.GONE
                itemPlaceholderText.visibility = View.VISIBLE
                itemPlaceholderText.text = item.type.toDisplayName(root.context)
            }
            root.setOnClickListener { onItemClick(item) }
        }
    }

    override fun getItemCount(): Int = items.size
}