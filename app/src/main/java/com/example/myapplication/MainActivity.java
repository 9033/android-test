package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

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

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE="com.example.myapplication.MESSAGE";

    private AWSCredentialsProvider credentialsProvider;
    private AWSConfiguration configuration;

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private MyApiClient apiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        https://docs.aws.amazon.com/ko_kr/aws-mobile/latest/developerguide/mobile-hub-getting-started.html#mobile-hub-add-aws-mobile-sdk-connect-to-your-backend
        */
        //cognito로 인증을 받고 나면 이 코드로도 실행은 되긴됨. 그런데 시간이 지나면 인증이 안됬다고 오류가 나옴.
//        AWSMobileClient.getInstance().initialize(this, new AWSStartupHandler() {
//            @Override
//            public void onComplete(AWSStartupResult awsStartupResult) {
//                Log.d("YourMainActivity", "AWSMobileClient is instantiated and you are connected to AWS!");
//            }
//        }).execute();

        //initialize with cognito
        AWSMobileClient.getInstance().initialize(this, new AWSStartupHandler() {
            @Override
            public void onComplete(AWSStartupResult awsStartupResult) {

                // Obtain the reference to the AWSCredentialsProvider and AWSConfiguration objects
                credentialsProvider = AWSMobileClient.getInstance().getCredentialsProvider();
                configuration = AWSMobileClient.getInstance().getConfiguration();

                // Use IdentityManager#getUserID to fetch the identity id.
                IdentityManager.getDefaultIdentityManager().getUserID(new IdentityHandler() {
                    @Override
                    public void onIdentityId(String identityId) {
                        Log.d("YourMainActivity", "Identity ID = " + identityId);

                        // Use IdentityManager#getCachedUserID to
                        //  fetch the locally cached identity id.
                        final String cachedIdentityId =
                                IdentityManager.getDefaultIdentityManager().getCachedUserID();
                    }

                    @Override
                    public void handleError(Exception exception) {
                        Log.d("YourMainActivity", "Error in retrieving the identity" + exception);
                    }
                });
            }
        }).execute();

        // Create the client
//        apiClient = new ApiClientFactory()
//                .credentialsProvider(AWSMobileClient.getInstance().getCredentialsProvider())
//                .build(MyApiClient.class);

        setContentView(R.layout.activity_main);
    }

    public void sendMessage(View view){
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText=(EditText)findViewById(R.id.editText);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE,message);
        startActivity(intent);
        //callCloudLogic();
    }

//    public void callCloudLogic() {
//        // Create components of api request
//        final String method = "GET";
//
//        final String path = "/greeting";
//
//        final String body = "";
//        final byte[] content = body.getBytes(StringUtils.UTF8);
//
//        final Map parameters = new HashMap<>();
//        parameters.put("lang", "en_US");
//
//        final Map headers = new HashMap<>();
//
//        // Use components to create the api request
//        ApiRequest localRequest =
//                new ApiRequest(apiClient.getClass().getSimpleName())
//                        .withPath(path)
//                        .withHttpMethod(HttpMethodName.valueOf(method))
//                        .withHeaders(headers)
//                        .addHeader("Content-Type", "application/json")
//                        .withParameters(parameters);
//
//        // Only set body if it has content.
//        if (body.length() > 0) {
//            localRequest = localRequest
//                    .addHeader("Content-Length", String.valueOf(content.length))
//                    .withBody(content);
//        }
//
//        final ApiRequest request = localRequest;
//
//        // Make network call on background thread
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Log.d(LOG_TAG,
//                            "Invoking API w/ Request : " +
//                                    request.getHttpMethod() + ":" +
//                                    request.getPath());
//
//                    final ApiResponse response = apiClient.execute(request);
//
//                    final InputStream responseContentStream = response.getContent();
//
//                    if (responseContentStream != null) {
//                        final String responseData = IOUtils.toString(responseContentStream);
//                        Log.d(LOG_TAG, "Response : " + responseData);
//                    }
//
//                    Log.d(LOG_TAG, response.getStatusCode() + " " + response.getStatusText());
//
//                } catch (final Exception exception) {
//                    Log.e(LOG_TAG, exception.getMessage(), exception);
//                    exception.printStackTrace();
//                }
//            }
//        }).start();
//    }
}
