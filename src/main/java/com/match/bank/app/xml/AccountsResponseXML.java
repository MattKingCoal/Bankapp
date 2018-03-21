package com.match.bank.app.xml;

import java.util.List;

import com.match.bank.account.Account;

public class AccountsResponseXML extends ResponseXML {

    @Override
    public String writeXMLResponse(List<Account> accounts) {
        StringBuilder sb = new StringBuilder();
        sb.append(DOC_TYPE);
        sb.append(START);
        sb.append("<accounts>\n");
        for (Account account : accounts) {
            sb.append(account.toAccountXML());
        }
        sb.append("</accounts>\n");
        sb.append(END);

        return sb.toString();
    }
}
