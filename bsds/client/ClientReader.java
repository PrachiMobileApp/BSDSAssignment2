/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsds.client;

import static com.bsds.client.JSONClient.NumberOfRequestSent;
import com.bsds.mvnjersey2.RFIDLiftData;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * Jersey REST client generated for REST resource:MyResource [myresource]<br>
 * USAGE:
 * <pre>
 *        ClientReader client = new ClientReader();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author prach
 */
public class ClientReader {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://34.236.3.111:8080/SKierMavenPProject/webapi/myresource";
    private String GET_URI = BASE_URI + "/myvert" + "?SkierID=";

    static AtomicInteger NumberOfRequestSent = new AtomicInteger(0);
    private static final int NUM_THREADS = 20;

    static synchronized void totalNumberOfRequestSent() {
        NumberOfRequestSent.addAndGet(1);
    }

    static final CopyOnWriteArrayList<Long> Latency = new CopyOnWriteArrayList<>();
    static final CopyOnWriteArrayList<Long> ResponseTime = new CopyOnWriteArrayList<>();
    static final ArrayList<String> graphlist = new ArrayList<>();

    public ClientReader() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI);
    }

    public static void main(String[] args) throws InterruptedException, FileNotFoundException, IOException {
        ClientReader client = new ClientReader();

        ExecutorService threadpool = Executors.newFixedThreadPool(100);

        System.out.println("READING DATA FROM RDS ==========");

        int offsetparam = 500 / NUM_THREADS;

        long startTimestamp = System.currentTimeMillis();
        for (int i = 0; i < NUM_THREADS; i++) {
            threadpool.submit(new TestReadThreads(i * offsetparam, (i + 1) * offsetparam, client));
        }

        threadpool.shutdown();
        threadpool.awaitTermination(1, TimeUnit.DAYS);
        long endTimestamp = System.currentTimeMillis();

        client.close();

        long totalThreadTime = endTimestamp - startTimestamp;

        System.out.println("Number Of Threads :" + NUM_THREADS);
        System.out.println("Number Of Request Send :" + NumberOfRequestSent);
        System.out.println("Wall Time:" + totalThreadTime);

        Double meanLatency = mean();

        for (int i = 0; i < Latency.size(); i++) {
            graphlist.add(Long.toString(Latency.get(i)) + "," + Long.toString(ResponseTime.get(i)));
        }

        //sorting Latency
        sortLatency();

        Long medianLetancy = median();
        System.out.println("MEDIAN" + medianLetancy);
        System.out.println("MEAN" + meanLatency);
        int nintynith = nintyNinthPercentile();
        int nintyfifth = nintyFifthPercentile();
        System.out.println("99th percentile latency:" + Latency.get(nintynith));
        System.out.println("95th percentile latency:" + Latency.get(nintyfifth));

        System.out.println("\n \n \n");

        for (int i = 0; i < graphlist.size(); i++) {
            System.out.println(graphlist.get(i));
        }
    }

    public String getIt(int SkierID, int DayNum) throws ClientErrorException {
        long requestStartTimestamp = System.currentTimeMillis();
        ResponseTime.add(requestStartTimestamp);
        DayNum = 1;
        String READ_URI = GET_URI + Integer.toString(SkierID) + "&dayNum=" + Integer.toString(DayNum);
        Response r = client.target(READ_URI).request(javax.ws.rs.core.MediaType.TEXT_PLAIN).get(Response.class);
        long requestEndTimestamp = System.currentTimeMillis();
        Latency.add(requestEndTimestamp - requestStartTimestamp);
        totalNumberOfRequestSent();
        return r.readEntity(String.class);

    }

    public static double mean() {
        double average = 0.0;
        for (int i = 0; i < Latency.size(); i++) {
            average += Latency.get(i);
        }
        return average / Latency.size();
    }

    /**
     * Method to calculate 99th percentile latency
     */
    public static int nintyNinthPercentile() {
        int nintynine = (Latency.size() / 100) * 99;
        return nintynine;
    }

    /**
     * Method to calculate 95th percentile.
     */
    public static int nintyFifthPercentile() {
        int nintyFive = (Latency.size() / 100) * 95;
        return nintyFive;
    }

    /**
     * Method to calculate median latency.
     */
    public static long median() {
        long medianLetancy;
        if (Latency.size() % 2 == 0) {
            medianLetancy = (Latency.get(Latency.size() / 2) + Latency.get(Latency.size() / 2 - 1)) / 2;
        } else {
            medianLetancy = Latency.get(Latency.size() / 2);
        }
        return medianLetancy;
    }

    /**
     * Method to sort the latency
     */
    public static void sortLatency() {
        Object[] a = Latency.toArray();
        Arrays.sort(a);
        for (int i = 0; i < a.length; i++) {
            Latency.set(i, (Long) a[i]);
        }
    }

    public void close() {
        client.close();
    }

}
