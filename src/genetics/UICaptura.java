package genetics;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class UICaptura extends JFrame implements ActionListener{

    public int noRes;
    public int noVar;
    public int individuos;
    public int precision;
    
    public ArrayList<ArrayList> restricciones;
    public ArrayList z;
    
    private JPanel bg;
    private JTextField [] zFields;
    private JTextField [][] resFields;
    private JTextField [] nnFields;
    private JLabel [] zLabels;
    private JLabel [][] resLabels;
    private JLabel [] nnLabels;
    
    private JButton continuar;
    private JComboBox minMax;
    private JComboBox [] condiciones;
    
    private JLabel aux;
    
    private static int LARGO = 35;
    private static int ANCHO = 22;
    private int posAux;
    private int posAux2;
    
    public UICaptura(){
        
        do{
            try{
                noVar = Integer.parseInt(JOptionPane.showInputDialog(null, "Introduzca el número de variables"));
                
                if ( noVar > 5 ){
                    JOptionPane.showMessageDialog(null, "El número máximo de variables es 5");
                }
                else if ( noVar < 1 ){
                    JOptionPane.showMessageDialog(null, "Se necesita al menos una variable para continuar");
                }
                
            }catch(Exception e){
                JOptionPane.showMessageDialog(null, "Introduzca un número válido");
            }
            
        }while( noVar > 5 || noVar < 1 );
        
        do{
            try{
                noRes = Integer.parseInt(JOptionPane.showInputDialog(null, "Introduzca el número de restricciones"));
                
                if ( noRes > 5 ){
                    JOptionPane.showMessageDialog(null, "El número máximo de restricciones es 5");
                }
                
            }catch(Exception e){
                JOptionPane.showMessageDialog(null, "Introduzca un número válido");
            }
            
        }while( noRes > 5 );
        
        do{
            try{
                precision = Integer.parseInt(JOptionPane.showInputDialog(null, "Número de bits de precisión"));
                
            }catch(Exception e){
                JOptionPane.showMessageDialog(null, "Introduzca un número válido");
            }
            
        }while( precision < 0 );
        
        do{
            try{
                individuos = Integer.parseInt(JOptionPane.showInputDialog(null, "Tamaño de la población"));
                
            }catch(Exception e){
                JOptionPane.showMessageDialog(null, "Introduzca un número válido");
            }
            
        }while( individuos < 1 );
        
        restricciones = new ArrayList<ArrayList>();
        z = new ArrayList();
        
        setSize(900,670);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setTitle("Algoritmo Genético");
        
        initializeFrame();
        
        setVisible(true);
        
    }
    
    private void initializeFrame(){
        
        zLabels = new JLabel [noVar];
        zFields = new JTextField[noVar];
        resLabels = new JLabel[noRes][noVar];
        resFields = new JTextField[noRes][noVar+1];
        condiciones = new JComboBox[noRes];
        
        //Add background panel
        bg = new JPanel();
        bg.setBackground(Color.WHITE);
        bg.setLayout(null);
        
        aux = new JLabel("Ingreso de datos");
        aux.setBounds(350,20, 300,22);
        aux.setFont(new Font("Arial",0,20));
        aux.setForeground(new Color(0, 0, 153));
        bg.add(aux);
        
        minMax = new JComboBox();
        minMax.addItem("Max");
        minMax.addItem("Min");
        minMax.setBounds(70, 60, 70, 20);
        minMax.setFont(new Font("Arial",0,18));  
        bg.add(minMax);

        aux = new JLabel("Z = ");
        aux.setBounds(150,60, 70,22);
        aux.setFont(new Font("Arial",0,18));
        aux.setForeground(new Color(0, 0, 0));
        bg.add(aux);
        
        for (int i = 0; i < noVar; i++) {
            
            zFields[i] = new JTextField();
            zFields[i].setBounds(100*(i+2),58, 45,28);
            zFields[i].setFont(new Font("Arial",0,18));
            zFields[i].setForeground(new Color(0, 0, 0));
            bg.add(zFields[i]);

            if(i!=(noVar-1))
                zLabels[i] = new JLabel("x"+i+" + ");
            else
                zLabels[i] = new JLabel("x"+i);
            
            zLabels[i].setBounds(100*(i+2)+50,60, 50,20);
            zLabels[i].setFont(new Font("Arial",0,18));
            zLabels[i].setForeground(new Color(0, 0, 0));
            bg.add(zLabels[i]);
            
        }
        
        aux = new JLabel("Restricciones");
        aux.setBounds(70,120, 300,22);
        aux.setFont(new Font("Arial",0,18));
        aux.setForeground(new Color(0, 0, 153));
        bg.add(aux);
        
        aux = new JLabel("____________________________________________________________________________");
        aux.setBounds(70,140, 800,22);
        aux.setFont(new Font("Arial",0,18));
        aux.setForeground(new Color(0, 0, 153));
        bg.add(aux);
        
        for (int i = 0; i < noRes; i++) {
            posAux = 200+(60*i);
            
            aux = new JLabel("r"+(i+1));
            aux.setBounds(70,posAux, 70,22);
            aux.setFont(new Font("Arial",0,18));
            aux.setForeground(new Color(0, 0, 153));
            bg.add(aux);
            
            for (int j = 0; j < noVar; j++) {
                
                resFields[i][j] = new JTextField();
                resFields[i][j].setBounds(120*(j+1),198+(60*i), 45,28);
                resFields[i][j].setFont(new Font("Arial",0,18));
                resFields[i][j].setForeground(new Color(0, 0, 0));
                bg.add(resFields[i][j]);
                
                posAux2 = 55+120*(j+1);
        
                if( j != (noVar-1))
                    resLabels[i][j] = new JLabel("x"+j+"   +");
                else
                    resLabels[i][j] = new JLabel("x"+j);
                
                resLabels[i][j].setBounds(posAux2,200+(60*i), 50,20);
                resLabels[i][j].setFont(new Font("Arial",0,18));
                resLabels[i][j].setForeground(new Color(0, 0, 0));
                bg.add(resLabels[i][j]);
                
                
            }
            
            condiciones[i] = new JComboBox();
            condiciones[i].addItem("<=");
            condiciones[i].addItem(">=");
            condiciones[i].setBounds(posAux2+45, 200+(60*i), 50, 20);
            condiciones[i].setFont(new Font("Arial",0,18));  
            bg.add(condiciones[i]);

            resFields[i][noVar] = new JTextField();
            resFields[i][noVar].setBounds(posAux2+125,198+(60*i), 50,28);
            resFields[i][noVar].setFont(new Font("Arial",0,18));
            resFields[i][noVar].setForeground(new Color(0, 0, 0));
            bg.add(resFields[i][noVar]);
        }
        
        for (int i = 0; i < noVar; i++) {
            
            aux = new JLabel("r"+(noRes+(i+1)));
            aux.setBounds(70+(150*i),posAux+60, 70,22);
            aux.setFont(new Font("Arial",0,18));
            aux.setForeground(new Color(0, 0, 153));
            bg.add(aux);
            
            aux = new JLabel("x"+i+" >= 0");
            aux.setBounds(100+(150*i),posAux+60, 70,22);
            aux.setFont(new Font("Arial",0,18));
            aux.setForeground(new Color(0, 0, 0));
            bg.add(aux);
        }
        
        continuar = new JButton("Continuar");
        continuar.setBounds(350,posAux+130, 160,30);
        continuar.setFont(new Font("Arial",0,16));
        continuar.setForeground(Color.WHITE);
        continuar.setBackground(new Color(0,0,153));
        continuar.setBorderPainted(false);
        continuar.addActionListener(this);
        bg.add(continuar);
        
        this.getContentPane().add(bg);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        boolean ok = true;
        boolean okfo =  true;
        boolean okres = true;
        String aux = "";
        
        if( e.getSource() == continuar ){
            
            for (int i = 0; i < noVar; i++) {
                
                if(zFields[i].getText().equals("") || zFields[i].getText().equals(" ")){
                    ok = false;
                    okfo = false;
                }
                else{
                    if( minMax.getSelectedItem().toString().equals("Max") ){
                        z.add(Double.parseDouble(zFields[i].getText().trim()));
                    }
                    else if( minMax.getSelectedItem().toString().equals("Min") ){
                        z.add(-1*Double.parseDouble(zFields[i].getText().trim()));
                    }
                }
            }
            
            for (int i = 0; i < noRes; i++) {
                
                restricciones.add(new ArrayList());
                
                for (int j = 0; j < (noVar+1); j++) {
                    if(resFields[i][j].getText().equals("") || resFields[i][j].getText().equals(" ")){
                        ok = false;
                        okres = false;
                    }
                    else{
                        restricciones.get(i).add(Double.parseDouble(resFields[i][j].getText().trim()));
                    }
                }
            }
            
            if ( !okfo ){
                JOptionPane.showMessageDialog(null, "Introduzca los coeficientes de la función objetivo");
            }   
            
            if( !okres ){
                JOptionPane.showMessageDialog(null, "Introduzca los coeficientes de las restricciones");
            }
            else{
                
                for (int i = 0; i < noRes; i++) {
                    restricciones.get(i).add((noVar), condiciones[i].getSelectedItem().toString());
                }
                
                for (int i = 0; i < noVar; i++) {
                    restricciones.add(new ArrayList());
                }
                
                
                int k = 0;
                for (int i = noRes; i < (noRes+noVar); i++) {
                    
                    for (int j = 0; j < (noVar-1); j++) {
                        restricciones.get(i).add((double)0);
                    }
                    
                    restricciones.get(i).add(k,(double)1);
                    restricciones.get(i).add(">=");
                    restricciones.get(i).add((double)0);
                    
                    k++;
                }
                
            }
            
            
            if( ok ){
                dispose();
                
            }
                
        }
        
    }
    
}
