package com.example.findmyrepresentatives

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_results.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * The results activity.
 * This screen shows a list of representatives.
 * @author Oren Scheer
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

        val query = intent.getStringExtra("query") // Sent in from MainActivity
        if (query != null) {
            getRepresentative(query)
        }

        back_button.setOnClickListener() {// For if the postal code doesn't actually exist, user can press back button in two ways
            finish()
        }
    }

    /**
     * Function that calls the Represent API and updates the RecyclerView
     * of the activity.
     * @param query the query url (point/postcode + coordinates/postal code) that the user inputted
     */
    private fun getRepresentative(query: String) {
        val data: MutableMap<String, String> = HashMap()
        if (intent.getStringExtra("latlong") != null) { // If location is being used, then the latitude and longitude must be passed to the Retrofit request handler as a Map
            data["point"] = intent.getStringExtra("latlong")!! // For use as a QueryMap in the API call
        }
        RepresentApi.retrofitService.getRepresentatives(query, data).enqueue(
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
                    else if (response.body() == null) { // Shouldn't happen
                        loading_list.visibility = View.GONE // Remove loading message
                        invalid_code.visibility = View.VISIBLE // Display error message
                        invalid_code.text = getString(R.string.no_internet)
                        back_button.visibility = View.VISIBLE // Display additional button to go back
                    }
                    else {
                        loading_list.visibility = View.GONE // Loading done
                        val body: RepresentDataSet = response.body()!! // Body won't be null
                        val reps: MutableList<Representative> // The API, using postal codes, returns two lists of representatives based on two different methods (center and boundaries of postal code area)
                        if (body.objects != null) { // There is also objects. This is used when finding by device location, as the Represent API returns only a list of representatives
                            reps = body.objects // In this case, these are all the representatives
                        }
                        else if (body.representatives_centroid == null && body.representatives_concordance == null) { // For some reason, no representatives found at this postal code (happens occasionally)
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