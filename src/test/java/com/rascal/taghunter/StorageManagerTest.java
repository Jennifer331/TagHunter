/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rascal.taghunter;

/**
 *
 * @author Xiaoyue Lei
 */
public class StorageManagerTest {
    public static void main(String[] args) {
        StorageManager sm = StorageManager.getInstance();
        sm.saveToFile("test1");
    }
}
