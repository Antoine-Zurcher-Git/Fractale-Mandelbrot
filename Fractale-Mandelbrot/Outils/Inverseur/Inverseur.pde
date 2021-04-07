


String nomFichier = "Fractale--2-2-1-1-1-1-0-1-1-1-31-600/";
String typeDebut = "";
String typeFin = "00.png";

fullScreen();

int nImg = 90;
int delta = 2;
for(int i  = 0 ; i <= 180 ; i += delta){
  String num = str(i);
  while(num.length() < 3){
    num = "0"+num;
  }
  PImage img = loadImage(nomFichier+typeDebut+num+typeFin);
  image(img,0,height,width,-height);
  String num2 = str(360-i);
  while(num2.length() < 3){
    num2 = "0"+num;
  }
  saveFrame("bis"+nomFichier+typeDebut+num2+typeFin);
}