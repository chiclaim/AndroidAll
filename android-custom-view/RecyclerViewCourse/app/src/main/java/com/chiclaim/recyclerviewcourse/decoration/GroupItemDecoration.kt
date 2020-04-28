package com.chiclaim.recyclerviewcourse.decoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import com.chiclaim.recyclerviewcourse.R


/**
 * RecyclerView Item 分组时展示 Divider 的装饰器
 */
class GroupItemDecoration(context: Context,
                          private val list: List<BaseGroupVO>,
                          private val groupStartMargin: Int = 0,
                          private val dividerMargin: Int = 0,
                          @ColorRes private val marginColor: Int = android.R.color.white,
                          @DrawableRes private val dividerRes: Int = R.drawable.module_res_common_divider) : RecyclerView.ItemDecoration() {

    open class BaseGroupVO(var isGroupStart: Boolean, var isGroupEnd: Boolean)

    private val mDividerDrawable: Drawable = context.resources.getDrawable(dividerRes)

    private val mMarginColorDrawable = ColorDrawable(context.resources.getColor(marginColor))

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        if (checkPosition(position)) return
        val top = if (list[position].isGroupStart) {
            groupStartMargin + mDividerDrawable.intrinsicHeight
        } else {
            mDividerDrawable.intrinsicHeight
        }

        var bottom = 0
        if (list[position].isGroupEnd) {
            bottom = mDividerDrawable.intrinsicHeight
        }

        outRect.set(0, top, 0, bottom)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        drawLines(c, parent)
    }

    private fun checkPosition(position: Int) = position == NO_POSITION || position >= list.size

    private fun drawLines(c: Canvas, parent: RecyclerView) {
        val count = parent.childCount
        for (index in 0 until count) {
            val child = parent.getChildAt(index)
            val position = parent.getChildAdapterPosition(child)
            if (checkPosition(position)) return
            drawTopLine(position, c, child)
            if (list[position].isGroupEnd) {
                drawBottomLine(c, child)
            }
        }
    }

    private fun drawTopLine(position: Int, c: Canvas, child: View) {
        val params = child.layoutParams as RecyclerView.LayoutParams
        var left = child.left - params.leftMargin
        val top = child.top - params.topMargin - mDividerDrawable.intrinsicHeight
        val right = child.right + params.rightMargin
        val bottom = top + mDividerDrawable.intrinsicHeight

        if (!list[position].isGroupStart) {
            left += dividerMargin
            drawMarginLine(c, child)
        }

        mDividerDrawable.setBounds(left, top, right, bottom)
        mDividerDrawable.draw(c)
    }


    private fun drawBottomLine(c: Canvas, child: View) {
        val params = child.layoutParams as RecyclerView.LayoutParams
        val left = child.left - params.leftMargin
        val top = child.bottom + params.bottomMargin
        val right = child.right + params.rightMargin
        val bottom = top + mDividerDrawable.intrinsicHeight
        mDividerDrawable.setBounds(left, top, right, bottom)
        mDividerDrawable.draw(c)
    }

    private fun drawMarginLine(c: Canvas, child: View) {
        val params = child.layoutParams as RecyclerView.LayoutParams
        val left = child.left - params.leftMargin
        val top = child.top - params.topMargin - mDividerDrawable.intrinsicHeight
        //val right = child.right + params.rightMargin
        val bottom = top + mDividerDrawable.intrinsicHeight
        mMarginColorDrawable.setBounds(left, top, dividerMargin, bottom)
        mMarginColorDrawable.draw(c)
    }


}