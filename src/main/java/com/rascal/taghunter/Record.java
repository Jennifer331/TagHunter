/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rascal.taghunter;

import com.opencsv.bean.CsvBindByName;
import java.util.Date;

/**
 *
 * @author Xiaoyue Lei
 */
public class Record {
    @CsvBindByName(column = "EPC")
    private String epc;
    
    @CsvBindByName(column = "Channel")
    private double channelInMhz;
    
    @CsvBindByName(column = "Phase")
    private double phaseAngleInRadians;
    
    @CsvBindByName(column = "RSSI")
    private double peakRssiInDbm;
    
    @CsvBindByName(column = "TIME")
    private String time;
    
    public Record(String epc, double channel, double phase, double rssi) {
        this.epc = epc;
        channelInMhz = channel;
        phaseAngleInRadians = phase;
        peakRssiInDbm = rssi;
        time = new Date().toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("EPC: ").append(epc).append(", ")
                .append("Channel: ").append(channelInMhz).append(",")
                .append("Phase: ").append(phaseAngleInRadians).append(", ")
                .append("RSSI: ").append(peakRssiInDbm).append("\n")
                .append("Time: ").append(time).append("\n");
        return sb.toString();
    }

    public String getEpc() {
        return epc;
    }

    public void setEpc(String epc) {
        this.epc = epc;
    }

    public double getChannelInMhz() {
        return channelInMhz;
    }

    public void setChannelInMhz(double channelInMhz) {
        this.channelInMhz = channelInMhz;
    }

    public double getPhaseAngleInRadians() {
        return phaseAngleInRadians;
    }

    public void setPhaseAngleInRadians(double phaseAngleInRadians) {
        this.phaseAngleInRadians = phaseAngleInRadians;
    }

    public double getPeakRssiInDbm() {
        return peakRssiInDbm;
    }

    public void setPeakRssiInDbm(double peakRssiInDbm) {
        this.peakRssiInDbm = peakRssiInDbm;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    
    
}
