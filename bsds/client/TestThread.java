/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsds.client;

import com.bsds.mvnjersey2.RFIDLiftData;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import org.json.JSONObject;

/**
 *
 * @author Ian Gortan
 */
public class TestThread extends Thread {

    String result;

    private JSONClient client;
    private JSONObject data;
    private int start;
    private int end;
    private ArrayList<RFIDLiftData> RFIDSringList;
    private int count = 0;

    public TestThread(int start, int end, JSONClient client, ArrayList<RFIDLiftData> RFIDSringList) {
        this.client = client;
        this.data = data;
        this.start = start;
        this.end = end;
        this.RFIDSringList = RFIDSringList;

    }

    public void run() {
        for (int i = start; i < end; i++) {
            client.putText(RFIDSringList.get(i));
        }

    }
}
