#include <Adafruit_GFX.h>   // Core graphics library
#include <RGBmatrixPanel.h> // Hardware-specific library
#include <Arduino.h>
#include <AltSoftSerial.h>

#if defined (__AVR_ATmega168__) || defined (__AVR_ATmega328P__)  
  AltSoftSerial BLEMini;
  AltSoftSerial BLEMin2;  
#else
  #define BLEMini Serial1
  #define BLEMin2 Serial2
#endif
#define CLK 11  // MUST be on PORTB! (Use pin 11 on Mega)
#define LAT 10
#define OE  9
#define A   A0
#define B   A1
#define C   A2
RGBmatrixPanel matrix(A, B, C, CLK, LAT, OE, false);

//Bluetooth
char on  = 2;
char off = 2;
char start = 2;
char last = 2;
char c = 0;
char blockChar = 0;
boolean blocked = false;
boolean reg = false;
boolean start1 = false;
boolean start2 = false;
int count = 3;

//Display
int brightness;
int readPort52;
int readPort53;
int dirX1;
int dirY1;
int posX1;
int posY1;
int dirX2;
int dirY2;
int posX2;
int posY2;
int moveCounter1;
int moveCounter2;
boolean changedDir1;
boolean changedDir2;
boolean dead;
boolean dead1;
boolean dead2;
boolean turnLim1;
boolean turnLim2;
boolean single;
boolean inits = false;
int xPassed1[465];
int yPassed1[465];
int xPassed2[465];
int yPassed2[465];
int pitstopX[30];
int pitstopY[30];
int stops;
int toStop;
int spaces1;
int spaces2;
int velocity;
int velocity2;
int veloc;
int veloc2;

void startAnim(){
  int mod = 3;
 for(int i = 0; i < mod * 4; i++){
  matrix.drawLine(2,2,15,7, matrix.Color333(i % mod, i+1 % mod, i+2 % mod));
 
  matrix.drawLine(16,2,16,7, matrix.Color333(i+1 % mod, i+2 % mod, i+3 % mod));
 
  matrix.drawLine(30,2,17,7, matrix.Color333(i+2 % mod, i+3 % mod, i+4 % mod));
 
  matrix.drawLine(30,8,18,8, matrix.Color333(i+3 % mod, i+4 % mod, i+5 % mod));
 
  matrix.drawLine(30,14,17,9, matrix.Color333(i % mod, i+1 % mod, i+2 % mod));
 
  matrix.drawLine(16,14,16,9, matrix.Color333(i+1 % mod, i+2 % mod, i+3 % mod));
 
  matrix.drawLine(2,14,15,9, matrix.Color333(i+2 % mod, i+3 % mod, i+4 % mod));
 
  matrix.drawLine(2,8,14,8, matrix.Color333(i+3 % mod, i+4 % mod, i+5 % mod)); 
  delay(200);
 }  
}

void foodBreak(){
 matrix.setCursor(8, 0);
 matrix.setTextSize(1);
 matrix.setTextColor(matrix.Color333(brightness,0,0));
 for(int i = 0; i < 10; i++){
   delay(80);
   matrix.fillRect(0,0,32,16, matrix.Color333(0,0,0));
   delay(120);
   matrix.print("EAT");
 }
}
 
 void redraw(int px, int py, int r, int g, int b){
  int drawMax = spaces1;
  if((spaces2 > drawMax) && !single){
   drawMax = spaces2;
  } 
  matrix.fillRect(0, 0, 32, 16, matrix.Color333(0, brightness, 0));
  matrix.fillRect(1, 1, 30, 14, matrix.Color333(0, 0, brightness));
  matrix.drawPixel(px, py, matrix.Color333(r, g, b));
  if(r != brightness){
    matrix.drawPixel(xPassed2[spaces2-1], yPassed2[spaces2-1], matrix.Color333(0, brightness * 2, brightness));  
  } else {
    matrix.drawPixel(xPassed1[spaces1-1], yPassed1[spaces1-1], matrix.Color333(brightness, 0, 0));
  }
  for(int i = 0; i < drawMax; i++){
    if(i % 5 != 0){
      matrix.drawPixel(xPassed1[i+1], yPassed1[i+1], matrix.Color333(brightness, 0, 0));
      if(!single){
      matrix.drawPixel(xPassed2[i+1], yPassed2[i+1], matrix.Color333(0, brightness * 2, brightness));
      }
    } else{
      matrix.drawPixel(xPassed1[i+1], yPassed1[i+1], matrix.Color333(brightness, brightness, brightness));
      if(!single){
      matrix.drawPixel(xPassed2[i+1], yPassed2[i+1], matrix.Color333(brightness, brightness, brightness));
      }
    }
    delay(40);
  }
 }
 
