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

import java.util.HashMap;
import java.util.Map;

/**
 * Protein object represents a protein within the FASTA database. Object
 * captures data relevant for writing reports - protein ID and sequence.
 *
 * @version 1.0
 * @author murra668
 */
public class Protein {

    /** Amino Acid Sequence of Protein. */
    String seq;
    
    /** Map of Amino Acid Frequencies. */
    Map<Character, Double> comp;

    /** Properties. */
    Map<String, Double> props;

    /** Number of Tyrosine. */
    int numTyr;

    /** Number of Phospho-Tyrosine. */
    int phosphoTyr;

    /**
     * Constructs a Protein Object. Captures ID and Sequence from line of Fasta
     * Database.
     *
     * @param protein Line from Fasta Database
     */
    public Protein(String[] protein) {
        
        this.comp = new HashMap();    
        this.props = new HashMap();

        /** Set Sequence. */
        this.seq = protein[2];

        /** Initialize to zero. */
        this.phosphoTyr = 0;
        
        /** Initialize frequencies. */
        initFreq();
        
        /** Calculate frequencies. */
        calcFreq();

        /** Calculate sequence properties. */
        calcProps();
    }
    
    /**
     * Initialize the frequency of each amino acid to zero.
     */
    private void initFreq() {

        for (char acid : AminoAcid.ACIDS) {
            comp.put(acid, 0.0);
        }
    }

    /**
     * Calculate the frequency of each amino acid within the proteins sequence.
     * Specifically record the number of tyrosine in the sequence.
     */
    private void calcFreq() {
        
        /** Increment for each amino acid. */
        for (char acid : this.seq.toCharArray()) {
            comp.computeIfPresent(acid, (k, v) -> v + 1);
        }
        
        /** Set the number of tyrosine. */
        numTyr = comp.get('Y').intValue();
        
        /** Calculate frequency. */
        for (char acid : AminoAcid.ACIDS) {
            comp.computeIfPresent(acid, (k, v) -> (v / seq.length()) * 100);
        }
    }

    /**
     * Calculate the properties of the sequence based on the amino acid comp.
     * There are ten properties: Hydrophobic, Polar, Small, Negative, Positive,
     * Amide, Large Aliphatic, Small Aliphatic, Aromatic, Hydroxy.
     */
    private void calcProps() {
        
        for (String prop : AminoAcid.PROPS){
            
            double value = 0;
            
            for (char acid : AminoAcid.ACIDS){
                
                /** If amino acid has property, add to properties. */
                if (Run.ACIDS.aAcids.get(acid).contains(prop)){
                    value += comp.get(acid);
                }
            }
            
            props.put(prop, value);
        }
    }
}
