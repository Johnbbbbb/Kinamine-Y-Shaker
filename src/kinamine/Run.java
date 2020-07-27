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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Main KinaMine object container. A run contains ArrayLists of peptide and
 * proteins from the extracted file and list of amino acid chars.
 *
 * @version 1.0
 * @author murra668
 */
public final class Run {

    /** List of peptide. */
    public final ArrayList<Peptide> pepList;

    /** Non-redundant database. */
    public final Map<String, Protein> database;

    /** Non-redundant collection of motifs. */
    public final Map<String, Motif> motifs;

    /** Amino Acids and Properties. */
    public static final AminoAcid ACIDS = new AminoAcid();

    /**
     * Constructs a run and processes the submitted peptide report and fasta
     * database for motif generation.
     *
     * @param peps lines from peptide report
     * @param prots lines for fasta database
     * @param score FDR score
     */
    public Run(ArrayList<String> peps, ArrayList<String> prots, double score) {

        this.pepList = new ArrayList<>();
        this.database = new HashMap();
        this.motifs = new HashMap();

        /** Extract peptides and generate custom protein database. */
        extractPeptides(peps, score);

        /** Extracts the proteins from the fasta database. */
        extractDatabase(prots);

        /** Generate motifs for each peptide. */
        generateMotifs();

    }

    /**
     * Extracts individual peptides from the peptide report and creates new
     * peptide objects, returns a list of peptides.
     *
     * @param lines Distinct peptide summary.
     * @param score FDR score.
     * @return ArrayList of peptides
     */
    private void extractPeptides(ArrayList<String> lines,
            double score) {

        /** Process each line of the peptide report */
        for (String line : lines) {
            line = line.replaceAll("\"", "");

            /** Peptide report is tabular. */
            String[] pepInfo = line.split("\\t");

            /**
             * Check to see if the peptide possesses a Conf score greater than
             * the FDR threshold, has a peptide id, and contains a
             * phospho-tyrosine.
             */
            if (Double.valueOf(pepInfo[9]) > score
                    & !"".equals(pepInfo[1])
                    & pepInfo[3].contains("Phosphorylation of Y")) { 

                List<String> temp = Arrays.asList(pepInfo[1].split(","));
                ArrayList<String> ids = new ArrayList<>();
                temp.stream().forEach((id) -> {
                    ids.add(id.trim());
                });

                /** Add IDs to inclusion list, if not present. */
                for (String ref : ids) {
                    if (!ref.contains("RRRRR")
                            & !database.containsKey(ref.trim())) {
                        this.database.put(ref, null);
                    }
                }

                /** Construct a new peptide object. */
                Peptide peptide = new Peptide(pepInfo, ids);

                /** Add the peptide to list if unique. */
                this.pepList.add(peptide);
            }
        }
    }

    /**
     * Extracts individual proteins from a FASTA database, creating new protein
     * objects from each entry and adding them to a master list.
     *
     * @param fastaDatabase
     * @return ArrayList of Proteins.
     */
    private void extractDatabase(ArrayList<String> fastaDatabase) {

        Set<String> protList = this.database.keySet();

        /** Loop through each entry. */
        for (String line : fastaDatabase) {

            /** FASTA database is tabular file. */
            String[] protInfo = line.split("\\t");

            /** Search if protein is in inclusion list. */
            for (String name : protList) {

                if (protInfo[0].contains(name)) {

                    /** Add protein to list. */
                    this.database.replace(name, new Protein(protInfo));
                }
            }
        }
    }

    /**
     * Generates motifs around each phospho-tyrosine of each peptide in pepList.
     * Motifs are -4 to 4 amino acids surrounding tyr. Not all entries from
     * distinct peptide summary have IDs. Presently, those entries are excluded.
     * For each peptide, find the corresponding protein, so the number of
     * phospho-tyr can be recorded and the sequence can be utilized if the motif
     * can not be generated from peptide sequence alone.
     */
    private void generateMotifs() {

        for (Peptide peptide : pepList) {

            /** Check to see if peptide has reference accession. */
            if (hasProtID(peptide)) {

                /** For phospho-tyrosine in the peptide. */
                for (int index : peptide.tyrIndex) {

                    /** Generate the motif using peptide sequence. */
                    genSeq(peptide, index);
//YOU ARE HERE
                }
            }
        }
    }

