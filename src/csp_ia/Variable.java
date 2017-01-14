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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * This class models a variable that takes value in a domain D
 * @author stephane
 *
 * @param <D>
 */
public class Variable < D > {

    /**
     * domain of the values for this variable
     */
    private final List < D > domain;
    /**
     * 
     */
    private int index;

    /**
     * Name of the variable
     */
    private String name;

    /**
     * If the variable has been assigned, this is the value of the variable
     */
    private D value;

    /**
     * boolean telling whether the variable has been assigned
     */
    private boolean isAssigned;

    /**
     * List of possible values for the variable: using propagation of constraints may reduce
     * the set of possible values during the search
     */
    private List < D > possibleValues;

    /**
     * tells how many variables are present in the problem
     */
    static int numVariables;

    
    /**
     * Constructor with a domain
     * @param domain
     */
    public Variable(List < D > domain) 
    {
        this.domain = domain;
        index = numVariables;
        numVariables++;
        possibleValues = new ArrayList < D > ();
        for (D val: domain) 
        {
            possibleValues.add(val);
        }
    }
    
    /**
     * Set the name of the variable
     * @param name
     */
    public void setName(String name) 
    {
        this.name = name;
    }
    
    
    public void assign()
    {
        isAssigned=true;
    }
    
    
    public void unassigne()
    {
        isAssigned=false;
    }

    /**
     * get the name of the variable
     * @return
     */
    public String getName() 
    {
        return name;
    }

    /**
     * tells whether the variable has been assigned
     * @return
     */
    public boolean isAssigned() 
    {
        return isAssigned;
    }

    /**
     * gets the index of the variable
     * @return
     */
    public int getIndex() 
    {
        return index;
    }

    public void setIndex(int index) 
    {
        this.index = index;
    }

    /**
     * return the value of the variable
     * @return
     */
    public D getValue() throws IllegalStateException 
    {
        if (!isAssigned)
            throw new IllegalStateException("the variable " + this.getName() + " is not assigned");
        else
            return value;
    }

    /**
     * set the value of the variable: this method not only sets the value of the variable,
     * but it also updates the variable isAssigned and the set of possible values (which will 
     * only contain the value <code>value</code>
     * @param value
     */
    public void setValue(D value) 
    {
        isAssigned = true;
        this.value = value;
        possibleValues.clear();
        possibleValues.add(value);
    }
    
    /**
     * Reset the state of the variable
     */
    public void reset() 
    {
        value = null;
        isAssigned = false;
        resetPossibleValues();
    }


    /**
     * reset the set of possible values (i.e. puts back all values from domain in the list of possible values
     */
    void resetPossibleValues() 
    {
        possibleValues.clear();
        for (D val: domain)
            possibleValues.add(val);
    }
    

    /**
     * replace the set of possible values with a list of values
     * We require the values to be in the domain
     */

    void resetPossibleValues(List < D > lval) 
    {
        possibleValues.clear();
        for (D val: lval)
            possibleValues.add(val);
    }

    /** 
     * returns the list of possible values
     */
    public List < D > getPossibleValues() 
    {
        return possibleValues;
    }

    /**
     * removes a value from the list of possible value
     */

    public void removePossibleValue(D val) 
    {
        possibleValues.remove(val);
    }

    /**
     * adds one possible value to the list of possible value
     */
    public void addPossibleValue(D val) 
    {
        if (domain.contains(val))
            possibleValues.add(val);
        else
            System.out.println("trying to add an incorrect possible value");
    }

    /**
     * This method checks whether a value val is a possible assignment for this variable, i.e., it tells whether val is a member of possible values
     */
    public boolean satisfies(D val) 
    {
        Iterator < D > it = possibleValues.iterator();
        while (it.hasNext()) 
        {
            if (!it.next().equals(val))
                return true;
        }
        return false;
    }

    /**
     * returns a string representation of the variable: if the variable is assigned, it prints the name and the value of the variable, otherwise, it prints the name and the list of possible values.
     */
    public String toString() 
    {
        if (isAssigned)
            return name + "[" + value + "]";
        else
            return name + possibleValues;
    }
}