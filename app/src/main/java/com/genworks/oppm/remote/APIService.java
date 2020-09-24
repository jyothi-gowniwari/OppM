package com.genworks.oppm.remote;

import com.genworks.oppm.model.AccountDescribe;
import com.genworks.oppm.model.DocumentModel;
import com.genworks.oppm.model.LocationModule;
import com.genworks.oppm.model.Login;
import com.genworks.oppm.model.LoginAndFetchModules;
import com.genworks.oppm.model.ProductnameQuery;
import com.genworks.oppm.model.SaveContactModule;
import com.genworks.oppm.model.SyncModule;
import com.genworks.oppm.model.UserAccountQuery;
import com.genworks.oppm.model.UserContactQuery;
import com.genworks.oppm.model.UserModule;
import com.genworks.oppm.model.UserOpportunityQuery;

import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIService {


    //Login Api
    @POST("api.php")
    @FormUrlEncoded
    Call<Login> GetLoginList(@Field("_operation") String operation,
                             @Field("username") String username,
                             @Field("password") String password);

    //LoginForFetchModule Api
    @POST("api.php")
    @FormUrlEncoded
    Call<LoginAndFetchModules> GetLoginModuleList(@Field("_operation") String operation,
                                                  @Field("username") String username,
                                                  @Field("password") String password);


    //Describe for Accounts Api
    @POST("api.php")
    @FormUrlEncoded
    Call<AccountDescribe> GetFieldDetailsList(@Field("_operation") String operation,
                                              @Field("_session") String session,
                                              @Field("module") String module);

    //Documents Api
    @POST("api.php")
    @FormUrlEncoded
    Call<DocumentModel> GetDocumentList(@Field("_operation") String operation,
                                            @Field("_session") String session,
                                            @Field("module") String module,
                                        @Field("record") String record);

    //SyncModule for Accounts
    @POST("api.php")
    @FormUrlEncoded
    Call<SyncModule> GetSyncModuleList(@Field("_operation") String operation,
                                       @Field("_session") String session,
                                       @Field("module") String module,
                                       @Field("syncToken") String syncToken,
                                       @Field("mode") String mode);

   // user Module
    @POST("api.php")
    @FormUrlEncoded
    Call<UserModule> UserRecordDetails(@Field("_operation") String operation,
                                       @Field("_session") String session,
                                       @Field("query") String query);

    // user Module
    @POST("api.php")
    @FormUrlEncoded
    Call<UserAccountQuery> UserAccountRecordDetails(@Field("_operation") String operation,
                                             @Field("_session") String session,
                                             @Field("query") String query);

    // product name
    @POST("api.php")
    @FormUrlEncoded
    Call<ProductnameQuery> UserProductnameDetails(@Field("_operation") String operation,
                                                          @Field("_session") String session,
                                                          @Field("query") String query);
    // user Module
    @POST("api.php")
    @FormUrlEncoded
    Call<UserContactQuery> UserContactRecordDetails(@Field("_operation") String operation,
                                                    @Field("_session") String session,
                                                    @Field("query") String query);

    @POST("api.php")
    @FormUrlEncoded
    Call<UserOpportunityQuery> UserOpportunityRecordDetails(@Field("_operation") String operation,
                                                              @Field("_session") String session,
                                                              @Field("query") String query);

    // Save Conatct Module
    @POST("api.php")
    @FormUrlEncoded
    Call<SaveContactModule> SaveContactDetails(@Field("_operation") String operation,
                                               @Field("_session") String session,
                                               @Field("module") String module,
                                               @Field("values") JSONObject values);

    @POST("api.php")
    @FormUrlEncoded
    Call<SaveContactModule> UpdateDetails(@Field("_operation") String operation,
                                               @Field("_session") String session,
                                               @Field("module") String module,
                                                @Field("record") String record,
                                               @Field("values") JSONObject values);

    @POST("api.php")
    @FormUrlEncoded
    Call<LocationModule> LocationDetails(@Field("_operation") String operation,
                                                  @Field("_session") String session,
                                                  @Field("query") String query);
    //closed won for opportunity
    @POST("api.php")
    @Multipart
    Call<SaveContactModule> SaveDocuments(@Part ("_operation") RequestBody operation,
                                          @Part ("_session") RequestBody session,
                                          @Part ("module") RequestBody module,
                                          @Part ("sourceRecord") RequestBody sourceRecord,
                                          @Part ("values") RequestBody values,
                                          @Part ("sourceModule") RequestBody sourceModule,
                                          @Part ("file") MultipartBody.Part file);


}

