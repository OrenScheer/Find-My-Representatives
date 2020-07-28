package com.example.findmyrepresentatives

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.recyclerview_item_row.view.*

class RecyclerAdapter(private var reps: List<Representative>)
    : RecyclerView.Adapter<RecyclerAdapter.RepresentativeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepresentativeHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item_row, parent, false)
        return RepresentativeHolder(inflatedView)
    }

    override fun getItemCount(): Int = reps.size

    override fun onBindViewHolder(holder: RepresentativeHolder, position: Int) {
        val rep = reps[position]
        holder.bindRepresentative(rep)
    }

    fun setRepresentatives(representatives: List<Representative>) {
        reps = representatives
    }

    class RepresentativeHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        private var view: View = v
        private var representative: Representative? = null

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            Log.d("RecyclerView", "CLICK!")
        }

        companion object {
            private val REP_KEY = "REP"
        }

        fun bindRepresentative(representative: Representative) {
            this.representative = representative
            if (representative.photo_url != "") {
                val url: String = representative.photo_url.replace("http:", "https:", ignoreCase = true)
                Glide.with(view).load(url).into(view.itemImage)
                Log.d("photo", url)
            }
            else {
                view.itemImage.setImageResource(R.drawable.unknown_person)
            }
            view.itemDate.text = representative.name
            view.itemDescription.text = representative.representative_set_name
        }
    }
}