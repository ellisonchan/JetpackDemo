package com.ellison.jetpackdemo.room

import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class MovieGestureCallback(private var listener: GestureListener?)
    : ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN or ItemTouchHelper.UP, ItemTouchHelper.LEFT) {
    private val adapter: MovieAdapter? = null

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        Log.d(MovieDataBase.TAG, "onSwiped() viewHolder:$viewHolder direction:$direction"
                + " listener:" + System.identityHashCode(listener)
                + " adapter:" + System.identityHashCode(adapter))

        if (listener != null && direction == ItemTouchHelper.LEFT) {
            listener!!.onDeleteItem(viewHolder)
        }

        if (adapter != null && direction == ItemTouchHelper.LEFT) {
            adapter.onDeleteItem(viewHolder)
        }
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder): Boolean {
        Log.d(MovieDataBase.TAG, "onMove() recyclerView:$recyclerView viewHolder:$viewHolder target:$target")
        if (listener != null) {
            listener!!.onSwapItem(viewHolder, target)
            return true
        }
        return false
    }

    override fun onMoved(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                         fromPos: Int, target: RecyclerView.ViewHolder, toPos: Int, x: Int, y: Int) {
        Log.d(MovieDataBase.TAG, "onMoved() recyclerView:$recyclerView")
        super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        Log.d(MovieDataBase.TAG, "onSelectedChanged() actionState:$actionState")
        super.onSelectedChanged(viewHolder, actionState)
        if (listener != null && actionState == ItemTouchHelper.ACTION_STATE_IDLE) {
            listener!!.onGestureDone()
        }
    }
}