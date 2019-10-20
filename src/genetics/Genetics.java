
package genetics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Genetics {

    public static void main(String[] args) {
        
        Model model = new Model();      //Modelo
        ArrayList aj, bj;               //Límites
        
        int num;                        //Número de Variables
        int pres;                       //Presición
        int ind;                        //Número de Individuos
        int bits;                       //Bits totales de cada Individuo
        int iter;                       //Número de iteración
        int[] mj;                       //Bits de cromosomas
        int[][] individuals;            //Datos de cada individuo
        float[] zjcol;                  //Columna Zj
        float[][] variables;            //Variables de cada individuo
        float[][] table;                //Tabla %Zj %Zjacm aleatorio y vector'
        boolean[] accom;                //Cumplimiento de restricciones
        boolean accAux;                 //Resumen de cumplimientos
        
        Random rand = new Random();
        
        bits = 0;
        iter = 0;
        
        aj = model.getAj();
        bj = model.getBj();
        
        num  = model.getNumVar();
        pres = model.getPre();
        ind  = model.getNumInd();
        
        mj    = new int[num];
        zjcol = new float[ind + 1];
        
        //Calculo de los bits de coromosomas para cada variable
        for (int i = 0; i < num; i++) {
            double a;
            a = ((double)bj.get(i) - (double)aj.get(i)) * Math.pow(10, pres);
            a = Math.log(a) / Math.log(2);
            
            mj[i] = ((int) a) + 1;
            bits = bits + mj[i];
        }
        
        individuals = new int[ind][bits];
        variables = new float[ind][num];
        accom = new boolean[ind];
        
        //Inicialización
        for (int i = 0; i < ind; i++) {
            accom[i] = false;
        }
        accAux = false;
        
        //Mientras todos los individuos cumplan las restricciones
        while(!accAux){
            //Generación aleatoria de individuos
            for (int i = 0; i < ind; i++) {
                if(!accom[i]){
                    for (int j = 0; j < bits; j++) {
                        individuals[i][j] = rand.nextInt(2);
                    }
                }
            }

            //Valores de cada variable para cada individuo
            for (int i = 0; i < ind; i++) {
                for (int j = 0; j < num; j++) {
                    int subDec;             //Subcadena decimal
                    int[] subBin;           //Subcadena binaria
                    int index;

                    subBin = new int[mj[j]];

                    //Posiciona el index en la posición donde empieza 
                    //  el cromosoma correspondiente a cada variable
                    index = 0;
                    for (int k = 0; k < j; k++) {
                        index = index + mj[k];
                    }

                    //Extrae la subcadena de bits correspondiente
                    for (int k = 0; k < mj[j]; k++) {
                        subBin[k] = individuals[i][index + k];
                    }

                    //Subcadena en decimal
                    subDec = Integer.parseInt(Arrays.toString(subBin), 2);

                    //Obtención de variable
                    variables[i][j] = ((float) bj.get(j) - (float) aj.get(j))/((float) Math.pow(2, mj[j]) - 1);
                    variables[i][j] = variables[i][j] * subDec;
                    variables[i][j] = (float) aj.get(j) + variables[i][j];
                }
            }

            //Evaluación en las restricciones
            for (int i = 0; i < ind; i++) {
                accom[i] = evaluation(num, variables[i], model.getRest());
            }

            //Resumen de los cumplimientos
            accAux = true;
            for (int i = 0; i < accom.length; i++) {
                if(!accom[i]){
                    accAux = false;
                    break;
                }
            }
        }
        
        //Impresión de tabla y obtención de la columna zj
        zjcol = printTable(iter, variables, model.getZ());
        
        //Calculo de la segunda parte de la tabla
        
    }
    
    public static boolean evaluation(int num, float[] vars, ArrayList<ArrayList> res){
        boolean[] results = new boolean[res.size()];
        float sum;
        
        for (int i = 0; i < res.size(); i++) {
            sum = 0;
            for (int j = 0; j < num; j++) {
                sum = sum + (float)res.get(i).get(j) * vars[j];
            }
            
            if(res.get(i).get(num).equals("<=")){
                if(sum <= (float)res.get(i).get(num + 1)){
                    results[i] = true;
                } else {
                    results[i] = false;
                }
            } else if (res.get(i).get(num).equals(">=")){
                if(sum >= (float)res.get(i).get(num + 1)){
                    results[i] = true;
                } else {
                    results[i] = false;
                }
            }
        }
        
        for (int i = 0; i < results.length; i++) {
            if(!results[i]){
                return false;
            }
        }
        return true;
    }
    
    public static float[] printTable(int iter, float[][] vars, ArrayList z){
        float zaux;
        float sum;
        float[] zj;
        
        zj = new float[vars.length + 1];
        
        System.out.println("");
        System.out.println(iter + ".");
        
        System.out.print("Vector\t");
        for (int i = 0; i < vars[0].length; i++) {
            System.out.print("X" + i + "\t");
        }
        System.out.print("Zj\n");
        
        sum = 0;
        for (int i = 0; i < vars.length; i++) {
            System.out.print("V" + i + "\t");
            for (int j = 0; j < vars[0].length; j++) {
                System.out.print((float)vars[i][j] + "\t");
            }
            
            zaux = 0;
            for (int j = 0; j < z.size(); j++) {
                zaux = zaux + ((float)vars[i][j] * (float)z.get(j));
            }
            System.out.print(zaux + "\n");
            sum = sum + zaux;
            zj[i] = zaux;
        }
        
        System.out.print("\t");
        for (int i = 0; i < vars[0].length; i++) {
            System.out.print("\t");
        }
        System.out.print(sum + "\n");
        zj[vars.length] = sum;
        
        return zj;
    }
    
}
