package com.example.searchuserappgithub

import android.app.Person
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.user_detail.*

class UserDetail : AppCompatActivity() {
    companion object {
        const val EXTRA_DATA = "extra_data"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_detail)
        setData()
        viewPagerConfig()
    }

    private fun viewPagerConfig() {
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)

        supportActionBar?.elevation = 0f
    }


    private fun setActionBarTitle(title: String) {
        if (supportActionBar != null) {
            this.title = title
        }
    }

    private fun setData() {
        val dataUser = intent.getParcelableExtra<Person>(EXTRA_DATA)!! as UserData
        dataUser.name?.let { setActionBarTitle(it) }
        var detail_name : TextView = findViewById(R.id.detail_name)
        var detail_username : TextView = findViewById(R.id.detail_username)
        var detail_company : TextView = findViewById(R.id.detail_company)
        var detail_location : TextView = findViewById(R.id.detail_location)
        var detail_repository : TextView = findViewById(R.id.detail_repository)
        var detail_avatar : ImageView = findViewById(R.id.detail_avatar)
        detail_name.text = dataUser.name
        detail_username.text = dataUser.username
        detail_company.text = getString(R.string.company, dataUser.company)
        detail_location.text = getString(R.string.location, dataUser.location)
        detail_repository.text = getString(R.string.repository, dataUser.repository)
        Glide.with(this)
            .load(dataUser.avatar)
            .into(detail_avatar)
    }

}