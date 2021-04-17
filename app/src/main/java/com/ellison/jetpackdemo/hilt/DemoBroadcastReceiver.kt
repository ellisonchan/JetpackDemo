package com.ellison.jetpackdemo.hilt

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ellison.jetpackdemo.hilt.model.network.NetworkService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DemoBroadcastReceiver: BroadcastReceiver() {
    @Inject lateinit var networkService: NetworkService
    override fun onReceive(context: Context?, intent: Intent?) {
        TODO("Not yet implemented")
    }
}