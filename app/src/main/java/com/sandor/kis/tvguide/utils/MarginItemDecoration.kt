package com.sandor.kis.tvguide.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginItemDecoration(private val space: Int):RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        with(outRect) {
            // Felső, alsó, bal és jobb margó hozzáadása
            top = space
            left = space
            right = space
            bottom = space
        }
    }
}