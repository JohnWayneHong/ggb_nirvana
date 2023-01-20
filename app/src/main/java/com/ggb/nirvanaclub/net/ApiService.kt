package com.ggb.nirvanaclub.net

import com.ggb.nirvanaclub.bean.*
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    //获取用户标签
    @GET("main/tag/user")
    fun getTag():Observable<HttpResult<List<IndexTagBean>>>

    //用户登录发送验证码
    @POST("sys/phoneCode")
    fun sendCode(@Body phone : String):Observable<HttpResult<SimpleUserInfo>>

    //用户登录
    @POST("sys/login")
    fun login(@Body body: RequestBody):Observable<HttpResult<Any>>

    //获取基础用户信息
    @GET("sys/info/{type}")
    fun info(@Path("type") type: Int) : Observable<HttpResult<SimpleUserInfo>>

    //查询所有的标签
    @GET("main/tag/all")
    fun getTagAll(): Observable<HttpResult<List<IndexTagBean>>>

    //保存用户订阅标签
    @POST("main/tag/user/save")
    fun saveUserTags(@Body tagIds: String): Observable<HttpResult<Any>>

    /**
     * 分页查询某个标签的文章
     * @param tagId 查询对应标签的文章
     * @param page 查询第几页
     * @param pageSize 查几条数据
     */
    @GET("main/article/info/{tagId}/{page}")
    fun pageArticleByTag(@Path("tagId") tagId: String, @Path("page") page: Int, @Query("pageSize") pageSize: Int): Observable<HttpResult<IndexArticleInfoBean>>

    //获取文章的内容
    @GET("main/article/{articleId}")
    fun getArticle(@Path("articleId") articleId: String): Observable<HttpResult<ArticleContentBean>>

    //获取文章的内容
//    @GET("main/getBlogByPage/{currentPage}/{query}/{order}")
    @GET("main/getBlogByPage")
    fun searchArticleByTime(@Query("currentPage") currentPage: Int,@Query("query") query: String,@Query("order") order: String): Observable<HttpResult<SearchArticleBean>>

    //点赞或取消点赞文章
    @PUT("main/article/like/{articleId}")
    fun likeOrCancel(@Path("articleId") articleId: String, @Body order: Map<String, String>): Observable<HttpResult<Any>>
}