int randX(){
  int randx = 0;
  while(randx == 0){
    int ran = random(1, 31);
    for(int i = 0; i < spaces1; i++){
    if((!(xPassed1[i] == ran && yPassed1[i] == ran) && !((i - 1) % 5 != 0)) && (!(xPassed2[i] == ran && yPassed2[i] == ran) && !((i - 1) % 5 != 0))){
      randx = ran;
    }
    }
  }
  return randx;  
}

int randY(){
  int randy = 0;
  while(randy == 0){
    int ran = random(1, 15);
    for(int i = 0; i < spaces1; i++){
    if((!(xPassed1[i] == ran && yPassed1[i] == ran) && !((i - 1) % 5 != 0)) && (!(xPassed2[i] == ran && yPassed2[i] == ran) && !((i - 1) % 5 != 0))){
      randy = ran;
    }
    }
  }
  return randy;  
}

void placePitstop(){
  int randx = randX();
  int randy = randY();
  pitstopX[stops] = randx;
  pitstopY[stops] = randy;
  matrix.drawPixel(randx, randy, matrix.Color333(0, brightness * 2, brightness));   
  stops++;
}

void pitstop(){
  for(int i = 0; i < stops; i++){
    if((pitstopX[i] == posX1 && pitstopY[i] == posY1)){
       foodBreak();
       redraw(pitstopX[i], pitstopY[i], brightness, 0, 0);
       velocity = 30;
    } 
  }
}


void pitstop2(){
  for(int i = 0; i < stops; i++){
    if((pitstopX[i] == posX2 && pitstopY[i] == posY2)){
      foodBreak();
      redraw(pitstopX[i], pitstopY[i], 0, brightness * 2, brightness); 
      velocity2 = 30;
    } 
  }
}

void turnLeft1(){
  if(dirX1 == 1){
      dirX1 = 0;
      dirY1 = 1;
    } else if(dirY1 == 1){
      dirX1 = -1;
      dirY1 = 0;
    } else if(dirY1 == -1){
      dirX1 = 1;
      dirY1 = 0;
    } else{
      dirX1 = 0;
      dirY1 = -1;
    }
    changedDir1 = false;
}

void turnRight1(){
    if(dirX1 == 1){
      dirX1 = 0;
      dirY1 = -1;
    } else if(dirY1 == 1){
      dirX1 = 1;
      dirY1 = 0;
    } else if(dirY1 == -1){
      dirX1 = -1;
      dirY1 = 0;
    } else{
      dirX1 = 0;
      dirY1 = 1;
    }
    changedDir1 = false;  
}


void turnLeft2(){
  if(dirX2 == 1){
      dirX2 = 0;
      dirY2 = 1;
    } else if(dirY2 == 1){
      dirX2 = -1;
      dirY2 = 0;
    } else if(dirY2 == -1){
      dirX2 = 1;
      dirY2 = 0;
    } else{
      dirX2 = 0;
      dirY2 = -1;
    }
    changedDir2 = false;
}

