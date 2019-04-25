package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.http.HttpMethodName;
import com.amazonaws.mobile.auth.core.IdentityHandler;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.apigateway.ApiRequest;
import com.amazonaws.mobileconnectors.apigateway.ApiResponse;
import com.amazonaws.util.IOUtils;
import com.amazonaws.util.StringUtils;

import com.amazonaws.mobile.api.idcca13vkyb6.MyApiClient;
import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.*;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class DisplayMessageActivity extends AppCompatActivity {
    private static final String LOG_TAG = DisplayMessageActivity.class.getSimpleName();

    private AWSCredentialsProvider credentialsProvider;
    private AWSConfiguration configuration;

    private MyApiClient apiClient;

    private String res="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AWSMobileClient.getInstance().initialize(this, new AWSStartupHandler() {
            @Override
            public void onComplete(AWSStartupResult awsStartupResult) {
                Log.d("DisplayMessageActivity", "AWSMobileClient is instantiated and you are connected to AWS!");
            }
        }).execute();

        // Create the client
        apiClient = new ApiClientFactory()
                .credentialsProvider(AWSMobileClient.getInstance().getCredentialsProvider())
                .build(MyApiClient.class);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        callCloudLogic(message);
//        TextView textView = findViewById(R.id.textView);
//        textView.setText(message);

    }

    public void callCloudLogic(String body) {
        // Create components of api request
        final String method = "GET";

        final String path = "/greeting";

//        final String body = "";
        final byte[] content = body.getBytes(StringUtils.UTF8);

        final Map parameters = new HashMap<>();
        parameters.put("lang", "jen_US");
        if( body.length()>0) parameters.put("name",body);

        final Map headers = new HashMap<>();

        // Use components to create the api request
        ApiRequest localRequest =
                new ApiRequest(apiClient.getClass().getSimpleName())
                        .withPath(path)
                        .withHttpMethod(HttpMethodName.valueOf(method))
                        .withHeaders(headers)
                        .addHeader("Content-Type", "application/json")
                        .withParameters(parameters);

        // Only set body if it has content.
        /*
        "The request signature we calculated does not match the signature you provided. Check your AWS Secret Access Key and signing method. Consult the service documentation for details.\n\nThe Canonical String for this request should have been\n'POST\n/Prod/greeting\nlang=en_US&name=hh\nhost:cca13vkyb6.execute-api.ap-northeast-2.amazonaws.com\nx-amz-date:20190425T080940Z\nx-amz-security-token:AgoGb3JpZ2luEHsaDmFwLW5vcnRoZWFzdC0yIoACWbvTCvxtARYiV9bY1A5tUSXY4fPgwlxIOjAjNgS7MD6N/bR8WUyu4979+03gs+rYaFQaSVxeizFNj7MeDb5XNS/geRnQ1C59+32+lCLPtKOeobUKyj6gOzajPvtWDuh2gn+zCgyT+Q+rFHknnLHghPlekx5KA8DW3Ct5Bi1ZpTsqQc9/2oQIw+IAnVoTUvR74dYaXozAiIt+EHXUumAFaxX2TWUQ5COkKkW7o9/xa0yOpfIQSUEU68qK1Io96wWQXDHjVitqOyFRG+PBoft79azMfRIPCZsKlOGeqADra4lk1tW0cE+lgau/mQRAb7KHwDi6kC7k0RM2QJqtkiL5Eiq/Bgjo//////////8BEAEaDDIyMjM3ODIzNjg5OCIM+SYGVENtm0B2pabyKpMGdJEy+CHUsksXLsTg69THsFlUFKEgCxR0qAinagasoDYlUZuBjwC1Dc0VY8/OetwDWcXKuf5wRanOL8ahaTnAva4DLfRUIodxkxfbJ1eKCqvMSch8DqjxCEWcZ9WF6RB0tFlFhvsIt/BYBReO50o/HnwbGC2jPk+PIlR0y4lbjSUhe2y8IhzUUv/98OksV2zPwOYj229c6dCEsgCZaYirsrU1z9N9/+nqqV00FujrxCSvgCfZBfl6dNU9L1bd0JdhJ1XXiP0d3rSYh1cYQJZRoOfkaLGyEVQIjtHO/esI0cQ7yb9YucIPFNksKIna9N6SyrOx1XU3eyO7c0Ya4YIigKfXM8K6syMKNuZ5vFkU4GzK3FeXP0ra0IPT8bhcdVBuK93Y5rA9pZpWgivWybfnYvU4dJqkxo1CSqZzqZ/7pNHnL7y0+6xBG6DLVdRZ/lPNPM6Se6mF0L5deMFQDBGfcimTMIK8tdMcWiRtfWZNsjFV/ChwJ5Kghb06MP1WWUCkYn2/S0BRjILwULZkJVyZXSlAxRsPvzeoKVc+zh4nm2c+wvtRcWg6zlZ41JABSugYEbiCHgz26ae93NjqXHPozn4nUnhFWc7tub9cINi8WGHwsN1IVtU5Jb4egE4WWjpgqkDHkfLszBxxx9r42dmerTecGeuTyrHmc7+NU4xL+nnNu66bxuvTho5JlYg1NU4c8+8okF5oTYlt8WChszHsNk9v/YoLNsVZSF3t3NLCQ02tbZ4x0KtNK6XGk4Vc1b9+EmL3zpGE8jr+ePmQRUIepweqSUqi081RLElCiGZ39O0KHUkQZptm2hSsKbFuIYWrrvD+weN0xgUNn0eogkkcV+c34LAC05Ckv/aLazxh2pd//3Ggl2TeurkGqihbIDJ/3mwTmimPaRhyc5mtEwLx18F+ySLJJi0NzQLLbOvmlVURIXjGuhy93hB74DlIEGBKqS5ACbH/+FoyUpqLEZpZWpnjnjCS1Z4E/5FHoPuoFYdvxpfRA2K8nSDYsPul4TNwVnJwkT2tOqq2HhEx9HICVHhlYzCwv4XmBQ==\n\nhost;x-amz-date;x-amz-security-token\n72b289ec78e0a928c565480a435453e30acb92eddb3b78ff168b28737cf6a849'\n\nThe String-to-Sign should have been\n'AWS4-HMAC-SHA256\n20190425T080940Z\n20190425/ap-northeast-2/execute-api/aws4_request\ncc6295a55ac73166f4271805ecf2ff43840cfd04721209c58246bc12b2500395'\n"}
2019-04-25 17:09:40.958 5948-6072/com.example.myapplication D/DisplayMessageActivity: 403 Forbidden
        * */
//        if (body.length() > 0) {
//            localRequest = localRequest
//                    .addHeader("Content-Length", String.valueOf(content.length))
//                    .withBody(content);
//        }

        final ApiRequest request = localRequest;

        // Make network call on background thread
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(LOG_TAG,
                            "Invoking API w/ Request : " +
                                    request.getHttpMethod() + ":" +
                                    request.getPath());

                    final ApiResponse response = apiClient.execute(request);

                    final InputStream responseContentStream = response.getContent();

                    if (responseContentStream != null) {
                        final String responseData = IOUtils.toString(responseContentStream);
                        Log.d(LOG_TAG, "Response : " + responseData);

                        //res=responseData;
                        JsonParser parser = new JsonParser();
                        JsonObject obj = (JsonObject) parser.parse(responseData);
                        res = String.valueOf(obj.get("name"));

                    }

                    Log.d(LOG_TAG, response.getStatusCode() + " " + response.getStatusText());
                } catch (final Exception exception) {
                    Log.e(LOG_TAG, exception.getMessage(), exception);
                    exception.printStackTrace();
                }
            }
        });

        try {
            t.start();
            t.join();
            TextView textView = findViewById(R.id.textView);
            textView.setText(res);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
