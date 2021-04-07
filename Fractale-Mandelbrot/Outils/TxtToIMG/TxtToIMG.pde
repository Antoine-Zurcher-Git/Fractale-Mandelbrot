String []fractale ;
float f[][];
int lx, ly;
float resolution, maximax, minimin;
int vitesseAffichage = 3;
int typeC = 0;

String nomFichier = "Fractale-j-41148-2,2,1,1,1,1,0,1,1,1,27,0,0";
boolean auto = false;
int autoDepar = 1;
 
void setup() {
  fractale = loadStrings("Fractales/"+nomFichier+".txt");
  String test[] = fractale[0].split(";");
  f = new float[test.length][fractale.length];

  lx = test.length;
  ly = fractale.length;


  for (int y=0; y<fractale.length; y++) {
    String ligne[] = fractale[y].split(";");
    for (int x=0; x <test.length; x++) {
      f[x][y] = float(ligne[x]);
    }
  }
  fullScreen();

  noStroke();
  float sousL[] = new float [test.length];
  float sousL2[] = new float [test.length];
  for (int i=0; i<test.length; i++) {
    sousL[i] = max(f[i]);
    sousL2[i] = min(f[i]);
  }
  maximax = max(sousL);
  minimin = min(sousL2);

  for (int y=0; y<fractale.length; y++) {
    for (int x=0; x <test.length; x++) {
      f[x][y] = f[x][y]-minimin;
    }
  }
  minimin=0;
  
  if(auto){
    typeC= 8;
    vitesseAffichage = 1;
    contraste = 32;
    seuil  = 0.3;
    cptc8.append(color(20, 30, 170));
    ptc8.append(0.01);
    cptc8.append(color(241, 254, 25));
    ptc8.append(0.125);
    cptc8.append(color(255, 255, 255));
    ptc8.append(0.32);
  }
  
  affichage(0, 0, 1920/3*2, 1080/3*2);
  affHUB();
  affHUBc3();
  
  //color ct = convertHSBtoRGB(170, 255, 255);
}

void draw() {
  if (mouseX < width/3.0 && mouseY > height*2/3.0) {
    affHUBc3();
  }
  if (auto == true) {
    
    affichage(0, 0, width, height);
    saveFrame("49/"+autoDepar+nomFichier+plusNom()+".png");
    autoDepar ++;
    fractale = loadStrings("49/Fractale-"+autoDepar+nomFichier+".txt");
    String test[] = fractale[0].split(";");
    f = new float[test.length][fractale.length];

    lx = test.length;
    ly = fractale.length;


    for (int y=0; y<fractale.length; y++) {
      String ligne[] = fractale[y].split(";");
      for (int x=0; x <test.length; x++) {
        f[x][y] = float(ligne[x]);
      }
    }
    
    //println("a");
  }
  if (mousePressed) {
    if (mouseX > 2*width/3.0 && mouseY > height*2/3.0) {
      seuil = (mouseX-2*width/3.0)/width*3.0;
      affHUB();
    }
  }
}

int typeCmax= 8;
float contraste = 50;
int typeCHSB [] = {3, 4, 6};

float seuil = 0.3;

float c3Pd = 43;//44;
float c3Pa = 170;//190;
float c3sp=1;
float c3l = 255*(1-int(c3sp+0.1))+c3sp*(c3Pa-c3Pd+255)%255;

float c3nBN = 1.0/40;
float c3deltaBN = 7/255.0;
float c3precision = 2;
float c3prec2 = 250;
int c3c= 0;
float maxContraste = 50;


FloatList ptc8 = new FloatList();
IntList cptc8 = new IntList();
int couleurSelect = 0;

