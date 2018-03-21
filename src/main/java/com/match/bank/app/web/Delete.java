package com.match.bank.app.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.match.bank.account.Account;
import com.match.bank.exception.AccountNotFoundException;
import com.match.bank.exception.DataLayerException;
import com.match.bank.exception.UserNotFoundException;
import com.match.bank.service.BankService;
import com.match.bank.service.PersistedBankService;

public class Delete extends HttpServlet {
    static Logger log = Logger.getLogger(Delete.class);

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
        int userId = Integer.parseInt(req.getParameter("ssid"));
        log.info("Deleting accounts for user: " + userId);
        try {
            List<Account> accounts = service.retrieveAccountsByUserId(userId);
            for (Account acc : accounts) {
                int accId = acc.getAccountID();
                service.deleteAccount(accId);
                log.info("Account deleted - " + accId);
            }
        } catch (DataLayerException | AccountNotFoundException e) {
            log.error("Problem deleting accounts: " + e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (UserNotFoundException e) {
            log.error(e.getMessage());
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        log.info("Deleted accounts for user: " + userId);
    }
}
