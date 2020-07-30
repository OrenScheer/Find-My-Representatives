package com.example.findmyrepresentatives

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.recyclerview_item_row.view.*
import java.util.*

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

        @SuppressLint("SetTextI18n")
        fun bindRepresentative(representative: Representative) {
            this.representative = representative
            val url: String = representative.photo_url.replace("http:", "https:", ignoreCase = true)
            Glide.with(view)
                .load(url)
                .placeholder(R.drawable.unknown_person)
                .into(view.rep_portrait)
            view.rep_name.text = representative.name
            view.rep_office_riding.text = representative.elected_office +
                    (if (representative.elected_office.toLowerCase(Locale.US) == "mayor" || representative.elected_office.toLowerCase(Locale.US) == "chair") " of "
                    else " for ") + representative.district_name
            view.rep_office_riding.text = view.rep_office_riding.text
            if (!representative.party_name.isBlank()) {
                view.rep_party.visibility = View.VISIBLE
                view.rep_party.text = representative.party_name

                val contains: (String) -> Boolean = {party: String -> party in representative.party_name.toLowerCase(Locale.US)}
                view.colour_bar.setBackgroundColor(Color.parseColor(
                    when {
                        contains("green") -> "#3D9835"
                        contains("conservative") -> "#124072"
                        contains("liberal")  || contains("libéral") -> "#D91920"
                        contains("ndp") || contains("new democratic") -> "#F58220"
                        contains("solidaire") -> "#FF5505"
                        contains("bloc") -> "#5EB0DD"
                        contains("coalition avenir") -> "#00A8E7"
                        contains("parti québécois") -> "#15336F"
                        else -> "#AFAFAF"
                    }
                ))
            }
            else {
                view.rep_party.visibility = View.GONE
                view.colour_bar.setBackgroundColor(Color.parseColor("#AFAFAF"))
            }
            view.rep_legislature.text = representative.representative_set_name
        }
    }
}