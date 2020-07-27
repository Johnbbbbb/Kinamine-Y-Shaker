/*
 * /**
 *****************************************************************************
 *
 * Copyright (c) Regents of the University of Minnesota. All Rights Reserved.
 *
 *
 * Author: Kevin Murray University of Minnesota - (murra668@umn.edu)
 *
 *****************************************************************************
 */


package javaapplication4;

import java.util.ArrayList;

/**
 *
 * @author murra668
 */
public class Motif {
    
    public String seq;
    
    public int index;
    
    public String ref;
    
    public ArrayList<String> regenSeqs;
    
    public Motif(String seq, String ref, int index, ArrayList<String> seqs){
        
        this.seq = seq;
        this.ref = ref;
        this.index = index;
        this.regenSeqs = seqs;
        
    }
    
}
