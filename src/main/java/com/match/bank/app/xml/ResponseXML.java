package com.match.bank.app.xml;

import java.util.List;

import com.match.bank.account.Account;

public abstract class ResponseXML {

    public final static String DOC_TYPE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE bankdata SYSTEM \"bank.dtd\">\n";
    public final static String START = "<bankdata>\n<origin>match</origin>\n";
    public final static String END = "</bankdata>\n";

    public abstract String writeXMLResponse(List<Account> accounts);

}