void keyPressed() {
  println(keyCode);
  if (keyCode==65) {//A
    typeC ++;
    if (typeC > typeCmax) {
      typeC = 0;
    }
  } else if (keyCode==81) {//Q
    typeC --;
    if (typeC < 0) {
      typeC = typeCmax;
    }
  }
  if (keyCode == 90) {//Z
    contraste += 0.5;
  }
  if (keyCode == 83) {//S
    contraste -= 0.5;
  }
  if (keyCode == 82) {//R
    affichage(0, 0, 1920/3*2, 1080/3*2);
  }
  if (keyCode == 69) {//E
    vitesseAffichage ++;
  }
  if (keyCode == 68) {//D
    vitesseAffichage --;
    if (vitesseAffichage < 1) {
      vitesseAffichage = 1;
    }
  }
  if (keyCode == 84) {//T
    seuil += 0.001;
  }
  if (keyCode == 71) {//G
    seuil -= 0.001;
  }
  if (keyCode == 87) {//W
    affichage(0, 0, width, height);
    saveFrame("Fractales/"+nomFichier+plusNom()+".png");
  }
  //affichage(0,0,1920,1080);
  if (keyCode == 88) {//X
    cptc8.remove(couleurSelect);
    ptc8.remove(couleurSelect);
    if (couleurSelect >= ptc8.size()) {
      couleurSelect = ptc8.size()-1;
    }
    affHUBc3();
  }
  if (keyCode == 67) {//C
    cptc8.append(color(0, 0, 0));
    ptc8.append(seuil);
    affHUBc3();
  }
  if (keyCode == 89) {//Y
    contraste += 5;
  }
  if (keyCode == 72) {//H
    contraste -= 5;
  }
  //if (keyCode == 85) {//U
  //  seuil += 0.05;
  //}
  //if (keyCode == 74) {//J
  //  seuil -= 0.05;
  //}


  affHUB();
}


void affichage(float px, float py, float tx, float ty) {
  background(255);
  resolution = tx/lx;
  //if (resolution*vitesseAffichage > 0.9) {
  noStroke();
  if ( dedans(typeC, typeCHSB)) {
    colorMode(HSB);
  }
  //int prc = 0;
  for (int x =0; x<lx; x+=vitesseAffichage) {
    for (int y=0; y<ly; y+=vitesseAffichage) {
      fill(couleur(f[x][y]));
      //println(int(resolution*x+px), int(resolution*y+py), int(resolution*vitesseAffichage)+1, 1+int(resolution*vitesseAffichage));
      rect(int(resolution*x+px), int(resolution*y+py), int(resolution*vitesseAffichage)+1, int(resolution*vitesseAffichage)+1);
    }
    //if (100.0*x/lx > prc +1) {
    //  println(prc+1);
    //  prc = int(100.0*x/lx);
    //}
  }
  if ( dedans(typeC, typeCHSB)) {
    colorMode(RGB);
  }
  //} 
  //else {
  //  if ( dedans(typeC, typeCHSB)) {
  //    colorMode(HSB);
  //  }
  //  int prc = 0;
  //  for (int x =0; x<lx; x+=vitesseAffichage) {
  //    for (int y=0; y<ly; y+=vitesseAffichage) {

  //      stroke(couleur(f[x][y]));
  //      point(int(resolution*x+px), int(resolution*y+py));
  //    }
  //    if (100.0*x/lx > prc +1) {
  //      println(prc+1);
  //      prc = int(100.0*x/lx);
  //    }
  //  }
  //  if ( dedans(typeC, typeCHSB)) {
  //    colorMode(RGB);
  //  }
  //}
}

void affHUB() {
  fill(255);
  noStroke();
  rect(width/3*2, 0, width/3, height);
  fill(0);
  textSize(25);
  text("(A/Q) Type couleur : "+typeC, 1295, 30);
  text("(Z/S) Contraste : "+contraste, 1295, 60);
  text("(E/D) vitesse Affichage : "+vitesseAffichage, 1295, 90);
  text("(ClicG) c3Pd : "+c3Pd, 1295, 120);
  text("(ClicD) c3Pa : "+c3Pa, 1295, 150);
  text("(ClicM) c3sp : "+c3sp, 1295, 180);
  text("(W) Enregistrer", 1295, 210);
  text("(T/G) Seuil : "+str(int(seuil*1000)/1000.0), 1295, 240);
  text("(X/C) : Ajouter/Effacer une couleur 8", 1295, 270);
  text("(Y/H) Contraste +- 5", 1295, 300);
  //text("(U/J) Seuil +- 0.05", 1295, 330);

  strokeWeight(int(width/3.0/255.0));
  stroke(0);
  noFill();
  rect(width*2/3, height*2/3, width, height);
  noStroke();
  int save = typeC;
  for (int i = 0; i <= typeCmax; i++) {
    typeC = i;
    if ( dedans(typeC, typeCHSB)) {
      colorMode(HSB);
    }
    for (float a = 0; a < 1; a += 1/255.0) {


      fill(couleur(a));
      rect( (2+a)*width/3.0, height*2/3.0+i*height/(3.0)/(typeCmax+1), width/3.0/255, height/(3.0)/(typeCmax+1));//(2+a)*width/3.0,height*2/3.0+(i+1)*height/(3.0)/(typeCmax+1)
    }
    if ( dedans(typeC, typeCHSB)) {
      colorMode(RGB);
    }
  }

  typeC = save;

  stroke(0);
  line((2+minimin)*width/3.0, height*2/3.0, (2+minimin)*width/3.0, height);
  line((2+seuil)*width/3.0, height*2/3.0, (2+seuil)*width/3.0, height);
  line((2+maximax)*width/3.0, height*2/3.0, (2+maximax)*width/3.0, height);
  //println(minimin, maximax);
  
}

