/**
 *****************************************************************************
 *
 * Copyright (c) Regents of the University of Minnesota. All Rights Reserved.
 *
 * Author: Kevin Murray University of Minnesota - (murra668@umn.edu)
 *
 *****************************************************************************
 */

package javaapplication4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class AminoAcid {
    
    /** Map of amino acids and properties. */
    public final Map<Character, ArrayList<String>> aAcids;
    
    /** Properties Global Static. */
    public final static String[] PROPS = {"Hydrophobic", "Polar", "Small",
        "Negative", "Postive", "Amide", "Large Aliphatic", "Small Aliphatic",
        "Aromatic", "Hydroxy"};
        
    /** Amino Acids Global Static. */
    public final static char[] ACIDS = {'A', 'C', 'D', 'E', 'F', 'G', 'H',
        'I', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'V', 'W', 'Y'};

     
    /**
     * Creates a static AminoAcid object. Contains the map of amino acids with
     * corresponding properties, along with globals of amino acids and props. 
     */
    public AminoAcid(){
        
        this.aAcids = new HashMap();
        
        /** Initialize amino acids in map. */
        initAcids();
        
        /** Initialize properties from frequencies. */
        initProps();      
    }
    
    /**
     * Initializes the amino acids from global static list. 
     */
    private void initAcids() {
        
        for (char acid : ACIDS){
            aAcids.put(acid, new ArrayList<>());
        }
        
    }
    
    /**
     * Initialize the properties from global static list. 
     */
    private void initProps() {
            
        aAcids.get('A').addAll(getProps(0,2,7));
        aAcids.get('C').addAll(getProps(0,2));
        aAcids.get('D').addAll(getProps(1,2,3));
        aAcids.get('E').addAll(getProps(1,3));
        aAcids.get('F').addAll(getProps(0,8));
        aAcids.get('G').addAll(getProps(0,2,7));
        aAcids.get('H').addAll(getProps(1,4));
        aAcids.get('I').addAll(getProps(0,6));
        aAcids.get('K').addAll(getProps(1,4));
        aAcids.get('L').addAll(getProps(0,6));
        aAcids.get('M').addAll(getProps(0));
        aAcids.get('N').addAll(getProps(1,2,5));
        aAcids.get('P').addAll(getProps(2));
        aAcids.get('Q').addAll(getProps(1,5));
        aAcids.get('R').addAll(getProps(1,4));
        aAcids.get('S').addAll(getProps(2,9));
        aAcids.get('T').addAll(getProps(2,9));
        aAcids.get('V').addAll(getProps(0,2,7));
        aAcids.get('W').addAll(getProps(0,8));
        aAcids.get('Y').addAll(getProps(0,8));        
        
    }
    
    /** 
     * Return list of properties corresponding to indexes.
     * 
     * @param ints Indices of properties.
     * @return
     */
    private ArrayList<String> getProps(int... ints) {
        
       ArrayList<String> props = new ArrayList<>();
       
       for (int index : ints){
           props.add(PROPS[index]);
       }
       
       return props;
        
    } 
}
