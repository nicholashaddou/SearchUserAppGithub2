package com.example.searchuserappgithub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.searchuserappgithub.databinding.ActivityMainBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private var listUserData: ArrayList<UserData> = ArrayList()
    private lateinit var adapter: UserAdapter
    private lateinit var binding: ActivityMainBinding

    companion object {
        private val TAG = MainActivity::class.java
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Explore"

        adapter = UserAdapter(listUserData)

        //Log.d("Adapter", adapter.toString())
        configureRecyclerView()
        searchUser()
        getUser()
    }

    //binding does not initialize for whatever reason
    private fun configureRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(binding.recyclerView.context)
        binding.recyclerView.setHasFixedSize(true)
    }
    private fun searchUser() {
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String): Boolean {
            if (query.isEmpty()) {
                return true
            } else {
                listUserData.clear()
                getUserSearch(query)
            }
            return true
        }
        override fun onQueryTextChange(newText: String): Boolean {
            return false
        }
    })
    }

    private fun getUser() {
        //getuser displays empty list, however there is an 'unauthorized' error, therefore i suspect my token is declined
        binding.progressBar.visibility = View.VISIBLE
        val asyncClient = AsyncHttpClient()
        //works now, had to renew token
        asyncClient.addHeader("Authorization", "token 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")
        asyncClient.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users"
        asyncClient.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                //progressbar does not go invisible
                binding.progressBar.visibility = View.INVISIBLE
                val result = String(responseBody)
                try {
                    val jsonArray = JSONArray(result)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val username: String = jsonObject.getString("login")
                        getUserDetail(username)
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT)
                        .show()
                    e.printStackTrace()
                }
            }
            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                binding.progressBar.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Unauthorized"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    private fun getUserSearch(id: String) {
        binding.progressBar.visibility = View.VISIBLE
        val asyncClient = AsyncHttpClient()
        asyncClient.addHeader("Authorization", "token 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")
        asyncClient.addHeader("User-Agent", "request")
        val url = "https://api.github.com/search/users?q=$id"
        asyncClient.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                binding.progressBar.visibility = View.INVISIBLE
                val result = String(responseBody)
                Log.d(TAG.toString(), result)
                try {
                    val jsonArray = JSONObject(result)
                    val item = jsonArray.getJSONArray("items")
                    for (i in 0 until item.length()) {
                        val jsonObject = item.getJSONObject(i)
                        val username: String = jsonObject.getString("login")
                        getUserDetail(username)
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT)
                        .show()
                    e.printStackTrace()
                    if(result==null){
                        Toast.makeText(this@MainActivity, "Not found", Toast.LENGTH_SHORT)
                    }
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                binding.progressBar.visibility = View.INVISIBLE
                val errorLog = when (statusCode) {
                    401 -> "$statusCode : Unauthorized"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@MainActivity, errorLog, Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    /*
    TODO:("show data using recyclerview")
    TODO:("Log adapter, is not initialized")
     */

    private fun getUserDetail(id: String) {
        binding.progressBar.visibility = View.VISIBLE
        val asyncClient = AsyncHttpClient()
        asyncClient.addHeader("Authorization", "token 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")
        asyncClient.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/$id"
        asyncClient.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                binding.progressBar.visibility = View.INVISIBLE
                val result = String(responseBody)
                Log.d(TAG.toString(), result)
                try {
                    val jsonObject = JSONObject(result)
                    val username: String = jsonObject.getString("login").toString()
                    val name: String = jsonObject.getString("name").toString()
                    val avatar: String = jsonObject.getString("avatar_url").toString()
                    val company: String = jsonObject.getString("company").toString()
                    val location: String = jsonObject.getString("location").toString()
                    val repository: String = jsonObject.getString("public_repos")
                    val followers: String = jsonObject.getString("followers")
                    val following: String = jsonObject.getString("following")
                    listUserData.add(
                        UserData(
                            username,
                            name,
                            avatar,
                            company,
                            location,
                            repository,
                            followers,
                            following
                        )
                    )
                    showRecyclerList()
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT)
                        .show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                binding.progressBar.visibility = View.INVISIBLE
                val errorLog = when (statusCode) {
                    401 -> "$statusCode : Unauthorized"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@MainActivity, errorLog, Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    private fun showSelectedUser(dataUser: UserData) {
        UserData(
            dataUser.username,
            dataUser.name,
            dataUser.avatar,
            dataUser.company,
            dataUser.location,
            dataUser.repository,
            dataUser.followers,
            dataUser.following
        )
        val intent = Intent(this@MainActivity, UserDetail::class.java)
        intent.putExtra(UserDetail.EXTRA_DATA, dataUser)

        this@MainActivity.startActivity(intent)
        Toast.makeText(
            this,
            "${dataUser.name}",
            Toast.LENGTH_SHORT
        ).show()

    }
    private fun showRecyclerList() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        val listDataAdapter = UserAdapter(userFilterList)
        binding.recyclerView.adapter = adapter

        listDataAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(dataUsers: UserData) {
                showSelectedUser(dataUsers)
            }
        })
    }
}