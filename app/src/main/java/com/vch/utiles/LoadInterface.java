package com.vch.utiles;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by pintu22 on 17/11/17.
 */

public interface LoadInterface {


    @Multipart
    @POST("Orders/order_status_change")
    Call<ResponseBody> OrderStatusChange(@Part("user_id") RequestBody user_id,
                                         @Part("order_id") RequestBody order_id,
                                         @Part("transaction_id") RequestBody transaction_id
    );

    /*********************************************************/

    @Multipart
    @POST("Orders/check_sum")
    Call<ResponseBody> generateChecksum( @Part("ORDER_ID") RequestBody ORDER_ID,
                                         @Part("email") RequestBody email,
                                         @Part("amount") RequestBody TXN_AMOUNT,
                                         @Part("mobile_no") RequestBody mobile_no,
                                         @Part("CUST_ID") RequestBody CUST_ID);

    /************************REGISTRATION_WITHOUT_IMAGE*********************************/

    @POST("registration")
    @Multipart
    Call<ResponseBody> registerCall(
            @Part("name") RequestBody name,
            @Part("email") RequestBody email,
            @Part("password") RequestBody password,
            @Part("mobile") RequestBody mobile,
            @Part("fcm_code") RequestBody fcm_code,
            @Part("use_referrer_code") RequestBody use_referrer_code);

    /************************REGISTRATION_WITH_IMAGE*********************************/

    @Multipart
    @POST("registration")
    Call<ResponseBody> registerCall(
            @Part("name") RequestBody name,
            @Part("email") RequestBody email,
            @Part("password") RequestBody password,
            @Part("mobile") RequestBody mobile,
            @Part("fcm_code") RequestBody fcm_code,
            @Part("use_referrer_code") RequestBody use_referrer_code,
            @Part MultipartBody.Part file);

    /************************CHECK_OTP*********************************/

    @Multipart
    @POST("check_otp")
    Call<ResponseBody> CheckOtpCall(
            @Part("user_id") RequestBody userId,
            @Part("otp") RequestBody otp,
            @Part("msg_type") RequestBody msg_type,
            @Part("fcm_code") RequestBody fcm_code);

    /************************USER_PROFILE*********************************/

    @Multipart
    @POST("user_profile")
    Call<ResponseBody> getProfileCall(@Part("user_id") RequestBody name);

    /************************HOME*********************************/
    @Multipart
    @POST("home")
    Call<ResponseBody> homeCall(@Part("user_id") RequestBody userId);

    /************************PRODUCTS*********************************/
    @Multipart
    @POST("menu_product")
    Call<ResponseBody> productsCall(
            @Part("menu_id") RequestBody name,
            @Part("user_id") RequestBody userId);

    /************************LOGIN*********************************/

    @Multipart
    @POST("login")
    Call<ResponseBody> loginCall(
            @Part("mobile") RequestBody mobile,
            @Part("password") RequestBody password,
            @Part("fcm_code") RequestBody regId);

    /************************PRODUCT_SEARCH*********************************/

    @Multipart
    @POST("product_search")
    Call<ResponseBody> searchCall(
            @Part("text") RequestBody text,
            @Part("user_id") RequestBody userId);

    /************************UPDATE_CART*********************************/

    @Multipart
    @POST("update_cart")
    Call<ResponseBody> updateCartCall(
            @Part("product_id") RequestBody product_id,
            @Part("user_id") RequestBody user_id,
            @Part("status") RequestBody status,
            @Part("quantity_number") RequestBody quantity_number);

    /************************MY_CART*********************************/

    @Multipart
    @POST("my_cart")
    Call<ResponseBody> myCartCall(@Part("user_id") RequestBody user_id);

    /************************GET_LOYALTY*********************************/

    @Multipart
    @POST("get_loyalty")
    Call<ResponseBody> loyaltyCall(@Part("user_id") RequestBody user_id);

    /************************SET_ADDRESS_CALL*********************************/

    @Multipart
    @POST("get_address")
    Call<ResponseBody> setAddressCall(@Part("user_id") RequestBody user_id);

    /************************DELIVERY_SLOT_CALL*********************************/

    @GET("get_slots")
    Call<ResponseBody> deliverySlotCall();

    /************************APPLY_COUPON*********************************/

    @Multipart
    @POST("apply_coupon")
    Call<ResponseBody> applyCouponCall(
            @Part("user_id") RequestBody user_id,
            @Part("coupon_code") RequestBody coupon_code,
            @Part("subtotal") RequestBody subtotal
    );

    /************************CHANGE_PHOTO*********************************/

    @Multipart
    @POST("update_photo")
    Call<ResponseBody> changePhotoCall(
            @Part("user_id") RequestBody user_id,
            @Part MultipartBody.Part file);
    /************************CHANGE_PHOTO*********************************/

    @Multipart
    @POST("forget_password")
    Call<ResponseBody> forgotPasswordCall(
            @Part("mobile") RequestBody mobile);

    /************************UPDATE_PROFILE*********************************/

    @Multipart
    @POST("update_profile")
    Call<ResponseBody> updateProfileCall(
            @Part("user_id") RequestBody user_id,
            @Part("user_name") RequestBody user_name,
            @Part("user_email") RequestBody user_email);

    /************************CHANGE_PASSWORD*********************************/

    @Multipart
    @POST("change_password")
    Call<ResponseBody> changePassword(
            @Part("user_id") RequestBody user_id,
            @Part("old_password") RequestBody old_password,
            @Part("new_password") RequestBody new_password);

