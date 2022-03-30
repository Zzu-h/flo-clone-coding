package com.example.indicator

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2

class CircleIndicator: LinearLayout {

    private var mContext: Context? = null
    private var mDefaultCircle: Int = 0
    private var mSelectCircle: Int = 0
    private var _itemSize: Float = 0f
    private var _marginLeftAndRight: Float = 0f

    var itemSize: Float get() = _itemSize
        set(value) {
            _itemSize = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, value.toFloat(), resources.displayMetrics)
        }
    var marginLeftAndRight: Float get() = _marginLeftAndRight
        set(value) {
            _marginLeftAndRight = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, value.toFloat(), resources.displayMetrics)
        }

    private var item: MutableList<View> = mutableListOf()

    constructor(context: Context) : super(context) { mContext = context }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) { mContext = context }

    /**
     * 기본 점 생성
     * @param viewPager 연결할 ViewPager
     * @param defaultCircle 기본 점의 이미지
     * @param selectCircle 선택된 점의 이미지
     */
    fun setViewPager(viewPager: ViewPager2, defaultCircle: Int, selectCircle: Int, itemSize: Int = 10, marginLeftAndRight: Int = 5){
        this.itemSize = itemSize.toFloat()
        this.marginLeftAndRight = marginLeftAndRight.toFloat()

        createDotPanel(viewPager.adapter!!.itemCount, defaultCircle, selectCircle, 0)

        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.d("pageSelected",position.toString())
                selectDot(position)
            }
        })
    }

    private fun createDotPanel(count: Int, defaultCircle: Int, selectCircle: Int, position: Int) {
        this.removeAllViews()
        mDefaultCircle = defaultCircle
        mSelectCircle = selectCircle

        val selectDrawable: Drawable? = ContextCompat.getDrawable(context, mSelectCircle)
        val unSelectDrawable: Drawable? = ContextCompat.getDrawable(context, mDefaultCircle)

        for (i in 0 until count) {
            item.add(View(mContext).apply {
                layoutParams = LayoutParams((itemSize+marginLeftAndRight).toInt(), itemSize.toInt())
                background = if(i == position) selectDrawable
                else unSelectDrawable
            })
            this.addView(item[i])
            println("$i")
        }
        this.gravity = Gravity.CENTER
    }

    /**
     * 선택된 점 표시
     * @param position
     */
    private fun selectDot(position: Int) {
        val selectDrawable: Drawable? = ContextCompat.getDrawable(context, mSelectCircle)
        val unSelectDrawable: Drawable? = ContextCompat.getDrawable(context, mDefaultCircle)

        println(selectDrawable.toString())
        for (i in item.indices)
            item[i].background = if (i == position) selectDrawable else unSelectDrawable
    }
}