void affHUBc3() {
  noStroke();
  fill(255);
  rect(0,2*height/3.0,2*width/3.0,height/3.0);
  if (typeC !=8) {
    colorMode(HSB);
    for (float x= 0; x<255; x+= 1.0/c3precision) {
      stroke(x, 255, 255);
      line(x*c3precision/(255*c3precision)*width/3.0, 2*height/3.0, x*c3precision/(255*c3precision)*width/3.0, height);
    }

    noStroke();
    fill(255*mouseX/(width/3.0), 255, 255);
    rect(width/3.0, 2.0*height/3, 200, height/3.0);

    for (float i = 0; i < 1; i+= 1.0/c3prec2) {
      float nColor1 = pow(i, 50/maxContraste);
      if (c3sp > 0) {
        //nColor1 = c3Pd+((c3Pd+c3l)%255-c3Pd)*nColor1;
        nColor1 = (c3Pd+nColor1*c3l)%255;
      } else {
        //nColor1 = c3Pd+((c3Pd-c3l)%255-c3Pd)*nColor1;
        nColor1 = (c3Pd-nColor1*c3l)%255;
      }
      stroke(nColor1, 255, 255);
      line(width/3.0, height*2/3 +i*c3prec2, width/3.0+200, height*2/3 +i*c3prec2);
    }
    //println("Depart : "+c3Pd+" Arrivée : "+c3Pa+" Sens : "+c3sp);
    colorMode(RGB);
  } else {
    float prec = 5;
    for (float x = 0; x < width/3.0; x += prec) {
      for (float y = height*2/3.0; y < height; y += prec) {
        stroke(x*3.0/width*255, 255*(y-height*2/3.0)*3.0/height, contraste);
        fill(x*3.0/width*255, 255*(y-height*2/3.0)*3.0/height, contraste);
        rect(x, y, prec, prec);
      }
    }
    for (int i = 0; i < cptc8.size(); i ++) {
      if (i == couleurSelect) {
        stroke(100);
      } else {
        stroke(cptc8.get(i));
      }
      fill(cptc8.get(i));
      rect(width/3.0, 2*height/3.0+i*height/3.0/cptc8.size(), 200, height/3.0/cptc8.size());
      fill(0);
      text(ptc8.get(i),width/3.0+200+50,2*height/3.0+height/3.0/cptc8.size()*(i+0.5));
    }
  }
}


