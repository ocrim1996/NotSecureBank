package com.notsecurebank.servlet;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class AccountViewServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LogManager.getLogger(AccountViewServlet.class);

    public AccountViewServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.info("doGet");

        // show account balance for a particular account
        if (request.getRequestURL().toString().endsWith("showAccount")) {
            String accountName = request.getParameter("listAccounts");
            if (accountName == null) {
                response.sendRedirect(request.getContextPath() + "/bank/main.jsp");
                return;
            }
            LOG.info("Balance for accountName = '" + accountName + "'.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/bank/balance.jsp?acctId=" + accountName);
            dispatcher.forward(request, response);
            return;
        }
        // this shouldn't happen
        else if (request.getRequestURL().toString().endsWith("showTransactions"))
            doPost(request, response);
        else
            super.doGet(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.info("doPost");

        // show transactions within the specified date range (if any)
        if (request.getRequestURL().toString().endsWith("showTransactions")) {
            String startTime = request.getParameter("startDate");
            String endTime = request.getParameter("endDate");

            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
            String sanitizedStartTime = null;
            String sanitizedEndTime = null;

            try {
                // Parsing delle date di input
                java.util.Date startDate = inputFormat.parse(startTime);
                java.util.Date endDate = inputFormat.parse(endTime);

                // Formattazione delle date di output nel formato desiderato
                sanitizedStartTime = outputFormat.format(startDate);
                sanitizedEndTime = outputFormat.format(endDate);

            } catch (ParseException e) {
                // Gestisci l'errore o restituisci un messaggio di errore all'utente
                e.printStackTrace();
            }

            LOG.info("Transactions within '" + sanitizedStartTime + "' and '" + sanitizedEndTime + "'.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/bank/transaction.jsp?" + ((sanitizedStartTime != null) ? "&startTime=" + sanitizedStartTime : "") + ((sanitizedEndTime != null) ? "&endTime=" + sanitizedEndTime : ""));
            dispatcher.forward(request, response);
        }
    }
}
