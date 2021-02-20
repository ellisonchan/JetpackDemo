package com.ellison.jetpackdemo.room

import androidx.recyclerview.widget.RecyclerView

interface GestureListener {
    fun onDeleteItem(targetHolder: RecyclerView.ViewHolder?) {}
    fun onSwapItem(fromHolder: RecyclerView.ViewHolder?, targetHolder: RecyclerView.ViewHolder?) {}
    fun onGestureDone() {}
}