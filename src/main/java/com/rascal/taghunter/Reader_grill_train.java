/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rascal.taghunter;

import com.impinj.octane.AntennaConfig;
import com.impinj.octane.AntennaConfigGroup;
import com.impinj.octane.ImpinjReader;
import com.impinj.octane.OctaneSdkException;
import com.impinj.octane.ReaderMode;
import com.impinj.octane.ReportConfig;
import com.impinj.octane.Settings;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Xiaoyue Lei
 */
public class Reader_grill_train{

    private static final String HOSTNAME = "hostname";
    private static final String DEFAULT_HOSTNAME = "169.254.1.1";
//    private static final String DEFAULT_HOSTNAME = "192.168.2.42";
    private static final double[] FREQUENCY_HOPPINT_tABLE = new double[]{902.75, 903.25, 903.75, 904.25, 904.75, 905.25, 905.75, 906.25, 906.75, 907.25, 907.75, 908.25, 908.75, 909.25, 909.75, 910.25, 910.75, 911.25, 911.75, 912.25, 912.75, 913.25, 913.75, 914.25, 914.75, 915.25, 915.75, 916.25, 916.75, 917.25, 917.75, 918.25, 918.75, 919.25, 919.75, 920.25, 920.75, 921.25, 921.75, 922.25, 922.75, 923.25, 923.75, 924.25, 924.75, 925.25, 925.75, 926.25, 926.75, 927.25};
    
    private static TagReportListenerImplementation listener;
    public static void main(String[] args) throws UnsupportedEncodingException {
        System.setOut(new PrintStream(System.out, true, "UTF8"));
        System.out.println("阅读器启动中……");
        initReader();
    }
    
    private static void initReader() {
        String hostname =System.getProperty(HOSTNAME);
        if (null == hostname)
            hostname = DEFAULT_HOSTNAME;
        
        try {
            ImpinjReader reader = new ImpinjReader();

            reader.connect(hostname);
            
            listener = new TagReportListenerImplementation();
            reader.setTagReportListener(listener);

            Settings settings = reader.queryDefaultSettings();
            ReportConfig r = settings.getReport();
            r.setIncludeChannel(true);
            r.setIncludePeakRssi(true);
            r.setIncludePhaseAngle(true);
            r.setIncludeDopplerFrequency(true);
            r.setIncludeLastSeenTime(true);
            
//            settings.setReaderMode(ReaderMode.AutoSetStaticDRM);
//            settings.setReaderMode(ReaderMode.AutoSetDenseReaderDeepScan);
//            settings.setReaderMode(ReaderMode.DenseReaderM8);
//            settings.setReaderMode(ReaderMode.DenseReaderM4);
//            settings.setReaderMode(ReaderMode.Hybrid);
//            settings.setReaderMode(ReaderMode.MaxMiller);
//            settings.setReaderMode(ReaderMode.MaxThroughput);
            
//            AntennaConfigGroup antennas = settings.getAntennas();
//            antennas.getAntenna((short)1).setIsMaxTxPower(false);
//            antennas.getAntenna((short)1).setTxPowerinDbm(32.5);
            
            reader.applySettings(settings);

            displayCurrentSettings(reader);
            reader.start();

            Scanner in = new Scanner(System.in);
            String input = "";
            boolean alive = true;
            while (alive) {
                System.out.println("请输入\n q 退出，"
                        + "s 停止采集，c 清除数据");
                input = in.nextLine();
                StorageManager.getInstance().suspend();
                switch (input) {
                    case "q":
                        alive = false;
                        break;
                    case "s":
                        System.out.println("正面数据请输入\"液体名称_h\"，反面数据请输入\"液体名称_t\"");
                        String filename = in.nextLine();
                        StorageManager.getInstance().saveToFile(filename, true);
                        StorageManager.getInstance().clear();
                        break;
                    case "d":
                        listener.reconnect();
                        break;
                }
                StorageManager.getInstance().resume();
            }
            
            reader.stop();
            reader.disconnect();

        } catch (OctaneSdkException e) {
            System.err.println(e);
        }
        
        System.out.println("See ya!");
    }
    
    private static void displayCurrentSettings(ImpinjReader r) throws OctaneSdkException{
        System.out.println("---------------");
        System.out.println("阅读器配置");
        System.out.println("---------------");
        
        Settings settings = r.querySettings();
        System.out.println("Reader mode: " + settings.getReaderMode());
        System.out.println("Search mode: " + settings.getSearchMode());
        System.out.println("Session: " + settings.getSession());
        
        ArrayList<AntennaConfig> ac = settings.getAntennas().getAntennaConfigs();

        if (ac.get(0).getIsMaxRxSensitivity()) {
            System.out.println("Rx sensitivity (Antenna 1) : Max");
        } else {
            System.out.println("Rx sensitivity (Antenna 1) : "
                    + ac.get(0).getRxSensitivityinDbm() + " dbm");
        }

        if (ac.get(0).getIsMaxTxPower()) {
            System.out.println("Tx power (Antenna 1) : Max");
        } else {
            System.out.println("Tx power (Antenna 1) : "
                    + ac.get(0).getTxPowerinDbm() + " dbm");
        }

        System.out.println("");
    }
    
}