color couleur(float c) {

  float nb1=0;
  float nb2=0;
  float nb3=0;

  switch(typeC) {
    case(0):
    nb1 = c*255.0*255.0/contraste;
    nb2 = c*255.0*255.0/contraste;
    nb3 = c*255.0*255.0/contraste;
    break;

    case(1):
    nb1 = 120-120*pow(c, 25/contraste);//0.4
    nb2 = 190-190*pow(c, 25/contraste);//*(ni/iteLimite);
    nb3 = 220-220*pow(c, 25/contraste);//*(ni/iteLimite);
    break;

    case(2):
    c = pow(c, 50/contraste);
    if (c <= seuil*0.5/0.3) {
      nb1 = 255;
      nb2 = (0.5-c)*255/(0.5);
      nb3 = 0;
    } else if (c > seuil*0.5/0.3 && c < 1) {
      nb1 = 255*(1-c);
      nb2 = 0;
      nb3 = 255*(c-0.5);
    } else {
      nb1 = 0;
      nb2 = 0;
      nb3 = 0;
    }
    break;

    case(3):
    nb1 = c;
    nb1 = pow(nb1, 50/contraste);
    nb1 = (sin((nb1-0.5)*PI)+1)/2;

    nb1 = (c3Pd+c3sp*nb1*c3l)%255;
    float xTemp = c;
    if (xTemp < c3deltaBN) {
      xTemp /= 5;
    }

    nb2 = (pow(xTemp, c3nBN)-pow(xTemp, 1.0/c3nBN))*255;
    nb3 = pow(xTemp, c3nBN)*255;
    nb2 = 255;
    nb3 = 255;
    break;


    case(4):
    if (c > seuil) {
      nb1 = c3Pa;
    } else {
      nb1 = c/seuil;
      nb1 = pow(nb1, 50/contraste);
      nb1 = (sin((nb1-0.5)*PI)+1)/2;
      nb1 = (c3Pd+c3sp*nb1*c3l)%255;
    }
    nb2 = 255;
    nb3 = 255;
    break;

    case(5):
    if (c > seuil) {
      color tempC;
      tempC = convertHSBtoRGB(c3Pa, 255, 255);
      nb1 = red(tempC);
      nb2 = green(tempC);
      nb3 = blue(tempC);
    } else {
      nb1 = c/seuil;
      nb1 = pow(nb1, 50/contraste);
      color depC = convertHSBtoRGB(c3Pd, 255, 255);
      color ArrC = convertHSBtoRGB(c3Pa, 255, 255);
      color finalC = lerpColor(depC, ArrC, nb1);
      nb1 = red(finalC);
      nb2 = green(finalC);
      nb3 = blue(finalC);
    }
    break;

    case(6):
    nb1 = c;
    nb1 = pow(nb1, 50/contraste);
    //nb1 = (sin((nb1-0.5)*PI)+1)/2;

    nb1 = (c3Pd+nb1*255.0/seuil)%255;

    nb2 = 255;
    nb3 = 255;
    break;

    case(7):
    nb1 = 0.2/seuil*c*255.0*255.0/contraste;
    nb2 = c*255.0*255.0/contraste;
    nb3 = c*255.0*255.0/contraste;
    break;

    case(8):
    color depart = color(0, 0, 0);
    color arrivee = color(0, 0, 0);
    float xDep = 0, xArr = 1;
    float x = pow(c, 50/contraste);
    for (int i = 0; i < cptc8.size(); i ++) {
      float empl = ptc8.get(i);
      if (empl <= x && empl >= xDep) {
        xDep = empl;
        depart = cptc8.get(i);
      } else if (empl >= x && empl <= xArr) {
        xArr = empl;
        arrivee = cptc8.get(i);
      }
    }
    color rep = lerpColor(depart, arrivee, (x-xDep)/(xArr-xDep));
    nb1 = red(rep);
    nb2 = green(rep);
    nb3 = blue(rep);
    break;
  }
  return color(nb1, nb2, nb3);
}

boolean dedans(int nb, int[]l) {
  for (int i = 0; i < l.length; i ++) {
    if (l[i] == nb) {
      return true;
    }
  }
  return false;
}

void mouseClicked() {

  if (typeC != 8) {
    if (mouseX <= width/3.0 && mouseY > 2*height/3.0) {
      if (mouseButton == LEFT) {
        c3Pd = 255*mouseX/(width/3.0);
      } else if (mouseButton == RIGHT) {
        c3Pa = 255*mouseX/(width/3.0);
      } else if (mouseButton == 3) {
        c3sp*=-1;
      }
      c3l = 255*(1-int(c3sp+0.1))+c3sp*(c3Pa-c3Pd+255)%255;
    }
  } else {
    if (mouseX <= width/3.0 && mouseY > 2*height/3.0) {
      cptc8.set(couleurSelect, color(mouseX*3.0/width*255, 255*(mouseY-height*2/3.0)*3.0/height, contraste));
      //ptc8.set(couleurSelect, seuil);
    } else if (mouseX > width/3.0 && mouseX <= width/3.0+200 && mouseY > 2*height/3.0) {
      couleurSelect = int((mouseY-2*height/3.0)/height*3.0*ptc8.size());
      affHUBc3();
    }
  }
}

String plusNom() {
  String rep = ",,";
  rep += typeC;
  rep +=",";
  //if (typeC <= 4) {
    
  //}
  rep += int(contraste);
  if (typeC >= 3 && typeC != 8) {
    rep +=",";
    rep += int(c3Pd);
    rep +=",";
    rep += int(c3Pa);
    rep +=",";
    rep += int(c3sp);
  }

  rep +=",";
  rep += int(int(seuil*1000)/1000.0);
  if(typeC == 8){
    for(int i = 0 ; i < ptc8.size() ; i ++){
      rep+=",C"+i+"-";
      color cActu = cptc8.get(i);
      rep+=int(red(cActu))+","+int(green(cActu))+","+int(blue(cActu))+","+int(int(ptc8.get(i)*1000)/1000.0);
    }
    rep+="r-";//+int(random(0,100));
  }
  println(rep);
  return rep;
}


color convertHSBtoRGB(float H, float S, float B) {
  float rp = 0;
  float gp = 0;
  float bp = 0;

  colorMode(HSB);
  color cEt = color(H, S, B);
  rp = red(cEt);
  gp = green(cEt);
  bp = blue(cEt);
  colorMode(RGB);
  return color(rp, gp, bp);
}