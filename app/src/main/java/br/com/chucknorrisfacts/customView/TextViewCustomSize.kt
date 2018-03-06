package br.com.chucknorrisfacts.customView

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import android.util.TypedValue

/**
 * @rodrigohsb
 */
class TextViewCustomSize : TextView {

    constructor(context: Context): super(context)

    constructor(context: Context, attributeSet: AttributeSet): super(context,attributeSet)

    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int)
                    : super(context,attributeSet,defStyle)

    private fun refitText(text: String) {
        if (text.length > 200){
            this.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f)
            return
        }
        this.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16f)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        refitText(this.text.toString())
    }
}