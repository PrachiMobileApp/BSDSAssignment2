/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsds.client;

/**
 *
 * @author prach
 */
public class TestReadThreads extends Thread {

    private ClientReader client;
    private int start;
    private int end;
    
    
    public TestReadThreads(int start, int end,ClientReader client){
        this.start=start;
        this.end=end;
        this.client=client;
      
    }
    
    public void run(){
        
        for(int i=start;i<end;i++){
            System.out.println(client.getIt(i,1));
        }
        
    }
       
    

}
