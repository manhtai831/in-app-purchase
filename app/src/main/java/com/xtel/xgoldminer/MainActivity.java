package com.xtel.xgoldminer;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.google.gson.Gson;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private BillingClient billingClient;
    private static final String TAG = "MainActivity";
    private Button button;
    List<SkuDetails> skuDetails = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: OK");

        button = (Button) findViewById(R.id.button);
        billingClient = BillingClient.newBuilder(this)
                .enablePendingPurchases()
                .setListener((billingResult, list) -> {
                    //TODO: Hàm này sẽ trả về kết quả khi người dùng thực hiện mua hàng.
                })
                .build();
        //TODO: Connect ứng dụng của bạn với Google Billing
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                //TODO: Sau khi connect thành công, thử lấy thông tin các sản phẩm


                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    Log.d(TAG, "onBillingSetupFinished: Connect OK");
                    List<String> skuList = new ArrayList<>();
                    skuList.add("99cent_buy_gold");
                    skuList.add("4699cent_buy_gold");
                    skuList.add("999cent_buy_gold");
                    skuList.add("199cent_buy_gold");
                    skuList.add("499cent_buy_gold");
                    skuList.add("1999cent_buy_gold");
                    SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                    params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
                    billingClient.querySkuDetailsAsync(params.build(),
                            new SkuDetailsResponseListener() {
                                @Override
                                public void onSkuDetailsResponse(BillingResult billingResult,
                                                                 List<SkuDetails> skuDetailsList) {

                                    Log.d(TAG, "onSkuDetailsResponse: response OK" + skuDetailsList);
                                    if (skuDetailsList != null) {
                                        skuDetails.addAll(skuDetailsList);
                                    }
                                }
                            });
                }


            }

            @Override
            public void onBillingServiceDisconnected() {
                //TODO: Connect Google Play not success
                Log.d(TAG, "onBillingServiceDisconnected: Connect Google Play not success");
            }
        });


        button.setOnClickListener(v -> {
            BillingFlowParams billingFlowParam = BillingFlowParams
                    .newBuilder()
                    .setSkuDetails(skuDetails.get(0))
                    .build();

            billingClient.launchBillingFlow(this, billingFlowParam);
        });
    }


}

