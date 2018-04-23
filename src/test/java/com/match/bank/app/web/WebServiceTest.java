package com.match.bank.app.web;

import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class WebServiceTest {

    public WebServiceTest() {
        // TODO Auto-generated constructor stub
    }

    // @Test
    public void testWebservice() throws IOException, JDOMException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet("http://localhost:8080/Bankapp/account?aid=12345");
        HttpResponse response = client.execute(request);
        ResponseHandler<String> handler = new BasicResponseHandler();
        String body = handler.handleResponse(response);
        request.releaseConnection();
        System.out.println(body);

        SAXBuilder builder = new SAXBuilder();
        InputStream is = new ByteArrayInputStream(body.getBytes());
        Document doc = builder.build(is);
        assertNotNull(doc);
        Element root = doc.getRootElement();
        assertNotNull(root);
        is.close();
    }
}
