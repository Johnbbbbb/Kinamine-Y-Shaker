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

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Reporter class to write reports. Currently writes out in .csv format.
 *
 * @version 1.0
 * @author murra668
 */
public class Reporter {

    /**
     * Write reports for run.
     *
     * @param run
     * @param outPath
     * @param outGroup
     */
    static void writeReports(Run run, String outPath, String outGroup) {
        
        File outDir = new File(outPath);
        if (!outDir.exists()) {
            outDir.mkdirs();
        }
        System.out.print(outDir.getAbsolutePath());
        /** Write substrates report. */
        File outputFileSub = new File(outDir,outGroup + "_Substrates.csv");
        Reporter.substrates(run, outputFileSub.getPath());

        /** Write substrate background frequency report. */
        File outputFileFreq = new File(outDir,outGroup +  "_SubBackFreq.csv");
        Reporter.frequencies(run, outputFileFreq.getPath());

    }

    /**
     * Writes substrates report from the ids and motifs of each peptide.
     *
     * @param run
     * @param outputFileName
     */
    private static void substrates(Run run, String outputFileName) {

        /** Format header */
        String header = "Substrates," + "Species," + "Reference," + "-7," + "-6,"
                + "-5," + "-4," + "-3," + "-2," + "-1," + "0," + "1,"
                + "2," + "3," + "4," + "5," + "6," + "7," + ","
                + "," + "," +"," + "," + "," +"," + "," + "," +"," + "Phosphosite"
                + "\n";

        /** Initialize details */
        String detail = null;

        try (FileWriter writer = new FileWriter(outputFileName)) {

            /* Write the column headers */
            writer.append(header);

           Collection<Motif> motifs = run.motifs.values();

            /** Loop through each motif */
            for (Motif motif : motifs) {

                /** Format ID and blanks */
                detail = "," + "," + motif.ref + ",";

                String seq = motif.seq;
                int index = motif.index;

                if (index < 8) {
                    for (int i = index; i < 8; i++) {
                        detail += ",";
                    }
                    for (int j = 0; j < seq.length(); j++) {
                        detail += seq.charAt(j) + ",";
                    }
                    if (seq.length() - index < 7) {
                        for (int i = seq.length() - index; i < 7; i++) {
                            detail += ",";
                        }
                    }
                } else if (seq.length() < 15) {
                    for (int j = 0; j < seq.length(); j++) {
                        detail += seq.charAt(j) + ",";
                    }
                    for (int i = seq.length(); i < 15; i++) {
                        detail += ",";
                    }

                } else {
                    for (int j = 0; j < seq.length(); j++) {
                        detail += seq.charAt(j) + ",";
                    }
                }

                /** Format trailing blanks */
                detail += "," + "," + "," + seq + ",";
                
                for (String id : motif.regenSeqs){
                    detail += id + ",";
                }
                detail += "\n";

                /** Append each line of the report. */
                writer.append(detail);
            }

            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Write substrates background frequency report from each protein in the
     * database.
     *
     * @param run
     * @param outputFileName
     */
    private static void frequencies(Run run, String outputFileName) {

        /** Initialize the header */
        StringBuffer header = new StringBuffer();
        header.append("Amino Acids");

        /** Write each protein accession. */
        Object[] prots = run.database.keySet().toArray();
        for (Object ref : prots) {
            header.append(",").append(ref);
        }
        header.append("\n");

        try (FileWriter writer = new FileWriter(outputFileName)) {

            /* Write the column headers. */
            writer.append(header);

            Collection<Protein> proteins = run.database.values();

            /** Write frequency of each amino acid. */
            for (char acid : AminoAcid.ACIDS) {
                StringBuffer detail = new StringBuffer();
                detail.append(acid);
                for (Protein protein : proteins) {
                    detail.append(",").append(protein.comp.get(acid));
                }
                detail.append("\n");
                writer.append(detail);
            }

            writer.append("Properties\n");

            /** Write the property frequency of each amino acid. */
            for (String prop : AminoAcid.PROPS) {
                StringBuffer props = new StringBuffer();
                props.append(prop);
                for (Protein protein : proteins) {
                    props.append(",").append(protein.props.get(prop));
                }
                props.append("\n");
                writer.append(props);
            }
            
            writer.append("X\n");
            
            StringBuffer tyr = new StringBuffer("Number of Y");
            StringBuffer phosphTyr = new StringBuffer("Number of pY");
            StringBuffer aa = new StringBuffer("Total AAs");

            /** Write the number of tyrosine, phospho-tyrosine, and length. */
            for (Protein protein : proteins) {
                tyr.append(",").append(protein.numTyr);
                phosphTyr.append(",").append(protein.phosphoTyr);
                aa.append(",").append(protein.seq.length());
            }

            writer.append(tyr + "\n");
            writer.append(phosphTyr + "\n");
            writer.append(aa + "\n");

            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
