package com.team.elysium.moneyfit.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.team.elysium.moneyfit.GlideRequests
import com.team.elysium.moneyfit.R
import com.team.elysium.moneyfit.dataclass.UserRank

/**
 * Created by sh on 2018-08-23.
 */
class RankAdapter(private val context: Context, private val rankList: ArrayList<UserRank>, private val glide: GlideRequests) : RecyclerView.Adapter<RankAdapter.RankViewHolder>() {

    override fun getItemCount(): Int {
        return rankList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_rank, parent, false)

        return RankViewHolder(view)
    }

    override fun onBindViewHolder(holder: RankViewHolder, position: Int) {

        if(holder.adapterPosition != RecyclerView.NO_POSITION) {

            val item = rankList[holder.adapterPosition]
            holder.rank.text = item.rank
            glide.load(if(item.thumbnail == null || item.thumbnail == "") ContextCompat.getDrawable(context, R.drawable.moneyfit_coin_img) else item.thumbnail).circleCrop().into(holder.thumbnail)
            holder.nickname.text = item.nickname
            holder.totalMoney.text = item.money
        }
    }

    class RankViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val rank: TextView = itemView.findViewById(R.id.rank_item_rank_text)
        val thumbnail: ImageView = itemView.findViewById(R.id.rank_item_thumbnail)
        val nickname: TextView = itemView.findViewById(R.id.rank_item_nickname)
        val totalMoney: TextView = itemView.findViewById(R.id.rank_item_money_total_week_text)
    }
}