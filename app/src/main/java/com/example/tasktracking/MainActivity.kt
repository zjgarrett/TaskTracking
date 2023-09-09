package com.example.tasktracking

import android.content.Context
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tasktracking.databinding.ActivityMainBinding
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.DurationUnit

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        // getting the recyclerview by its id
        val recyclerview = findViewById<RecyclerView>(R.id.main_recycler)

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)

        // ArrayList of class ItemsViewModel
        val data = ArrayList<Task>()

        // This loop will create 20 Views containing
        // the image with the count of view
//        for (i in 1..20) {
//            data.add(ItemsViewModel(R.drawable.ic_baseline_folder_24, "Item " + i))
//        }

        val sD = 1
        val tC = 2.toLong()
        val tG = 4.toLong()
        val unit = DurationUnit.HOURS.ordinal
        data.add(DurationTask("First task", sD, tG, tC, unit))
        data.add(RepetitionTask("Second Task", sD, 6, 1))
        data.add(DurationTask("First task", sD, tG, tC, unit))
        data.add(DurationTask("First task", sD, tG, tC, unit))
        data.add(DurationTask("First task", sD, tG, tC, unit))
        data.add(DurationTask("First task", sD, tG, tC, unit))
        data.add(DurationTask("First task", sD, tG, tC, unit))
        data.add(DurationTask("First task", sD, tG, tC, unit))
        data.add(DurationTask("First task", sD, tG, tC, unit))
        data.add(DurationTask("First task", sD, tG, tC, unit))
        data.add(DurationTask("First task", sD, tG, tC, unit))
        data.add(DurationTask("First task", sD, tG, tC, unit))
        data.add(DurationTask("First task", sD, tG, tC, unit))
        data.add(DurationTask("First task", sD, tG, tC, unit))
        data.add(DurationTask("First task", sD, tG, tC, unit))

        // This will pass the ArrayList to our Adapter
        val adapter = TaskAdapter(data)

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}