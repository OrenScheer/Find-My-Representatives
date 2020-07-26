package com.example.findmyrepresentatives

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recyclerview_item_row.view.*

class RecyclerAdapter(private val reps: List<Representative>)
    : RecyclerView.Adapter<RecyclerAdapter.RepresentativeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepresentativeHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item_row, parent, false)
        return RepresentativeHolder(inflatedView)
    }

    override fun getItemCount(): Int = reps.size

    override fun onBindViewHolder(holder: RecyclerAdapter.RepresentativeHolder, position: Int) {
        val rep = reps[position]
        holder.bindRepresentative(rep)
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
            Picasso.get().load(representative.photo_url).into(view.itemImage)
            view.itemDate.text = representative.name
            view.itemDescription.text = representative.representative_set_name
        }
    }
}