package com.example.project_ltandroid.retrofit;



import com.example.project_ltandroid.model.CategoryModel;
import com.example.project_ltandroid.model.NewProductModel;
import com.example.project_ltandroid.model.ToolbarModel;


import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;

public interface ApiBanHang {
    @GET("getloaisp.php")
    Observable<CategoryModel> getCategory();

    @GET("gettoolbar.php")
    Observable<ToolbarModel> getToolbar();

    @GET("getsp.php")
    Observable<NewProductModel> getNewProduct();

}
