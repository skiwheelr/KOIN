/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.koin.transaction;

import java.math.BigDecimal;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

/**
 *
 * @author Terrapin
 */
public class TransactionService {

    Web3j web3 = Web3j.build(new HttpService());
    Credentials credentials;

    public boolean sendEther(Web3j web3, Credentials credentials, BigDecimal etherToExchange) {
        TransactionReceipt transactionReceipt;
        boolean flag = false;
        try {
            System.out.println("Present Project Directory : " + System.getProperty("user.dir"));
            transactionReceipt = Transfer.sendFunds(web3, credentials, "0x0146e80a7f3fee9c789a779fac835bda983ea2c8", etherToExchange, Convert.Unit.ETHER).sendAsync().get();
            System.out.println("Funds transfer completed: " + transactionReceipt.getBlockHash());
            flag = true;
        } catch (Exception e) {
            flag = false;
        }
        finally {
            return flag;
        }
    }
    
    public boolean etherSupplySufficient(Web3j web3, Credentials credentials, BigDecimal etherToExchange) {
        boolean flag = false;
        try {
            EthGetBalance ethBalance = web3.ethGetBalance(credentials.getAddress(), DefaultBlockParameterName.LATEST).sendAsync().get();
            BigDecimal ethBalanceBD = new BigDecimal(ethBalance.getBalance());
            
            if(ethBalanceBD.compareTo(etherToExchange) >= 0) {
                System.out.println("Sufficient ether supply");
                flag = true;
            }
            
            else {
                flag = false;
            }
        } catch (Exception e) {
            System.err.println("Ether...");
        }
        finally {
            return flag;
        }
    }
    
    
}
