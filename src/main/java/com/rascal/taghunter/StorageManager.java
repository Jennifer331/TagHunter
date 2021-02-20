/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rascal.taghunter;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Xiaoyue Lei
 */
public class StorageManager {
    private static StorageManager sm = null;
    private static List<Record> records;
    private volatile boolean notRecord = false;
    
    public static StorageManager getInstance() {
        if (null == sm) {
            sm = new StorageManager();
            records = new LinkedList<>();
        }
        return sm;
    }
    
    public void suspend() {
        notRecord = true;
    }
    
    public void resume() {
        notRecord = false;
    }
    
    public void add(List<Record> rs) {
        if (notRecord)
            return;
        
        records.addAll(rs);
    }
    
    public void clear() {
        records.clear();
    }

    public void saveToFile(String name) {
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        File file = new File("d:\\Atom\\exp\\" + df.format(new Date()) + "\\" + name + ".csv");
        file.getParentFile().mkdirs();
        try {
            Writer writer = new FileWriter(file);
            StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer).build();
            beanToCsv.write(records);
            writer.close();
        } catch (IOException|CsvDataTypeMismatchException|CsvRequiredFieldEmptyException ex) {
            ex.printStackTrace();
//            Logger.getLogger(StorageManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
