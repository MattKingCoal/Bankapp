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
import com.match.bank.exception.AccountNotFoundException;
import com.match.bank.exception.DataLayerException;
import com.match.bank.exception.InsufficientFundsException;
import com.match.bank.service.BankService;
import com.match.bank.service.PersistedBankService;

public class Withdraw extends HttpServlet {
    static Logger log = Logger.getLogger(Withdraw.class);

    private BankService service;

    @Override
    public void init(ServletConfig config) throws ServletException {
        service = PersistedBankService.getInstance();
    }

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int withdrawalAmount = Integer.parseInt(req.getParameter("amount"));
        int accountId = Integer.parseInt(req.getParameter("aid"));
        log.info("Attempting to withdraw " + withdrawalAmount + " from accountId: " + accountId);
        Account account = null;
        try {
            account = service.withdraw(accountId, withdrawalAmount);
        } catch (InsufficientFundsException | AccountNotFoundException e) {
            log.error(e.getMessage());
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        } catch (DataLayerException e) {
            log.error("Problem withdrawing from account: " + e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        List<Account> accs = new ArrayList<>();
        if (account != null) {
            accs.add(account);
        }
        String responseXml = new AccountsResponseXML().writeXMLResponse(accs);
        resp.setContentType("text/xml");
        resp.getOutputStream().write(responseXml.getBytes());
    }

}
