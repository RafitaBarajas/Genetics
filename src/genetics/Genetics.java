
package genetics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Genetics {

    public static void main(String[] args) throws InterruptedException {
        
        UICaptura ui = new UICaptura(); //Interfaz de captura
        
        while(ui.isDisplayable()){}
        
        Model model = new Model(ui.noVar, ui.noVar+ui.noRes, ui.restricciones, ui.precision, ui.individuos, ui.z);      //Modelo
        
        ArrayList aj, bj;               //Límites
        
        int num;                        //Número de Variables
        int pres;                       //Presición
        int ind;                        //Número de Individuos
        int bits;                       //Bits totales de cada Individuo
        int iter;                       //Número de iteración
        int numsurv;                    //Número de sobrevievientes
        
        int[] mj;                       //Bits de cromosomas
        int[] surv;                     //Sobrevivientes
        int[][] individuals;            //Datos de cada individuo
        int[][] indaux;                 //Auxiliar para los individuos
        
        double[] zjcol;                 //Columna Zj
        double[][] variables;           //Variables de cada individuo
        double[][] table;               //Tabla %Zj %Zjacm aleatorio y vector'
        
        boolean[] accom;                //Cumplimiento de restricciones
        boolean accAux;                 //Resumen de cumplimientos
        
        String subBinAux;               //Auxiliar cadena binaria
        
        Random rand = new Random();
        
        bits = 0;
        iter = 0;
        
        aj = model.getAj();
        bj = model.getBj();
        
        num  = model.getNumVar();
        pres = model.getPre();
        ind  = model.getNumInd();
        
        mj    = new int[num];
        surv  = new int[ind];
        //zjcol = new double[ind + 1];
        table = new double[ind][4];
        
        //Calculo de los bits de coromosomas para cada variable
        for (int i = 0; i < num; i++) {
            double a;
            a = ((double)bj.get(i) - (double)aj.get(i)) * Math.pow(10, pres);
            a = Math.log(a) / Math.log(2);
            
            mj[i] = ((int) a) + 1;
            bits = bits + mj[i];
        }
        
        individuals = new int[ind][bits];
        indaux      = new int[ind][bits];
        variables   = new double[ind][num];
        accom       = new boolean[ind];
        
        //Inicialización
        for (int i = 0; i < ind; i++) {
            accom[i] = false;
        }
        accAux = false;
        
        //Hasta que todos los individuos cumplan las restricciones
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
                    subBinAux = "";
                    for (int k = 0; k < mj[j]; k++) {
                        subBinAux = subBinAux + subBin[k];
                    }
                    subDec = Integer.parseInt(subBinAux, 2);

                    //Obtención de variable
                    variables[i][j] = ((double) bj.get(j) - (double) aj.get(j))/((double) Math.pow(2, mj[j]) - 1);
                    variables[i][j] = variables[i][j] * subDec;
                    variables[i][j] = (double) aj.get(j) + variables[i][j];
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
        
        while(iter < 100){                    ////////////////////CAMBIAR A 100, 5 PA PROBAR
            //Impresión de tabla y obtención de la columna zj
            zjcol = printTable(iter, variables, model.getZ());
            iter++;

            //Calculo de la segunda parte de la tabla
            //%zj
            for (int i = 0; i < ind; i++) {
                table[i][0] = zjcol[i] / zjcol[zjcol.length - 1];
            }

            //%zjacm
            for (int i = 0; i < ind; i++) {
                if(i == 0){
                    table[i][1] = table[i][0];
                } else {
                    table[i][1] = table[i][0] + table[i - 1][1];
                }
            }

            //aleatorio
            for (int i = 0; i < ind; i++) {
                table[i][2] = rand.nextDouble();
            }

            //vector'
            for (int i = 0; i < ind; i++) {
                for (int j = 0; j < ind; j++) {
                    double limsup;
                    double liminf;

                    if(j == 0){
                        liminf = 0;
                        limsup = table[j][1];
                    } else {
                        liminf = table[j - 1][1];
                        limsup = table[j][1];
                    }

                    if(table[i][2] > liminf && table[i][2] <= limsup){
                        table[i][3] = j;
                    }
                }
            }

            //Obtención de sobrevivientes
            //Inicialización
            numsurv = 0;
            for (int i = 0; i < surv.length; i++) {
                surv[i] = -1;
            }

            for (int i = 0; i < ind; i++) {
                int cont;

                cont = 0;
                for (int j = 0; j < ind; j++) {
                    if ((int)table[i][3] == surv[j]) {
                        cont++;
                    }
                }

                if(cont == 0){
                    surv[numsurv] = (int)table[i][3];
                    numsurv++;
                }
            }

            //Asignación vectores'
            //Copia como auxiliar
            for (int i = 0; i < ind; i++) {
                for (int j = 0; j < bits; j++) {
                    indaux[i][j] = individuals[i][j];
                }
            }

            //Asignación
            for (int i = 0; i < ind; i++) {
                if(surv[i] == -1){
                    break;
                } else {
                    for (int j = 0; j < bits; j++) {
                        individuals[i][j] = indaux[surv[i]][j];
                    }
                }
            }

            accAux = false;
            for (int i = 0; i < ind; i++) {
                accom[i] = false;
            }

            while(!accAux){
                //Mutación o cruce
                for (int i = numsurv; i < ind; i++) {
                    int r;

                    if(!accom[i]){
                        //Si solo sobrevive uno, no puede haber cruce
                        if(numsurv > 1){
                            r = rand.nextInt(2);
                        } else {
                            r = 0;
                        }

                        if (r == 0) { //Mutación
                            int v, bit;

                            v   = rand.nextInt(numsurv); //Vector sobreviviente a mutar
                            bit = rand.nextInt(bits);    //Bit que mutará

                            for (int j = 0; j < bits; j++) {
                                if(j == bit){
                                    if(indaux[surv[v]][j] == 0){
                                        individuals[i][j] = 1;
                                    } else {
                                        individuals[i][j] = 0;
                                    }
                                } else {
                                    individuals[i][j] = indaux[surv[v]][j];
                                }
                            }

                        } else {      //Cruce
                            int v1, v2, bit;

                            v1  = rand.nextInt(numsurv); //Primer vector a cruzar
                            v2  = rand.nextInt(numsurv); //Segundo vector a cruzar
                            bit = rand.nextInt(bits);    //Bit donde se cruzarán

                            //Hasta que sean diferentes vectores
                            while (v1 == v2){
                                v2 = rand.nextInt(numsurv);
                            }

                            for (int j = 0; j < bits; j++) {
                                if(j < bit){
                                    individuals[i][j] = indaux[surv[v1]][j];
                                } else {
                                    individuals[i][j] = indaux[surv[v2]][j];
                                }
                            }
                        }
                    }

                }

                //Actualización de cada variable para cada individuo
                for (int i = 0; i < ind; i++) {
                    for (int j = 0; j < num; j++) {
                        int subDec;             //Subcadena decimal
                        int[] subBin;           //Subcadena binaria
                        int index;

                        subBin = new int[mj[j]];

                        //Posicionamiento del index
                        index = 0;
                        for (int k = 0; k < j; k++) {
                            index = index + mj[k];
                        }

                        //Extracción de la subcadena de bits correspondiente
                        for (int k = 0; k < mj[j]; k++) {
                            subBin[k] = individuals[i][index + k];
                        }

                        //Subcadena en decimal
                        subBinAux = "";
                        for (int k = 0; k < mj[j]; k++) {
                            subBinAux = subBinAux + subBin[k];
                        }
                        subDec = Integer.parseInt(subBinAux, 2);

                        //Obtención de variable
                        variables[i][j] = ((double) bj.get(j) - (double) aj.get(j))/((double) Math.pow(2, mj[j]) - 1);
                        variables[i][j] = variables[i][j] * subDec;
                        variables[i][j] = (double) aj.get(j) + variables[i][j];
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
        }
        //FIN
    }
    
    public static boolean evaluation(int num, double[] vars, ArrayList<ArrayList> res){
        boolean[] results = new boolean[res.size()];
        double sum;
        
        for (int i = 0; i < res.size(); i++) {
            sum = 0;
            for (int j = 0; j < num; j++) {
                sum = sum + (double)res.get(i).get(j) * vars[j];
            }
            
            if(res.get(i).get(num).equals("<=")){
                if(sum <= (double)res.get(i).get(num + 1)){
                    results[i] = true;
                } else {
                    results[i] = false;
                }
            } else if (res.get(i).get(num).equals(">=")){
                if(sum >= (double)res.get(i).get(num + 1)){
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
    
    public static double[] printTable(int iter, double[][] vars, ArrayList z){
        int s;
        double zaux;
        double sum;
        double[] zj;
        
        zj = new double[vars.length + 1];
        
        System.out.println("");
        System.out.println(iter + ".");
        
        System.out.print("Vector\t");
        for (int i = 0; i < vars[0].length; i++) {
            System.out.print("X" + i + "\t\t\t");
        }
        System.out.print("Zj\n");
        
        sum = 0;
        for (int i = 0; i < vars.length; i++) {
            System.out.print("V" + i + "\t");
            for (int j = 0; j < vars[0].length; j++) {
                System.out.print((double)vars[i][j] + "\t");
            }
            
            zaux = 0;
            for (int j = 0; j < z.size(); j++) {
                zaux = zaux + ((double)vars[i][j] * (double)z.get(j));
            }
            System.out.print(zaux + "\n");
            sum = sum + zaux;
            zj[i] = zaux;
        }
        
        System.out.print("\t");
        for (int i = 0; i < vars[0].length; i++) {
            System.out.print("\t\t\t");
        }
        System.out.print(sum + "\n");
        zj[vars.length] = sum;
        
        return zj;
    }
    
}