    /************************CHANGE_PASSWORD*********************************/

    @Multipart
    @POST("add_contact_us")
    Call<ResponseBody> contactUsCall(
            @Part("name") RequestBody name,
            @Part("phone") RequestBody mobile,
            @Part("email") RequestBody email,
            @Part("message") RequestBody messaga);

    /************************ADD_ADDRESS_CALL*********************************/

    @Multipart
    @POST("add_address")
    Call<ResponseBody> addAddressCall(
            @Part("label") RequestBody label,
            @Part("customer_name") RequestBody customer_name,
            @Part("house_number") RequestBody house_number,
            @Part("address") RequestBody address,
            @Part("mobile_no") RequestBody mobile_no,
            @Part("landmark") RequestBody landmark,
            @Part("pincode") RequestBody pincode,
            @Part("user_id") RequestBody user_id);


    /************************PLACE_ORDER_CALL*********************************/

    @Multipart
    @POST("Orders/add_order")
    Call<ResponseBody> placeOrderCall(
            @Part("user_id") RequestBody user_id,
            @Part("address_id") RequestBody address_id,
            @Part("delivery_time") RequestBody delivery_time,
            @Part("delType") RequestBody delType,
            @Part("redeam_point") RequestBody redeam_point,
            @Part("coupon_discount") RequestBody coupon_discount,
            @Part("coupon_code") RequestBody coupon_code    ,
            @Part("cashback") RequestBody cashback,
            @Part("payment_method") RequestBody payment_method,
            @Part("sub_total") RequestBody sub_total,
            @Part("spice_level") RequestBody spice_level,
            @Part("remark") RequestBody remark
            );

    /************************DELETE_ADDRESS*********************************/

    @Multipart
    @POST("delete_address")
    Call<ResponseBody> deleteAddressCall(
            @Part("user_id") RequestBody user_id,
            @Part("address_id") RequestBody address_id);

    /************************DELETE_ADDRESS*********************************/

    @Multipart
    @POST("add_user_review")
    Call<ResponseBody> addReviewCall(
            @Part("user_id") RequestBody user_id,
            @Part("rating") RequestBody rating,
            @Part("review") RequestBody review);

    /************************GET_ORDER*********************************/

    @Multipart
    @POST("get_orders")
    Call<ResponseBody> getOrderCall(@Part("user_id") RequestBody user_id);
    /************************GET_ORDER*********************************/

    @Multipart
    @POST("get_tifin_order")
    Call<ResponseBody> getTiffinBox(@Part("user_id") RequestBody user_id);

     /************************get_meal_plan*********************************/

    @GET("get_meal_plan")
    Call<ResponseBody> getMealPlan();

    @GET("policies")
    Call<ResponseBody> policyCall();
    @GET("terms_condition")
    Call<ResponseBody> termsNCondition();

    /************************ADD_ADDRESS_CALL*********************************/

    @Multipart
    @POST("update_address")
    Call<ResponseBody> updateAddressCall(
            @Part("label") RequestBody label,
            @Part("customer_name") RequestBody customer_name,
            @Part("house_number") RequestBody house_number,
            @Part("address") RequestBody address,
            @Part("mobile_no") RequestBody mobile_no,
            @Part("landmark") RequestBody landmark,
            @Part("pincode") RequestBody pincode,
            @Part("user_id") RequestBody user_id,

            @Part("address_id") RequestBody address_id
            );



    /************************ADD_ADDRESS_CALL*********************************/

    @Multipart
    @POST("tiffin_box")
    Call<ResponseBody> addTiffinBox(
            @Part("user_id") RequestBody user_id,
            @Part("meal_plan_id") RequestBody meal_plan_id,
            @Part("Sdays") RequestBody Sdays,
            @Part("meal_qty") RequestBody meal_qty,
            @Part("plan_amount") RequestBody plan_amount,
            @Part("lunch_dinner") RequestBody lunch_dinner,
            @Part("payment_method") RequestBody payment_method,
            @Part("user_address_id") RequestBody user_address_id
            );

    @Multipart
    @POST("add_party_order")
    Call<ResponseBody> partyOrderCall(
            @Part("user_id") RequestBody user_id,
            @Part("name") RequestBody name,
            @Part("mobile") RequestBody mobile,
            @Part("email") RequestBody email,
            @Part("message") RequestBody message);



    @Multipart
    @POST("ordered_payment_status")
    Call<ResponseBody> orderPaymentStatusCall(
            @Part("order_id") RequestBody order_id,
            @Part("tracking_id") RequestBody tracking_id,
            @Part("bank_ref_no") RequestBody bank_ref_no,
            @Part("order_status") RequestBody order_status,
            @Part("failure_message") RequestBody failure_message,
            @Part("payment_mode") RequestBody payment_mode,
            @Part("status_message") RequestBody status_message,
            @Part("currency") RequestBody currency,
            @Part("amount") RequestBody amount,
            @Part("billing_name") RequestBody billing_name,
            @Part("billing_address") RequestBody billing_address,
            @Part("billing_city") RequestBody billing_city,
            @Part("billing_state") RequestBody billing_state,
            @Part("billing_zip") RequestBody billing_zip,
            @Part("billing_country") RequestBody billing_country,
            @Part("billing_tel") RequestBody billing_tel,
            @Part("billing_email") RequestBody billing_email,
            @Part("response_code") RequestBody response_code,
            @Part("trans_date") RequestBody trans_date,
            @Part("card_name") RequestBody status_code);

}
