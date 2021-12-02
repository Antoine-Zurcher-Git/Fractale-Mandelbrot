


class cpl{
  
  float r,i;
  cpl(float ri,float ii){
    r = ri;
    i = ii;
  }
  
  float norme(){
    return sqrt(r*r+i*i);
  }
  
  float arg(){
    if(r > 0){
      return atan(i/r);
    }else if(r < 0){
      return PI-atan(-i/r);
    }else{
      if(i > 0){
        return PI/2.0;
      }else if(i < 0){
        return -PI/2.0;
      }else{
        return 0;
      }
    }
  }
  
  void aj(cpl z){
    r += z.r;
    i += z.i;
  }
  
  void pro(float a){
    r *= a;
    i *= a;
  }
  
}

cpl cosI(cpl z){
  cpl rep = new cpl(0,0);
  rep.r = cos(z.r)*ch(z.i);
  rep.i = -sin(z.r)*sh(z.i);
  return rep;
}

cpl sinI(cpl z){
  cpl rep = new cpl(0,0);
  rep.r = sin(z.r)*ch(z.i);
  rep.i = cos(z.r)*sh(z.i);
  return rep;
}

cpl invI(cpl z){
  cpl rep = new cpl(0,0);
  float n = z.norme();
  n *= n;
  rep.r = z.r/n;
  rep.i = -z.i/n;
  return rep;
}

cpl shI(cpl z){
  cpl rep = new cpl(0,0);
  rep.r = sh(z.r)*cos(z.i);
  rep.i = ch(z.r)*sin(z.i);
  return rep;
}

cpl chI(cpl z){
  cpl rep = new cpl(0,0);
  rep.r = ch(z.r)*cos(z.i);
  rep.i = sh(z.r)*sin(z.i);
  return rep;
}

cpl prodI(cpl z1,cpl z2){
  cpl rep = new cpl(0,0);
  rep.r = z1.r*z2.r-z1.i*z2.i;
  rep.i = z1.r*z2.i+z1.i*z2.r;
  return rep;
}

cpl puisI(cpl z,float p){
  cpl rep = new cpl(0,0);
  float n = z.norme();
  float arg = z.arg();
  rep.r = pow(n,p)*cos(arg*p);
  rep.i = pow(n,p)*sin(arg*p);
  return rep;
}

cpl expI(cpl z){
  cpl rep = new cpl(0,0);
  rep.r = exp(z.r)*cos(z.i);
  rep.i = exp(z.r)*sin(z.i);
  return rep;
}

cpl lnI(cpl z){
  cpl rep = new cpl(0,0);
  float n = log(z.norme());
  float arg = z.arg();
  rep.r = n;
  rep.i = arg;
  return rep;
}

cpl multI(cpl z,float nb){
  cpl rep = new cpl(0,0);
  rep.r = z.r*nb;
  rep.i = z.i*nb;
  return rep;
}

cpl addI(cpl z1,cpl z2){
  return new cpl(z1.r+z2.r,z1.i+z2.i);
}