

float fractaleF[][];
Table fractaleTable;
boolean enregistrerEnCSV = false;

void draw() {
  if (x == 0) {
    if (enregistrerEnCSV == false) {
      fractaleF = new float[int(width/resolution)+1][int(height/resolution)+1];
    } else {
      fractaleTable = new Table();
      for (int xTable = 0; xTable < int(width/resolution)+1; xTable ++) {
        fractaleTable.addColumn(str(xTable));
      }
    }
  }
  dessin();

  if (cd <= 10) {
    cd ++;
  }

  if (HUD) {

    stroke(200, 20, 20);
    line(width/4, 0, width/4, height);
    line(width*3/4, 0, width*3/4, height);
    line(0, height/4, width, height/4);
    line(0, height*3/4, width, height*3/4);
    fill(0);
    noStroke();
    rect(0, height/4, width/8, height/5);
    fill(200, 20, 20);
    textSize(height/25);

    text(int(resolution), width/1000, height/3.5);
    text(nomPara[paraIndice], width/1000, height/3.1);
    for (int i = 0; i < parametres[paraIndice].length; i ++) {
      if (paraIndice == 4) {
        textSize(height/30);
        text( nf(parametres[paraIndice][i], 0, 3), width/1000+i*2*height/20, height/2.7);
      } else {
        text(int(parametres[paraIndice][i]), width/1000+i*2*height/25, height/2.7);
      }
    }
    textSize(height/40);
    text("J : Commandes", width/1000, height/2.4);
  } else if (menuCommande) {

    //Affiche les commandes
    background(0);
    fill(255);
    textSize(height/(commandes.length));
    for (int i = 0; i < commandes.length; i ++) {
      text(commandes[i][0]+" : "+commandes[i][1], width/1000, (i+1)*height/(commandes.length+1));
    }
  }

  if (tcs && x > width+1) {
    tcs();
  } else if (lin && x > width+1) {
    lin();
  } else if (grossi && x > width+1) {
    grosPoisson();
  }
}

void dessin() {
  if (x <= width+1) {

    int pa = int(x*100/width);
    if (pa > pp) {
      print(pa);
      print(" ; ");
      //if(pa == 5 || pa == 100){
      //  saveFrame("Fractale-"+int(random(10000, 99999))+"-"+str(int(parametres[0][0]))+","+str(int(parametres[0][1]))+","+str(int(parametres[1][0]))+","+str(int(parametres[1][1]))+","+str(int(parametres[2][0]))+","+str(int(parametres[2][1]))+","+str(int(parametres[2][2]))+","+str(int(parametres[3][0]))+","+str(int(parametres[3][1]))+","+str(int(parametres[3][2]))+","+str(int(parametres[4][0]*100))+","+str(int(parametres[4][1]*100))+","+str(int(parametres[5][0]))+".png");
      //  println("Sauvegarde");
      //}
      pp = pa;
    }
    // println(x*100/width);
    Table test = new Table();
    TableRow newRow = test.addRow();
    if(enregistrerEnCSV){
      newRow = fractaleTable.addRow();
    }
    
    for (float y = 0; y < height+1; y += resolution) {
      float tX = xc+(x-width/2.0)*echelle;
      float tY = yc+(y-height/2.0)*echelle;
      if (reverseXY) {
        tX = yc+(y-height/2.0)*echelle;
        tY = xc+(x-width/2.0)*echelle;
      }
      float r = tX;
      float i = tY;
      float s;
      if (Mandel) {
        //s = suiteRecursive(parametres[4][0], parametres[4][1], seuil, 0, r, i);
        s = suiteIterative(parametres[4][0], parametres[4][1], seuil, r, i);
      } else {
        //s = suiteRecursive(r, i, seuil, 0, parametres[4][0], parametres[4][1]);
        s = suiteIterative(r, i, seuil, parametres[4][0], parametres[4][1]);
      }
      float ni = s;
      noStroke();
      if (couleurs > 0) {

        if (couleurs < 3) {
          float ro = 0, ve = 0, bl = 0;
          if (couleurs > 1) {
            if (ni <= iteLimite/2) {
              ro = 255;
              ve = (iteLimite/2-ni)*255/(iteLimite/2);
              bl = 0;
            } else if (ni > iteLimite/2 && ni < iteLimite) {
              ro = iteLimite-ni;
              ve = 0;
              bl = ni - iteLimite/2;
            } else {
              ro = 0;
              ve = 0;
              bl = 0;
            }
          } else {
            ro = 120-120*pow(((ni-IntMin)/iteLimite), 25/maxContraste);//0.4
            ve = 190-190*pow(((ni-IntMin)/iteLimite), 25/maxContraste);//*(ni/iteLimite);
            bl = 220-220*pow(((ni-IntMin)/iteLimite), 25/maxContraste);//*(ni/iteLimite);
          }
          fill(color(ro, ve, bl));
          rect(x, y, resolution, resolution);
        } else if (couleurs == 3) {
          colorMode(HSB);
          float nColor1 = (ni-IntMin)/iteLimite;
          nColor1 = pow(nColor1, 50/maxContraste);
          nColor1 = (sin((nColor1-0.5)*PI)+1)/2;

          nColor1 = (c3Pd+c3sp*nColor1*c3l)%255;
          float xTemp = (ni-IntMin)/iteLimite;
          if (xTemp < c3deltaBN) {
            xTemp /= 5;
          }

          float nColor2 = (pow(xTemp, c3nBN)-pow(xTemp, 1.0/c3nBN))*255;
          float nColor3 = pow(xTemp, c3nBN)*255;
          nColor2 = 255;
          nColor3 = 255;
          //println((ni-IntMin)/iteLimite,nColor1,nColor2,nColor3);
          fill(nColor1, nColor2, nColor3);
          rect(x, y, resolution, resolution);
          colorMode(RGB);
        }
      } else {
        fill((ni-IntMin)*255/maxContraste);
        rect(x, y, resolution, resolution);
      }
      if (int(x/resolution) < int(width/resolution)+1 && int(y/resolution) <int(height/resolution)+1) {
        if (enregistrerEnCSV == false) {
          fractaleF[int(x/resolution)][int(y/resolution)] = (ni-IntMin)/iteLimite;
        } else {
          newRow.setFloat(str(int(y/resolution)), (ni-IntMin)/iteLimite);
        }
      }
    }
    x += resolution;
  }
}