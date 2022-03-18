package com.example.searchuserappgithub

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

var followersFilterList = ArrayList<UserData>()

class followerAdapter(listUser: ArrayList<UserData>) : RecyclerView.Adapter<followerAdapter.ListViewHolder>() {
    init {
        followersFilterList = listUser
    }

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.user, viewGroup, false)
        val search = ListViewHolder(view)
        mcontext = viewGroup.context
        return search
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val dataHolder = followersFilterList[position]
        Glide.with(holder.itemView.context)
            .load(dataHolder.avatar)
            .apply(RequestOptions().override(250, 250))
            .into(holder.imgAvatar)
        holder.txtUsername.text = dataHolder.username
        holder.txtName.text = dataHolder.name
        holder.txtCompany.text = dataHolder.company
        holder.txtLocation.text = dataHolder.location
        holder.itemView.setOnClickListener {
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(UserData: UserData)
    }

    override fun getItemCount(): Int = followersFilterList.size

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgAvatar: ImageView = itemView.findViewById(R.id.img_avatar)
        var txtUsername: TextView = itemView.findViewById(R.id.txt_username)
        var txtName: TextView = itemView.findViewById(R.id.txt_name)
        var txtCompany: TextView = itemView.findViewById(R.id.txt_company)
        var txtLocation: TextView = itemView.findViewById(R.id.txt_location)
    }

}