/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.koin.controller;

import java.math.BigInteger;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;

import dev.koin.request.RequestService;
import dev.koin.transaction.TransactionService;

/**
 *
 * @author Terrapin
 */
public class Controller {

    public static void main(String[] args) {
        RequestService request = new RequestService();
        TransactionService transaction = new TransactionService();

        request.requestVersion();
        String password = "Khalifa2017";
        String path = "src/main/resources";
        String walletFile = "UTC--2017-09-09T03-44-31.939051329Z--308bb6a6598580bf340d31521d3ebea959bf109f";
        System.out.println(request.connectToEthereumWallet(password, path, walletFile));
        Web3j web3 = request.getWeb3();
        Credentials credentials = request.getCredentials();
        System.out.println(credentials.getAddress());
        request.transferKoin(BigInteger.valueOf(10000), credentials);
        System.out.println(request.getBalance(credentials).toString());
    }
}