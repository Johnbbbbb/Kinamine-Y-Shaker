/**
 *****************************************************************************
 * <p>
 * Copyright (c) Regents of the University of Minnesota. All Rights Reserved.
 * <p>
 * Author: Kevin Murray University of Minnesota - (murra668@umn.edu)
 * <p>
 *****************************************************************************
 */
package javaapplication4;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Driver class for KinaMine. Processes arguments collected from GUI.
 *
 * @version 1.0
 * @author murra668
 */
public class KinaMineDriver {

    /**
     * Main run method for KinaMine. Processes arguments and stores file
     * contents for compiling a run.
     *
     * @param args
     * @param debug
     * @return
     */
    public static boolean run(String[] args, boolean debug) {
   

        /** Process arguments. */
        String pepPath = args[0];
        String fastaPath = args[1];
        String outPath = args[2];
        double fdrScore = 0.5;
        String outGroup = "/" + args[4];

        /** Read peptide report. */
        ArrayList<String> peptides = retTabFile(pepPath);
        peptides.remove(0);

        ArrayList<String> proteins = new ArrayList<>();
        
        /** Read fasta database */
        if (fastaPath.contains("fasta")){
            proteins = retFastaFile(fastaPath);
        } else {
            proteins = retTabFile(fastaPath);
        }
        

        /** Create new run. */
        Run run = new Run(peptides, proteins, fdrScore);

        /** Write run reports. */
        Reporter.writeReports(run, outPath, outGroup);

        return true;
    }

    
    /**
     * Reads tabular files.
     *
     * @param path
     * @return Arraylist of lines.
     */
    public static ArrayList<String> retTabFile(String path) {

        /** Initialize lines */
        ArrayList<String> lines = new ArrayList<>();

        try {

            /** Configure reader. */
            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);

            /** Initialize first line, headers - discard */
            String line = br.readLine();

            /**
             * Read each line in the configuration file and add each line to an
             * array (to be returned)
             */
            while (line != null) {

                /** If line is all tabs, end of file */
//                if (line.startsWith("\t")) {
//                    break;
//                }

                /** Add line to list. */
                lines.add(line);
                line = br.readLine();
            }

            /** Close the file */
            br.close();

        } catch (FileNotFoundException filenotfoundexxption) {
            System.out.println(path + ", does not exist");
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
        }

        return lines;
    }

    public static ArrayList<String> retFastaFile(String path) {

        /** Initialize lines */
        ArrayList<String> lines = new ArrayList<>();

        try {

            /** Configure reader. */
            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);

            String line = br.readLine();
            String prot = "";

            while (line != null) {
                
                if (line.startsWith(">")){
                    String[] temp = line.split(" ");
                    prot = temp[0].trim() + "\t \t";
                } else {
                    prot +=  line.trim();
                    lines.add(prot);
                }
                
                line = br.readLine();
            }

            br.close();

        } catch (FileNotFoundException filenotfoundexxption) {
            System.out.println(path + ", does not exist");
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
        }

        return lines;
    }
    
    public static void main(String[] args) {
        KinaMineDriver.run(args, true);
    }

}

