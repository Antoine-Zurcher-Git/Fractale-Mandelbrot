/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Fractale;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author antoi
 */
public class Graphismes extends JFrame {
    

    //Graphismes -------------------------------------------------------------------------------
    
    //Indep Etat
    JPanel fonds[] = new JPanel[3]; //0 -> principale , 1 -> creer, 2 -> colorier
    Color fondCouleur = new Color(250,250,90);//Couleur de fond
    
    // Etat = 1 --> Créer
    
    //Construction du menu de construction
    //Paterns :
    //Patern 1
    String basicPatern1[] = {"1","0","360"};//Données rentrées de base
    String patern1NB[] = {"Multiplicateur Angle","Angle Start (deg)","Angle Max (deg)"};//Nom des parametres
    JPanel patern1NPjp[] = new JPanel[patern1NB.length];//Panel contenant les données (label et textField)
    JTextField patern1Entree[] = new JTextField[patern1NB.length];//TextField contenant les données
    //Données :
    String basicDonnee[] = {"1920","1080","0","255","0.003","0","0","20","1","0"};
    String donneeNB[] = {"Width","Height","Type","iteLimite","Echelle","Xc","Yc","SeuilDef","Mandelbrot (1)","ReverseXY (0)"};
    //Parametres : 
    String basicParametres[] = {"2","2","1","1","1","1","0","1","1","1","0","0","0","0"};
    String parametresNB[] = {"puis cpl 1 (2)","puis cpl 2 (2)","puis R 1 (1)","puis R 2 (1)","puis cst cpl 1 (1)","puis cst cpl 2 (1)","puis cst cpl 3 (0)","puis cst R 1 (1)","puis cst R 2 (1)","puis cst R 3 (1)","z0 R (0)","z0 cpl (0)","Type (eff) (0)","Grossissement (0)"};
    //Patern choix
    String basicPatern[] = {"0","0"};
    String paternNB[] = {"Normal : unique","Circulaire"};
    JPanel panelPatern[] = new JPanel[2];
    JLabel labelErreurCreation = new JLabel();
    
    JLabel pourcentageCreer = new JLabel("0%");
    
    //Preview
    boolean preview = false;
    BufferedImage previewI;
    JLabel imagePreview = new JLabel();
    
    BufferedImage previewICreer;
    JLabel imagePreviewCreer = new JLabel();
    
    
    int etat = 0;
    
    boolean affOption = true;
    JPanel donnees = new JPanel();
    
    int taillePolice = 15;
    double coeffPoliceGrosBouttons = 2;
    
    int couleurBase = 170;
    int couleurDelta = 30;
    
    int paternSelected = -1;
    
    
    //Fichier selectionné
    File fichierSelect;
    Lecteur lect;
        
    
    //Patern
    Patern paternCreer;
    
    
    
    
    Graphismes(){
        for(int i = 0 ; i < fonds.length ; i ++){
            fonds[i] = new JPanel();
        }
        setSize(1200,675);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initialisationMenuPrincipal();
        initialisationCreer();
        initialisationColorier();

        this.add(fonds[etat]);
        repaint();
        setVisible(true);
    }

