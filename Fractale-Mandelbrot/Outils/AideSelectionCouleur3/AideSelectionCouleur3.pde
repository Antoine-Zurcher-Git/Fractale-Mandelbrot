
float c3Pd = 170;//44;
float c3Pa = 40;//190;
float c3sp=-1;
float c3l = 255*(1-int(c3sp+0.1))+c3sp*(c3Pa-c3Pd+255)%255;

float c3nBN = 1.0/40;
float c3deltaBN = 7/255.0;

void setup() {
  size(700, 700);
  colorMode(HSB, 255, 100, 100);
}

float c3precision = 2;
float c3prec2 = 350;
int c3c= 0;
float maxContraste = 50;
void draw() {

  println(mouseX/c3precision);
  //println((mouseX-100+255)%255);
  for (float x= 0; x<255; x+= 1.0/c3precision) {
    stroke(x, 255, 255);
    line(x*c3precision, 0, x*c3precision, height);
  }
  noStroke();
  fill(mouseX/c3precision, 255*c3precision, 255*c3precision);
  rect(255*c3precision, 0, 700-255*c3precision, 700);
  
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
    line(255*c3precision, i*c3prec2, width, i*c3prec2);
  }
  println("Depart : "+c3Pd+" Arrivée : "+c3Pa+" Sens : "+c3sp);
}

void mouseClicked(){
  if(mouseButton == LEFT){
    c3Pd = mouseX/c3precision;
  }else if(mouseButton == RIGHT){
    c3Pa = mouseX/c3precision;
  }else if(mouseButton == 3){
    c3sp*=-1;
  }
  c3l = 255*(1-int(c3sp+0.1))+c3sp*(c3Pa-c3Pd+255)%255;
  
}