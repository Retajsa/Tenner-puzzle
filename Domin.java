/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication29;
import java.lang.Math.*; 
/**
 *
 * @author noura
 */
public class Domin implements Cloneable {
    
 private boolean dominValues [] = new boolean [10];
 private int value;
 

  // true for an index menas that the value or the index number is valid as an assigment
    public Domin() {
    
       for ( int i =0;i<10;i++)
           dominValues [i]=true;
          value=-1;
    }
    /*public void ReAssignDominValue(){
          for (int i =0;i<10;i++)
           dominValues [i]=true; 
          
       
    }*/
    public void setDominValue(int i,boolean answer) {
        dominValues[i]=answer;
    }
    
    public int getDominLength() {
        return dominValues.length;
    }
       public boolean allChecked() {
        
        for ( int i =0;i<10;i++){
           if(dominValues [i]!=false)
               return false;
    }
            return true;
}
       public boolean getDominValue(int i) {
        return dominValues[i];
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
      public int DominSize() {
          int counter=0;
           for (int i =0;i<10;i++){
           if(dominValues [i]==true) 
             counter++;
             }
       return counter;
    }
 @Override
 public Object clone() throws CloneNotSupportedException
  {
  return (Domin)super.clone(); 
  }
   
    }