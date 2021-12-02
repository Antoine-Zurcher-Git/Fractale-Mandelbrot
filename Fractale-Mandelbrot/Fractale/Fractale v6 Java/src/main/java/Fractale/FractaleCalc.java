/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Fractale;


/**
 *
 * @author antoi
 */
public class FractaleCalc {
    
    int width,height,type;
    int iteLimite = 255;
    double parametres[][] = {{2, 2}, // Re((zn)^)
        {1, 1}, // Re((zn))^
        {1, 1, 0}, // Re(c)^
        {1, 1, 1}, // Re(c^)
        {0, 0}, //z0
        {0}, //type
        {0}//grossissement
      };
    double echelle,xc,yc,seuilDef;
    boolean Mandel,reverseXY,iteratif;
    boolean pourcentage;

    int resultat[][];
    
    FractaleCalc(int wi,int hi,int typei,int iteLimitei,double[][] parametresi,double echellei,double xci,double yci,double seuilDefi,boolean Mandeli,boolean reverseXYi,boolean iteratifi,boolean pourcentagei){
        width=wi;//Largeur de pixel
        height=hi;//Hauteur de pixel
        type = typei;//Type de fonction (définit dans suite())
        iteLimite = iteLimitei;//Nombre d'itération limite
        parametres = parametresi;//Tableau des parametres
        echelle = echellei;//Zoom sur l'image
        xc=xci;//Position x du centre de l'image
        yc=yci;//Position y du centre de l'image
        seuilDef = seuilDefi;//Seuil à partir duquel la fonction diverge
        Mandel = Mandeli;//Mandelbrot ou Julia
        reverseXY=reverseXYi;//Axe inversés
        iteratif=iteratifi;//iteratif ou recursif
        resultat = new int[width][height];
        pourcentage=pourcentagei;
    }

    
    int[][] execution(){
        double lastPrc = 0;
        for(int x=0;x<width;x++){
            for(int y=0;y<height;y++){
                resultat[x][y] = calcNnb(x,y);

                if(pourcentage){
                    double prc = (x*height+y+1)*100.0/(height*width);
                    if((int)(prc/10) > lastPrc/10){
                        lastPrc = (int)(prc);
                        System.out.print(lastPrc+"|");
                    }
                }
            }
        }
        System.out.println("");
        return resultat;
    }
    
    int calcNnb(int xm,int ym){
        //Calcul le nombre d'itération pour la position xm,ym dans la matrice
        //Définit à partir de xm et ym la position réelle, puis utilise calcNreel()
        double tX = xc+(xm-width/2.0)*echelle;
        double tY = yc+(ym-height/2.0)*echelle;
        if (reverseXY) {
            tX = yc+(ym-height/2.0)*echelle;
            tY = xc+(xm-width/2.0)*echelle;
        }
        return calcNreel(tX,tY);
    }
    
    int calcNreel(double r,double i){
        //Calcul le nombre d'itération pour la position r,i dans le plan complexe
        //Détermine la méthode à utiliser (iteratif ou recursif) ainsi que mandel ou julia, puis utilise suiteRecursive() ou suite Iterative()
        int s = 0;
        if (Mandel) {
            if(iteratif){
                s = suiteIterative(parametres[4][0], parametres[4][1], seuilDef, r, i);
            }else{
                s = suiteRecursive(parametres[4][0], parametres[4][1], seuilDef, 0, r, i);
            }
        } else {
            if(iteratif){
                s = suiteIterative(r, i, seuilDef, parametres[4][0], parametres[4][1]);
            }else{
                s = suiteRecursive(r, i, seuilDef, 0, parametres[4][0], parametres[4][1]);
            }
        }
        return s;
    }
    
    int suiteRecursive(double r, double i, double seuil, int ite, double cr, double ci) {
        //Calcul le nombre d'iteration pour un mandelbrot classique
        double vs[] = suite(r, i, cr, ci);
        r = vs[0];
        i = vs[1];

        if ( (r*r+i*i) > Math.pow(seuil, 2) || ite > iteLimite) {
          return (ite+1);
        } else {
          return (suiteRecursive(r, i, seuil, ite+1, cr, ci));
        }
    }     

