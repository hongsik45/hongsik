package com.team.elysium.moneyfit.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.team.elysium.moneyfit.App
import com.team.elysium.moneyfit.R

/**
 * Created by sh on 2018-07-13.
 */
class WeekAdapter(private val context: Context, private val viewId: Int,
                  private val succeeded: ArrayList<Boolean>, private val dayStringArray: Array<String>) : BaseAdapter() {
    
    override fun getCount(): Int {
        return dayStringArray.size
    }

    override fun getItem(i: Int): Any {
        return dayStringArray[i]
    }

    override fun getItemId(i: Int): Long {
        return 0
    }

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View? {
        val itemView: View
        val holder: ViewHolder

        if (view == null) {
            itemView = LayoutInflater.from(context).inflate(this.viewId, null)
            holder = ViewHolder()
            holder.day = itemView.findViewById(R.id.day)
            holder.checked = itemView.findViewById(R.id.checked)

            itemView.tag = holder

        } else {
            holder = view.tag as ViewHolder
            itemView = view
        }

        holder.bind(dayStringArray[i], succeeded[i])

        return itemView
    }

    private class ViewHolder {
        var day: TextView? = null
        var checked: ImageView? = null

        fun bind(typeName: String, checked: Boolean) {
            day!!.text = typeName
            if(checked) {
                this.checked!!.visibility = View.VISIBLE
                day!!.setTextColor(ContextCompat.getColor(App.getGlobalApplicationContext(), R.color.week_success_color))
            }
        }
    }
}