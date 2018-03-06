package br.com.chucknorrisfacts.customView

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView

/**
 * @rodrigohsb
 */
class TextViewCustomBackground : TextView {
    
    constructor(context: Context): super(context)

    constructor(context: Context, attributeSet: AttributeSet): super(context,attributeSet)

    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int)
                    : super(context,attributeSet,defStyle)

    private fun refitText(text: String) {
            if (text.isEmpty()){
        //            this.text = "uncategorized"
                    this.setBackgroundColor(resources.getColor(android.R.color.darker_gray))
                    return
                }
            this.setBackgroundColor(resources.getColor(android.R.color.holo_blue_light))
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            refitText(this.text.toString())
        }
}