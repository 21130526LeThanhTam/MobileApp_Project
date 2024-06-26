package com.example.project_ltandroid.retrofit;



import com.example.project_ltandroid.model.CategoryModel;
import com.example.project_ltandroid.model.CategoryNameModel;
import com.example.project_ltandroid.model.NewProductModel;
import com.example.project_ltandroid.model.ToolbarModel;


import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiBanHang {
    @GET("getloaisp.php")
    Observable<CategoryModel> getCategory();

    @GET("gettoolbar.php")
    Observable<ToolbarModel> getToolbar();

    @GET("getsp.php")
    Observable<NewProductModel> getNewProduct();

    @POST("getchitiet.php")
    @FormUrlEncoded
    Observable<NewProductModel> getProduct(
            @Field("page") int page,
            @Field("category_id") int category_id
    );

    @POST("getcategoryname.php")
    @FormUrlEncoded
    Observable<CategoryNameModel> getCategoryName(
            @Field("category_id") int category_id
    );



}
