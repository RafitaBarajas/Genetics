
package genetics;

import java.util.ArrayList;

public class Model {
    
    //Número de Variables
    int numVar;
    
    //Función objetivo 
    ArrayList z;
    //Se guardan los coeficientes de las variables
    
    //Número de Restricciones
    int numRest;
    
    //Restricciones
    ArrayList<ArrayList> rest;
    //Se guardan los coeficientes de cada restricción
    //Al igual que el tipo de restricción (<=, >=)
    //Como arreglo bidimensional
    
    //Límites inferiores
    ArrayList aj;
    
    //Límites superiores
    ArrayList bj;
    
    
    public Model(){
        
    }
    

    public int getNumVar() {
        return numVar;
    }

    public ArrayList getZ() {
        return z;
    }

    public int getNumRest() {
        return numRest;
    }

    public ArrayList<ArrayList> getRest() {
        return rest;
    }

    public ArrayList getAj() {
        return aj;
    }

    public ArrayList getBj() {
        return bj;
    }
    
}
