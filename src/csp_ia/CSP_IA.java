/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csp_ia;

/**
 *
 * @author Gabriel Achraf
 */
public class CSP_IA {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {      
        
        //concernant les temps pour les tests fulls, si vous voulez vous assurer que les sudokus sont complétés, vous pouvez décommenter les lignes 364 et 382 pour afficher les sudokus résolus
//        
//        
        Sudoku tmp= new Sudoku();//nouvelle instance
        tmp.testFull(10,1);//test full avec l'heuristique 1
        
        System.out.println();
        
        tmp.testFull(1,2);//test full avec la seconde heuristique
        
        System.out.println();
        
        tmp.testFull(10,4);//test full sans heuristique
//        
//        
//
//        
//        GraphColouring graphcolor= new GraphColouring("australia.txt");//pb facile
//        graphcolor.start();
//        
//        GraphColouring t= new GraphColouring();//pb compliqué
//        t.start_gc();
//        


        //vous pouvez faire des tests simples comme ci dessous :
        //Warning il faut enlever la premiere ligne du fichier texte sudokus.txt (Grid 01)
        
//        tmp.testSimple(1,true);//premiere heuristique avec AC3
//        System.out.println();
//        System.out.println();
//        tmp.testSimple(1,false);//premiere heuristique sans AC3
//        System.out.println();
//        System.out.println();
//        tmp.testSimple(2,true);//deuxieme heuristique avec AC3
//        System.out.println();
//        System.out.println();
//        tmp.testSimple(2,false);//deuxieme heuristique sans AC3
//        System.out.println();
//        System.out.println();
//        tmp.testSimple(4,true);//sans heuristique avec AC3
//        System.out.println();
//        System.out.println();
//        tmp.testSimple(4,false);//sans heuristique sans AC3
//        
        
    }
    
}
