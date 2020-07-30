package com.example.findmyrepresentatives

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_results.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.IllegalArgumentException

/**
 * The results activity.
 * This screen shows a list of representatives.
 */
class ResultsActivity (): AppCompatActivity() {
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: RecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        linearLayoutManager = LinearLayoutManager(this) // Responsible for positioning individual views
        recyclerView.layoutManager = linearLayoutManager

        adapter = RecyclerAdapter(emptyList()) // Responsible for making individual views for each representative
        recyclerView.adapter = adapter

        val postalCode = intent.getStringExtra("postalCode") // Sent in from MainActivity
        if (postalCode != null) {
            getRepresentative(postalCode)
        }

        back_button.setOnClickListener() {// For if the postal code doesn't actually exist, user can press back button in two ways
            finish()
        }
    }

    /**
     * Function that calls the Represent API and updates the RecyclerView
     * of the activity.
     * @param postalCode the postal code that the user inputted
     */
    private fun getRepresentative(postalCode: String) {
        RepresentApi.retrofitService.getRepresentatives(postalCode).enqueue(
            object: Callback<RepresentDataSet> {
                override fun onFailure(call: Call<RepresentDataSet>, t: Throwable) {
                    loading_list.visibility = View.GONE // Remove loading message
                    invalid_code.visibility = View.VISIBLE // Display error message
                    invalid_code.text = getString(R.string.no_internet)
                    back_button.visibility = View.VISIBLE // Display additional button to go back
                }

                override fun onResponse(call: Call<RepresentDataSet>, response: Response<RepresentDataSet>) {
                    if (response.code() == 404) { // Postal code is of a valid format, but doesn't actually exist
                        loading_list.visibility = View.GONE // Remove loading message
                        invalid_code.visibility = View.VISIBLE // Display error message
                        back_button.visibility = View.VISIBLE // Display additional button to go back
                    }
                    else if (response.body() == null) {
                        Log.d("null response", "shouldn't happen")
                        throw NullPointerException()
                    }
                    else {
                        loading_list.visibility = View.GONE // Loading done
                        val body: RepresentDataSet = response.body()!! // Body won't be null
                        val reps: MutableList<Representative> // The API returns two lists of representatives based on two different methods (center and boundaries of postal code area)
                        if (body.representatives_centroid == null && body.representatives_concordance == null) { // For some reason, no representatives found at this postal code (shouldn't happen)
                            loading_list.visibility = View.GONE // Remove loading message
                            invalid_code.visibility = View.VISIBLE // Display error message
                            invalid_code.text = getString(R.string.no_results)
                            back_button.visibility = View.VISIBLE // Display additional button to go back
                            return
                        }
                        else if (body.representatives_centroid == null) { // Most representatives are normally in representatives_centroid
                            reps = body.representatives_concordance!! // But if not, representatives_concordance is the entire list
                        }
                        else {
                            reps = body.representatives_centroid // Otherwise, the list is representatives_centroid
                            reps.let {
                                body.representatives_concordance?.let(it::addAll) // And add anything from representatives_concordance as well (will still work if null)
                            }
                        }
                        reps.apply { // Sort the results (top -> federal representative -> provincial representative -> mayor -> city councillor -> anything else (school board trustees, etc.)
                            var ind : Int = indexOfFirst { // Check if there is a city councillor
                                it.elected_office == "Councillor"
                            }
                            if (ind != -1) { // If there is, move it to the start
                                add(0, removeAt(ind))
                            }
                            ind = indexOfFirst { // Same for all the other high-priority representatives
                                it.elected_office == "Mayor"
                            }
                            if (ind != -1) {
                                add(0, removeAt(ind))
                            }
                            ind = indexOfFirst {
                                it.elected_office == "MPP"
                                        || it.elected_office == "MLA"
                                        || it.elected_office == "MNA"
                            }
                            if (ind != -1) {
                                add(0, removeAt(ind))
                            }
                            ind = indexOfFirst {
                                it.elected_office == "MP"
                                        && it.representative_set_name == "House of Commons"
                            }
                            if (ind != -1) {
                                add(0, removeAt(ind))
                            }
                        }
                        adapter.reps = reps // Update the RecyclerView data
                        adapter.notifyDataSetChanged() // Trigger changing the RecyclerView
                    }
                }
            }
        )
    }
}