    void initialisationMenuPrincipal(){
        
        fonds[0].setBackground(fondCouleur);
        fonds[0].setLayout(new FlowLayout());
        
        JLabel titre = new JLabel("Fractale");
        titre.setFont(new Font("Dialog", Font.PLAIN, (int)(taillePolice*coeffPoliceGrosBouttons) ));
        titre.setPreferredSize(new Dimension(350,200));
        fonds[0].add(titre);
        
        JButton bouton = new JButton("Créer une fractale");
        bouton.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){   changeEtat(etat,1);    }});
        bouton.setPreferredSize(new Dimension(350,60));
        bouton.setFont(new Font("Dialog", Font.PLAIN,(int)(taillePolice*coeffPoliceGrosBouttons)));
        fonds[0].add(bouton);
        
        bouton = new JButton("Colorier une fractale");
        bouton.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){   changeEtat(etat,2);    }});
        bouton.setPreferredSize(new Dimension(350,60));
        bouton.setFont(new Font("Dialog", Font.PLAIN,(int)(taillePolice*coeffPoliceGrosBouttons)));
        fonds[0].add(bouton);
        
        
    }

    void initialisationCreer(){
        fonds[1].setBackground(fondCouleur);
        fonds[1].setLayout(new BoxLayout(fonds[1],BoxLayout.LINE_AXIS));
        
        int alternanceCouleur = 1;

        JPanel ensemble = new JPanel();
        ensemble.setLayout(new BoxLayout(ensemble,BoxLayout.PAGE_AXIS));

        //Titre
        JLabel titre = new JLabel("Créer");
        titre.setFont(new Font("Dialog", Font.PLAIN, (int)(taillePolice*coeffPoliceGrosBouttons)));
        titre.setPreferredSize(new Dimension(350,200));
        ensemble.add(titre);
        
        //Bouton Menu Principal
        JButton bouton = new JButton("Menu Principal");
        bouton.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){   changeEtat(etat,0);    }});
        bouton.setPreferredSize(new Dimension(350,60));
        bouton.setFont(new Font("Dialog", Font.PLAIN,(int)(taillePolice*coeffPoliceGrosBouttons)));
        ensemble.add(bouton);

        //Label d'erreur
        labelErreurCreation.setFont(new Font("Dialog", Font.PLAIN, (int)(taillePolice*coeffPoliceGrosBouttons)));
        labelErreurCreation.setPreferredSize(new Dimension(350,200));
        ensemble.add(labelErreurCreation);
        
        

        
        donnees.setLayout(new BoxLayout(donnees,BoxLayout.PAGE_AXIS));
        
        donnees.setPreferredSize(new Dimension(this.getWidth()/3,(this.getHeight()) ));
        //Entrée Données
        JPanel donneeEntree = new JPanel();
        donneeEntree.setLayout(new BoxLayout(donneeEntree,BoxLayout.PAGE_AXIS));
        
        
        JPanel donneNBjp[] = new JPanel[donneeNB.length];
        JTextField entrees[] = new JTextField[donneeNB.length];
        for(int i = 0 ; i < donneeNB.length ; i ++){
            donneNBjp[i] = new JPanel();
            donneNBjp[i].setLayout(new BorderLayout());
            JLabel nom = new JLabel(donneeNB[i]+" : ");
            nom.setFont(new Font("Dialog", Font.PLAIN, taillePolice));
            donneNBjp[i].setBackground(new Color(couleurBase+alternanceCouleur*couleurDelta,couleurBase+alternanceCouleur*couleurDelta,couleurBase+alternanceCouleur*couleurDelta));
            alternanceCouleur *= -1;
            //titre.setPreferredSize(new Dimension(350,200));
            donneNBjp[i].add(nom,BorderLayout.WEST); 

            entrees[i] = new JTextField(16);
            entrees[i].setText(basicDonnee[i]);
            donneNBjp[i].add(entrees[i],BorderLayout.EAST);
            donneeEntree.add(donneNBjp[i]);
        }
        
        
        //Entrée Parametres
        JPanel parametreEntree = new JPanel();
        parametreEntree.setLayout(new BoxLayout(parametreEntree,BoxLayout.PAGE_AXIS));
        JPanel parametresNBjp[] = new JPanel[parametresNB.length];
        JTextField parametres[] = new JTextField[parametresNB.length];
        for(int i = 0 ; i < parametresNB.length ; i ++){
            parametresNBjp[i] = new JPanel();
            parametresNBjp[i].setLayout(new BorderLayout());
            JLabel nom = new JLabel(parametresNB[i]+" : ");
            nom.setFont(new Font("Dialog", Font.PLAIN, taillePolice));
            parametresNBjp[i].setBackground(new Color(couleurBase+alternanceCouleur*couleurDelta,couleurBase+alternanceCouleur*couleurDelta,couleurBase+alternanceCouleur*couleurDelta));
            alternanceCouleur *= -1;
            //titre.setPreferredSize(new Dimension(350,200));
            parametresNBjp[i].add(nom,BorderLayout.WEST); 

            parametres[i] = new JTextField(16);
            parametres[i].setText(basicParametres[i]);
            parametresNBjp[i].add(parametres[i],BorderLayout.EAST);
            parametreEntree.add(parametresNBjp[i]);
        }
        
        

        //Entrée Patern
        ButtonGroup  selectPatern = new ButtonGroup ();
        JPanel paternEntree = new JPanel();
        paternEntree.setLayout(new BoxLayout(paternEntree,BoxLayout.PAGE_AXIS));
        JPanel paternNBjp[] = new JPanel[paternNB.length];
        JRadioButton  patern[] = new JRadioButton [paternNB.length];
        for(int i = 0 ; i < paternNB.length ; i ++){
            paternNBjp[i] = new JPanel();
            paternNBjp[i].setLayout(new BorderLayout());
            patern[i] = new JRadioButton(paternNB[i]);
            patern[i].setSelected(basicPatern[i] == "1");
            final int num = i;
            patern[i].addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){   selectPatern(num);    }});
            selectPatern.add(patern[i]);
            paternNBjp[i].add(patern[i],BorderLayout.EAST); 
            paternEntree.add(paternNBjp[i]);
        }
        
        //parametreEntree.add(paternEntree);
        //donnees.add(parametreEntree);
        

        //Panel Patern
        for(int i = 0 ; i < panelPatern.length ; i ++){
            panelPatern[i] = new JPanel();
            panelPatern[i].setLayout(new BoxLayout(panelPatern[i],BoxLayout.PAGE_AXIS));
            switch(i){
                case(0):
                break;

                case(1):
                
                for(int k = 0 ; k < patern1NB.length ; k ++){
                    patern1NPjp[k] = new JPanel();
                    patern1NPjp[k].setLayout(new BorderLayout());
                    JLabel nom = new JLabel(patern1NB[k]+" : ");
                    nom.setFont(new Font("Dialog", Font.PLAIN, taillePolice));
                    patern1NPjp[k].setBackground(new Color(couleurBase+alternanceCouleur*couleurDelta,couleurBase+alternanceCouleur*couleurDelta,couleurBase+alternanceCouleur*couleurDelta));
                    alternanceCouleur *= -1;
                    patern1NPjp[k].add(nom,BorderLayout.WEST); 
                    patern1Entree[k] = new JTextField(16);
                    patern1Entree[k].setText(basicPatern1[k]);
                    patern1NPjp[k].add(patern1Entree[k],BorderLayout.EAST);
                    panelPatern[i].add(patern1NPjp[k]);
                }
                break;
            }
        }

        
        //Bouton Charger
        bouton = new JButton("Charger");
        bouton.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){   initialiserCreer(entrees,parametres); }});
        bouton.setPreferredSize(new Dimension(350,60));
        bouton.setFont(new Font("Dialog", Font.PLAIN,(int)(taillePolice*coeffPoliceGrosBouttons)));
        ensemble.add(bouton);
        
        //Bouton Créé
        bouton = new JButton("Créer");
        bouton.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){   run();    }});
        bouton.setPreferredSize(new Dimension(350,60));
        bouton.setFont(new Font("Dialog", Font.PLAIN,(int)(taillePolice*coeffPoliceGrosBouttons)));
        ensemble.add(bouton);
        
        
        //Bouton preview
        JButton boutonPreview = new JButton("Preview");
        boutonPreview.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){   createPreviewCreer();    }});
        boutonPreview.setPreferredSize(new Dimension(350,60));
        boutonPreview.setFont(new Font("Dialog", Font.PLAIN,(int)(taillePolice*coeffPoliceGrosBouttons)));
        ensemble.add(boutonPreview);
        
        //Bouton Options
        JButton boutonOption = new JButton("Options");
        boutonOption.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){   afficherOptionCreer();    }});
        boutonOption.setPreferredSize(new Dimension(350,60));
        boutonOption.setFont(new Font("Dialog", Font.PLAIN,(int)(taillePolice*coeffPoliceGrosBouttons)));
        ensemble.add(boutonOption);
        
        //Pourcentage d'avancement
        pourcentageCreer.setFont(new Font("Dialog", Font.PLAIN, (int)(taillePolice*coeffPoliceGrosBouttons)));
        pourcentageCreer.setPreferredSize(new Dimension(350,200));
        ensemble.add(pourcentageCreer);
        
        
        donnees.add(donneeEntree);
        donnees.add(parametreEntree);
        donnees.add(paternEntree);
        
        
        fonds[1].add(ensemble);
        fonds[1].add(donnees);
        fonds[1].add(imagePreviewCreer);
        

    }
    
    void initialisationColorier(){
        fonds[2].setBackground(fondCouleur);
        fonds[2].setLayout(new FlowLayout());
        
        JPanel ensemble = new JPanel();
        ensemble.setLayout(new BoxLayout(ensemble,BoxLayout.PAGE_AXIS));


        JLabel titre = new JLabel("Colorier");
        titre.setFont(new Font("Dialog", Font.PLAIN, (int)(taillePolice*coeffPoliceGrosBouttons)));
        titre.setPreferredSize(new Dimension(350,200));
        ensemble.add(titre);

        JButton bouton = new JButton("Menu Principal");
        bouton.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){   changeEtat(etat,0);    }});
        bouton.setPreferredSize(new Dimension(350,60));
        bouton.setFont(new Font("Dialog", Font.PLAIN,(int)(taillePolice*coeffPoliceGrosBouttons)));
        ensemble.add(bouton);
        
        JButton selectionFichier = new JButton("Selectionner");
        selectionFichier.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){   selectFichier();    }});
        selectionFichier.setPreferredSize(new Dimension(350,60));
        selectionFichier.setFont(new Font("Dialog", Font.PLAIN,(int)(taillePolice*coeffPoliceGrosBouttons)));
        ensemble.add(selectionFichier);
        
        JButton boutonPreview = new JButton("Preview");
        boutonPreview.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){   createPreviewColor();    }});
        boutonPreview.setPreferredSize(new Dimension(350,60));
        boutonPreview.setFont(new Font("Dialog", Font.PLAIN,(int)(taillePolice*coeffPoliceGrosBouttons)));
        ensemble.add(boutonPreview);
        
        JButton boutonSauvegarder = new JButton("Sauvegarder");
        boutonSauvegarder.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){   try {
            saveImageColor();
            } catch (IOException ex) {
                Logger.getLogger(Graphismes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }});
        boutonSauvegarder.setPreferredSize(new Dimension(350,60));
        boutonSauvegarder.setFont(new Font("Dialog", Font.PLAIN,(int)(taillePolice*coeffPoliceGrosBouttons)));
        ensemble.add(boutonSauvegarder);
        
        fonds[2].add(ensemble);
        
        //fond[2].add(new JLabel(new ImageIcon(previewI)));
        
        
        fonds[2].add(imagePreview);
        
        
        
        

    }
    
    void saveImageColor() throws IOException{
        lect.creationImage();
    }
    
    void afficherOptionCreer(){
        
        if(affOption){
            fonds[1].remove(donnees);
            repaint();
            setVisible(true);
            affOption = false;
        }else{
            fonds[1].add(donnees);
            repaint();
            setVisible(true);
            affOption= true;
            
        }
        System.out.println(affOption);
    }

    void selectPatern(int num){
        if(paternSelected >= 0){
            //fonds[1].remove(panelPatern[paternSelected]);
            donnees.remove(panelPatern[paternSelected]);
        }
        //fonds[1].add(panelPatern[num]);
        donnees.add(panelPatern[num]);
        repaint();
        setVisible(true);
        paternSelected = num;
    }

    void run(){
        
        paternCreer.execution();
        String nom = paternCreer.save();
        System.out.println(nom);
        
        
    }
    
    private void selectFichier(){
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File (System.getProperty("user.dir")+ System.getProperty("file.separator")+"Resultat" ));
        //FileNameExtensionFilter filter = new FileNameExtensionFilter("mandelbrot");
        //chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            System.out.println("You chose to open this file: " + chooser.getSelectedFile().getName());
        }
        
        fichierSelect = chooser.getSelectedFile();
        chargeFichier();
        
    }
    
    private void chargeFichier(){
        // charge le fichier fichierSelect
        lect = new Lecteur(fichierSelect,true);
        lect.lecture(true);
        //lect.creationImage();
    }
    
    private void createPreviewColor(){
        previewI = lect.compile(0,500);
        preview = true;
        imagePreview.setIcon(new ImageIcon(previewI));
    }
    
    private void initialiserCreer(JTextField [] entrees,JTextField [] parametres){
        System.out.println("Debut du chargement des données");
        boolean erreurEntree = false;
        String erreurEntreeNom = "";
        //System.out.println("Entrée");
        double entreesD[] = new double[entrees.length];
        double parametresD[] = new double[parametres.length];
        
        System.out.println("Debut des entrees");
        for(int i = 0 ; i < entrees.length ; i ++){

            try{
                double valeur = Double.parseDouble(entrees[i].getText());
                entreesD[i] = valeur;
                
            }catch(Exception e){
                erreurEntree = true;
                erreurEntreeNom += "Entree n-"+(i+1)+" ; ";
            }
        }
        
        boolean erreurParametres = false;
        String erreurParaNom = "";
        System.out.println("Debut des parametres");
        for(int i = 0 ; i < parametres.length ; i ++){

            try{
                double valeur = Double.parseDouble(parametres[i].getText());
                parametresD[i] = valeur;
            }catch(Exception e){
                erreurParametres = true;
                erreurParaNom += "Parametre n-"+(i+1)+" ; ";
            }
        }
        
        //System.out.println("Patern");
        //System.out.println("num : "+paternSelected);
        
        System.out.println("Debut des paternValeurs");
        JTextField paternValeurs[];
        double paternValeursD[];
        boolean erreurPatern = false;
        String erreurPateNom = "";
        if(paternSelected == 1){
            paternValeurs = patern1Entree;
        }else{ 
            paternValeurs = new JTextField[0];
        }
        paternValeursD = new double[paternValeurs.length];
        if(paternValeurs.length > 0){
            for(int i = 0 ; i < paternValeurs.length ; i ++){
                //System.out.println(paternValeurs[i].getText());
                try{
                    double valeur = Double.parseDouble(paternValeurs[i].getText());
                    paternValeursD[i] = valeur;
                }catch(Exception e){
                    erreurPatern = true;
                    erreurPateNom += "Patern n-"+(i+1)+" ; ";
                }
            }
        }
        
        
        labelErreurCreation.setText("");
        if(erreurEntree || erreurParametres || erreurPatern){ 
            System.out.println("");
            System.out.println(erreurEntreeNom+" et "+erreurParaNom + " et " + erreurPatern);
            labelErreurCreation.setText("Erreur");
        }else{
            
            int paterne = paternSelected;
            int w = (int)(entreesD[0]); 
            int h = (int)(entreesD[1]) ;
            int type = (int)(entreesD[2]);
            int iteLimite = (int)(entreesD[3]);
            double parametre[][] = {{parametresD[0],parametresD[1]},{parametresD[2],parametresD[3]},{parametresD[4],parametresD[5],parametresD[6]},{parametresD[7],parametresD[8],parametresD[9]},{parametresD[10],parametresD[11]},{parametresD[12]},{parametresD[13]}};
            double echelle = entreesD[4];
            double xc = entreesD[5];
            double yc = entreesD[6];
            double seuilDef = entreesD[7];
            boolean Mandel = true;
            boolean reverseXY = false;
            boolean iteratif = true;
            boolean pourcentage = true;
            if(entreesD[8] == 0){//mandel
                Mandel = false;
            }
            if(entreesD[9] == 1){
                reverseXY = true;
            }
            System.out.println("Debut de la creation du Patern");
            paternCreer = new Patern(paterne,w,h,type,iteLimite,parametre,echelle,xc,yc,seuilDef,Mandel,reverseXY,iteratif,pourcentage);
            System.out.println("Debut de l'initialisation du Patern");
            System.out.println(paterne);
            switch(paterne){
                case(0):
                    paternCreer.defPaterne0();
                    break;
                    
                case(1):
                    paternCreer.defPaterne1(paternValeursD[0],paternValeursD[1],paternValeursD[2]);
                    break;
            }
            
            
        }
//        if(affOption){
//            fonds[1].remove(donnees);
//            repaint();
//            setVisible(true); 
//            affOption = false;
//        }
        System.out.println("Chargement fini");
    }
    
    private void createPreviewCreer(){
        previewICreer = paternCreer.preview(500);
        imagePreviewCreer.setIcon(new ImageIcon(previewICreer));
        
    }
    
    private void changeEtat(int dep,int arr){
        //Permet de changer d'état (de menu)
        remove(fonds[dep]);
        add(fonds[arr]);
        etat = arr;
        repaint();
        setVisible(true);
    }
    

}