    /**
     * Find the proteins associated with the peptide ID in the database. Mark
     * each protein's phospho-tyrosine field if visited.
     *
     * @param id
     * @return protein sequence
     */
    private void markMod(List<String> id) {

        /** Loop through each reference. */
        for (String ref : id) {

            /** If found, mark pY and capture sequence. */
            if (database.containsKey(ref)) {
                database.get(ref).phosphoTyr++;
            }
        }
    }

    /**
     * Generate the peptide motif using the given index and sequence. Peptide
     * motifs are the immediate -4 to +4 around a given index.
     * <p>
     * Some peptides may not have enough sequence to generate full motif.
     *
     * @param peptide
     * @param index index of phospho-tyrosine in seq
     * @param pSeq protein sequence
     */
    private void genSeq(Peptide peptide, int index) {

        String motif = "";
        String seq = peptide.seq;
//        System.out.print(index);
//        System.out.print("\n");

        /** Select surrounding amino acids. */
        if (index - 7 >= 1 & index + 7 <= seq.length()) {
            motif = seq.substring(index - 8, index + 7);
            index = 8;
        } else if (index - 7 < 1 & index + 7 <= seq.length()) {
            motif = seq.substring(0, index + 7);
        } else if (index - 7 >= 1 & index + 7 > seq.length()) {
            
            motif = seq.substring(index - 8, seq.length());
            index = 8;
        } else {
            motif = seq;
        }

        addMotif(motif, peptide, index);

    }

    /**
     * Determine if peptide has a protein ID.
     *
     * @param peptide
     * @return
     */
    public boolean hasProtID(Peptide peptide) {
        return !peptide.id.isEmpty();
    }

    /**
     * Adds seq to motif map. Also pair peptide refs and index of phospho-
     * -tyrosine.
     *
     * @param seq
     * @param ref
     * @param index
     */
    private void addMotif(String seq, Peptide peptide, int index) {

        /** Check if sequence is unique. */
        if (!motifs.containsKey(seq)) {
            ArrayList<String> regenSeqs = regenSeq(peptide.id, seq, index);
            motifs.put(seq, new Motif(seq, peptide.ref, index, regenSeqs));
            markMod(peptide.id);
        } else {
            List<String> refs = peptide.id;
            List<String> ids = parseRef(motifs.get(seq).ref);
            ArrayList<String> newID = new ArrayList<>();

            for (String ref : refs) {
                if (!ids.contains(ref)) {
                    newID.add(ref);
                }
            }

            if (!newID.isEmpty()) {
                markMod(newID);
                ids.addAll(newID);
                String temp = "";
                for (String id : ids) {
                    temp += id + ";";
                }
                ArrayList<String> regenSeqs = regenSeq(ids, seq, index);
                motifs.put(seq, new Motif(seq, temp, index, regenSeqs));
            }
        }
    }

    /** Parse the reference string of peptide.
     *
     * @param ref
     * @return
     */
    public List<String> parseRef(String ref) {
        List<String> temp = Arrays.asList(ref.split(";"));
        ArrayList<String> ids = new ArrayList<>();
        temp.stream().forEach((id) -> {
            ids.add(id.replace("\"", ""));
        });
//////////////////ya te ta tally tee tel to
        return ids;
    }

    private ArrayList<String> regenSeq(List<String> ids, String seq, int i) {

        ArrayList<String> seqs = new ArrayList<>();
//this is where I need to remove phosphosite
        /*for (String id : ids) {
            if (database.containsKey(id)) {
//                System.out.print(id);
//                System.out.print("\n");
//                id = id.replace("\"", "");
//                System.out.print(id);
//                System.out.print("\n");
                String prot = database.get(id).seq;
                int index = prot.indexOf(seq) + i;
                
                String motif = "";
                
                if (index - 7 >= 1 & index + 7 <= prot.length()) {
                    motif = prot.substring(index - 8, index + 7);
                } else if (index - 7 < 1 & index + 7 <= prot.length()) {
                    motif = prot.substring(0, index + 7);
                } else if (index - 7 >= 1 & index + 7 > prot.length()) {
                    motif = prot.substring(index - 8, prot.length());
                } else {
                    motif = prot;
                }
                
                if (!seqs.contains(motif)){
                    seqs.add(motif);
                }

            }
        }
        */
        return seqs;

    }

}
