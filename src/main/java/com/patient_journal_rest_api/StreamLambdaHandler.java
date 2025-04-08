package com.patient_journal_rest_api;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

// Serverless Java Container natively supports API Gateway's proxy integration models
// for requests and responses, you can create and inject custom models for methods
// that use custom mappings.

//from the AWS official springboot 3 serverless java container on github
//https://github.com/aws/serverless-java-container/wiki/Quick-start---Spring-Boot3
public class StreamLambdaHandler implements RequestStreamHandler {
    private static SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;
    static {
        try {
            //initialize SpringBoot app context
            handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(PatientJournalRestApiApplication.class);
            // If you are using HTTP APIs with the version 2.0 of the proxy model, use the getHttpApiV2ProxyHandler
            // method: handler = SpringBootLambdaContainerHandler.getHttpApiV2ProxyHandler(Application.class);
            // ^^ we are using Api gateway rest api so no need ^^
        } catch (ContainerInitializationException e) {
            // if we fail here. We re-throw the exception to force another cold start
            e.printStackTrace();
            throw new RuntimeException("Could not initialize Spring Boot application", e);
        }
    }

    /*
    *
    * http address
    * https://1luu4uw639.execute-api.us-west-1.amazonaws.com/Prod
    * */

    // Client makes HTTP request to API gateway we are using
    // /{proxy+} so all requests to the endpoint go to lambda

    // Lambda takes the inbound payload
    // ex: POST/GET/etc JSON payload etc etc etc
    // Lambda initilizes SpringBoot application context
    // and 'passes' in the payload to spring for processing
    // we will have the API controller handle /api/<insert here>
    // and if it is anything else like / or /profile /event
    // the user will get served the index.html
    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context)
            throws IOException {
        handler.proxyStream(inputStream, outputStream, context);
    }
}