void turnRight2(){
    if(dirX2 == 1){
      dirX2 = 0;
      dirY2 = -1;
    } else if(dirY2 == 1){
      dirX2 = 1;
      dirY2 = 0;
    } else if(dirY2 == -1){
      dirX2 = -1;
      dirY2 = 0;
    } else{
      dirX2 = 0;
      dirY2 = 1;
    }
    changedDir2 = false;  
}


void crashTest1(){
  for(int i = 0; i < spaces1 + 1; i++){
    if((posX1 == 31 || posX1 == 0 || posY1 == 15 || posY1 == 0) || (xPassed1[i] == posX1 && yPassed1[i] == posY1 && (i - 1) % 5 != 0) || (xPassed2[i] == posX1 && yPassed2[i] == posY1 && (i - 1) % 5 != 0)){
      dead1 = true;
    }
  }  
}

void crashTest2(){
  for(int i = 0; i < spaces2 + 1; i++){
    if((posX2 == 31 || posX2 == 0 || posY2 == 15 || posY2 == 0) || (xPassed1[i] == posX2 && yPassed1[i] == posY2 && (i - 1) % 5 != 0) || (xPassed2[i] == posX2 && yPassed2[i] == posY2 && (i - 1) % 5 != 0)){
      dead2 = true;
    }
  }  
}

void singlep(){
      matrix.fillRect(0, 0, 32, 16, matrix.Color333(0, 0, 0));
      matrix.setCursor(8, 0);
      matrix.setTextSize(1);
      matrix.setTextColor(matrix.Color333(brightness,0,0));
      matrix.print("BIT");
      matrix.setCursor(8, 8);
      matrix.print("BOX");
  boolean loops = true;
  while(loops){
   char s = BLEMini.read();
   if(s == 0x70){
    single = true;
    dead2 = true;
    start2 = true;
    loops = false; 
   } else if(s == 0x71){
    single = false;
    dead2 = false;
    start2 = false;
    loops = false; 
   }
  } 
  inits = true;
}

void initgame(){
  velocity  = 30;
  velocity2 = 30;
  veloc  = 0;
  veloc2 = 0;
  stops = 0;
  toStop = 10;
  dead = true;
  dead1 = false;
  dead2 = false;
  
  brightness = 2;
  start1 = false;
  start2 = false;
  dirX1 = 1;
  dirY1 = 0;
  posX1 = 10;
  posY1 = 10;
  dirX2 = -1;
  dirY2 = 0;
  posX2 = 11;
  posY2 = 12;
  moveCounter1 = 10;
  for(int i = 0; i < 465; i++){
     xPassed1[i] = 0;
     yPassed1[i] = 0;
     xPassed2[i] = 0;
     yPassed2[i] = 0;
  }
  for(int i = 30; i < 30; i++){
     pitstopX[i] = 0;
     pitstopY[i] = 0; 
  }
  turnLim1 = true;
  turnLim2 = true;
  spaces1 = 1;
  spaces2 = 1;
  matrix.fillRect(0, 0, 32, 16, matrix.Color333(0, brightness, 0));
  matrix.fillRect(1, 1, 30, 14, matrix.Color333(0, 0, brightness));
  if(!inits){
  singlep();
  }
  delay(50);
  matrix.fillRect(0, 0, 32, 16, matrix.Color333(0, 0, 0));
  matrix.fillRect(0, 0, 32, 16, matrix.Color333(0, brightness, 0));
  matrix.fillRect(1, 1, 30, 14, matrix.Color333(0, 0, brightness));
  
}


void deadTest(){
  crashTest1();
  crashTest2();
  if(dead1 && (dead2 || single)){ 
      dead = false;
      matrix.fillRect(0, 0, 32, 16, matrix.Color333(0, 0, 0));
      matrix.setCursor(1, 0);
      matrix.setTextSize(1);
      matrix.setTextColor(matrix.Color333(brightness,0,0));
      matrix.print(spaces1);
      if(!single){
      matrix.setCursor(1, 9);   // next line
      matrix.setTextColor(matrix.Color333(0,brightness,brightness));  
      matrix.print(spaces2);
      }
  }  
} 



