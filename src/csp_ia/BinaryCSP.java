/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csp_ia;

/**
 *
 * @author gabriel
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;


public class BinaryCSP<D> 
{
    D a;
    List<Variable<D>> vars;
    List<BinaryConstraint<D>> constraints;

    public BinaryCSP() 
    {
            vars = new ArrayList<Variable<D>>(); 
            constraints = new ArrayList<BinaryConstraint<D>>();
    }
    
    
    
    
 
    public int chose_next_variable(int choice_heuri)
    {
        switch(choice_heuri)
        {
            case 1://var qui a le moins de valeurs possibles
            {
                int index=0,min=1000;
                for(int i=0;i<vars.size();i++)
                {
                    if(!vars.get(i).isAssigned())
                    {
                        int size=vars.get(i).getPossibleValues().size();
                        if(size<min)
                        {
                            min=size;
                            index=i;
                        }
                    }
                }
                return index;
            }
            case 2://variable qui a le plus de contraintes avec des variables non affectées
            {
                int index=0,max=-100;
                for(int i=1;i<vars.size();i++)
                {
                    Variable<D> val=vars.get(i);
                    if(!val.isAssigned())
                    {
                        int nb_contraintes=0;
                        for(int j=0;j<constraints.size();j++)
                        {
                            if(constraints.get(j).concerns(val))
                            {
                                if(constraints.get(j).depends(val).isAssigned())
                                {
                                    nb_contraintes++;
                                }
                            }
                        }
                        if(nb_contraintes>max)
                        {
                            max=nb_contraintes;
                            index=i;
                        }
                    }
                }
                return index;
            }
            case 3:
            {
                //heuristique sur le choix de la valeur
                return 0;// on ne l'a pas fait
            }
            case 4://ordre normale de gauche à dorite et de haut en bas (=sans heuristique)
            {
                int i=0;
                while(vars.get(i).isAssigned() && i<vars.size()-1)
                {
                    i++;
                }
                return i;
            }
            default :
                return 0;
        }
    }   
    
    public boolean satisfies_all(Variable<D> var, D value)// verifie qu'une variable et qu'une valeur respecte toutes les contraintes
    {
        for(int i=0;i<constraints.size();i++)
        {
            if(constraints.get(i).concerns(var))
            {
                if(constraints.get(i).depends(var).isAssigned())
                {
                    if(constraints.get(i).depends(var).getValue()==value)
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    public void cut_domain(Variable<D> var)//enleves des domaines la valeur de la variable qui vient d'etre assignée
    {
        for(int i=0;i<constraints.size();i++)
        {
            BinaryConstraint contrainte= constraints.get(i);
            if(contrainte.concerns(var))
            {
                if(contrainte.depends(var).isAssigned()==false)
                {
                    contrainte.depends(var).removePossibleValue(var.getValue());
                }
            }
        }
    }
    
    public boolean all_assigned()//vérifie si toutes les variables sont assignées
    {
        for(Variable<D> i: vars)
        {
            if(!i.isAssigned())
            {
                return false;
            }
        }
        return true;
    }
    
    
    public class save_domain_class//class optionnelle qui permet de mémoriser les domaines avant des les restreindre
    {
        public List<D> list=new ArrayList<D>();
    }
    
     public int count=0;//coompteur pour savoir si l'on vient de lancer la fonction

    public boolean forwardcheck(ArrayList<save_domain_class> tmp,final int heuri_choice,final boolean with_AC3)//principal fonction
    {
        if(count==0)// si on vient de lancer la fonction, on peut des à present restreindre les domaines des variables non affectées grace à celle deja affectées
        {
            for(int i=0;i<vars.size();i++)
            {
                if(vars.get(i).isAssigned())
                {
                    cut_domain(vars.get(i));
                }
            }
        }
        count++;
        
        if(!all_assigned())// tant que toutes les variables ne sont pas affectées
        {
            int tmp_index=chose_next_variable(heuri_choice);//on choisit la prochaine variable
            Variable<D> val=vars.get(tmp_index);
            tmp= new ArrayList<>();
            for( Variable<D> t : vars)//on sauvegarde les domaines de chaque variable
            {
                save_domain_class z= new save_domain_class();
                z.list=new ArrayList<>();
                z.list.addAll(t.getPossibleValues());
                tmp.add(z);
            }
            if(!val.isAssigned())// si la variable choisit n'est pas assignée
            {
                for (int i=0;i<val.getPossibleValues().size(); i++)//on teste les valeur de possibleValues()
                {
                    if(satisfies_all(val,val.getPossibleValues().get(i)))//si la variable satisfait toutes les contraintes
                    {
                        val.setValue(val.getPossibleValues().get(i));//on fixe la variable
                        if(with_AC3)
                        {
                            forwardCheckAC3();//on restreind les domaines avec AC3
                        }
                        else
                        {
                            cut_domain(val);// on restreind les domaines
                        }
                        
                        
                        for(int v=0;v<vars.size();v++)
                        {
                            if(vars.get(v).getPossibleValues().isEmpty())// si parmiles possiblesValues des variables, certaines listes sont vides
                            {
                                if(i==tmp.get(tmp_index).list.size())//si la valeur est deja la derniere de PossibleValues() => on Backtrack
                                {
                                    val.unassigne();
                                    return false;//on renvoit false
                                }
                                else
                                {//Sinon on reset les domaines des variables
                                    for(int f=0;f<vars.size();f++)
                                    {
                                        vars.get(f).resetPossibleValues(tmp.get(f).list);
                                    }
                                }
                            }                              
                        }
                        if(forwardcheck(tmp,heuri_choice,with_AC3))//pour aller en profondeur dans l'arbre
                        {
                            return true;
                        }
                        else
                        {//s'il renvoie faux à un endroit, on reset les domaines des variables                           
                            for(int f=0;f<vars.size();f++)
                            {
                                vars.get(f).resetPossibleValues(tmp.get(f).list);
                            }
                            val.unassigne();
                        }
                    }
                }
                return false;
            }
        }
        return true;
    }
   
    
    public boolean revise(Variable<D> x, Variable<D> y)//rend les arcs consistants
    {
        boolean found=false;
        for(int i=0;i<x.getPossibleValues().size();i++)
        {
            boolean no_value=false;
            for(int j=0;j<y.getPossibleValues().size();j++)
            {
                if(x.getPossibleValues().get(i)!=y.getPossibleValues().get(j))
                {
                    no_value=true;
                }
            }
            if(!no_value)
            {
                x.removePossibleValue(x.getPossibleValues().get(i));
                found=true;
            }
        }
        return found;
    }
    
   
    public boolean forwardCheckAC3()
    {
        List<BinaryConstraint<D>> queue= new ArrayList<BinaryConstraint<D>>(constraints);
        
        while(queue.size()>1)
        {
            Variable<D> var1, var2;
            var1=queue.get(0).getX();
            var2=queue.get(0).getY();
            queue.remove(0);
            if(revise(var1,var2)) //appel revise()
            {
                int a =queue.size();
                for(int i=0;i<a;i++)
                {
                    if(queue.get(i).concerns(var1))
                    {
                        BinaryConstraint new_contrainte= new BinaryConstraint(queue.get(i).depends(var1),var1);//ajoute une nouvelle contrainte
                        queue.add(new_contrainte);
                    }
                }
            }
        }
        return true;
    }
   
}
    