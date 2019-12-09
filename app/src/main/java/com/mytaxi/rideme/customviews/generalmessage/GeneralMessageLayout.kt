package com.mytaxi.rideme.customviews.generalmessage

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.databinding.InverseBindingMethod
import androidx.databinding.InverseBindingMethods
import com.mytaxi.rideme.R

@InverseBindingMethods(
    InverseBindingMethod(
        type = GeneralMessageLayout::class,
        attribute = "generalMessage",
        method = "getGeneralMessage",
        event = "android:generalMessageVisibleAttrChanged"
    )
)
class GeneralMessageLayout(context: Context, attributeSet: AttributeSet?) :
    ConstraintLayout(context, attributeSet) {

    constructor(context: Context) : this(context, null)

    companion object {

        private const val ANIM_DURATION = 300L
        private const val SHOW_DURATION = 60000L

        @JvmStatic
        @BindingAdapter("generalMessage")
        fun setGeneralMessage(
            generalMessageLayout: GeneralMessageLayout,
            generalMessage: GeneralMessage?
        ) {
            generalMessageLayout.generalMessage = generalMessage ?: GeneralMessage()
            if (generalMessage?.visible == true) {
                generalMessageLayout.showMessage()
            }
        }

        @JvmStatic
        @BindingAdapter(value = ["android:generalMessageVisibleAttrChanged"])
        fun setListener(
            generalMessageLayout: GeneralMessageLayout?,
            listener: InverseBindingListener?
        ) {
            if (listener != null) {
                generalMessageLayout?.ivClose?.setOnClickListener {
                    generalMessageLayout.hideMessage()
                    listener.onChange()
                }
            }
        }
    }

    private var tvMessage: TextView? = null
    private var clRoot: ConstraintLayout? = null
    private var ivClose: ImageView? = null

    private var messageVisible = false

    private var generalMessage: GeneralMessage = GeneralMessage()

    init {
        val inflater: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = inflater.inflate(R.layout.layout_general_message, this, true)
        tvMessage = layout.findViewById(R.id.l_general_message_tv_message)
        clRoot = layout.findViewById(R.id.l_general_message_cl_root)
        ivClose = layout.findViewById(R.id.l_general_message_iv_close)
    }

    fun getGeneralMessage() = generalMessage

    private fun showMessage() {
        if (generalMessage.type != GeneralMessage.Type.NONE) {
            clRoot?.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    if (generalMessage.type == GeneralMessage.Type.ERROR) {
                        R.color.color_error
                    } else {
                        R.color.color_succeed
                    }
                )
            )
            tvMessage?.text = generalMessage.getMessage()
            if (!messageVisible) {
                showMessageAnimation()
            }
        }
    }

    private fun showMessageAnimation() {
        messageVisible = true
        clRoot?.animate()
            ?.setInterpolator(DecelerateInterpolator())
            ?.setDuration(ANIM_DURATION)
            ?.translationY(0f)
            ?.setListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(p0: Animator?) {}
                override fun onAnimationEnd(p0: Animator?) {
                    postDelayed({ hideMessage() }, SHOW_DURATION)
                }
                override fun onAnimationCancel(p0: Animator?) {}
                override fun onAnimationStart(p0: Animator?) {}
            })
    }

    private fun hideMessage() {
        if (messageVisible) {
            messageVisible = false
            generalMessage.visible = false
            clRoot?.animate()
                ?.setInterpolator(DecelerateInterpolator())
                ?.setDuration(ANIM_DURATION)
                ?.translationY(-(clRoot?.height?.toFloat() ?: 0f))
                ?.setListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(p0: Animator?) {}
                    override fun onAnimationCancel(p0: Animator?) {}
                    override fun onAnimationStart(p0: Animator?) {}
                    override fun onAnimationEnd(p0: Animator?) {}
                })
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (!messageVisible) {
            clRoot?.translationY = -(clRoot?.measuredHeight ?: 0).toFloat()
        }
    }
}