package com.aos.app2.ex

import android.text.Html
import android.text.Spanned
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import androidx.databinding.adapters.TextViewBindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.aos.app2.R
import com.aos.app2.view.SpaceItemDecoration

/**
 * Created by:  qiliantao on 2020.08.04
 * email     :  qiliantao@mockuai.com
 * Describe  :
 */
@BindingAdapter("isRefresh")
fun SwipeRefreshLayout.isRefresh(isRefresh: Boolean) {
    isRefreshing = isRefresh
}

@BindingAdapter("onRefresh")
fun SwipeRefreshLayout.onRefresh(action: () -> Unit) {
    setOnRefreshListener { action() }
}

@BindingAdapter("itemTopPadding", "itemLeftPadding", "itemBottomPadding", "itemRightPadding",requireAll = false)
fun RecyclerView.addItemPadding(top: Int = 0, left: Int = 0, bottom: Int = 0, right: Int = 0) {
    addItemDecoration(SpaceItemDecoration(top, left, bottom, right))
}

@BindingAdapter("visibleUnless")
fun bindVisibleUnless(view: View, visible: Boolean) {
    view.visibility = if (visible) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

@BindingAdapter("goneUnless")
fun bindGoneUnless(view: View, gone: Boolean) {
    view.visibility = if (gone) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter("htmlText")
fun bindHtmlText(view: TextView, html:String){
    view.text = if (fromN()) Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY) else Html.fromHtml(html)
}

@BindingAdapter("txt")
fun setTxt(view: TextView, text: CharSequence?) {
    val oldText = view.text
    if (text !== oldText && (text != null || oldText.isNotEmpty())) {
        if (text is Spanned) {
            if (text == oldText) {
                return
            }
        }
        view.text = text
    }
}

@BindingAdapter("articleStar")
fun bindArticleStar(view: ImageView, collect:Boolean){
    view.setImageResource(if (collect) R.drawable.timeline_like_pressed else R.drawable.timeline_like_normal)
}
