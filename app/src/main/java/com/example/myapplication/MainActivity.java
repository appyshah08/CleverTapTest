package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.clevertap.android.sdk.CleverTapAPI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CleverTapAPI clevertapDefaultInstance = CleverTapAPI.getDefaultInstance(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupFireBaseToken(clevertapDefaultInstance);
        Button btn = (Button) findViewById(R.id.btnPlay);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> prodViewedAction = new HashMap<String, Object>();
                prodViewedAction.put("Product ID", 1);
                prodViewedAction.put("Product Image", "https://d35fo82fjcw0y8.cloudfront.net/2018/07/26020307/customer-success-clevertap.jpg");
                prodViewedAction.put("Product Name", "CleverTap");


                clevertapDefaultInstance.pushEvent("Product Viewed", prodViewedAction);
            }
        });
        clevertapDefaultInstance.createNotificationChannel(getApplicationContext(),"testCleverTap","Test CleverTap","Clevertap testing", NotificationManager.IMPORTANCE_MAX,true);
        setUserDetails(clevertapDefaultInstance);

    }

    private void setupFireBaseToken(CleverTapAPI clevertapDefaultInstance) {
        try {
            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            if (!task.isSuccessful()) {
                              //  Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                                return;
                            }
                            // Get new FCM registration token
                            String token = task.getResult();
                            clevertapDefaultInstance.pushFcmRegistrationId(token, true);
                        }
                    });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setUserDetails(CleverTapAPI cleverTapInstance) {
        try {
            HashMap<String, Object> profileUpdate = new HashMap<String, Object>();
            profileUpdate.put("Name", "Dhaval GOsalia");    // String
            profileUpdate.put("Identity", 77026548);      // String or number
            profileUpdate.put("Phone", "+9920099200");
            profileUpdate.put("Email", "gosaliadhaval8@gmail.com");
            profileUpdate.put("Gender", "M");
            profileUpdate.put("DOB", new Date());
            ArrayList<String> stuff = new ArrayList<String>();
            stuff.add("bag");
            stuff.add("shoes");
            profileUpdate.put("MyStuff", stuff);                        //ArrayList of Strings
            String[] otherStuff = {"Jeans","Perfume"};
            profileUpdate.put("MyStuff", otherStuff);

            cleverTapInstance.onUserLogin(profileUpdate);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}