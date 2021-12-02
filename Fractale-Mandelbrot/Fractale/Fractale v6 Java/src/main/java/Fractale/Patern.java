/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Fractale;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author antoi
 */
public class Patern {
    
    int width,height,type,paterne;
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
    int nPaterne;
    int resultat[][][];
    int numeroDeVersion = 0;
    
    Patern(int paternei,int wi,int hi,int typei,int iteLimitei,double[][] parametresi,double echellei,double xci,double yci,double seuilDefi,boolean Mandeli,boolean reverseXYi,boolean iteratifi,boolean pourcentagei){
        paterne = paternei;
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
        pourcentage = pourcentagei;//pourcentage -> affiche ou non le pourcentage d'avancement
        System.out.println("Creation d'un patern");
        
    }
    
    int nPatern(){
        int rep =0;
        switch(paterne){
            case(0)://Normal
                rep = 1;
                break;
            case(1):
                rep = (int)( (Math.toDegrees(angleMax)-Math.toDegrees(angleDep))/multiplicateurAngle );
                break;
        }
        return rep;
    }
    
    void defPaterne0(){//Normale
        nPaterne = nPatern();
        resultat = new int[nPaterne][width][height];
        System.out.println("Definition du Patern de type 0");
    }
    
    double multiplicateurAngle = 0.5;// 1 -> 1 image par degré, 0.5 -> 2 images par degré
    double angleDep = Math.toRadians(0);
    double angleMax = Math.toRadians(360);
    double rayon;
    
    void defPaterne1(double multiplicateurAnglei,double angleDepDeg,double angleMaxDeg){//Circulaire
        multiplicateurAngle = multiplicateurAnglei;
        angleDep = Math.toRadians(angleDepDeg);
        angleMax = Math.toRadians(angleMaxDeg);
        rayon = Math.sqrt(parametres[4][0]*parametres[4][0]+parametres[4][1]*parametres[4][1]);
        nPaterne = nPatern();
        resultat = new int[nPaterne][width][height];
        System.out.println("Definition du Patern de type 1");
    }
    
    BufferedImage preview(int largeur){
        double coeff = 1.0*largeur/width;
        if(coeff > 1){
            coeff = 1;
        }
        BufferedImage image = new BufferedImage((int)(width*coeff),(int)(height*coeff),BufferedImage.TYPE_INT_RGB);
        //Graphics g = image.getGraphics();
        FractaleCalc ex = new FractaleCalc((int)(width*coeff),(int)(height*coeff),type,iteLimite,parametres,echelle/coeff,xc,yc,seuilDef,Mandel,reverseXY,iteratif,pourcentage);
        int result[][] = ex.execution();
        Lecteur lect = new Lecteur("",false);
        for(int i = 0; i < image.getWidth() ; i ++){
            for(int j = 0 ; j < image.getHeight() ; j ++){
                int col = lect.couleur((double)(result[i][j]/iteLimite));
                image.setRGB(i,j,col);
            }
        }
        return image;

    }
    
    void execution(){
        System.out.print("Debut de l'execution");
        switch(paterne){
            case(0):
            System.out.println(" de type 0");
            System.out.println("•---• Etape : 1/1 | Avancement : 100% •---•");
            FractaleCalc ex0 = new FractaleCalc(width,height,type,iteLimite,parametres,echelle,xc,yc,seuilDef,Mandel,reverseXY,iteratif,pourcentage);
            
            resultat[0] = ex0.execution();
            break;
            
            case(1):
                System.out.println(" de type 1");
                int inb = 0;
                for(double angle = angleDep ; angle < angleMax ; angle += Math.toRadians(multiplicateurAngle)){
                    System.out.println("•---• Etape : "+(inb+1)+"/"+nPaterne+" | Avancement : "+((int)(inb*100.0/nPaterne))+"% •---•");
                    double paraSave0 = parametres[4][0];
                    double paraSave1 = parametres[4][1];
                    parametres[4][0] = rayon*Math.cos(angle);
                    parametres[4][1] = rayon*Math.sin(angle);
                    FractaleCalc ex1 = new FractaleCalc(width,height,type,iteLimite,parametres,echelle,xc,yc,seuilDef,Mandel,reverseXY,iteratif,pourcentage);
                    resultat[inb] = ex1.execution();
                    inb ++;
                    parametres[4][0] = paraSave0;
                    parametres[4][1] = paraSave1;
                    
                }
                break;
        }
        System.out.println("Fin de l'execution");
    }
    
