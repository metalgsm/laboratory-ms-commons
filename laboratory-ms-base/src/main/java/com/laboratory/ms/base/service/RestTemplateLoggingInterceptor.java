package com.laboratory.ms.base.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class RestTemplateLoggingInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestTemplateLoggingInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        logRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        logResponse(response);
        return response;
    }

    private void logRequest(HttpRequest request, byte[] body) throws IOException {
    	String _body = new String(body, StandardCharsets.UTF_8);
    	
    	try {
    		ObjectMapper mapper = new ObjectMapper();
    		
    		JsonNode _json = mapper.readTree(body);
    		
    		ObjectNode json = (ObjectNode) _json;
    		
    		_body = json.toString();
    		
    	}catch (Exception e) {
			// TODO: handle exception
		}
    	
        if (LOGGER.isDebugEnabled()) {
            StringBuilder msg = new StringBuilder();
            // @formatter:off
            msg.append("Request=[")
                .append("URI=").append(request.getURI()).append(";")
                .append("Method=").append(request.getMethod()).append(";")
                .append("Headers=").append(request.getHeaders()).append(";")
                .append("Body=").append(_body)
                .append("]");
            // @formatter:on
            LOGGER.debug(msg.toString());
        }
    }

    private void logResponse(ClientHttpResponse response) throws IOException {
        if (LOGGER.isDebugEnabled()) {
            StringBuilder msg = new StringBuilder();
            // @formatter:off
            msg.append("Response=[")
                .append("Status code=").append(response.getStatusCode()).append(";")
                .append("Status text=").append(response.getStatusText()).append(";")
                .append("Headers=").append(response.getHeaders()).append(";")
                .append("Body=").append(StreamUtils.copyToString(response.getBody(), StandardCharsets.UTF_8))
                .append("]");
            // @formatter:on
            LOGGER.debug(msg.toString());
        }
    }

}
