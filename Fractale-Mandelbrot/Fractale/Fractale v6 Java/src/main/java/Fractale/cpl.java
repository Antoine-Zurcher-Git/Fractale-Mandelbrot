/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Fractale;

/**
 *
 * @author antoi
 */



class cpl{
  
  double r,i;
  cpl(double ri,double ii){
    r = ri;
    i = ii;
  }
  
  double norme(){
    return Math.sqrt(r*r+i*i);
  }
  
  double arg(){
    if(r > 0){
      return Math.atan(i/r);
    }else if(r < 0){
      return Math.PI-Math.atan(-i/r);
    }else{
      if(i > 0){
        return Math.PI/2.0;
      }else if(i < 0){
        return -Math.PI/2.0;
      }else{
        return 0;
      }
    }
  }
  
  void aj(cpl z){
    r += z.r;
    i += z.i;
  }
  
  void pro(double a){
    r *= a;
    i *= a;
  }
  
}


