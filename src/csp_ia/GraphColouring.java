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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GraphColouring extends BinaryCSP < Integer > {

    /**
     Reads a graph from a file
     the file contains 
         - the number of colors 
         - the list of vertices
         - the list of edges
     two examples are provided in the file australia.txt (the textbook problem) and gc.txt ( a problem with 191 vertices that needs 8 colors to be colored)
     */
    public GraphColouring()
    {
        
    }
    
    public GraphColouring(String filename) {
        super();
        try {

            BufferedReader reader = new BufferedReader(new FileReader(new File(filename)));
            String line = reader.readLine();
            int numColors = 0;
            if (line.equals("% number of colors"))
                numColors = Integer.parseInt(reader.readLine());
            List < Integer > domain = new ArrayList < Integer > ();
            for (int i = 1; i <= numColors; i++)
                domain.add(i);
            System.out.println("problem with " + numColors + " colors");
            line = reader.readLine();
            if (line.equals("% Nodes")) {
                line = reader.readLine();
                while (!line.equals("% Constraints")) {
                    Variable < Integer >
                        var = new Variable < Integer > (domain);
                    var.setName(line);
                    vars.add(var);
                    line = reader.readLine();
                }
                System.out.println(vars);
                line = reader.readLine();
                while (line != null) {
                    String[] nodes = line.split(" ");
                    Variable<Integer> x = getVar(nodes[0]);
                    Variable<Integer> y = getVar(nodes[1]);
                    constraints.add(new BinaryConstraint<>(x,y));
                    line = reader.readLine();
                }
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("problem reading the file " + filename + "\n" + e);
        }
    }
    
    public void start()
    {
        long start = System.currentTimeMillis();
        ArrayList<save_domain_class>  tmp= new ArrayList<save_domain_class>();
        this.forwardcheck(tmp,1,false);
        long finish = System.currentTimeMillis();
        System.out.println("found in " + (finish - start) + "ms");
        System.out.println(this);
    }
    
    public Variable < Integer > getVar(String name) {
        for (Variable < Integer > v: vars)
            if (v.getName().equals(name))
                return v;
        System.err.println("variable not found " + name);
        return null;
    }

    public String toString() {
        String res = "";
        for (Variable < Integer > v: vars)
            res += v.toString() + "\n";
        return res;
    }

    public void start_gc() {
       GraphColouring gc = new GraphColouring("gc.txt");
		System.out.println("Problem to solve: " + gc.constraints.size() +" constraints");
		boolean cont = true;
		int numColors = 8;
		List<Integer> colors = new ArrayList<Integer>();
		for (int i=1;i<=numColors;i++)
			colors.add(i);
		while (cont){
                    ArrayList<save_domain_class> tmp = new ArrayList<save_domain_class>();
			if (gc.forwardcheck(tmp,1,false)){
                            System.out.println(gc);
				System.out.println("the graph can be colored with " + colors.size() + " colors");
				cont = false;
			}
			else{
				System.out.println("the graph can NOT be coloredwith " + colors.size() + " colors");
				colors.add(colors.size()+1);
				for (Variable<Integer> v : gc.vars){
					v.resetPossibleValues(colors);
				}
			}
		}

    }



}