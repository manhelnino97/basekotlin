package com.omi.pharmacy.helper.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.support.annotation.IdRes
import android.support.annotation.UiThread
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.EditText

/**
 * Created by tinhvv on 10/24/18.
 */
fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.isVisble(): Boolean = this.visibility == View.VISIBLE

@UiThread
inline fun View.fadeOut() {
    fadeOut(500)
}

@UiThread
inline fun View.fadeIn() {
    fadeIn(500)
}

@UiThread
inline fun View.fadeIn(duration: Long) {
    this.clearAnimation()
    val anim = AlphaAnimation(this.alpha, 1.0f)
    anim.duration = duration
    this.startAnimation(anim)
}

@UiThread
inline fun View.fadeOut(duration: Long) {
    this.clearAnimation()
    val anim = AlphaAnimation(this.alpha, 0.0f)
    anim.duration = duration
    this.startAnimation(anim)
}

//inline fun View.children(): List<View> {
//    if (this !is ViewGroup) return listOf()
//    else {
//        return children()
//    }
//}
//
//fun View.removeChildAt(position: Int): Boolean {
//    if (this !is ViewGroup)
//        return false
//    else {
//        if (position >= children().size) {
//            return false
//        }
//        this.removeViewAt(position)
//        return true
//    }
//}

fun View.addChild(view: View) {
    if (this is ViewGroup) {
        this.addView(view)
    }
}


fun View.setupHiddenKeyboard(activity: Activity) {
    // Set up touch listener for non-text box views to hide keyboard.
    if (this !is EditText) {
        this.setOnTouchListener { _, motion ->
            if (motion.action == MotionEvent.ACTION_UP && (motion.eventTime - motion.downTime) <= 200) {
                KeyboardHelper.shared.hideSoftKeyboard(activity)
                false
            } else {
//                motion.action == MotionEvent.ACTION_UP
                false
            }
        }
    }

    //If a layout container, iterate over children and seed recursion.
    if (this is ViewGroup) {
        for (i in 0 until this.childCount) {
            val innerView = this.getChildAt(i)
            innerView.setupHiddenKeyboard(activity)
        }
    }
}

fun EditText.showETError(error: String) {
    this.error = error
    this.requestFocus()
}

fun View.goneBottomToTop() {
    this.animate()
            .translationY(0f - this.height.toFloat())
            .alpha(0.0f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    gone()
                }
            })
}

fun View.visibleTopToBottom() {
    visible()
    this.animate()
            .translationY(0f)
            .alpha(1.0f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    visible()
                }
            })
}

fun View.visibleSlideFromTop() {
    this.animate().translationY(-this.height.toFloat())
            .setDuration(0)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    visible()
                    animate().translationY(0.0f).duration = 300
                }
            })
}

fun View.hideSlideToTop() {
    this.animate().translationY(-this.height.toFloat())
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    gone()
                }
            })
}


fun <T : View?> AppCompatActivity.bindBy(@IdRes idRes: Int): Lazy<T>? {
    return unsafeLazy { findViewById<T>(idRes)}
}

fun <T : View?> View.bindBy(@IdRes idRes: Int): Lazy<T>? {
    @Suppress("UNCHECKED_CAST")
    return unsafeLazy { findViewById<T>(idRes)}
}

fun <T : View> AppCompatActivity.bind(@IdRes idRes: Int): T? {
    return findViewById(idRes)
}

fun <T : View> View.bind(@IdRes idRes: Int): T? {
    return findViewById(idRes)
}

private fun <T> unsafeLazy(initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE, initializer)