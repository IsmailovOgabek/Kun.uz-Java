package com.example.service;


import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SmsService {


    public void sendSms(String phone, String message) {
        String token = "Bearer " + getSmsToken();
        OkHttpClient client = new OkHttpClient();


        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("mobile_phone", phone)
                .addFormDataPart("message", message)
                .addFormDataPart("from", "4546")

                .build();
        Request request = new Request.Builder()
                .url("https://notify.eskiz.uz/api/message/sms/send")
                .method("POST", body)
                .header("Authorization", token)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                System.out.println(response);
            } else {
                throw new IOException();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

     /*   Thread thread = new Thread(() -> {
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    System.out.println(response);
                } else {
                    throw new IOException();
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        });

        Thread.UncaughtExceptionHandler uncaughtExceptionHandler = (th, ex) -> {
            ex.printStackTrace();
        };
        thread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        thread.start();*/
    }

    private String getSmsToken() {
        return "token....";
    }
}