    int suiteIterative(double r, double i, double seuil, double cr, double ci) {

      int ite = 0;
      while (r*r+i*i < seuil*seuil && ite < iteLimite) {
        double vs[] = suite(r, i, cr, ci);
        r = vs[0];
        i = vs[1];
        ite ++;
      }

      return ite;
    }

    double [] suite(double r, double i, double cr, double ci) {
        double rInt = r;
        double iInt = i;
        switch(type) {

            
          case(1)://Type 1
          r = 0;
          i = 0;
          for (int k = 0; k <= parametres[0][0]; k ++) {
            if (k !=1 && k != 0) {
              r += Math.pow(-1, k)*Main.pcR(true, rInt, iInt, rInt, iInt, k);

              i += Math.pow(-1, k)*Main.pcR(false, rInt, iInt, rInt, iInt, k);
            } else if (k == 0) {
              r += Main.pcR(true, cr, ci, cr, ci, parametres[3][0]);//cr;
              i += Main.pcR(false, cr, ci, cr, ci, parametres[3][0]);//ci;
            }
          }
          break;





          case(2)://Type 2
          if (cr == 0 && ci == 0) {
            double []renv = {seuilDef, seuilDef};
            return renv;
          }
          double cpr = Main.pcR(true, cr, ci, cr, ci, (int)parametres[1][0]);
          double cpi = Main.pcR(false, cr, ci, cr, ci, (int)(parametres[1][1]));
          double rIntp = Main.pcR(true, rInt, iInt, rInt, iInt, (int)(parametres[0][0]));
          double iIntp = Main.pcR(false, rInt, iInt, rInt, iInt, (int)(parametres[0][1]));
          r = Main.cosCpl(true, rIntp, iIntp)+Main.invCpl(true, cpr, cpi);
          i = Main.cosCpl(false, rIntp, iIntp)+Main.invCpl(false, cpr, cpi);
          break;




          case(3)://Type 3
          if (cr == 0 && ci == 0) {
            double []renv = {seuilDef, seuilDef};
            return renv;
          }
          cpr = Main.pcR(true, cr, ci, cr, ci, (int)(parametres[1][0]));
          cpi = Main.pcR(false, cr, ci, cr, ci, (int)(parametres[1][1]));

          r = Main.shCpl(true, cpr, cpi) +Main.pcR(true, rInt, iInt, rInt, iInt, parametres[0][0]);
          i = Main.shCpl(false, cpr, cpi) +Main.pcR(false, rInt, iInt, rInt, iInt, parametres[0][1]);
          break;



          case(4)://Type 4
          if (cr == 0 && ci == 0) {
            double []renv = {seuilDef, seuilDef};
            return renv;
          }
          cpr = Main.pcR(true, cr, ci, cr, ci, (int)(parametres[0][0]));
          cpi = Main.pcR(false, cr, ci, cr, ci, (int)(parametres[0][1]));
          r = Main.sinCpl(true, rInt, iInt)+Main.invCpl(true, cpr, cpi);
          i = Main.sinCpl(false, rInt, iInt)+Main.invCpl(false, cpr, cpi);
          break;



          case(5)://Type 5
          if (cr == 0 && ci == 0) {
            double []renv = {seuilDef, seuilDef};
            return renv;
          }
          cpr = Main.pcR(true, cr, ci, cr, ci, (int)(parametres[1][0]));
          cpi = Main.pcR(false, cr, ci, cr, ci, (int)(parametres[1][1]));

          r = Main.chCpl(true, cpr, cpi) + Main.pcR(true, rInt, iInt, rInt, iInt, parametres[0][0]);
          i = Main.chCpl(false, cpr, cpi) + Main.pcR(false, rInt, iInt, rInt, iInt, parametres[0][1]);
          break;

          case(6)://Type 6 : Burning Ship
          rIntp = (Math.abs(rInt));
          iIntp = (Math.abs(iInt));
          r =Main.pcR(true, rIntp, iIntp, rIntp, iIntp, parametres[0][0]) +Main.pcR(true, cr, ci, cr, ci, parametres[3][0]);
          i =Main.pcR(false, rIntp, iIntp, rIntp, iIntp, parametres[0][1]) +Main.pcR(false, cr, ci, cr, ci, parametres[3][1]);
          break;

          case(7)://Type 7
          rIntp = Main.chCpl(true, Math.abs(rInt), Math.abs(iInt));//(abs(rInt));
          iIntp = Main.chCpl(false, Math.abs(rInt), Math.abs(iInt));//(abs(iInt));
          r =Main.pcR(true, rIntp, iIntp, rIntp, iIntp, parametres[0][0]) +Main.pcR(true, cr, ci, cr, ci, parametres[3][0]);
          i =Main.pcR(false, rIntp, iIntp, rIntp, iIntp, parametres[0][1]) +Main.pcR(false, cr, ci, cr, ci, parametres[3][1]);
          break;

          case(8)://Type 8
          r = 0;
          i = 0;
          for (int k = 0; k <= parametres[0][0]; k ++) {
            if (k !=1 && k != 0) {
              r += Math.pow(-1, k)*Main.pcR(true, rInt, iInt, rInt, iInt, k)/k;
              i += Math.pow(-1, k)*Main.pcR(false, rInt, iInt, rInt, iInt, k)/k;
            } else if (k == 0) {
              r += cr;
              i += ci;
            }
          }
          break;

          case(9)://Type 9
          r = 0;
          i = 0;
          for (int k = 0; k <= parametres[0][0]; k ++) {
            if (k !=1 && k != 0) {
              r += Math.pow(-1, k)*Main.pcR(true, rInt, iInt, rInt, iInt, k)/Main.factorielle(k);
              i += Math.pow(-1, k)*Main.pcR(false, rInt, iInt, rInt, iInt, k)/Main.factorielle(k);
            } else if (k == 0) {
              r += cr;
              i += ci;
            }
          }
          break;

          case(10)://Type 10
          r = 0;
          i = 0;
          for (int k = 0; k <= parametres[0][0]; k ++) {
            if (k !=1 && k != 0) {
              r +=Main.pcR(true, rInt, iInt, rInt, iInt, k)/k;
              i +=Main.pcR(false, rInt, iInt, rInt, iInt, k)/k;
            } else if (k == 0) {
              r += cr;
              i += ci;
            }
          }
          break;

          case(11)://Type 11
          r = 0;
          i = 0;
          rIntp =Main.chCpl(true, Math.abs(rInt), Math.abs(iInt));
          iIntp =Main.chCpl(false, Math.abs(rInt), Math.abs(iInt));
          for (int k = 0; k <= parametres[0][0]; k ++) {
            if (k !=1 && k != 0) {
              r +=Main.pcR(true, rIntp, iIntp, rIntp, iIntp, k)/(k);
              i +=Main.pcR(false, rIntp, iIntp, rIntp, iIntp, k)/(k);
            } else if (k == 0) {
              r += cr;
              i += ci;
            }
          }
          break;

          case(12)://Type 12
          r = 0;
          i = 0;
          rIntp =Main.cosCpl(true, Math.abs(rInt), Math.abs(iInt));
          iIntp =Main.cosCpl(false, Math.abs(rInt), Math.abs(iInt));
          for (int k = 0; k <= parametres[0][0]; k ++) {
            if (k !=1 && k != 0) {
              r +=Main.pcR(true, rIntp, iIntp, rIntp, iIntp, k)/(k);
              i +=Main.pcR(false, rIntp, iIntp, rIntp, iIntp, k)/(k);
            } else if (k == 0) {
              r += cr;
              i += ci;
            }
          }
          break;

          case(13):
          r = cr;
          i = ci;
          for (int k = 1; k < parametres[0][0]; k ++) {
            r += Math.pow(-1, k+1)*Main.pcR(true, rInt, iInt, rInt, iInt, k)/k;
            i += Math.pow(-1, k+1)*Main.pcR(false, rInt, iInt, rInt, iInt, k)/k;
          }
          break;

          case(14):
          r = Math.pow(Main.pcR(true, rInt, iInt, rInt, iInt, parametres[0][0]), parametres[1][0])
            +parametres[2][2]*Math.pow(cr, parametres[2][0])
            +parametres[3][2]*Main.pcR(true, cr, ci, cr, ci, parametres[3][0]);
          i = Math.pow(Main.pcR(false, r, iInt, r, iInt, parametres[0][1]), parametres[1][1])
            +parametres[2][2]*Math.pow(ci, parametres[2][1])
            +parametres[3][2]*Main.pcR(false, cr, ci, cr, ci, parametres[3][1]);
          break;

          case(15):
          double rp2 =Main.pcR(true, rInt, iInt, rInt, iInt, parametres[0][0]);
          double ip2 =Main.pcR(false, rInt, iInt, rInt, iInt, parametres[0][1]);
          double crp2 =Main.pcR(true, cr, ci, cr, ci, parametres[3][0]);
          double cip2 =Main.pcR(false, cr, ci, cr, ci, parametres[3][1]);
          r =Main.invCpl(true, crp2+rp2, cip2+ip2);
          i =Main.invCpl(false, crp2+rp2, cip2+ip2);
          break;

          case(16):
          crp2 =Main.pcR(true, cr, ci, cr, ci, parametres[3][0]);
          cip2 =Main.pcR(false, cr, ci, cr, ci, parametres[3][1]);
          rp2 =Main.pcR(true, rInt, iInt, rInt, iInt, parametres[0][0]);
          ip2 =Main.pcR(false, rInt, iInt, rInt, iInt, parametres[0][1]);
          r =Main.invCpl(true, crp2, cip2)+rp2;
          i =Main.invCpl(false, crp2, cip2)+ip2;
          break;

          case(17):
          crp2 =Main.pcR(true, cr, ci, cr, ci, parametres[3][0]);
          cip2 =Main.pcR(false, cr, ci, cr, ci, parametres[3][1]);
          rp2 =Main.pcR(true, rInt, iInt, rInt, iInt, parametres[0][0]);
          ip2 =Main.pcR(false, rInt, iInt, rInt, iInt, parametres[0][1]);
          double rp3 =Main.cosCpl(true, rp2, ip2);
          double ip3 =Main.cosCpl(false, rp2, ip2);
          r =Main.invCpl(true, rp3, ip3)+crp2;
          i =Main.invCpl(false, rp3, ip3)+cip2;
          break;

          case(18):
          crp2 =Main.pcR(true, cr, ci, cr, ci, parametres[3][0]);
          cip2 =Main.pcR(false, cr, ci, cr, ci, parametres[3][1]);
          rp2 =Main.pcR(true, rInt, iInt, rInt, iInt, parametres[0][0]);
          ip2 =Main.pcR(false, rInt, iInt, rInt, iInt, parametres[0][1]);
          rp3 =Main.cosCpl(true, rp2, ip2);
          ip3 =Main.cosCpl(false, rp2, ip2);
          double rp4 =Main.sinCpl(true, rp3, ip3);
          double ip4 =Main.sinCpl(false, rp3, ip3);
          r =Main.invCpl(true, rp4, ip4)+crp2;
          i =Main.invCpl(false, rp4, ip4)+cip2;
          break;

          case(19):
          rp2 =Main.cosCpl(true, rInt, iInt);
          ip2 =Main.cosCpl(false, rInt, iInt);

          rp3 =Main.cosCpl(true, rp2, ip2);
          ip3 =Main.cosCpl(false, rp2, ip2);

          rp4 =Main.cosCpl(true, rp3, ip3);
          ip4 =Main.cosCpl(false, rp3, ip3);

          r = rp4 +Main.pcR(true, cr, ci, cr, ci, parametres[1][0]);
          i = ip4 +Main.pcR(false, cr, ci, cr, ci, parametres[1][1]);
          break;


          case(20):

          cpl zInt = new cpl(rInt, iInt);
          cpl c = new cpl(cr, ci);

          cpl z =Main.addI(Main.shI((Main.puisI(zInt, parametres[0][0]))),Main.chI(Main.invI(Main.cosI(c))));

          r = z.r;
          i = z.i;
          break;

          case(21):
          zInt = new cpl(rInt, iInt);
          zInt =Main.puisI(zInt, parametres[0][0]);
          c = new cpl(cr, ci);

          z = Main.prodI(Main.addI(new cpl(1, 0),Main.sinI(zInt)),Main.chI(c));

          r = z.r;
          i = z.i;
          break;

          case(22):
          zInt = new cpl(rInt, iInt);
          zInt =Main.puisI(zInt, parametres[0][0]);
          c = new cpl(cr, ci);
          c =Main.puisI(c, parametres[1][0]);

          z = Main.cosI( Main.prodI(zInt,Main.invI(c)) );

          r = z.r;
          i = z.i;
          break;


          case(23):
          zInt = new cpl(rInt, iInt);
          zInt =Main.puisI(zInt, parametres[0][0]);
          c = new cpl(cr, ci);
          c =Main.puisI(c, parametres[1][0]);

          z = Main.addI(Main.shI( Main.prodI(zInt,Main.invI(c)) ),Main.chI( Main.prodI(zInt,Main.invI(c)) ));

          r = z.r;
          i = z.i;
          break;

          case(24):
          zInt = new cpl(rInt, iInt);
          zInt =Main.puisI(zInt, parametres[0][0]);
          c = new cpl(cr, ci);
          c =Main.puisI(c, parametres[1][0]);

          z = Main.addI(Main.prodI(Main.sinI(zInt),Main.invI(Main.addI(new cpl(1, 0), c))), c);

          r = z.r;
          i = z.i;
          break;

          case(25):
          zInt = new cpl(rInt, iInt);
          zInt =Main.puisI(zInt, parametres[0][0]);
          c = new cpl(cr, ci);
          c =Main.puisI(c, parametres[1][0]);

          z = Main.addI(Main.sinI(Main.prodI(zInt, c)), c);

          r = z.r;
          i = z.i;
          break;

          case(26):
          zInt = new cpl(rInt, iInt);
          zInt =Main.puisI(zInt, parametres[0][0]);
          c = new cpl(cr, ci);
          c =Main.puisI(c, parametres[1][0]);

          z =  Main.prodI(Main.addI(Main.sinI(zInt),Main.chI(c)),Main.invI(Main.addI(zInt, c)) );

          r = z.r;
          i = z.i;
          break;

          case(27):
          zInt = new cpl(rInt, iInt);
          zInt =Main.puisI(zInt, parametres[0][0]);
          c = new cpl(cr, ci);
          c =Main.puisI(c, parametres[1][0]);

          z = Main.addI(Main.cosI(zInt),Main.puisI(c, zInt.norme()));

          r = z.r;
          i = z.i;
          break;

          case(28):
          zInt = new cpl(rInt, iInt);
          zInt =Main.puisI(zInt, parametres[0][0]);
          c = new cpl(cr, ci);
          c =Main.puisI(c, parametres[1][0]);

          z = Main.addI(Main.sinI(c), Main.prodI(c,Main.cosI(zInt)));

          r = z.r;
          i = z.i;
          break;

          case(29):
          zInt = new cpl(rInt, iInt);
          zInt =Main.puisI(zInt, parametres[0][0]);
          c = new cpl(cr, ci);
          c =Main.puisI(c, parametres[1][0]);

          z = Main.addI(Main.prodI(c,Main.invI(Main.cosI(zInt))), Main.prodI(c, Main.shI(zInt)));

          r = z.r;
          i = z.i;
          break;

          case(30):
          zInt = new cpl(rInt, iInt);
          zInt =Main.puisI(zInt, parametres[0][0]);
          c = new cpl(cr, ci);
          c =Main.puisI(c, parametres[1][0]);

          z = Main.addI(c, Main.prodI(Main.invI(Main.cosI(zInt)), c));

          r = z.r;
          i = z.i;
          break;

          case(31):
          zInt = new cpl(rInt, iInt);
          zInt =Main.puisI(zInt, parametres[0][0]);
          c = new cpl(cr, ci);
          c =Main.puisI(c, parametres[1][0]);

          z = Main.addI(c,Main.invI(Main.cosI(Main.addI(c,Main.cosI(Main.invI(zInt))) ) ) );

          r = z.r;
          i = z.i;
          break;

          case(32)://Type 6 : Burning Ship
          zInt = new cpl(Math.abs(rInt), Math.abs(iInt));
          zInt =Main.puisI(zInt, parametres[0][0]);
          c = new cpl(cr, ci);
          c =Main.puisI(c, parametres[1][0]);

          z = Main.addI(zInt, c );

          r = z.r;
          i = z.i;
          break;

          case(33):
          zInt = new cpl(rInt, iInt);
          zInt =Main.puisI(zInt, parametres[0][0]);
          c = new cpl(cr, ci);
          c =Main.puisI(c, parametres[1][0]);
          cpl mz = new cpl(zInt.r*(-1), zInt.i*(-1));
          cpl uni = new cpl(-1, 0);
          z =Main.addI(Main.expI(zInt),Main.addI(mz, Main.addI(uni, c)));
          r = z.r;
          i = z.i;
          break;

          case(34):
          zInt = new cpl(rInt, iInt);
          zInt =Main.puisI(zInt, parametres[0][0]);
          c = new cpl(cr, ci);
          c =Main.puisI(c, parametres[1][0]);

          z = Main.expI(Main.addI(zInt, c));

          r = z.r;
          i = z.i;
          break;

          case(35):
          zInt = new cpl(rInt, iInt);
          zInt =Main.puisI(zInt, parametres[0][0]);
          c = new cpl(cr, ci);
          c =Main.puisI(c, parametres[1][0]);

          z = Main.expI(Main.addI(Main.cosI(zInt), Main.invI(c)));

          r = z.r;
          i = z.i;
          break;

          case(36):
          zInt = new cpl(rInt, iInt);
          zInt =Main.puisI(zInt, parametres[0][0]);
          c = new cpl(cr, ci);
          c =Main.puisI(c, parametres[1][0]);

          z = Main.addI(c, Main.expI(zInt));

          r = z.r;
          i = z.i;
          break;

          case(37):
          zInt = new cpl(rInt, iInt);
          zInt =Main.puisI(zInt, parametres[0][0]);
          c = new cpl(cr, ci);
          c =Main.puisI(c, parametres[1][0]);

          z = Main.expI(Main.addI(Main.invI(Main.cosI(zInt)), c));

          r = z.r;
          i = z.i;
          break;


          case(38):
          zInt = new cpl(rInt, iInt);
          zInt =Main.puisI(zInt, parametres[0][0]);
          c = new cpl(cr, ci);
          c =Main.puisI(c, parametres[1][0]);

          z = Main.expI(Main.addI(Main.chI(zInt), Main.cosI(c)));

          r = z.r;
          i = z.i;
          break;

          case(39):
          zInt = new cpl(rInt, iInt);
          zInt =Main.puisI(zInt, parametres[0][0]);
          c = new cpl(cr, ci);
          c =Main.puisI(c, parametres[1][0]);

          z = Main.lnI(Main.expI(Main.addI(zInt, c)));

          r = z.r;
          i = z.i;
          break;

          case(40):
          zInt = new cpl(rInt, iInt);
          zInt = Main.puisI(zInt, parametres[0][0]);
          c = new cpl(cr, ci);
          c = Main.puisI(c, parametres[1][0]);

          z = Main.lnI((Main.addI(Main.cosI(zInt), Main.invI(c))));

          r = z.r;
          i = z.i;
          break;

          case(41):
          zInt = new cpl(rInt, iInt);
          zInt = Main.puisI(zInt, parametres[0][0]);
          c = new cpl(cr, ci);
          c = Main.puisI(c, parametres[1][0]);

          z = Main.addI(Main.expI(Main.cosI(zInt)),Main.lnI(Main.invI(c)));

          r = z.r;
          i = z.i;
          break;


          case(42):
          zInt = new cpl(rInt, iInt);
          zInt = Main.puisI(zInt, parametres[0][0]);
          c = new cpl(cr, ci);
          c = Main.puisI(c, parametres[1][0]);
          z = Main.addI(zInt,c);
          for(int l = 3; l < parametres[0][1]+3 ; l ++){
            z = Main.addI(z,Main.multI(Main.addI(Main.chI(Main.puisI(z,l)),Main.puisI(Main.sinI(c),l)),1.0/l));
          }

          r = z.r;
          i = z.i;
          break;

          case(43):
          zInt = new cpl(rInt, iInt);
          zInt = Main.puisI(zInt, parametres[0][0]);
          c = new cpl(cr, ci);
          c = Main.puisI(c, parametres[1][0]);

          z = Main.addI(Main.cosI(Main.lnI(Main.expI(zInt))),Main.lnI(Main.expI(Main.invI(c))));

          r = z.r;
          i = z.i;
          break;

          case(44):
          zInt = new cpl(rInt, iInt);
          zInt = Main.puisI(zInt, parametres[0][0]);
          c = new cpl(cr, ci);
          c = Main.puisI(c, parametres[1][0]);

          z = Main.lnI(Main.expI(Main.addI(Main.invI(c),zInt)));

          r = z.r;
          i = z.i;
          break;


          case(45):
          zInt = new cpl(rInt, iInt);
          zInt = Main.puisI(zInt, parametres[0][0]);
          c = new cpl(cr, ci);
          c = Main.puisI(c, parametres[1][0]);

          z = Main.addI(Main.invI(Main.expI(zInt)),(Main.invI(c)));

          r = z.r;
          i = z.i;
          break;

          case(46):
          zInt = new cpl(rInt, iInt);
          zInt = Main.puisI(zInt, parametres[0][0]);
          c = new cpl(cr, ci);
          c = Main.puisI(c, parametres[1][0]);

          z = Main.addI(Main.cosI(Main.expI(zInt)),(Main.sinI(c)));

          r = z.r;
          i = z.i;
          break;

          case(47):
          zInt = new cpl(rInt, iInt);
          zInt = Main.puisI(zInt, parametres[0][0]);
          c = new cpl(cr, ci);
          c = Main.puisI(c, parametres[1][0]);

          z = Main.addI((Main.prodI(Main.sinI(zInt),Main.expI(zInt))),(Main.prodI(Main.sinI(c),Main.expI(c))));

          r = z.r;
          i = z.i;
          break;

          //case(48): pas ouf
          //zInt = new cpl(rInt, iInt);
          //zInt =Main.puisI(zInt, parametres[0][0]);
          //c = new cpl(cr, ci);
          //c =Main.puisI(c, parametres[1][0]);

          //z =Main.addI(addI(zInt,shI(prodI(new cpl(rInt,iInt),expI(prodI(new cpl(0,2*PI*3/7.0),cosI(c)) )))) ,c);

          //r = z.r;
          //i = z.i;
          //break;


          case(0):
          r = Math.pow(Main.pcR(true, rInt, iInt, rInt, iInt, parametres[0][0]), parametres[1][0])
            +parametres[2][2]*Math.pow(cr, parametres[2][0])
            +parametres[3][2]*Main.pcR(true, cr, ci, cr, ci, parametres[3][0]);
          i = Math.pow(Main.pcR(false, rInt, iInt, rInt, iInt, parametres[0][1]), parametres[1][1])
            +parametres[2][2]*Math.pow(ci, parametres[2][1])
            +parametres[3][2]*Main.pcR(false, cr, ci, cr, ci, parametres[3][1]);
          break;
        }
        double ren[] = {r, i};
        return ren;
      }
    
}
