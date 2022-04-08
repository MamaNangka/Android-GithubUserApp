package com.k6genap.githubuserapp.view.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.k6genap.githubuserapp.data.model.User
import com.k6genap.githubuserapp.databinding.ItemRowUserBinding


class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>(){

    private val UserLists = ArrayList<User>()

    private var UserClickCallback: OnItemClickCallback? =null

    fun setOnItemClickCallback (onItemClickCallback: OnItemClickCallback){
        this.UserClickCallback = onItemClickCallback
    }

    fun setList(users: ArrayList<User>){
        UserLists.clear()
        UserLists.addAll(users)
        notifyDataSetChanged()
    }

    inner class UserViewHolder(val binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.root.setOnClickListener{
                UserClickCallback?.onItemClicked(user)
            }
            binding.apply {
                Glide.with(itemView)
                    .load(user.avatar_url)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
                    .apply(RequestOptions.circleCropTransform())
                    .into(imgItemAvatar)
                tvItemUsername.text = user.login


            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder((view))
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(UserLists[position])
    }

    override fun getItemCount(): Int = UserLists.size

    interface OnItemClickCallback{
        fun onItemClicked(data: User)
    }
}