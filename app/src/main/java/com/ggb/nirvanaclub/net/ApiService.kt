package com.ggb.nirvanaclub.net

import com.ggb.nirvanaclub.bean.*
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    //注释的均为老版接口地址

    //获取用户标签,已改为获取前20热度标签列表
//    @GET("main/tag/user")
    @GET("/v2/api/tag/recommand/list")
    fun getTag():Observable<HttpResult<List<IndexTagBean>>>

    //用户注册发送验证码
//    @POST("sys/phoneCode")
    @POST("/v2/api/user/register/sendVerify")
    fun sendCode(@Body body: RequestBody):Observable<HttpResult<Any>>

    //验证验证码
    @POST("/v2/api/user/register/verifyCode")
    fun verifyCode(@Body body: RequestBody):Observable<HttpResult<Any>>

    //用户登出
    @POST("/v2/api/user/login")
    fun login(@Body body: RequestBody):Observable<HttpResult<Any>>

    //用户登录
    @POST("/v2/api/user/logout")
    fun loginOut(@Body body: RequestBody):Observable<HttpResult<Any>>

    //获取基础用户信息
    @GET("/v2/api/user/myinfo")
    fun info() : Observable<HttpResult<SimpleUserInfo>>

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
//    @GET("main/article/info/{tagId}/{page}")
    @GET("/v2/api/tag/blog/list")
    fun pageArticleByTag(@Query("tagId") tagId: String, @Query("page") page: Int, @Query("size") pageSize: Int): Observable<HttpResult<List<IndexArticleInfoBean>>>

    /**
     * 分页查询用户点赞的文章
     * @param page 查询第几页
     * @param pageSize 查几条数据
     */
    @GET("/v2/api/like/list")
    fun pageArticleByLike(@Query("page") page: Int, @Query("size") pageSize: Int): Observable<HttpResult<List<ArticleLikeInfoBean>>>

    /**
     * 分页查询用户收藏的文章
     * @param page 查询第几页
     * @param pageSize 查几条数据
     */
    @GET("/v2/api/favorite/blog/list")
    fun pageArticleByCollection(@Query("page") page: Int, @Query("size") pageSize: Int): Observable<HttpResult<List<ArticleCollectionInfoBean>>>


    //获取文章的内容
    //@GET("main/article/{articleId}")
    @GET("/v2/api/blog/details/{id}")
    fun getArticle(@Path("id") articleId: String): Observable<HttpResult<ArticleContentBean>>

    //获取文章的内容
//    @GET("main/getBlogByPage/{currentPage}/{query}/{order}")
//    @GET("main/getBlogByPage")
    @GET("/v2/api/blog/list")
    fun searchArticle(@Query("page") page: Int,@Query("query") query: String,@Query("size") size: Int): Observable<HttpResult<List<SearchArticleBean>>>

    //点赞文章
    @POST("/v2/api/like/add")
    fun likeArticle(@Body body: RequestBody): Observable<HttpResult<Any>>

    //取消点赞文章
    @POST("/v2/api/like/reduce")
    fun dislikeArticle(@Body body: RequestBody): Observable<HttpResult<Any>>

    //收藏文章
    @POST("/v2/api/favorite/blog/add")
    fun addArticleToCollection(@Body body: RequestBody): Observable<HttpResult<Any>>

    //取消点赞文章
    @POST("/v2/api/favorite/blog/cancel")
    fun deleteArticleToCollection(@Body body: RequestBody): Observable<HttpResult<Any>>

    //获取浏览博客历史
    @GET("/v2/api/history/blog")
    fun getArticleHistory(@Query("page") page: Int,@Query("size") size: Int): Observable<HttpResult<List<ArticleHistoryBean>>>

    //取消单个文章的浏览历史
    @POST("/v2/api/history/blog/del")
    fun deleteSingleArticleHistory(@Body body: RequestBody): Observable<HttpResult<Any>>

    //取消所有文章的浏览历史
    @POST("/v2/api/history/blog/delAll")
    fun deleteAllArticleHistory(): Observable<HttpResult<Any>>

    //验证账户是否被注册
    @POST("/v2/api/user/register/checkAccount")
    fun checkAccountIsRegister(@Body body: RequestBody): Observable<HttpResult<RegisterCheckBean>>

    //获取随机美女图片
    @GET("image/girl/list/random")
    fun developGetRandomGirl(): Observable<ThirdHttpResult<List<DevelopRandomGirlListBean>>>

    //获取随机纯文字
    @POST("home/text")
    fun communityText(): Observable<ThirdHttpResult<List<DevelopJokesListBean>>>

    //获取随机纯文字
    @POST("home/pic")
    fun communityPicture(): Observable<ThirdHttpResult<List<DevelopJokesListBean>>>

    //获取主页的推荐关注数据
    @POST("home/attention/recommend")
    fun communityRecommendSubscript(): Observable<ThirdHttpResult<List<DevelopJokesSubscriptListBean>>>

    //获取主页的推荐关注数据
    @GET("article/list/{pager}/json")
    fun communityAndroid(@Path("pager") page :Int): Observable<PlayAndroidHttpResult<CommunityAndroidBean>>

    //获取主页的推荐关注数据
    @GET("user_article/list/{pager}/json")
    fun communitySquare(@Path("pager") page :Int): Observable<PlayAndroidHttpResult<CommunityAndroidBean>>

    /**
     * 获取知识体系
     * http://www.wanandroid.com/tree/json
     */
    @GET("tree/json")
    fun communityKnowledgeTree(): Observable<PlayAndroidHttpResult<List<CommunityKnowledgeBean>>>

    /**
     * 导航数据
     * http://www.wanandroid.com/navi/json
     */
    @GET("navi/json")
    fun communityNavigation(): Observable<PlayAndroidHttpResult<List<CommunityNavigationBean>>>

    /**
     * 开眼视频Banner数据
     * http://www.wanandroid.com/navi/json
     */
    @GET("v2/feed?num=1")
    fun communityDailyBanner(): Observable<CommunityDailyBean>

    /**
     * 开眼视频往期视频列表数据
     * http://www.wanandroid.com/navi/json
     */
    @GET
    fun communityDailyVideo(@Url url: String): Observable<CommunityDailyBean>

    /**
     * 开眼视频视频详情数据
     * http://www.wanandroid.com/navi/json
     */
    @GET("v4/video/related")
    fun communityDailyVideoContent(@Query("id") id: Int): Observable<CommunityDailyIssueBean>
}