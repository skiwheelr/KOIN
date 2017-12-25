/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.koin.request;

import javax.servlet.AsyncContext;

/**
 *
 * @author akargarm
 */
public class AsyncProcessor implements Runnable {
    private AsyncContext asyncContext;
    
    public AsyncProcessor(AsyncContext asyncContext) {
        this.asyncContext = asyncContext;
    }

    @Override
    public void run() {
        
        
    }
}
