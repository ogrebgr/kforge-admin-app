package com.bolyartech.forge.admin.units.admin_users

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bolyartech.forge.admin.R
import com.bolyartech.forge.admin.data.AdminUserExportedView

class AdminUsersAdapter(private val adminUsers: List<AdminUserExportedView>, private val listener: ClickListener) :
    RecyclerView.Adapter<AdminUsersAdapter.ViewHolder>() {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        internal val tvUsername = view.findViewById<TextView>(R.id.tvUsername)
        internal val tvName = view.findViewById<TextView>(R.id.tvName)
        internal val ivStar = view.findViewById<ImageView>(R.id.ivStar)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rvr__admin_users, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return adminUsers.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = adminUsers.get(position)

        holder.view.setOnClickListener {
            listener.onItemClick(adminUsers[position])
        }
        holder.tvUsername.text = item.username
        holder.tvName.text = item.name
        if (item.isSuperUser) {
            holder.ivStar.visibility = View.VISIBLE
        } else {
            holder.ivStar.visibility = View.INVISIBLE
        }
    }


    interface ClickListener {
        fun onItemClick(item: AdminUserExportedView)
    }
}