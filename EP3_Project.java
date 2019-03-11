import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class EP3_Project extends PApplet {

String ask;
String inputData = "";

int checkInput = 0;
int checkMass = 0;
int checkVolume = 0;
int dot = 0;

float H;
float Atm = 101325;
float G = 9.81f;
float P;

float x;
float y;
int d;

float totalVolume;
int numOfLiquids = 0;
int num = 1;

float[] mass;
float[] volume;
float[] percent;
float[] density;
float[] top;

public void setup(){
  
}

public void draw(){
 background(250);
  container();
  if(checkInput == 0){
    ask = "How many liquids will be in the container?";
    inputData = ask;
  }
  if(checkInput == 1){
    ask = "What will be the mass of Liquid "+(checkMass+1)+"?";
    inputData = ask;
  }
  if(checkInput == 2){
    ask = "What will be the volume of Liquid "+(checkVolume+1)+"?";
    inputData = ask;
  }
  if(checkInput == 4){
    drawFluid();
    container();
    getPoint();
    fill(255);
    ellipse(x,y,5,5);
    fill(0);
  }
  if(checkInput == 5){
      ask = "Sorry the max amount of liquids is 20";
      inputData = ask;
  }
  textSize(15);
  text(inputData, 450, 75);
  textSize(11);
  for(int i =0; i<numOfLiquids; i = i + 1){
    if(i ==0){
      text("atm = "+Atm+"Pa", 450, 150);
    }
    text("mass "+(i+1)+" = "+mass[i]+"  kg" ,450,175+(25*i));
    text("volume "+(i+1)+" = "+volume[i]+" m^3",650,175+(25*i));  
    text("density "+(i+1)+" = "+density[i]+" kg/m^3",850,175+(25*i));
  }
  
}

public void keyPressed() {
  if (inputData.equals(ask)) {
    if(ask.equals("Sorry the max amount of liquids is 20")){
     checkInput = 1;
    }else{
      checkInput = 3;
    }
    inputData="";
  }
  
  if (key>='0' && key<='9' ) {
    inputData+=key;
    for(int i = 0; i <= inputData.length()-1; i = i+1){
      if(inputData.charAt(i) == '.'){
        dot = 1;
      }
    }
  }else if(key == '.' && numOfLiquids != 0){    
    if(dot < 1){
      inputData+=key;
      dot = 1;
    }
  }else if(key == BACKSPACE && inputData.length() > 0){
    inputData = inputData.substring (0,inputData.length()-1);
    for(int i = 0; i <= inputData.length()-1; i = i+1){
      if(inputData.charAt(i) == '.'){
        dot = dot +1;
      }
    }
    if(dot > 1){
      dot = 1;
    }else{
      dot = 0;
    }    
  }else if (key == ENTER && inputData.length() > 0 && inputData != "."){
    if(inputData.equals(".")){
      inputData = "";
    }
    if(ask.equals("How many liquids will be in the container?") && inputData != "" && inputData != "."){
      if(inputData.equals(ask) == false){
        if(Integer.valueOf(inputData) > 20){          
          numOfLiquids = 20;
          checkInput = 5;
        }else{  
          numOfLiquids = Integer.valueOf(inputData);
        }
        mass = new float[numOfLiquids];
        volume = new float[numOfLiquids];
        density = new float[numOfLiquids];
        percent = new float[numOfLiquids];
        top = new float[numOfLiquids+1];
        top[0]= 150;
        top[numOfLiquids] = 650;
        num = numOfLiquids;
        if(checkInput != 5){
          checkInput = 1;
        }
      }
    }else if(ask.equals("What will be the mass of Liquid "+(checkMass+1)+"?") && inputData != "" && inputData != "."){
      if(inputData.equals(ask) == false && inputData != "."){
        mass[checkMass] = Float.valueOf(inputData);
        checkMass = checkMass + 1;  
        checkInput = 2;
      }
    }else if(ask.equals("What will be the volume of Liquid "+(checkVolume+1)+"?") && inputData!= "" && inputData != "."){
      if(inputData.equals(ask) == false){ 
        volume[checkVolume] = Float.valueOf(inputData);
        totalVolume = totalVolume+volume[checkVolume];
        density[checkVolume] = mass[checkVolume]/volume[checkVolume];
        checkVolume = checkVolume + 1;
        if(checkVolume == numOfLiquids){
          checkInput = 4;
          ask = "  ";
          inputData = "";
          sort();
          for(int i = 0; i< numOfLiquids; i = i+1){
            percent[i] = (volume[i]/totalVolume)*500;
          }
          for(int j= 1; j< numOfLiquids;j = j+1){
            top[j]=top[j-1]+PApplet.parseInt(percent[j-1]);
          }
        }else{
          checkInput = 1;
        }
      }  
    }else if(ask.equals("Sorry the max amount of liquids is 20")){
      checkInput = 1;
    }
  }
}

public void sort(){
 for(int i = (numOfLiquids - 1); i >= 0; i = i-1){
   for(int j = 1; j <= i; j = j+1){
     /*if(density[j-1] == density[j]){
        volume[j-1] = volume[j]+volume[j-1];
        mass[j-1] = mass[j-1]+mass[j]; 
        density[j] = 0;
        volume[j]= 0;
        mass[j] =0;
      }*/
     if(density[j-1]>density[j]){
       float fill1 = density[j];
       float fill2 = mass[j];
       float fill3 = volume[j];       
       density[j] = density[j-1];
       mass[j] = mass[j-1];
       volume[j] = volume[j-1];
       
       density[j-1] = fill1;
       mass[j-1] = fill2;
       volume[j-1] = fill3;
      }
    }
  }
} 

public void container(){
  fill(0);
  rect(40+20,140,10,510);
  rect(350+20,140,10,510);
  rect(40+20,648,320,12);
} 

public void drawFluid(){
  int liquidColor = (175/num);
  for(int i = 0; i < numOfLiquids; i = i+1){
    fill(0,150-(liquidColor*i),200-(liquidColor*i));    
    rect(50+20, top[i], 300, percent[i]);
  }
  for(int i = 0; i < numOfLiquids; i = i+1){
    fill(0);
    int LR;
    if(i%2 == 0){
      LR = 10;
    }else{
      LR = 390;
    }
    if(density[i] != 0){
      text(""+((top[i+1]-top[i])/100)+" m",LR,((top[i+1]-top[i])/2)+top[i]);
    }
  }  
}

public void getPoint(){
  if(mousePressed){
    if((mouseX>=50+20) && (mouseX<=350+20)){
      if((mouseY>=150)&&(mouseY<=650)){
        x = mouseX;
        y = mouseY;        
        H = (y - 150)/100;
      }
    }
  }
  findPressure();
} 

public void findPressure(){
  for(int i = 0; i<numOfLiquids; i = i+1){
     if(y>top[i]){
      d = i;
    }
  }
  P = (G*((y-top[d])/100)*density[d])+Atm;
  if( d != 0){
    for(int i = 0; i<d; i = i+1){
      P = P + (G*((top[i+1] - top[i])/100)*density[i]);   
    }    
  }
  fill(255);
  fill(0);
  if( y != 0){
  text("Depth = "+ H +" m", 40, 700);   
  text("Depth in current liquid = "+((y-top[d])/100)+" m", 40, 720);
  text("Pressure = "+P+" Pa", 40, 740);
  }
}
  public void settings() {  size(1050,800); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#666666", "--stop-color=#cccccc", "EP3_Project" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
