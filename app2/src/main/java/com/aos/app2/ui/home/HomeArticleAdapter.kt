package com.aos.app2.ui.home

import com.aos.app2.BR
import com.aos.app2.R
import com.aos.app2.base.BaseBindAdapter
import com.aos.app2.bean.Article


class HomeArticleAdapter(layoutResId: Int = R.layout.item_article_constraint) : BaseBindAdapter<Article>(layoutResId, BR.article) {

    private var showStar = true

    fun showStar(showStar: Boolean) {
        this.showStar = showStar
    }

    override fun convert(helper: BindViewHolder, item: Article) {
        super.convert(helper, item)
        helper.addOnClickListener(R.id.articleStar)
        if (showStar) helper.setImageResource(R.id.articleStar, if (item.collect) R.drawable.timeline_like_pressed else R.drawable.timeline_like_normal)
        else helper.setVisible(R.id.articleStar, false)

        helper.setText(R.id.articleAuthor, if (item.author.isBlank()) "分享者: ${item.shareUser}" else item.author)
    }
}