void bluetooth(){
  char y = BLEMini.read();
  char x = BLEMin2.read();
  if(y == 0x61){
    //BLEMini.write("right ");
    if(turnLim1){
      turnRight1();
    }
    turnLim1 = false; 
  } else if(y == 0x62){
    //BLEMini.write("left ");
    if(turnLim1){
      turnLeft1();
    }
    turnLim1 = false;
  } else if (y == 0x63){
    start1 = true;
  } else if (y == 0x64){
    reg = true;
      if(reg){
  matrix.setTextColor(matrix.Color333(brightness,0,brightness));
  matrix.print('o');
  }
  } 
  if (x == 0x65) {
    if(turnLim2){
      turnRight2();
    }
    turnLim2 = false;
  } else if (x == 0x66) {
    if(turnLim2){
     turnLeft2();
    } 
    turnLim2 = false;
  } else if(x == 0x67){
     start2 = true; 
  } else if (x == 0x68){
    reg = true;
    if(reg){
     matrix.setTextColor(matrix.Color333(brightness,0,brightness));
     matrix.print('o');
    } 
  }
  if(start1 && (start2 || single)){
     initgame(); 
  }
  
  delay(1);
}



void setup() {
  matrix.begin();
  pinMode(53, INPUT);
  pinMode(52, INPUT);
  pinMode(3, OUTPUT);
  digitalWrite(3, LOW);
  digitalWrite(53, HIGH);
  digitalWrite(52, HIGH);
  BLEMini.begin(57600);
  BLEMin2.begin(57600); 
  Serial.begin(57600);
  startAnim();
  initgame();
}

void moveTick1(){
  if(velocity == veloc){
  if(velocity > 15){
  velocity -= 1;
  }
  
  pitstop();
  if(!dead1){
  yPassed1[spaces1] = posY1;
  xPassed1[spaces1] = posX1;    
  posX1 = posX1 + dirX1;
  posY1 = posY1 + dirY1;
  }

  if(!dead1){
    if(spaces1 % 5 != 0){
      matrix.drawPixel(posX1, posY1, matrix.Color333(brightness, 0, 0));
    } else{
      matrix.drawPixel(posX1, posY1, matrix.Color333(0, 0, 0));
      matrix.drawPixel(posX1, posY1, matrix.Color333(brightness, brightness, brightness));
    }
  }
   
   if(!dead1){
    turnLim1 = true;  
    spaces1 ++;
   }
    if(!dead1 && (!dead2 || single)){
       if(toStop < 1){
        toStop = 15;
        placePitstop();
       } else {
        toStop--; 
       }
    }
    veloc = 0;
  }
  veloc++;
}

void moveTick2(){
 if((velocity2 == veloc2) && !single){
  if(velocity2 > 15){
  velocity2 -= 1;  
  }
  pitstop2();
  if(!dead2){
  xPassed2[spaces2] = posX2;
  yPassed2[spaces2] = posY2;    
  posX2 = posX2 + dirX2;
  posY2 = posY2 + dirY2;
  }
 if(!dead2){ 
    if(spaces2 % 5 != 0){
      matrix.drawPixel(posX2, posY2, matrix.Color333(0, brightness * 2, brightness));
    } else {
      matrix.drawPixel(posX2, posY2, matrix.Color333(0, 0, 0));
      matrix.drawPixel(posX2, posY2, matrix.Color333(brightness, brightness, brightness)); 
    }
   }
    if(!dead2){
    turnLim2 = true;  
    spaces2 ++;
    }
   veloc2 = 0; 
 }
 veloc2++;
}

void loop() {
  //readPort52 = digitalRead(52);
  //readPort53 = digitalRead(53);
  if(dead){
  bluetooth();
  
  if(moveCounter1 == 0){
    moveTick1();
    moveTick2();
    moveCounter1 = 10;          
    deadTest();
  }
  moveCounter1 --;
  }
  if(!dead){
    bluetooth();
    
  }
}


