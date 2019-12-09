package com.mytaxi.rideme.features.vehicles.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.mytaxi.rideme.R
import com.mytaxi.rideme.app.Application
import com.mytaxi.rideme.data.repositories.taxifleetrepository.Poi
import com.mytaxi.rideme.logger.Logger
import javax.inject.Inject


class TaxiListAdapter(
    private val viewLiveData: MutableLiveData<ListViewData>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {

        private const val TAG = "TransferTemplatesAdapter"
        const val TAXI_VIEW_HOLDER = 0
        const val POOLING_VIEW_HOLDER = 1
    }

    @Inject
    lateinit var logger: Logger
    private val taxiList = arrayListOf<Poi>()

    override fun getItemCount(): Int = taxiList.size

    init {
        Application.appComponent.inject(this)
    }

    override fun getItemViewType(position: Int): Int {
        logger.d(TAG, "getItemViewType(), position=$position")
        var viewHolderId = TAXI_VIEW_HOLDER
        if (taxiList[position].fleetType == "TAXI") {
            viewHolderId = TAXI_VIEW_HOLDER
        } else if (taxiList[position].fleetType == "POOLING") {
            viewHolderId = POOLING_VIEW_HOLDER
        }
        return viewHolderId
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        logger.d(TAG, "onCreateViewHolder()")
        val viewHolder: RecyclerView.ViewHolder
        when (viewType) {
            TAXI_VIEW_HOLDER -> viewHolder = TaxiViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_taxi,
                    parent,
                    false
                ), viewLiveData
            )
            POOLING_VIEW_HOLDER -> viewHolder = PoolingViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_pooling,
                    parent,
                    false
                ), viewLiveData
            )
            else -> viewHolder = TaxiViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_taxi,
                    parent,
                    false
                ), viewLiveData
            )
        }
        return viewHolder
    }

    // Bind data to data specific viewHolder
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        logger.d(TAG, "onBindViewHolder(), position=$position")
        when (holder) {
            is TaxiViewHolder -> {
                val viewHolderContent = taxiList[position]
                holder.bind(viewHolderContent)
            }
            is PoolingViewHolder -> {
                val viewHolderContent = taxiList[position]
                holder.bind(viewHolderContent)
            }
        }
    }

    fun swapData(taxiList: ArrayList<Poi>?) {
        logger.d(TAG, "swapData() size=${taxiList?.size}")
        this.taxiList.clear()
        this.taxiList.addAll(taxiList as ArrayList<Poi>)
        notifyDataSetChanged()
    }

}