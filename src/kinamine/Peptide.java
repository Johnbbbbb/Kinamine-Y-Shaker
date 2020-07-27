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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Peptide object representing the relevant information from the submitted
 * peptide report. Stores the values necessary for writing a report formatted
 * for existing Excel sheets.
 *
 * @version 1.0
 * @author murra668
 */
public class Peptide {
    
    /** Sequence of peptide. */
    String seq;
    
    /** Accession of Protein associated with this peptide. */
    List<String> id;
     
    /** Full Accessions. */
    String ref;

    /** Motif of phospho-tyrosine. */
    ArrayList<String> motif;

    /** Index of phospho-tyrosine in the peptide sequence. */
    ArrayList<Integer> tyrIndex;

    /** Index of phospho-tyrosine in protein sequence. */
    ArrayList<Integer> tyrProtIndex;

    /** Length of peptide sequence. */
    int length;

    /**
     * Constructs a Peptide object from the parsed peptide info from a line in
     * the peptide report.
     *
     * @param pepInfo tab-separated line of peptide info
     * @param ids
     */
    public Peptide(String[] pepInfo, List ids) {
         
        /** Store sequence */
        this.seq = pepInfo[2];

        /** Store sequence length. */
        this.length = pepInfo[2].length();

        /** Initialize empty motif array. */
        this.motif = new ArrayList<>();

        /** Extract peptide references. */
        this.id = ids;     
        String pepInformation = pepInfo[1];
        pepInformation = pepInformation.replace(",", ";");
        this.ref = pepInformation;
        

        /** Store the index of phospho-tyrosine of the peptide and protein. */
        this.tyrIndex = getIndex(pepInfo[3]);
        this.tyrProtIndex = getIndex(pepInfo[4]);
    }

    /**
     * Gets the index of the phospho-tyrosine in the peptide and corresponding
     * protein, as indicated by the modification column of the peptide report.
     * There may be more than one phospho-tyr within a peptide.
     * <p>
     * Location formatted as Phospho(Y)@index.
     *
     * @param mods string of mod locations
     * @return ArrayList of integers.
     */
    private ArrayList<Integer> getIndex(String mods) {

        /** Modifications are separated by ','. */
        ArrayList<Integer> index = new ArrayList<>();
        /** Check if phospho-tyr. */
        if(mods.contains("Phosphorylation of Y")){
            //System.out.print(mods);
        
            
            
            //take only the part saying Phosphorylation␣of␣Y
            Pattern p = Pattern.compile("Phosphorylation of Y\\((.*?)\\)");
            Matcher m = p.matcher(mods);
            String newmods = "10000";
            // if we find a match, get the group 
            if (m.find())
            {
                newmods = m.group(1);           
                //System.out.format("'%s'\n", newmods);
            }

            
            
            
            List<String> psm = Arrays.asList(newmods.split(","));
            //System.out.print(psm);

            /** Initialize new index array. */
            //ArrayList<Integer> index = new ArrayList<>();

            /** Check all modifications. */
            for (int i = 0; i < psm.size(); i++) {

            
            //if (psm.get(i).contains("Phosphorylation of Y")) {
                /** Trim for correct indexing. */
                String mod = psm.get(i).trim();

                /** Add to index array */
                String numberOnly= mod.replaceAll("[^0-9]", "");
                index.add(Integer.valueOf(numberOnly));
                //System.out.print(numberOnly);
                //System.out.print("\n");
            }
        }

        return index;

    }
}
