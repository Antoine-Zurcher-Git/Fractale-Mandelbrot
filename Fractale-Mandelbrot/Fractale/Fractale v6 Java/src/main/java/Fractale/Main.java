/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Fractale;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author antoi
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        
        //Utilisation :
        /*
        Ecriture :
        • Patern test = new Patern(int paterne,int w,int h,int type,int iteLimite,double[][] parametres,double echelle,double xc,double yc,double seuilDef,boolean Mandel,boolean reverseXY,boolean iteratif);
        
        paterne -> numéro du paterne (0 -> normal, 1 -> circulaire)
        w -> width (1920)
        h -> height (1080)
        type -> type de fonction (0 à 45)
        iteLimite -> nombre d'iteration max avant de se dire que ça diverge pas (254)
        parametres -> {{2, 2}, // Re((zn)^){1, 1}, // Re((zn))^{1, 1, 0}, // Re(c)^{1, 1, 1}, // Re(c^){0, 0}, //z0{0}, //type{0}//grossissement};
        echelle -> zoom (0.003)
        xc -> position du centre (0)
        yc -> position du centre (0)
        seuilDef -> seuil à partir duquel on considere que ça converge (20)
        Mandel 
        reverseXY (false)
        iteratif (true)
        
        • test.defPaterneX();
        • test.execution();
        • test.save();
        
        Lecture :
        • Lecteur testl = new Lecteur(nom);
        • test1.lecture();
        • test1.creationImage();
        
        */
        
        
        
        
        //cree();
        Graphismes jeu = new Graphismes();
        //dess("Fractale-0-m-45-3-2-1-1-1-1-0-1-1-1-0-0-0-0-0--68-250--34");
//        Lecteur test = new Lecteur("",false);
//        for(double x = 0 ; x <= 1 ; x += 0.1){
//            System.out.println(test.couleur(x));
//        }
        
        
    }
    
    static String cree(){
        double parametres[][] = {{3, 2},  {1, 1},  {1, 1, 0},  {1, 1, 1},  {0, 0},  {0},  {0}};
        Patern test = new Patern(0,1920,1080,45,254,parametres,0.003993,0,-0.68926,20,true,true,true,true);
        test.defPaterne0();
        test.execution();
        return test.save();
    }
    
    static void dess(String nom) throws IOException{
        Lecteur test1 = new Lecteur(nom,false);
        test1.lecture(true);
        test1.creationImage();
    }
    
    static cpl cosI(cpl z){
        cpl rep = new cpl(0,0);
        rep.r = Math.cos(z.r)*ch(z.i);
        rep.i = -Math.sin(z.r)*sh(z.i);
        return rep;
    }

    static cpl sinI(cpl z){
        cpl rep = new cpl(0,0);
        rep.r = Math.sin(z.r)*ch(z.i);
        rep.i = Math.cos(z.r)*sh(z.i);
        return rep;
    }

    static cpl invI(cpl z){
        cpl rep = new cpl(0,0);
        double n = z.norme();
        n *= n;
        rep.r = z.r/n;
        rep.i = -z.i/n;
        return rep;
    }

    static cpl shI(cpl z){
        cpl rep = new cpl(0,0);
        rep.r = sh(z.r)*Math.cos(z.i);
        rep.i = ch(z.r)*Math.sin(z.i);
        return rep;
    }

    static cpl chI(cpl z){
        cpl rep = new cpl(0,0);
        rep.r = ch(z.r)*Math.cos(z.i);
        rep.i = sh(z.r)*Math.sin(z.i);
        return rep;
    }

    static cpl prodI(cpl z1,cpl z2){
        cpl rep = new cpl(0,0);
        rep.r = z1.r*z2.r-z1.i*z2.i;
        rep.i = z1.r*z2.i+z1.i*z2.r;
        return rep;
    }

    static cpl puisI(cpl z,double p){
        cpl rep = new cpl(0,0);
        double n = z.norme();
        double arg = z.arg();
        rep.r = Math.pow(n,p)*Math.cos(arg*p);
        rep.i = Math.pow(n,p)*Math.sin(arg*p);
        return rep;
    }

    static cpl expI(cpl z){
        cpl rep = new cpl(0,0);
        rep.r = Math.exp(z.r)*Math.cos(z.i);
        rep.i = Math.exp(z.r)*Math.sin(z.i);
        return rep;
    }

    static cpl lnI(cpl z){
        cpl rep = new cpl(0,0);
        double n = Math.log(z.norme());
        double arg = z.arg();
        rep.r = n;
        rep.i = arg;
        return rep;
    }

    static cpl multI(cpl z,double nb){
        cpl rep = new cpl(0,0);
        rep.r = z.r*nb;
        rep.i = z.i*nb;
        return rep;
    }

    static cpl addI(cpl z1,cpl z2){
        return new cpl(z1.r+z2.r,z1.i+z2.i);
    }
    
  static double sh(double x) {
    return 0.5*(Math.exp(x)-Math.exp(-x));
  }

  static double ch(double x) {
    return 0.5*(Math.exp(x)+Math.exp(-x));
  }

  static double cosCpl(boolean reel,double r,double i){
    if(reel){
      return Math.cos(r)*ch(i);
    }else{
      return -Math.sin(r)*sh(i);
    }
  }

  static double sinCpl(boolean reel,double r,double i){
    if(reel){
      return Math.sin(r)*ch(i);
    }else{
      return Math.cos(r)*sh(i);
    }
  }

  static double invCpl(boolean reel,double r,double i){
    double nt = r*r+i*i;
    if(reel){
      return r/nt;
    }else{
      return -i/nt;
    }
  }

  static double shCpl(boolean reel,double r,double i){
    if(reel){
      return sh(r)*Math.cos(i);
    }else{
      return ch(r)*Math.sin(i);
    }
  }

  static double chCpl(boolean reel,double r,double i){
    if(reel){
      return ch(r)*Math.cos(i);
    }else{
      return sh(r)*Math.sin(i);
    }
  }

  static double factorielle(int k){
    if(k <= 1){
      return 1;
    }else{
      double r = 1;
      for(int i = 1; i <= k; i ++){
        r *= i;
      }
      return r;
    }
  }

  static double pcR(boolean reel, double r, double i, double rs, double is, double p) {
    if (p == 1) {
      if (reel) {
        return r;
      } else {
        return i;
      }
    } else if (p <= 0) {
      return 1;
    } else {
      return pcR(reel, r*rs-i*is, r*is+rs*i, rs, is, p-1);
    }
  }

}
