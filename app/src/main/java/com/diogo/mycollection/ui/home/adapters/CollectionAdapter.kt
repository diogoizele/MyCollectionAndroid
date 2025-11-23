package com.diogo.mycollection.ui.home.adapters

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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
            println("item: $item")

            itemTitle.text = item.title

            if (item.author.isNullOrBlank()) {
                itemAuthor.visibility = View.GONE
            } else {
                itemAuthor.text = item.author
                itemAuthor.visibility = View.VISIBLE
            }
            itemTag.text = item.type.toDisplayName(root.context)
            itemRating.text = item.rating.toString()

            if (item.image != null) {
                itemImage.visibility = View.VISIBLE
                itemPlaceholderText.visibility = View.GONE

                val base64 = item.image.substringAfter(",")
                val bytes = Base64.decode(base64, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

                itemImage.setImageBitmap(bitmap)
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