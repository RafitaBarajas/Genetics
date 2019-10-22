
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
    
    //Bits de Presición
    int pre;
    
    //Número de Individuos
    int numInd;
    
    //Número de poblaciones
    int numPob;
    
    //Opción maximizar o minimizar, 0-min   1-max
    int opcMM;
    
    
    public Model(int numVar, int numRest, ArrayList<ArrayList> rest, int pre, int numInd, int numPob, int opcMM, ArrayList z){
        this.numVar = numVar;
        this.numRest = numRest;
        this.rest = rest;
        this.pre = pre;
        this.numInd = numInd;
        this.z = z;
        this.numPob = numPob;
        this.opcMM = opcMM;
        
        aj = new ArrayList();
        for (int i = 0; i < numVar; i++) {
            aj.add((double)0);
        }
        
        ArrayList<ArrayList> aux = new ArrayList();
        
        for (int i = 0; i < numVar; i++) {
            aux.add(new ArrayList());
            
            for (int j = 0; j < (numRest-numVar); j++) {
                if( (double)(rest.get(j).get(i)) != 0){
                    aux.get(i).add((double)(rest.get(j).get(numVar+1))/(double)(rest.get(j).get(i)));
                }
            }
        }
        
        double maxAux;
        
        bj = new ArrayList();
        for (int i = 0; i < aux.size(); i++) {
            maxAux = (double)aux.get(i).get(0);
            
            for (int j = 0; j < aux.get(i).size(); j++) {
                if( (double)aux.get(i).get(j) > maxAux ){
                    maxAux = (double)aux.get(i).get(j);
                }
            }
            
            bj.add(maxAux);
        }
        
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

    public int getPre() {
        return pre;
    }

    public int getNumInd() {
        return numInd;
    }

    public int getNumPob() {
        return numPob;
    }

    public int getOpcMM() {
        return opcMM;
    }
    
}
