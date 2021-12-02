/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Fractale;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author antoi
 */
public class Lecteur {
    
    int donneesTotal;
    String nom = "";
    File fichier;
    boolean useFichier = false;
    
    int numeroDeVersion,width,height,type,iteLimite;
    double parametres;
    double echelle,xc,yc,seuilDef;
    boolean Mandel,reverseXY,iteratif;
    int paterne,nPaterne;
        
    //tcs
    double multiplicateurAngle = 0.5;// 1 -> 1 image par degré, 0.5 -> 2 images par degré
    double angleDep = Math.toRadians(0);
    double angleMax = Math.toRadians(360);
    double rayon;
    
    int nPrePara = 1+3+3+1+1+1;
    
    int resultat [][][];
    
    int donneesParaTemp[];
    boolean pourcentage;

    Lecteur(String nomi,boolean pourcentagei){
        System.out.println("Creation d'un lecteur");
        nom = nomi;
        pourcentage = pourcentagei; 
    }
    
    Lecteur(File fichieri,boolean pourcentagei){
        System.out.println("Creation d'un lecteur");
        fichier = fichieri;
        pourcentage = pourcentagei; 
        useFichier = true;
    }
    
    void dessine(int p,String dossier) throws IOException{
        
        BufferedImage image = compile(p);
        
        String cara = "";
        switch(paterne){
            case(1):
                cara = p+"-"+((int)((p*(angleMax-angleDep)/100.0+angleDep)))+"--";
                break;
        }
        
        File outputfile = new File(dossier+cara+nom+".jpg");
        if(useFichier){
            outputfile = new File(dossier+cara+fichier.getName()+".jpg");
        }
        ImageIO.write(image, "jpg", outputfile);
        
    }
    
    BufferedImage compile(int p){
        
        BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        //Graphics g = image.getGraphics();
        for(int i = 0; i < image.getWidth();i++){
            for(int j = 0; j < image.getHeight(); j ++){
                //int ni = resultat[p][i][j];
                //int col = ((ni)*255/50 << 16) | ((ni)*255/50 << 8) | (ni)*255/50;
                int col = couleur((double)(resultat[p][i][j]/iteLimite));
                image.setRGB(i,j,col);
            }
        }
        return image;
    }
   
    BufferedImage compile(int p,int largeur){
        double coeff = 1.0*largeur/width;
        if(coeff >= 1){
            return compile(p);
        }else{
            BufferedImage image = new BufferedImage((int)(width*coeff),(int)(height*coeff),BufferedImage.TYPE_INT_RGB);
            //Graphics g = image.getGraphics();
            
            for(int i = 0; i < image.getWidth() ; i ++){
                for(int j = 0 ; j < image.getHeight() ; j ++){
                    int col = couleur((double)(resultat[p][(int)(i/coeff)][(int)(j/coeff)]/iteLimite));
                    image.setRGB(i,j,col);
                }
            }
            return image;
        }
    }

    int couleur(double x){

        return ( (int)(x*255) << 16) | ( (int)(x*255) << 8) | (int)(x*255);
    }
    
    void creationImage() throws IOException{
        System.out.println("Debut de la creation d'image");
        
        String directoryName = "Resultat/Paterne"+paterne+"-"+nom+"/";
        if(useFichier){
            directoryName = "Resultat/Paterne"+paterne+"-"+fichier.getName()+"/";
        }
        File directory = new File(directoryName);
        if (! directory.exists()){
            directory.mkdir();
        }
    
        System.out.println("Enregistrement dans le fichier :"+directoryName);
        for(int p = 0 ; p < nPaterne ; p ++){
            System.out.println("•---• Etape : "+(p+1)+"/"+nPaterne+" | Avancement : "+((int)(p*100.0/nPaterne))+"% •---•");
            dessine(p,directoryName);
        }
        System.out.println("Fin de la creation d'images");
    }
    
    int cp(int []trip){
        return trip[0]+trip[1]*256+trip[2]*256*256;
    }
    
