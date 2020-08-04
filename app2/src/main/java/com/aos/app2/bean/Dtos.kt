package com.aos.app2.bean

import java.io.Serializable

data class ArticleList( val offset: Int,
                        val size: Int,
                        val total: Int,
                        val pageCount: Int,
                        val curPage: Int,
                        val over: Boolean,
                        val datas: List<Article>): Serializable

data class Article( val id: Int, val originId: Int, val title: String, val chapterId: Int, val chapterName: String,
                    val envelopePic: String, val link: String, val author: String, val origin: String,
                    val publishTime: Long, val zan: Int, val desc: String, val visible: Int, val niceDate: String,
                    val niceShareDate: String, val courseId: Int, var collect: Boolean, val apkLink:String,
                    val projectLink:String, val superChapterId:Int, val superChapterName:String?, val type:Int,
                    val fresh:Boolean, val audit:Int, val prefix:String, val selfVisible:Int, val shareDate:Long,
                    val shareUser:String, val tags:Any, val userId:Int):Serializable


data class Banner(val desc: String,
                  val id: Int,
                  val imagePath: String,
                  val isVisible: Int,
                  val order: Int,
                  val title: String,
                  val type: Int,
                  val url: String)

data class Hot(val id: Int,
               val link: String,
               val name: String,
               val order: Int,
               val visible: Int,
               val icon: String)

data class Navigation(val articles: List<Article>,
                      val cid: Int,
                      val name: String)

data class SystemChild(val child: List<SystemChild>,
                       val courseId: Int,
                       val id: Int,
                       val name: String,
                       val order: Int,
                       val parentChapterId: Int,
                       val visible: Int):Serializable

data class SystemParent(val children: List<SystemChild>,
                        val courseId: Int,
                        val id: Int,
                        val name: String,
                        val order: Int,
                        val parentChapterId: Int,
                        val visible: Int,
                        val userControlSetTop: Boolean) : Serializable


data class User(val admin: Boolean,
                val chapterTops: List<String>,
                val collectIds: List<Int>,
                val email: String,
                val icon: String,
                val id: Int,
                val nickname: String,
                val password: String,
                val publicName: String,
                val token: String,
                val type: Int,
                val username: String){

    override fun equals(other: Any?): Boolean {
        return if (other is User){
            this.id == other.id
        }else false
    }
}

