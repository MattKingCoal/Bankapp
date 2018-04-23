package com.match.bank.app.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.match.bank.account.Account;
import com.match.bank.app.xml.AccountsResponseXML;
import com.match.bank.exception.DataLayerException;
import com.match.bank.service.BankService;
import com.match.bank.service.PersistedBankServiceFactory;

public class Open extends HttpServlet {
    static Logger log = Logger.getLogger(Open.class);

    private BankService service;

    @Override
    public void init(ServletConfig config) throws ServletException {
        service = PersistedBankServiceFactory.getInstance();
    }

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int ssid = Integer.parseInt(req.getParameter("ssid"));
        int initialbalance = Integer.parseInt(req.getParameter("amount"));
        log.info("Opening account. SSID: " + ssid + ", Initial balance: " + initialbalance);
        List<Account> accounts = new ArrayList<>();
        try {
            Account newAcc = service.openAccount(ssid, initialbalance);
            log.info("Account succesfully created - " + newAcc);
            accounts.add(newAcc);

        } catch (DataLayerException e) {
            log.error("Problem creating account: " + e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        String responseXml = new AccountsResponseXML().writeXMLResponse(accounts);
        resp.setContentType("text/xml");
        resp.getOutputStream().write(responseXml.getBytes());
    }
}
