/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication4;

import static java.lang.System.exit;
/**
 *
 * @author James E Johnson jj@umn.edu
 */
public class JavaApplication4 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here        i
        if (args.length < 5) {
            System.err.print("usage: mkdir -p outputs && java -Djava.awt.headless=true -jar 'JavaApplication4.jar' 'psm_phos' reference.fasta outputs 0.5 output");
            exit(1);
        }
        KinaMineDriver.run(args, true);

    }
    
}