    void lecture(boolean lectureData){
        System.out.println("Lecture du fichier Resultat/"+nom+".mandelbrot");
        boolean numeroDeVersionB=true,widthB=false,heightB=false,typeB=false,iteLimiteB=false,parametresB=false,echelleB=false,xcB=false,ycB=false,seuilDefB=false,MandelB=false,reverseXYB=false,iteratifB=false,paterneB=false,nPaterneB=false,paraPaternsB=false;
        boolean dataB = false;
        boolean multiplicateurAngleB = true,angleDepB = false,angleMaxB = false;
        
        FileInputStream monFichier = null;
        
        int nPara = -2;
        int nIntraPara = 0;
        int nParaTotal = 0;
        int donneesPara[] = new int[1000];
        for(int i = 0 ; i < 1000; i ++){donneesPara[i] = -1;}
        
        int np = 0;
        int nx = 0;
        int ny = 0;
        
        int n = 0;
        
        int triplN = 0;
        int tripl[] = new int[3];
        
        int lastPrc = 0;
        
        try {
          // Ouvre un flux pointant sur le fichier
            if(useFichier){
                monFichier = new FileInputStream(fichier);
            }else{
                monFichier = new FileInputStream("Resultat/"+nom+".mandelbrot");
            }
            
            
            
            
            
            while (true) {
                int valeurEntiereOctet = monFichier.read();
                //System.out.print(" " + valeurEntiereOctet);
                
                if(n < 20){
                    //System.out.println("Lecture : "+valeurEntiereOctet);
                    n++;
                }
                
                
                if(dataB && lectureData){
                    if(nx == 0 && ny == 0 && np == 0){
                        System.out.println("Chargement du Data");
                        resultat = new int[nPaterne][width][height];
                    }
                    resultat[np][nx][ny] = valeurEntiereOctet;
                    if(pourcentage){
                        int prc = (ny+nx*height+np*width*height)*100/(width*height*nPaterne);
                        if(prc > lastPrc){
                            lastPrc = prc;
                            System.out.print(prc+"|");
                        }
                    }
                    ny ++;
                    if(ny >= height){
                        nx ++;
                        ny = 0;
                    }
                    if(nx >= width){
                        np ++;
                        nx = 0;
                    }
                    if(np >= nPaterne){
                        dataB = false;
                        System.out.println("");
                        System.out.println("Fin du chargement du data");
                    }
                }else if(lectureData == false){
                    break;
                }
                
                if(paraPaternsB){
                    switch(paterne){
                        case(0):
                            paraPaternsB = false;
                            dataB = true;
                            break;
                            
                        case(1):
                            if(multiplicateurAngleB){
                                tripl[triplN] = valeurEntiereOctet;
                                triplN ++;
                                if(triplN >= 3){
                                    triplN = 0;
                                    multiplicateurAngleB = false;
                                    angleDepB = true;
                                    multiplicateurAngle = (float)(cp(tripl))/Math.pow(10,3);
                                }
                            }
                            if(angleDepB){
                                tripl[triplN] = valeurEntiereOctet;
                                triplN ++;
                                if(triplN >= 3){
                                    triplN = 0;
                                    angleDepB = false;
                                    angleMaxB = true;
                                    angleDep = Math.toRadians(cp(tripl));
                                }
                            }
                            if(angleMaxB){
                                tripl[triplN] = valeurEntiereOctet;
                                triplN ++;
                                if(triplN >= 3){
                                    triplN = 0;
                                    angleMaxB = false;
                                    paraPaternsB = false;
                                    dataB = true;
                                    angleMax = Math.toRadians(cp(tripl));
                                }
                            }
                            break;
                    }
                    
                }
                
                if(nPaterneB){
                    tripl[triplN] = valeurEntiereOctet;
                    triplN ++;
                    if(triplN >= 3){
                        triplN = 0;
                        nPaterneB = false;
                        paraPaternsB = true;
                        nPaterne = cp(tripl);
                        System.out.println("nPaterne : "+nPaterne);
                    }
                }
                
                if(paterneB){
                    paterne = valeurEntiereOctet;
                    paterneB = false;
                    nPaterneB = true;
                    System.out.println("paterne : "+paterne);
                }
                
                if(iteratifB){
                    if(valeurEntiereOctet == 0){
                        iteratif = false;
                    }else{
                        iteratif = true;
                    }
                    iteratifB = false;
                    paterneB = true;
                    System.out.println("iteratif : "+iteratif);
                }
                
                
                if(reverseXYB){
                    if(valeurEntiereOctet == 0){
                        reverseXY = false;
                    }else{
                        reverseXY = true;
                    }
                    reverseXYB = false;
                    iteratifB = true;
                    System.out.println("reverseXY : "+reverseXY);
                }
                
                if(MandelB){
                    if(valeurEntiereOctet == 0){
                        Mandel = false;
                    }else{
                        Mandel = true;
                    }
                    MandelB = false;
                    reverseXYB = true;
                    System.out.println("Mandel : "+Mandel);
                }
                
                if(seuilDefB){
                    tripl[triplN] = valeurEntiereOctet;
                    triplN ++;
                    if(triplN >= 3){
                        triplN = 0;
                        seuilDefB = false;
                        MandelB = true;
                        seuilDef = (float)(cp(tripl))/Math.pow(10,4);
                        System.out.println("seuilDef : "+seuilDef);
                    }
                }
                
                if(ycB){
                    tripl[triplN] = valeurEntiereOctet;
                    triplN ++;
                    if(triplN >= 3){
                        triplN = 0;
                        ycB = false;
                        seuilDefB = true;
                        yc = (float)(cp(tripl))/Math.pow(10,6);
                        System.out.println("yc : "+yc);
                    }
                }
                
                if(xcB){
                    tripl[triplN] = valeurEntiereOctet;
                    triplN ++;
                    if(triplN >= 3){
                        triplN = 0;
                        xcB = false;
                        ycB = true;
                        xc = (float)(cp(tripl))/Math.pow(10,6);
                        System.out.println("xc : "+xc);
                    }
                }
                
                if(echelleB){
                    tripl[triplN] = valeurEntiereOctet;
                    triplN ++;
                    if(triplN >= 3){
                        triplN = 0;
                        echelleB = false;
                        xcB = true;
                        echelle = 1.0/cp(tripl);
                        System.out.println("echelle : "+echelle);
                    }
                    
                }
                
                if(parametresB){
                    if(nPara == -2){
                        nPara = valeurEntiereOctet;
                        
                    }else if(nIntraPara == 0){
                        nIntraPara = valeurEntiereOctet*3;
                        nPara --;
                    }else{
                        nIntraPara --; 
                    }
                    
                    if(nPara == 0 && nIntraPara == 0){
                        parametresB = false;
                        echelleB = true;
                    }
                    
                    donneesPara[nParaTotal] = valeurEntiereOctet;
                    nParaTotal ++;
                }
                
                if(iteLimiteB){
                    iteLimite = valeurEntiereOctet;
                    iteLimiteB = false;
                    parametresB = true;
                    System.out.println("iteLimite : "+iteLimite);
                }
                
                if(typeB){
                    type = valeurEntiereOctet;
                    typeB = false;
                    iteLimiteB = true;
                    System.out.println("type : "+type);
                }
                
                if(heightB){
                    tripl[triplN] = valeurEntiereOctet;
                    triplN ++;
                    if(triplN >= 3){
                        triplN = 0;
                        heightB = false;
                        typeB = true;
                        height = cp(tripl);
                        System.out.println("height : "+height);
                    }
                }
                
                if(widthB){
                    tripl[triplN] = valeurEntiereOctet;
                    triplN ++;
                    //System.out.println("valeurEntiereOctet : "+valeurEntiereOctet);
                    if(triplN >= 3){
                        triplN = 0;
                        widthB = false;
                        heightB = true;
                        width = cp(tripl);
                        //for(int i = 0 ; i < 3 ; i ++){
                        //    System.out.println(tripl[i]);
                        //}
                        System.out.println("width : "+width);
                    }
                }
                
                if(numeroDeVersionB){
                    numeroDeVersion = valeurEntiereOctet;
                    numeroDeVersionB = false;
                    widthB = true;
                    System.out.println("Numero de Version : "+numeroDeVersion);
                }
                
                
                
                if (valeurEntiereOctet  == -1) {
                    // Nous avons atteint la fin du fichier
                      // Sortons de la boucle
                    break;
                }  
            } // Fin de la boucle while
            donneesParaTemp = donneesPara;
            // monFichier.close(); pas à cet endroit
        } catch (IOException exception) {
            System.out.println("Impossible de lire le fichier : "+ exception.toString());
        } finally {
            try {
                monFichier.close();
            } catch (Exception exception1){
               exception1.printStackTrace() ;
            }
               System.out.println("Lecture du fichier terminee.");	           
        }
        
    }
    
}
