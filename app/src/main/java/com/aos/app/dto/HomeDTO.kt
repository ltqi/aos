package com.aos.app.dto

import java.io.Serializable

/**
 * Created by:  qiliantao on 2020.06.22 12:55
 * email     :  qiliantao@mockuai.com
 * Describe  :
 */

data class ArticleList( val offset: Int,
                        val size: Int,
                        val total: Int,
                        val pageCount: Int,
                        val curPage: Int,
                        val over: Boolean,
                        val datas: List<Article>): Serializable {

    data class Article( val id: Int, val originId: Int, val title: String, val chapterId: Int, val chapterName: String,
                        val envelopePic: String, val link: String, val author: String, val origin: String,
                        val publishTime: Long, val zan: Int, val desc: String, val visible: Int, val niceDate: String,
                        val niceShareDate: String, val courseId: Int, var collect: Boolean, val apkLink:String,
                        val projectLink:String, val superChapterId:Int, val superChapterName:String?, val type:Int,
                        val fresh:Boolean, val audit:Int, val prefix:String, val selfVisible:Int, val shareDate:Long,
                        val shareUser:String, val tags:Any, val userId:Int): Serializable
}

//　"desc":"一起来做个App吧",
//　　　　　　"id":10,
//　　　　　　"imagePath":"http://www.wanandroid.com/blogimgs/50c115c2-cf6c-4802-aa7b-a4334de444cd.png",
//　　　　　　"isVisible":1,
//　　　　　　"order":0,
//　　　　　　"title":"一起来做个App吧",
//　　　　　　"type":0,
//　　　　　　"url":"http://www.wanandroid.com/blog/show/2"
data class Banner(val desc: String,
                  val id: Int,
                  val imagePath: String,
                  val isVisible: Int,
                  val order: Int,
                  val title: String,
                  val type: Int,
                  val url: String)

data class SystemParent(val children: List<SystemChild>,
                        val courseId: Int,
                        val id: Int,
                        val name: String,
                        val order: Int,
                        val parentChapterId: Int,
                        val visible: Int,
                        val userControlSetTop: Boolean) : Serializable {
    data class SystemChild(val child: List<SystemChild>,
                           val courseId: Int,
                           val id: Int,
                           val name: String,
                           val order: Int,
                           val parentChapterId: Int,
                           val visible: Int):Serializable
}

data class Hot(val id: Int,
               val link: String,
               val name: String,
               val order: Int,
               val visible: Int,
               val icon: String)

data class Navigation(val articles: List<ArticleList.Article>,
                      val cid: Int,
                      val name: String)

class Title(val title: Int, val icon: Int, val action: () -> Unit)



