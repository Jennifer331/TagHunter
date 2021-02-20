/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rascal.taghunter;

import com.google.gson.Gson;
import com.impinj.octane.ImpinjReader;
import com.impinj.octane.Tag;
import com.impinj.octane.TagReport;
import com.impinj.octane.TagReportListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Xiaoyue Lei
 */
public class TagReportListenerImplementation implements TagReportListener{
    private static final int PORT = 8002;
    ServerSocket serverSocket;
    Socket clientSocket;
    PrintWriter out;
    Gson gson = new Gson();
    
    public TagReportListenerImplementation() {
        try {
            serverSocket = new ServerSocket(PORT);
        } catch(IOException e) {
            System.err.println(e);
        }
    }
    @Override
    public void onTagReported(ImpinjReader reader, TagReport tr) {
        List<Tag> tags = tr.getTags();
        localStorage(tags);
        broadcast(tags);
    }
    
    private void localStorage(List<Tag> tags) {
        List<Record> records = new LinkedList<>();
        
        for (Tag t : tags) {
            String epc = t.getEpc().toString();
            double channel = t.getChannelInMhz();
            double phase = t.getPhaseAngleInRadians();
//            System.out.println("channel: " + channel);
            double rssi = t.getPeakRssiInDbm();
            
            records.add(new Record(epc, channel, phase, rssi));
        }
        
        StorageManager.getInstance().add(records);
    }
   
    int max = 0;
    private void broadcast(List<Tag> tags) {
        try {
            if (null == out) {
                clientSocket = serverSocket.accept();
                out = new PrintWriter(clientSocket.getOutputStream(), true);
            }
            String message = gson.toJson(tags);
//            System.out.println(message);
            max = Math.max(max, message.length());
//            System.out.println("max length till now: " + max);
            out.println(message);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public void reconnect() {
        out = null;
    }
}
