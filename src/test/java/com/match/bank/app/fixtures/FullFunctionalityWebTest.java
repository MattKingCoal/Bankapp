package com.match.bank.app.fixtures;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaders;

public class FullFunctionalityWebTest {
    private final String urlStub = "http://localhost:8080/Bankapp/";
    private String accountId;

    public FullFunctionalityWebTest() {
    }

    public boolean openAccountForUserWithAmountSucceeds(int ssid, int amount)
            throws JDOMException, MalformedURLException, IOException {
        SAXBuilder builder = new SAXBuilder(XMLReaders.DTDVALIDATING);
        String urlStr = urlStub + "open?ssid=" + ssid + "&amount=" + amount;
        HttpURLConnection con = (HttpURLConnection) new URL(urlStr).openConnection();
        con.setRequestMethod("POST");
        boolean success;
        try (InputStream is = con.getInputStream()) {
            Document doc = builder.build(is);
            Element root = doc.getRootElement();
            Element aE = root.getChild("accounts").getChildren().get(0);
            this.accountId = aE.getChildText("account-id");
            int id = Integer.parseInt(aE.getChildText("user-id"));
            int bal = new BigDecimal(aE.getChildText("balance")).intValue();
            success = (id == ssid && bal == amount);
        }
        return success;
    }

    public int balanceOfNewlyOpenedAccount() throws JDOMException, IOException {
        SAXBuilder builder = new SAXBuilder(XMLReaders.DTDVALIDATING);
        String urlStr = urlStub + "account?aid=" + accountId;
        int bal;
        try (InputStream is = new URL(urlStr).openStream()) {
            Document doc = builder.build(is);
            Element root = doc.getRootElement();
            Element aE = root.getChild("accounts").getChildren().get(0);
            bal = new BigDecimal(aE.getChildText("balance")).intValue();
        }
        return bal;
    }

    public boolean afterWithdrawalOfFromAccountBalanceEquals(int amount, int balance)
            throws IOException, JDOMException {
        SAXBuilder builder = new SAXBuilder(XMLReaders.DTDVALIDATING);
        String urlStr = urlStub + "withdraw?aid=" + accountId + "&amount=" + amount;
        HttpURLConnection con = (HttpURLConnection) new URL(urlStr).openConnection();
        con.setRequestMethod("POST");
        boolean success;
        try (InputStream is = con.getInputStream()) {
            Document doc = builder.build(is);
            Element root = doc.getRootElement();
            Element aE = root.getChild("accounts").getChildren().get(0);
            int bal = new BigDecimal(aE.getChildText("balance")).intValue();
            success = (bal == balance);
        }
        return success;
    }

    public boolean afterDepositOfToAccountBalanceEquals(int amount, int balance) throws JDOMException, IOException {
        SAXBuilder builder = new SAXBuilder(XMLReaders.DTDVALIDATING);
        String urlStr = urlStub + "deposit?aid=" + accountId + "&amount=" + amount;
        HttpURLConnection con = (HttpURLConnection) new URL(urlStr).openConnection();
        con.setRequestMethod("POST");
        boolean success;
        try (InputStream is = con.getInputStream()) {
            Document doc = builder.build(is);
            Element root = doc.getRootElement();
            Element aE = root.getChild("accounts").getChildren().get(0);
            int bal = new BigDecimal(aE.getChildText("balance")).intValue();
            success = (bal == balance);
        }
        return success;
    }

    public boolean deleteAccountsForUserSucceeds(int ssid) throws JDOMException, IOException {
        String urlStr = urlStub + "delete?ssid=" + ssid;
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(urlStr);
        int responseCode;
        try {
            HttpResponse response = client.execute(request);
            responseCode = response.getStatusLine().getStatusCode();
        } finally {
            request.reset();
        }

        return (responseCode == 200);
    }

    public boolean retrieveAccountsForUserReturnsNoAccounts(int ssid) throws Exception {
        SAXBuilder builder = new SAXBuilder(XMLReaders.DTDVALIDATING);
        String urlStr = urlStub + "accounts?ssid=" + ssid;
        boolean success;
        try (InputStream is = new URL(urlStr).openStream()) {
            Document doc = builder.build(is);
            Element root = doc.getRootElement();
            List<Element> aEls = root.getChild("accounts").getChildren();
            success = aEls.isEmpty();
        }
        return success;
    }
}
