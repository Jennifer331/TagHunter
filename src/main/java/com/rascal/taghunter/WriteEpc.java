/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rascal.taghunter;

import com.impinj.octane.AntennaConfigGroup;
import com.impinj.octane.BitPointers;
import com.impinj.octane.ImpinjReader;
import com.impinj.octane.MemoryBank;
import com.impinj.octane.Settings;
import com.impinj.octane.Tag;
import com.impinj.octane.TagData;
import com.impinj.octane.TagOpCompleteListener;
import com.impinj.octane.TagOpReport;
import com.impinj.octane.TagOpSequence;
import com.impinj.octane.TagReport;
import com.impinj.octane.TagReportListener;
import com.impinj.octane.TagWriteOp;
import com.impinj.octane.TargetTag;
import com.impinj.octane.TxPowerTableEntry;
import com.impinj.octane.WordPointers;
import java.util.Scanner;

/**
 *
 * @author Xiaoyue Lei
 */
public class WriteEpc {

    private static final String HOSTNAME= "hostname";
    private static final String DEFAULT_HOSTNAME = "169.254.1.1";
    
    public static void main(String[] args) {
        System.out.println("Write Epc...");
        initReader();
    }
    
    private static void initReader() {
        String hostname = System.getProperty(HOSTNAME);
        if (null == hostname)
            hostname = DEFAULT_HOSTNAME;
        
        ImpinjReader reader = new ImpinjReader();
        reader.connect(hostname);
        
        Settings settings = reader.queryDefaultSettings();
        
        AntennaConfigGroup antennas = settings.getAntennas();
        TxPowerTableEntry lowestPower = reader.queryFeatureSet().getTxPowers().get(0);
        antennas.getAntenna((short)1).setIsMaxTxPower(false);
        antennas.getAntenna((short)1).setTxPowerinDbm(lowestPower.Dbm);
        
        reader.applySettings(settings);
        
        reader.setTagReportListener(new TagReportListener() {
            @Override
            public void onTagReported(ImpinjReader reader, TagReport tr) {
                for (Tag t : tr.getTags())
                    System.out.println("Tag Reported: " + t.getEpc().toHexString());
            }
        });
        
        reader.setTagOpCompleteListener(new TagOpCompleteListener() {
            @Override
            public void onTagOpComplete(ImpinjReader reader, TagOpReport tor) {
                System.out.println("TagOpComplete!");
            }
        });
        
        reader.start();
        writeEpc(reader, "300833B2DDD9014000000000", "300833B2DDD9014000000001");
        Scanner in = new Scanner(System.in);
        in.next();
        
        reader.stop();
        reader.disconnect();
    }
    
    private static void writeEpc(ImpinjReader reader, String currentEpc, String newEpc) {
        TagOpSequence seq = new TagOpSequence();
        
        seq.setTargetTag(new TargetTag());
        seq.getTargetTag().setBitPointer(BitPointers.Epc);
        seq.getTargetTag().setMemoryBank(MemoryBank.Epc);
        seq.getTargetTag().setData(currentEpc);
        
        TagWriteOp epcWrite = new TagWriteOp();
        epcWrite.Id = ;
        epcWrite.setMemoryBank(MemoryBank.Epc);
        epcWrite.setWordPointer(WordPointers.Epc);
        epcWrite.setData(TagData.fromHexString(newEpc));
        
        seq.getOps().add(epcWrite);
        reader.addOpSequence(seq);
    }
}
