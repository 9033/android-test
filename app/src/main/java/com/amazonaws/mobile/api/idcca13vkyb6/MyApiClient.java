/*
 * Copyright 2010-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.amazonaws.mobile.api.idcca13vkyb6;

import java.util.*;

import com.amazonaws.mobile.api.idcca13vkyb6.model.Empty;


@com.amazonaws.mobileconnectors.apigateway.annotation.Service(endpoint = "https://cca13vkyb6.execute-api.ap-northeast-2.amazonaws.com/Prod")
public interface MyApiClient {


    /**
     * A generic invoker to invoke any API Gateway endpoint.
     * @param request
     * @return ApiResponse
     */
    com.amazonaws.mobileconnectors.apigateway.ApiResponse execute(com.amazonaws.mobileconnectors.apigateway.ApiRequest request);
    
    /**
     * 
     * 
     * @param name 
     * @return Empty
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/greeting", method = "GET")
    Empty greetingGet(
            @com.amazonaws.mobileconnectors.apigateway.annotation.Parameter(name = "name", location = "query")
            String name);
    
    /**
     * 
     * 
     * @return Empty
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/my-ip", method = "GET")
    Empty myIpGet();
    
    /**
     * 
     * 
     * @return Empty
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/skyred", method = "GET")
    Empty skyredGet();
    
    /**
     * 
     * 
     * @return Empty
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/tester", method = "GET")
    Empty testerGet();
    
    /**
     * 
     * 
     * @param username 
     * @return Empty
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/user/{username}/greet", method = "GET")
    Empty userUsernameGreetGet(
            @com.amazonaws.mobileconnectors.apigateway.annotation.Parameter(name = "username", location = "path")
            String username);
    
}

