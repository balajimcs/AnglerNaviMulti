package com.angler.anglernavimultilang

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.google.android.material.navigation.NavigationView
import java.util.*


class MainActivity : LocalizationActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout
    var currentLanguage = "en"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val headerView = navigationView.getHeaderView(0)
        val btnChange = headerView.findViewById<Button>(R.id.btnChange)
//        val btnChange1 = headerView.findViewById<Button>(R.id.btnChange1)
        btnChange.setOnClickListener {
            // Toggle the language
            if (currentLanguage == "ta") {
                setLanguage("en") // Set to Tamil
                currentLanguage = "en"
                btnChange.text = "Change to Tamil"
            } else {
                setLanguage("ta") // Set to English
                currentLanguage = "ta"
                btnChange.text = "Change to English"
            }
        }


        drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        navigationView.setNavigationItemSelectedListener(this)
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, DashboardFragment()).commit()
            navigationView.setCheckedItem(R.id.nav_item1)
        }


    }




    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_item1 -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, DashboardFragment()).commit()
            R.id.nav_item2 -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, WebFragment()).commit()
            R.id.nav_item3 -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ViewFragment()).commit()
            R.id.nav_item4 -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, GridViewFragment()).commit()
            R.id.nav_item5 -> {
                val intent = Intent(this, AnglerMap::class.java)
                startActivity(intent)
            }

            }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            ExitAlertUtils.showExitAlert(this) {
                super.onBackPressed() // Finish the activity when exit is confirmed
            }
        }
    }


}