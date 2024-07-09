package com.mcompany.a7minuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mcompany.a7minuteworkout.databinding.ActivityHistoryBinding
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {


    private var binding : ActivityHistoryBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        setSupportActionBar(binding?.toolbarHistoryActivity)

        //Navigate Up

        if (supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding?.toolbarHistoryActivity?.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val dao = (application as WorkoutApp).db.historyDao()

        getAllCompletedDates(dao)



    }


    private fun getAllCompletedDates(dao: HistoryDao){

        Log.e("Date: ", "Loading")

        lifecycleScope.launch {
            dao.fetchAllDates().collect{
                dateList ->

                if(dateList.isNotEmpty()){
                    binding?.tvHistory?.visibility = View.VISIBLE
                    binding?.rvHistory?.visibility = View.VISIBLE
                    binding?.tvNoDataAvailable?.visibility = View.GONE

                    val dates = ArrayList<String>()

                    for (i in dateList){
                        dates.add(i.date)
                    }

                    binding?.rvHistory?.layoutManager = LinearLayoutManager(this@HistoryActivity)

                    val sortedDates = dateList.sortedByDescending { it.date }

                    val historyAdapter = HistoryAdapter(ArrayList(sortedDates.map { it.date }))


//                    val historyAdapter = HistoryAdapter(ArrayList(dates))

                    binding?.rvHistory?.adapter = historyAdapter



                } else{
                    binding?.tvHistory?.visibility = View.GONE
                    binding?.rvHistory?.visibility = View.GONE
                    binding?.tvNoDataAvailable?.visibility = View.VISIBLE
                }
            }
        }



    }

}