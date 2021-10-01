package com.castprogramms.elegion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import com.castprogramms.elegion.ui.calendar.bottom_sheet.EventBottomSheetFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.shuhart.materialcalendarview.CalendarDay
import com.shuhart.materialcalendarview.MaterialCalendarView
import com.shuhart.materialcalendarview.OnDateSelectedListener
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    lateinit var navController: NavController
    lateinit var navView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = findNavController(R.id.nav_host_fragment)
        navView = findViewById(R.id.nav_view)

        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.item_home, R.id.item_profile)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        val sheets = findViewById<FrameLayout>(R.id.standard_bottom_sheet)
        val behavior = BottomSheetBehavior.from(sheets)
//        behavior.state = BottomSheetBehavior.STATE_HIDDEN

        val calendar = findViewById<MaterialCalendarView>(R.id.calendarView)
        calendar.addOnDateChangedListener(object :OnDateSelectedListener{
            override fun onDateSelected(
                widget: MaterialCalendarView,
                date: CalendarDay,
                selected: Boolean
            ) {
//                Toast.makeText(this@MainActivity, date.day.toString(), Toast.LENGTH_SHORT).show()
//                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                val modalBottomSheet = EventBottomSheetFragment()
                modalBottomSheet.show(supportFragmentManager, EventBottomSheetFragment::class.toString())
            }
        })
    }
}
