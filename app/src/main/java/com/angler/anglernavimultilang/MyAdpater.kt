package com.angler.anglernavimultilang

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView

class MyAdpater(private val anglerList: ArrayList<Angler>) : RecyclerView.Adapter<MyAdpater.MyViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = anglerList[position]
        holder.titleImage.setImageResource(currentItem.titleImage)
        holder.heading.text = currentItem.heading

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailedActivity::class.java)
            intent.putExtra("imageResId", currentItem.titleImage)
            intent.putExtra("heading", currentItem.heading)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return  anglerList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val titleImage : ShapeableImageView = itemView.findViewById(R.id.title_image)
        val heading : TextView = itemView.findViewById(R.id.heading)
    }
}