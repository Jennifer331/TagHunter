/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rascal.taghunter;

import com.impinj.octane.ImpinjReader;
import com.impinj.octane.OctaneSdkException;
import com.impinj.octane.ReportConfig;
import com.impinj.octane.Settings;
import java.util.Scanner;

/**
 *
 * @author Xiaoyue Lei
 */
public class Reader{

    private static final String HOSTNAME = "hostname";
    private static final String DEFAULT_HOSTNAME = "169.254.1.1";
    public static void main(String[] args) {
        System.out.println("TagHunter wakes up!");
        initReader();
    }
    
    private static void initReader() {
        String hostname =System.getProperty(HOSTNAME);
        if (null == hostname)
            hostname = DEFAULT_HOSTNAME;
        
        try {
            ImpinjReader reader = new ImpinjReader();

            reader.connect(hostname);

            reader.setTagReportListener(new TagReportListenerImplementation());

            Settings settings = reader.queryDefaultSettings();
            ReportConfig r = settings.getReport();
            r.setIncludeChannel(true);
            r.setIncludePeakRssi(true);
            r.setIncludePhaseAngle(true);
            reader.applySettings(settings);

            reader.start();

            System.out.println("Print s to stop...");
            Scanner in = new Scanner(System.in);
            String input = "";
            while (!input.equals("s")) {
                input = in.nextLine();
            }
            
            reader.stop();
            reader.disconnect();
            
            System.out.println("Saving... Please input the file name:");
            String filename = in.nextLine();
            StorageManager.getInstance().saveToFile(filename);
        } catch (OctaneSdkException e) {
            System.err.println(e);
        }
        
        System.out.println("See ya!");
    }
}
