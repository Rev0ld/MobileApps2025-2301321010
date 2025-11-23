package com.example.reviewapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import java.io.File

class ReviewAdapter : ListAdapter<Review, ReviewAdapter.VH>(DIFF) {

    public var lambdaOnClick : ((Review?) -> Unit)? = null
    public var lambdaOnMapClick: ((Review?) -> Unit)? = null


    object DIFF : DiffUtil.ItemCallback<Review>() {
        override fun areItemsTheSame(oldItem: Review, newItem: Review) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Review, newItem: Review) = oldItem == newItem
    }

    class ItemReviewBinding private constructor(val root: View) {
        val tvName: TextView = root.findViewById(R.id.tvName)
        val tvRating: TextView = root.findViewById(R.id.tvRating)
        val img: ImageView = root.findViewById(R.id.img)
        val btnViewOnMap: Button = root.findViewById(R.id.btnViewOnMap)


        companion object {
            fun inflate(inflater: LayoutInflater, parent: ViewGroup, attachToParent: Boolean): ReviewAdapter.ItemReviewBinding {
                val view = inflater.inflate(R.layout.item_review, parent, attachToParent)
                return ItemReviewBinding(view)
            }
        }
    }
    inner class VH(val b: ItemReviewBinding) : RecyclerView.ViewHolder(b.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val c = getItem(position)
        holder.b.tvName.text = c.name
        holder.b.tvRating.text = c.rating

        val imgView = holder.b.img

        when {
            !c.photoPath.isNullOrBlank() && File(c.photoPath).exists() -> imgView.load(File(c.photoPath))
            else -> imgView.setImageResource(android.R.drawable.sym_def_app_icon)
        }

        holder.itemView.setOnClickListener { onItemClick(c) }
        holder.b.btnViewOnMap.setOnClickListener { lambdaOnMapClick?.let { it(c) } }

    }
    private fun onItemClick(c: Review?) {
        lambdaOnClick?.let { it(c) }

    }






















}