    String save(){
        System.out.println("Debut de l'enregistrement");
        FileOutputStream monFichier = null;
        
        String nom = "Resultat/Fractale-"+paterne;
        if(Mandel){
            nom += "-m-";
        }else{
            nom += "-j-";
        }
        nom += ""+type;
        int nParametres = 0;
        for(int i = 0; i < parametres.length ; i ++){
            for(int l = 0 ; l < parametres[i].length; l ++){
                nom += "-"+((int)(parametres[i][l]));
                nParametres ++;
            }
        }
        nom += "-"+((int)(100*xc));
        nom += "-"+((int)(100*yc));
        nom += "-"+((int)(1.0/echelle));
        nom += "--"+((int)(Math.random()*100));
        
        
        //Format :  ( o -> octet entre 0 et 255 , c -> format (n1,n2,n3) n1*1+n2*256+n3*256^2 entre 0 et 256^3-1 ie : 16 777 215)
        /*
    o    numeroDeVersion 
    c    width
    c    height
    o    type
    o    iteLimite
    o    parametres.length
    o    parametres[0].length
    c    parametres[0][0]*10^4
        ...
    c    parametres[0][i]*10^4
        ...
    o    parametres[l].length
        ...
    c    (int)1.0/echelle
    c    (int) xc*10^6
    c    (int) yc*10^6
    c    (int) seuilDef*10^4
    o    0 si mandel false
    o    0 si reverseXY false
    o    0 si iteratif false
    o    paterne
    c    nPaterne
    ?    parametrePaterne1
    ?    parametrePaterne2
        ...
    ?    parametrePaternei
    o    resultat[0][0][0]
    o    resultat[0][0][1]
        ...
    o    resultat[0][0][height]
    o    resultat[0][1][0]
        ...
    o    resultat[0][1][height]
        ...
    o    resultat[0][width][0]
        ...
    o    resultat[0][width][height]
    o    resultat[1][0][0]
        ...
    o    resultat[nPaterne][width][height]
        
        */
        
        System.out.println("Debut Enregistrement parametres");
        int nDonnees = 1+3+3+1+1+1+1*parametres.length+3*nParametres+3+3+3+3+1+1+1+1+3+(resultat.length*width*height);
        switch(paterne){
            case(0):
                nDonnees += 0;
                break;
            case(1):
                nDonnees += 3+3+3;
                break;
        }
        int donnees[] = new int[nDonnees];
        
        int compte = 0;
        
        //numeroDeVersion : o
        donnees[compte] = numeroDeVersion;
        compte ++;
        
        //width : c
        int[] temp = c(width);
        donnees[compte] = temp[0];
        donnees[compte+1] = temp[1];
        donnees[compte+2] = temp[2];
        compte += 3;
        
        //height : c
        temp = c(height);
        donnees[compte] = temp[0];
        donnees[compte+1] = temp[1];
        donnees[compte+2] = temp[2];
        
        //System.out.println(donnees[compte]+" ; "+donnees[compte+1]+" ; "+donnees[compte+2]);
        compte += 3;
        
        //type : o
        donnees[compte] = type;
        compte ++;
        
        //iteLimite : o
        donnees[compte] = iteLimite;
        compte ++;
        
        //parametres.length : o
        donnees[compte] = parametres.length;
        compte ++;
        
        for(int i = 0; i < parametres.length ; i ++){
            
            //parametres[i].length : o
            donnees[compte] += parametres[i].length;
            compte ++;
            
            for(int l = 0 ; l < parametres[i].length; l ++){
                
                //parametres[i][l]*10^4 : c
                temp = c( (int)(parametres[i][l]*Math.pow(10,4)));
                donnees[compte] = temp[0];
                donnees[compte+1] = temp[1];
                donnees[compte+2] = temp[2];
                compte += 3;
            }
        }
        
        //(int)1.0/echelle : c
        temp = c((int)(1.0/echelle));
        donnees[compte] = temp[0];
        donnees[compte+1] = temp[1];
        donnees[compte+2] = temp[2];
        compte += 3;
        
        //(int) xc*10^6 : c
        temp = c((int) (xc*Math.pow(10,6)));
        donnees[compte] = temp[0];
        donnees[compte+1] = temp[1];
        donnees[compte+2] = temp[2];
        compte += 3;
        
        //(int) yc*10^6 : c
        temp = c((int) (yc*Math.pow(10,6)));
        donnees[compte] = temp[0];
        donnees[compte+1] = temp[1];
        donnees[compte+2] = temp[2];
        compte += 3;
        
        //(int) seuilDef*10^4 : c
        temp = c((int)(seuilDef*Math.pow(10,4)));
        donnees[compte] = temp[0];
        donnees[compte+1] = temp[1];
        donnees[compte+2] = temp[2];
        compte += 3;
        
        //Mandel : o
        if(Mandel){donnees[compte]=1;}else{donnees[compte]=0;}
        compte ++;
        
        //reverseXY : o
        if(reverseXY){donnees[compte]=1;}else{donnees[compte]=0;}
        compte ++;
        
        //iteratif : o
        if(iteratif){donnees[compte]=1;}else{donnees[compte]=0;}
        compte ++;
        
        //paterne  o
        donnees[compte] = paterne;
        compte ++;
        
        //nPaterne : c
        temp = c(nPaterne);
        donnees[compte] = temp[0];
        donnees[compte+1] = temp[1];
        donnees[compte+2] = temp[2];
        compte += 3;
        
        switch(paterne){
            case(0):
                break;
            case(1):
                //multiplicateurAngle*1000 : c
                temp = c((int)(multiplicateurAngle*Math.pow(10,1000)));
                donnees[compte] = temp[0];
                donnees[compte+1] = temp[1];
                donnees[compte+2] = temp[2];
                compte += 3;
                
                //degree(angleDep) : c
                temp = c((int)Math.toDegrees(angleDep));
                donnees[compte] = temp[0];
                donnees[compte+1] = temp[1];
                donnees[compte+2] = temp[2];
                compte += 3;
                
                //degree(angleMax) : c
                temp = c((int)Math.toDegrees(angleMax));
                donnees[compte] = temp[0];
                donnees[compte+1] = temp[1];
                donnees[compte+2] = temp[2];
                compte += 3;
                break;
                
        }
        System.out.println("Fin de l'enregistrement des parametres");
        System.out.println("Debut enregistrement data");
        int lastPrc = 0;
        for(int p = 0 ; p < nPaterne ; p ++){
            for(int x = 0 ; x < width ; x ++){
                for(int y = 0 ; y < height ; y ++){
                    donnees[compte] = resultat[p][x][y];
                    compte ++;
                    if(pourcentage){
                        int prc = (int)((y+x*height+p*width*height)*100.0/(nPaterne*width*height));
                        if(prc > lastPrc){
                            lastPrc = prc;
                            System.out.print(prc+"|");
                        }
                    }
                }
            }
        }
        System.out.println("");
        


        System.out.println("Fin de l'enregistrement data");
        System.out.println("Debut de la sauvegarde du fichier");
        

        try {
            monFichier = new FileOutputStream(nom+".mandelbrot");
            for (int i = 0; i < donnees.length; i++) {
                monFichier.write(donnees[i]);
            }
            System.out.println("Sauvegarde du fichier "+nom);
        } catch (IOException exception) {
            System.out.println("Impossible d'écrire dans le fichier :"+ exception.toString());
        } finally{
            try{
                monFichier.close();
            } catch (Exception exception1) {
                exception1.printStackTrace();
            }
        }
        return nom;
    }
    
    int[] c(int n){
        int rep[] = new int[3];
        rep[2] = (int)(n/(256*256));
        rep[1] = (int) ((n-256*256*rep[2])/256);
        rep[0] = (int)(n-256*256*rep[2]-256*rep[1]);
        return rep;
    }
    
    
    
    
}
