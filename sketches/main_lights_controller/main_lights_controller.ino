#include <Key.h>
#include <Keypad.h>

#define MultiTreeLeft 29
#define BlueTreeLeft 28
#define Snowflakes 27
#define Whites 26
#define IciclesBack 25
#define IciclesFront 24
#define RedTreeRight 23
#define MultiTreeRight 22

#define SPKROFF 53


const byte numRows= 4; //number of rows on the keypad
const byte numCols= 4; //number of columns on the keypad

//keymap defines the key pressed according to the row and columns just as appears on the keypad
char keymap[numRows][numCols]= 
{
{'1', '2', '3', 'A'}, 
{'4', '5', '6', 'B'}, 
{'7', '8', '9', 'C'},
{'*', '0', '#', 'D'}
};

//Code that shows the the keypad connections to the arduino terminals
byte rowPins[numRows] = {47, 46, 45, 44}; //connect to the row pinouts of the keypad
byte colPins[numCols] = {43, 42, 41, 40}; //connect to the column pinouts of the keypad
Keypad keypad = Keypad( makeKeymap(keymap), rowPins, colPins, numRows, numCols );

int input;
boolean hasInput;
int numChs = 8;
int startDelay = 0;
double lagMod = 1.0;
boolean playMusic = true;
boolean speakersOn = false;

int count = 0; //Counts input digits
int *digits; //Points to array of input digits

String inputString = "";         // a string to hold incoming data

const int chs[] = {MultiTreeLeft, BlueTreeLeft, Snowflakes, Whites, IciclesBack, IciclesFront, RedTreeRight, MultiTreeRight};



void setup() {
  Serial.begin(9600);
  Serial1.begin(9600);
    
  pinMode(SPKROFF, OUTPUT);

  
  for(int i=0; i<numChs; i++){
    pinMode(chs[i], OUTPUT);
}

Serial.print("Setup Complete");
}

void loop() {
  
 //look for input
char keypressed = keypad.getKey();
if (keypressed != NO_KEY)
{
Serial.println(keypressed);
int keyToInt = keypressed - '0';
if(keyToInt > 0 && keyToInt <= numChs){
  toggle(chs[keyToInt-1]);
 }
if(keypressed == 'A'){
//  plaxXmasEve();
}
else if(keypressed == 'B'){
  playFrozen();
}

//else if(keypressed == 'C'){
//  countdown();
//  playPains();
//}
}
 
  // if there's any serial available, read it:
  while (Serial.available() > 0) {

    // look for the next valid integer in the incoming serial stream:
    int input = Serial.parseInt();

    // look for the newline. That's the end of your
    // sentence:
    if (Serial.read() == '\n') {
 if(input != -1 && input <= numChs){
  toggle(chs[input-1]);
 }
    }
}
}


void flashAll(double time){
  for(int i=0; i<numChs; i++){
    digitalWrite(chs[i], HIGH);
  }
  delay(time);
  for(int i=0; i<numChs; i++){
    digitalWrite(chs[i], LOW);
  }
  delay(time);
}


//Methods for blinking LEDs. Paramaters: Count = number of times led should flash; delayTime = How flast led should blink in millis
//void blinkRed(int count, int delayTime) {
//  for (int i=0; i<count; i++) {
//    digitalWrite(redLED, HIGH);
//    delay(delayTime/2);
//    digitalWrite(redLED, LOW);
//    delay(delayTime/2);
//  }
//}

//void blinkYellow(int count, int delayTime){
//  for (int i=0; i<count; i++) {
//    digitalWrite(yellowLED, HIGH);
//    delay(delayTime/2);
//    digitalWrite(yellowLED, LOW);
//    delay(delayTime/2);
//
//  }
//}
//
//void blinkGreen(int count, int delayTime) {
//  for (int i=0; i<count; i++) {
//    digitalWrite(greenLED, HIGH);
//    delay(delayTime/2);
//    digitalWrite(greenLED, LOW);
//    delay(delayTime/2);
//  }
//}

void toggle(int ch){
  digitalWrite(ch, !(digitalRead(ch)));
}

void countdown() {
  Serial.println("Starting...");
  for (int i = 0; i<10; i++){
    Serial.println(10-i);
    delay(1000);
  }
  Serial.println("GO!");
}


void playFrozen() {
  Serial.println("Playing Let It Go");

  if(playMusic){
  Serial1.print(4);
  Serial.println("Song started");
//  while(Serial1.available() <= 0) {
//  }
  int responseInt = Serial1.parseInt();
  Serial.println("Response: ");
  Serial.println(responseInt);
  
  if(speakersOn){
    digitalWrite(SPKROFF, HIGH);
  }
  else {
    digitalWrite(SPKROFF, LOW);
  }
}
delay(0.0 * lagMod);
Serial.println("Cue: 0.0");
digitalWrite(MultiTreeLeft, LOW);
digitalWrite(BlueTreeLeft, LOW);
digitalWrite(Snowflakes, LOW);
digitalWrite(Whites, LOW);
digitalWrite(IciclesBack, LOW);
digitalWrite(IciclesFront, LOW);
digitalWrite(RedTreeRight, LOW);
digitalWrite(MultiTreeRight, LOW);
delay(8180.0 * lagMod);
Serial.println("Cue: 8.18");
digitalWrite(IciclesBack, HIGH);
digitalWrite(IciclesFront, HIGH);
delay(6420.0 * lagMod);
Serial.println("Cue: 14.6");
digitalWrite(IciclesBack, LOW);
digitalWrite(IciclesFront, LOW);
delay(400.0 * lagMod);
Serial.println("Cue: 15.0");
digitalWrite(Snowflakes, HIGH);
digitalWrite(IciclesBack, LOW);
digitalWrite(IciclesFront, LOW);
delay(14230.0 * lagMod);
Serial.println("Cue: 29.23");
digitalWrite(BlueTreeLeft, HIGH);
digitalWrite(RedTreeRight, HIGH);
delay(7170.0 * lagMod);
Serial.println("Cue: 36.4");
digitalWrite(IciclesBack, LOW);
digitalWrite(Snowflakes, LOW);
digitalWrite(IciclesFront, LOW);
digitalWrite(RedTreeRight, HIGH);
delay(6600.0 * lagMod);
Serial.println("Cue: 43.0");
digitalWrite(MultiTreeLeft, HIGH);
digitalWrite(BlueTreeLeft, LOW);
digitalWrite(MultiTreeRight, HIGH);
digitalWrite(RedTreeRight, LOW);
delay(3750.0 * lagMod);
Serial.println("Cue: 46.75");
digitalWrite(MultiTreeLeft, LOW);
digitalWrite(BlueTreeLeft, HIGH);
digitalWrite(MultiTreeRight, LOW);
digitalWrite(RedTreeRight, HIGH);
delay(3550.0 * lagMod);
Serial.println("Cue: 50.3");
digitalWrite(MultiTreeLeft, HIGH);
digitalWrite(BlueTreeLeft, LOW);
digitalWrite(RedTreeRight, LOW);
digitalWrite(MultiTreeRight, HIGH);
delay(10350.0 * lagMod);
Serial.println("Cue: 60.65");
digitalWrite(MultiTreeRight, HIGH);
digitalWrite(MultiTreeLeft, LOW);
digitalWrite(IciclesBack, HIGH);
digitalWrite(BlueTreeLeft, HIGH);
delay(250.0 * lagMod);
digitalWrite(MultiTreeRight, LOW);
delay(250.0 * lagMod);
digitalWrite(MultiTreeRight, HIGH);
delay(250.0 * lagMod);
digitalWrite(MultiTreeRight, LOW);
delay(250.0 * lagMod);
digitalWrite(MultiTreeRight, HIGH);
delay(250.0 * lagMod);
digitalWrite(MultiTreeRight, LOW);
delay(250.0 * lagMod);
digitalWrite(MultiTreeRight, HIGH);
delay(250.0 * lagMod);
digitalWrite(MultiTreeRight, LOW);
delay(250.0 * lagMod);
digitalWrite(MultiTreeRight, HIGH);
delay(250.0 * lagMod);
digitalWrite(MultiTreeRight, LOW);
delay(250.0 * lagMod);
digitalWrite(MultiTreeRight, HIGH);
delay(250.0 * lagMod);
digitalWrite(MultiTreeRight, LOW);
delay(250.0 * lagMod);
digitalWrite(MultiTreeRight, HIGH);
delay(250.0 * lagMod);
digitalWrite(MultiTreeRight, LOW);
delay(250.0 * lagMod);
digitalWrite(MultiTreeRight, HIGH);
delay(250.0 * lagMod);
digitalWrite(MultiTreeRight, LOW);
delay(250.0 * lagMod);
digitalWrite(MultiTreeRight, HIGH);
delay(250.0 * lagMod);
digitalWrite(MultiTreeRight, LOW);
delay(250.0 * lagMod);
digitalWrite(MultiTreeRight, HIGH);
delay(250.0 * lagMod);
digitalWrite(MultiTreeRight, LOW);
delay(250.0 * lagMod);
digitalWrite(MultiTreeRight, HIGH);
delay(250.0 * lagMod);
digitalWrite(MultiTreeRight, LOW);
delay(250.0 * lagMod);
digitalWrite(MultiTreeRight, HIGH);
delay(250.0 * lagMod);
digitalWrite(MultiTreeRight, LOW);
delay(250.0 * lagMod);
digitalWrite(MultiTreeRight, HIGH);
delay(250.0 * lagMod);
digitalWrite(MultiTreeRight, LOW);
delay(250.0 * lagMod);
digitalWrite(MultiTreeRight, HIGH);
delay(250.0 * lagMod);
digitalWrite(MultiTreeRight, LOW);
delay(250.0 * lagMod);
digitalWrite(MultiTreeRight, HIGH);
delay(20.0 * lagMod);
Serial.println("Cue: 67.67");
digitalWrite(MultiTreeRight, LOW);
digitalWrite(IciclesFront, HIGH);
digitalWrite(MultiTreeLeft, HIGH);
digitalWrite(IciclesBack, LOW);
digitalWrite(RedTreeRight, HIGH);
digitalWrite(BlueTreeLeft, LOW);
delay(250.0 * lagMod);
digitalWrite(MultiTreeLeft, LOW);
delay(250.0 * lagMod);
digitalWrite(MultiTreeLeft, HIGH);
delay(250.0 * lagMod);
digitalWrite(MultiTreeLeft, LOW);
delay(250.0 * lagMod);
digitalWrite(MultiTreeLeft, HIGH);
delay(250.0 * lagMod);
digitalWrite(MultiTreeLeft, LOW);
delay(250.0 * lagMod);
digitalWrite(MultiTreeLeft, HIGH);
delay(250.0 * lagMod);
digitalWrite(MultiTreeLeft, LOW);
delay(250.0 * lagMod);
digitalWrite(MultiTreeLeft, HIGH);
delay(250.0 * lagMod);
digitalWrite(MultiTreeLeft, LOW);
delay(250.0 * lagMod);
digitalWrite(MultiTreeLeft, HIGH);
delay(250.0 * lagMod);
digitalWrite(MultiTreeLeft, LOW);
delay(250.0 * lagMod);
digitalWrite(MultiTreeLeft, HIGH);
delay(250.0 * lagMod);
digitalWrite(MultiTreeLeft, LOW);
delay(250.0 * lagMod);
digitalWrite(MultiTreeLeft, HIGH);
delay(250.0 * lagMod);
digitalWrite(MultiTreeLeft, LOW);
delay(250.0 * lagMod);
digitalWrite(MultiTreeLeft, HIGH);
delay(250.0 * lagMod);
digitalWrite(MultiTreeLeft, LOW);
delay(250.0 * lagMod);
digitalWrite(MultiTreeLeft, HIGH);
delay(250.0 * lagMod);
digitalWrite(MultiTreeLeft, LOW);
delay(250.0 * lagMod);
digitalWrite(MultiTreeLeft, HIGH);
delay(250.0 * lagMod);
digitalWrite(MultiTreeLeft, LOW);
delay(250.0 * lagMod);
digitalWrite(MultiTreeLeft, HIGH);
delay(250.0 * lagMod);
digitalWrite(MultiTreeLeft, LOW);
delay(250.0 * lagMod);
digitalWrite(MultiTreeLeft, HIGH);
delay(250.0 * lagMod);
digitalWrite(MultiTreeLeft, LOW);
delay(250.0 * lagMod);
digitalWrite(MultiTreeLeft, HIGH);
delay(250.0 * lagMod);
digitalWrite(MultiTreeLeft, LOW);
delay(250.0 * lagMod);
digitalWrite(MultiTreeLeft, HIGH);
delay(180.0 * lagMod);
Serial.println("Cue: 74.85");
digitalWrite(IciclesBack, LOW);
digitalWrite(BlueTreeLeft, HIGH);
digitalWrite(IciclesFront, LOW);
digitalWrite(MultiTreeLeft, LOW);
digitalWrite(RedTreeRight, LOW);
delay(3420.0 * lagMod);
Serial.println("Cue: 78.27");
digitalWrite(RedTreeRight, HIGH);
delay(7030.0 * lagMod);
Serial.println("Cue: 85.3");
digitalWrite(MultiTreeLeft, HIGH);
digitalWrite(IciclesBack, LOW);
digitalWrite(Whites, HIGH);
digitalWrite(Snowflakes, LOW);
digitalWrite(BlueTreeLeft, LOW);
digitalWrite(MultiTreeRight, HIGH);
digitalWrite(RedTreeRight, LOW);
delay(3570.0 * lagMod);
Serial.println("Cue: 88.87");
digitalWrite(MultiTreeRight, HIGH);
digitalWrite(MultiTreeLeft, HIGH);
digitalWrite(RedTreeRight, LOW);
digitalWrite(IciclesBack, HIGH);
digitalWrite(IciclesFront, HIGH);
delay(400.0 * lagMod);
digitalWrite(MultiTreeRight, LOW);
delay(0.0 * lagMod);
digitalWrite(MultiTreeLeft, LOW);
delay(400.0 * lagMod);
digitalWrite(MultiTreeRight, HIGH);
delay(0.0 * lagMod);
digitalWrite(MultiTreeLeft, HIGH);
delay(400.0 * lagMod);
digitalWrite(MultiTreeRight, LOW);
delay(0.0 * lagMod);
digitalWrite(MultiTreeLeft, LOW);
delay(400.0 * lagMod);
digitalWrite(MultiTreeRight, HIGH);
delay(0.0 * lagMod);
digitalWrite(MultiTreeLeft, HIGH);
delay(400.0 * lagMod);
digitalWrite(MultiTreeRight, LOW);
delay(0.0 * lagMod);
digitalWrite(MultiTreeLeft, LOW);
delay(400.0 * lagMod);
digitalWrite(MultiTreeRight, HIGH);
delay(0.0 * lagMod);
digitalWrite(MultiTreeLeft, HIGH);
delay(400.0 * lagMod);
digitalWrite(MultiTreeRight, LOW);
delay(0.0 * lagMod);
digitalWrite(MultiTreeLeft, LOW);
delay(400.0 * lagMod);
digitalWrite(MultiTreeRight, HIGH);
delay(0.0 * lagMod);
digitalWrite(MultiTreeLeft, HIGH);
delay(400.0 * lagMod);
digitalWrite(MultiTreeRight, LOW);
delay(0.0 * lagMod);
digitalWrite(MultiTreeLeft, LOW);
delay(400.0 * lagMod);
digitalWrite(MultiTreeRight, HIGH);
delay(0.0 * lagMod);
digitalWrite(MultiTreeLeft, HIGH);
delay(400.0 * lagMod);
digitalWrite(MultiTreeRight, LOW);
delay(0.0 * lagMod);
digitalWrite(MultiTreeLeft, LOW);
delay(400.0 * lagMod);
digitalWrite(MultiTreeRight, HIGH);
delay(0.0 * lagMod);
digitalWrite(MultiTreeLeft, HIGH);
delay(400.0 * lagMod);
digitalWrite(MultiTreeRight, LOW);
delay(0.0 * lagMod);
digitalWrite(MultiTreeLeft, LOW);
delay(400.0 * lagMod);
digitalWrite(MultiTreeRight, HIGH);
delay(0.0 * lagMod);
digitalWrite(MultiTreeLeft, HIGH);
delay(400.0 * lagMod);
digitalWrite(MultiTreeRight, LOW);
delay(0.0 * lagMod);
digitalWrite(MultiTreeLeft, LOW);
delay(400.0 * lagMod);
digitalWrite(MultiTreeRight, HIGH);
delay(0.0 * lagMod);
digitalWrite(MultiTreeLeft, HIGH);
delay(400.0 * lagMod);
digitalWrite(MultiTreeRight, LOW);
delay(0.0 * lagMod);
digitalWrite(MultiTreeLeft, LOW);
delay(400.0 * lagMod);
digitalWrite(MultiTreeRight, HIGH);
delay(0.0 * lagMod);
digitalWrite(MultiTreeLeft, HIGH);
delay(400.0 * lagMod);
digitalWrite(MultiTreeRight, LOW);
delay(0.0 * lagMod);
digitalWrite(MultiTreeLeft, LOW);
delay(400.0 * lagMod);
digitalWrite(MultiTreeRight, HIGH);
delay(0.0 * lagMod);
digitalWrite(MultiTreeLeft, HIGH);
delay(400.0 * lagMod);
digitalWrite(MultiTreeRight, LOW);
delay(0.0 * lagMod);
digitalWrite(MultiTreeLeft, LOW);
delay(400.0 * lagMod);
digitalWrite(MultiTreeRight, HIGH);
delay(0.0 * lagMod);
digitalWrite(MultiTreeLeft, HIGH);
delay(400.0 * lagMod);
digitalWrite(MultiTreeRight, LOW);
delay(0.0 * lagMod);
digitalWrite(MultiTreeLeft, LOW);
delay(400.0 * lagMod);
digitalWrite(MultiTreeRight, HIGH);
delay(0.0 * lagMod);
digitalWrite(MultiTreeLeft, HIGH);
delay(400.0 * lagMod);
digitalWrite(MultiTreeRight, LOW);
delay(0.0 * lagMod);
digitalWrite(MultiTreeLeft, LOW);
delay(400.0 * lagMod);
digitalWrite(MultiTreeRight, HIGH);
delay(0.0 * lagMod);
digitalWrite(MultiTreeLeft, HIGH);
delay(180.0 * lagMod);
Serial.println("Cue: 99.45");
digitalWrite(MultiTreeLeft, LOW);
digitalWrite(RedTreeRight, HIGH);
digitalWrite(BlueTreeLeft, HIGH);
digitalWrite(MultiTreeRight, LOW);
delay(400.0 * lagMod);
digitalWrite(RedTreeRight, LOW);
delay(0.0 * lagMod);
digitalWrite(BlueTreeLeft, LOW);
delay(400.0 * lagMod);
digitalWrite(RedTreeRight, HIGH);
delay(0.0 * lagMod);
digitalWrite(BlueTreeLeft, HIGH);
delay(400.0 * lagMod);
digitalWrite(RedTreeRight, LOW);
delay(0.0 * lagMod);
digitalWrite(BlueTreeLeft, LOW);
delay(400.0 * lagMod);
digitalWrite(RedTreeRight, HIGH);
delay(0.0 * lagMod);
digitalWrite(BlueTreeLeft, HIGH);
delay(400.0 * lagMod);
digitalWrite(RedTreeRight, LOW);
delay(0.0 * lagMod);
digitalWrite(BlueTreeLeft, LOW);
delay(400.0 * lagMod);
digitalWrite(RedTreeRight, HIGH);
delay(0.0 * lagMod);
digitalWrite(BlueTreeLeft, HIGH);
delay(400.0 * lagMod);
digitalWrite(RedTreeRight, LOW);
delay(0.0 * lagMod);
digitalWrite(BlueTreeLeft, LOW);
delay(400.0 * lagMod);
digitalWrite(RedTreeRight, HIGH);
delay(0.0 * lagMod);
digitalWrite(BlueTreeLeft, HIGH);
delay(150.0 * lagMod);
Serial.println("Cue: 102.8");
digitalWrite(MultiTreeLeft, HIGH);
digitalWrite(MultiTreeRight, HIGH);
digitalWrite(RedTreeRight, HIGH);
digitalWrite(BlueTreeLeft, HIGH);
digitalWrite(Snowflakes, LOW);
digitalWrite(IciclesBack, LOW);
digitalWrite(IciclesFront, LOW);
delay(3600.0 * lagMod);
Serial.println("Cue: 106.4");
digitalWrite(RedTreeRight, HIGH);
digitalWrite(MultiTreeLeft, LOW);
digitalWrite(Whites, LOW);
digitalWrite(MultiTreeRight, LOW);
digitalWrite(Snowflakes, HIGH);
delay(6980.0 * lagMod);
Serial.println("Cue: 113.38");
digitalWrite(MultiTreeLeft, HIGH);
digitalWrite(MultiTreeRight, HIGH);
delay(520.0 * lagMod);
Serial.println("Cue: 113.9");
digitalWrite(MultiTreeLeft, LOW);
digitalWrite(MultiTreeRight, LOW);
delay(3140.0 * lagMod);
Serial.println("Cue: 117.04");
digitalWrite(IciclesBack, HIGH);
delay(1000.0 * lagMod);
digitalWrite(IciclesBack, LOW);
delay(0.0 * lagMod);
Serial.println("Cue: 118.04");
digitalWrite(IciclesFront, HIGH);
delay(1000.0 * lagMod);
digitalWrite(IciclesBack, HIGH);
delay(0.0 * lagMod);
digitalWrite(IciclesFront, LOW);
delay(1000.0 * lagMod);
digitalWrite(IciclesBack, LOW);
delay(0.0 * lagMod);
digitalWrite(IciclesFront, HIGH);
delay(60.0 * lagMod);
Serial.println("Cue: 120.1");
digitalWrite(RedTreeRight, HIGH);
digitalWrite(BlueTreeLeft, HIGH);
delay(150.0 * lagMod);
digitalWrite(RedTreeRight, LOW);
delay(0.0 * lagMod);
digitalWrite(BlueTreeLeft, LOW);
delay(150.0 * lagMod);
digitalWrite(RedTreeRight, HIGH);
delay(0.0 * lagMod);
digitalWrite(BlueTreeLeft, HIGH);
delay(150.0 * lagMod);
digitalWrite(RedTreeRight, LOW);
delay(0.0 * lagMod);
digitalWrite(BlueTreeLeft, LOW);
delay(150.0 * lagMod);
digitalWrite(RedTreeRight, HIGH);
delay(0.0 * lagMod);
digitalWrite(BlueTreeLeft, HIGH);
delay(150.0 * lagMod);
digitalWrite(RedTreeRight, LOW);
delay(0.0 * lagMod);
digitalWrite(BlueTreeLeft, LOW);
delay(150.0 * lagMod);
digitalWrite(RedTreeRight, HIGH);
delay(0.0 * lagMod);
digitalWrite(BlueTreeLeft, HIGH);
delay(40.0 * lagMod);
digitalWrite(IciclesBack, HIGH);
delay(0.0 * lagMod);
digitalWrite(IciclesFront, LOW);
delay(110.0 * lagMod);
digitalWrite(RedTreeRight, LOW);
delay(0.0 * lagMod);
digitalWrite(BlueTreeLeft, LOW);
delay(150.0 * lagMod);
digitalWrite(RedTreeRight, HIGH);
delay(0.0 * lagMod);
digitalWrite(BlueTreeLeft, HIGH);
delay(150.0 * lagMod);
digitalWrite(RedTreeRight, LOW);
delay(0.0 * lagMod);
digitalWrite(BlueTreeLeft, LOW);
delay(130.0 * lagMod);
Serial.println("Cue: 121.58");
digitalWrite(BlueTreeLeft, LOW);
digitalWrite(RedTreeRight, LOW);
digitalWrite(IciclesBack, LOW);
digitalWrite(IciclesFront, LOW);
digitalWrite(Snowflakes, LOW);
delay(540.0 * lagMod);
Serial.println("Cue: 122.12");
digitalWrite(Whites, HIGH);
digitalWrite(IciclesBack, LOW);
digitalWrite(MultiTreeLeft, HIGH);
digitalWrite(MultiTreeRight, HIGH);
delay(6900.000000000015 * lagMod);
Serial.println("Cue: 129.02");
digitalWrite(BlueTreeLeft, HIGH);
digitalWrite(MultiTreeLeft, LOW);
digitalWrite(RedTreeRight, HIGH);
digitalWrite(MultiTreeRight, LOW);
digitalWrite(Snowflakes, HIGH);
delay(6999.999999999985 * lagMod);
Serial.println("Cue: 136.02");
digitalWrite(Whites, HIGH);
digitalWrite(MultiTreeLeft, HIGH);
digitalWrite(BlueTreeLeft, LOW);
digitalWrite(RedTreeRight, LOW);
digitalWrite(MultiTreeRight, HIGH);
delay(1380.0 * lagMod);
Serial.println("Cue: 137.4");
digitalWrite(Whites, LOW);
digitalWrite(MultiTreeLeft, LOW);
digitalWrite(BlueTreeLeft, HIGH);
digitalWrite(RedTreeRight, HIGH);
digitalWrite(MultiTreeRight, LOW);
digitalWrite(Snowflakes, LOW);
digitalWrite(IciclesBack, HIGH);
digitalWrite(IciclesFront, HIGH);
delay(2440.0 * lagMod);
Serial.println("Cue: 139.84");
digitalWrite(BlueTreeLeft, LOW);
digitalWrite(MultiTreeLeft, HIGH);
digitalWrite(RedTreeRight, LOW);
digitalWrite(MultiTreeRight, HIGH);
digitalWrite(Whites, HIGH);
delay(3180.0 * lagMod);
Serial.println("Cue: 143.02");
digitalWrite(BlueTreeLeft, HIGH);
digitalWrite(Snowflakes, HIGH);
digitalWrite(MultiTreeLeft, LOW);
digitalWrite(MultiTreeRight, LOW);
digitalWrite(RedTreeRight, HIGH);
delay(3520.0 * lagMod);
Serial.println("Cue: 146.54");
digitalWrite(Snowflakes, HIGH);
digitalWrite(BlueTreeLeft, LOW);
digitalWrite(MultiTreeLeft, LOW);
digitalWrite(IciclesBack, LOW);
digitalWrite(IciclesFront, LOW);
digitalWrite(Whites, LOW);
digitalWrite(RedTreeRight, LOW);
delay(3460.0 * lagMod);
Serial.println("Cue: 150.0");
digitalWrite(BlueTreeLeft, HIGH);
delay(100.0 * lagMod);
digitalWrite(BlueTreeLeft, LOW);
delay(0.0 * lagMod);
Serial.println("Cue: 150.1");
digitalWrite(RedTreeRight, HIGH);
delay(100.0 * lagMod);
digitalWrite(BlueTreeLeft, HIGH);
delay(0.0 * lagMod);
digitalWrite(RedTreeRight, LOW);
delay(100.0 * lagMod);
digitalWrite(BlueTreeLeft, LOW);
delay(0.0 * lagMod);
digitalWrite(RedTreeRight, HIGH);
delay(100.0 * lagMod);
digitalWrite(BlueTreeLeft, HIGH);
delay(0.0 * lagMod);
digitalWrite(RedTreeRight, LOW);
delay(100.0 * lagMod);
digitalWrite(BlueTreeLeft, LOW);
delay(0.0 * lagMod);
digitalWrite(RedTreeRight, HIGH);
delay(100.0 * lagMod);
digitalWrite(BlueTreeLeft, HIGH);
delay(0.0 * lagMod);
digitalWrite(RedTreeRight, LOW);
delay(100.0 * lagMod);
digitalWrite(BlueTreeLeft, LOW);
delay(0.0 * lagMod);
digitalWrite(RedTreeRight, HIGH);
delay(100.0 * lagMod);
digitalWrite(BlueTreeLeft, HIGH);
delay(0.0 * lagMod);
digitalWrite(RedTreeRight, LOW);
delay(100.0 * lagMod);
digitalWrite(BlueTreeLeft, LOW);
delay(0.0 * lagMod);
digitalWrite(RedTreeRight, HIGH);
delay(100.0 * lagMod);
digitalWrite(BlueTreeLeft, HIGH);
delay(0.0 * lagMod);
digitalWrite(RedTreeRight, LOW);
delay(100.0 * lagMod);
digitalWrite(BlueTreeLeft, LOW);
delay(0.0 * lagMod);
digitalWrite(RedTreeRight, HIGH);
delay(100.0 * lagMod);
digitalWrite(BlueTreeLeft, HIGH);
delay(0.0 * lagMod);
digitalWrite(RedTreeRight, LOW);
delay(100.0 * lagMod);
digitalWrite(BlueTreeLeft, LOW);
delay(0.0 * lagMod);
digitalWrite(RedTreeRight, HIGH);
delay(100.0 * lagMod);
digitalWrite(BlueTreeLeft, HIGH);
delay(0.0 * lagMod);
digitalWrite(RedTreeRight, LOW);
delay(100.0 * lagMod);
digitalWrite(BlueTreeLeft, LOW);
delay(0.0 * lagMod);
digitalWrite(RedTreeRight, HIGH);
delay(100.0 * lagMod);
digitalWrite(BlueTreeLeft, HIGH);
delay(0.0 * lagMod);
digitalWrite(RedTreeRight, LOW);
delay(100.0 * lagMod);
digitalWrite(BlueTreeLeft, LOW);
delay(0.0 * lagMod);
digitalWrite(RedTreeRight, HIGH);
delay(100.0 * lagMod);
digitalWrite(BlueTreeLeft, HIGH);
delay(0.0 * lagMod);
digitalWrite(RedTreeRight, LOW);
delay(100.0 * lagMod);
digitalWrite(BlueTreeLeft, LOW);
delay(0.0 * lagMod);
digitalWrite(RedTreeRight, HIGH);
delay(100.0 * lagMod);
digitalWrite(BlueTreeLeft, HIGH);
delay(0.0 * lagMod);
digitalWrite(RedTreeRight, LOW);
delay(100.0 * lagMod);
digitalWrite(BlueTreeLeft, LOW);
delay(0.0 * lagMod);
digitalWrite(RedTreeRight, HIGH);
delay(100.0 * lagMod);
digitalWrite(BlueTreeLeft, HIGH);
delay(0.0 * lagMod);
digitalWrite(RedTreeRight, LOW);
delay(100.0 * lagMod);
digitalWrite(BlueTreeLeft, LOW);
delay(0.0 * lagMod);
digitalWrite(RedTreeRight, HIGH);
delay(100.0 * lagMod);
digitalWrite(BlueTreeLeft, HIGH);
delay(0.0 * lagMod);
digitalWrite(RedTreeRight, LOW);
delay(100.0 * lagMod);
digitalWrite(BlueTreeLeft, LOW);
delay(0.0 * lagMod);
digitalWrite(RedTreeRight, HIGH);
delay(100.0 * lagMod);
digitalWrite(BlueTreeLeft, HIGH);
delay(0.0 * lagMod);
digitalWrite(RedTreeRight, LOW);
delay(100.0 * lagMod);
digitalWrite(BlueTreeLeft, LOW);
delay(0.0 * lagMod);
digitalWrite(RedTreeRight, HIGH);
delay(100.0 * lagMod);
digitalWrite(BlueTreeLeft, HIGH);
delay(0.0 * lagMod);
digitalWrite(RedTreeRight, LOW);
delay(100.0 * lagMod);
digitalWrite(BlueTreeLeft, LOW);
delay(0.0 * lagMod);
digitalWrite(RedTreeRight, HIGH);
delay(100.0 * lagMod);
digitalWrite(BlueTreeLeft, HIGH);
delay(0.0 * lagMod);
digitalWrite(RedTreeRight, LOW);
delay(100.0 * lagMod);
digitalWrite(BlueTreeLeft, LOW);
delay(0.0 * lagMod);
digitalWrite(RedTreeRight, HIGH);
delay(100.0 * lagMod);
digitalWrite(BlueTreeLeft, HIGH);
delay(0.0 * lagMod);
digitalWrite(RedTreeRight, LOW);
delay(100.0 * lagMod);
digitalWrite(BlueTreeLeft, LOW);
delay(0.0 * lagMod);
digitalWrite(RedTreeRight, HIGH);
delay(100.0 * lagMod);
digitalWrite(BlueTreeLeft, HIGH);
delay(0.0 * lagMod);
digitalWrite(RedTreeRight, LOW);
delay(100.0 * lagMod);
digitalWrite(BlueTreeLeft, LOW);
delay(0.0 * lagMod);
digitalWrite(RedTreeRight, HIGH);
delay(100.0 * lagMod);
digitalWrite(BlueTreeLeft, HIGH);
delay(0.0 * lagMod);
digitalWrite(RedTreeRight, LOW);
delay(100.0 * lagMod);
digitalWrite(BlueTreeLeft, LOW);
delay(0.0 * lagMod);
digitalWrite(RedTreeRight, HIGH);
delay(30.0 * lagMod);
Serial.println("Cue: 153.73");
digitalWrite(BlueTreeLeft, HIGH);
digitalWrite(RedTreeRight, HIGH);
digitalWrite(IciclesBack, HIGH);
digitalWrite(IciclesFront, HIGH);
delay(1070.0 * lagMod);
Serial.println("Cue: 154.8");
digitalWrite(IciclesBack, LOW);
digitalWrite(IciclesFront, LOW);
delay(5600.0 * lagMod);
Serial.println("Cue: 160.4");
digitalWrite(IciclesBack, HIGH);
digitalWrite(IciclesFront, HIGH);
delay(1100.0 * lagMod);
Serial.println("Cue: 161.5");
digitalWrite(IciclesBack, LOW);
digitalWrite(IciclesFront, LOW);
delay(6000.0 * lagMod);
Serial.println("Cue: 167.5");
digitalWrite(IciclesBack, HIGH);
digitalWrite(IciclesFront, HIGH);
digitalWrite(Whites, HIGH);
delay(700.0 * lagMod);
Serial.println("Cue: 168.2");
digitalWrite(Whites, LOW);
delay(500.0 * lagMod);
Serial.println("Cue: 168.7");
digitalWrite(IciclesBack, LOW);
digitalWrite(IciclesFront, LOW);
delay(5920.0 * lagMod);
Serial.println("Cue: 174.62");
digitalWrite(Whites, LOW);
digitalWrite(MultiTreeLeft, HIGH);
digitalWrite(Snowflakes, LOW);
digitalWrite(BlueTreeLeft, LOW);
digitalWrite(MultiTreeRight, HIGH);
digitalWrite(RedTreeRight, LOW);
delay(3780.0 * lagMod);
Serial.println("Cue: 178.4");
digitalWrite(IciclesBack, HIGH);
delay(800.0 * lagMod);
Serial.println("Cue: 179.2");
digitalWrite(IciclesFront, HIGH);
delay(200.0 * lagMod);
digitalWrite(IciclesBack, LOW);
delay(800.0 * lagMod);
digitalWrite(IciclesFront, LOW);
delay(200.0 * lagMod);
digitalWrite(IciclesBack, HIGH);
delay(800.0 * lagMod);
digitalWrite(IciclesFront, HIGH);
delay(200.0 * lagMod);
digitalWrite(IciclesBack, LOW);
delay(0.0 * lagMod);
Serial.println("Cue: 181.4");
digitalWrite(MultiTreeLeft, HIGH);
digitalWrite(IciclesBack, LOW);
digitalWrite(IciclesFront, LOW);
digitalWrite(MultiTreeRight, HIGH);
digitalWrite(BlueTreeLeft, HIGH);
digitalWrite(RedTreeRight, HIGH);
delay(100.0 * lagMod);
digitalWrite(MultiTreeLeft, LOW);
delay(0.0 * lagMod);
digitalWrite(MultiTreeRight, LOW);
delay(100.0 * lagMod);
digitalWrite(MultiTreeLeft, HIGH);
delay(0.0 * lagMod);
digitalWrite(MultiTreeRight, HIGH);
delay(100.0 * lagMod);
digitalWrite(MultiTreeLeft, LOW);
delay(0.0 * lagMod);
digitalWrite(MultiTreeRight, LOW);
delay(100.0 * lagMod);
digitalWrite(MultiTreeLeft, HIGH);
delay(0.0 * lagMod);
digitalWrite(MultiTreeRight, HIGH);
delay(100.0 * lagMod);
digitalWrite(MultiTreeLeft, LOW);
delay(0.0 * lagMod);
digitalWrite(MultiTreeRight, LOW);
delay(100.0 * lagMod);
digitalWrite(MultiTreeLeft, HIGH);
delay(0.0 * lagMod);
digitalWrite(MultiTreeRight, HIGH);
delay(100.0 * lagMod);
digitalWrite(MultiTreeLeft, LOW);
delay(0.0 * lagMod);
digitalWrite(MultiTreeRight, LOW);
delay(100.0 * lagMod);
digitalWrite(MultiTreeLeft, HIGH);
delay(0.0 * lagMod);
digitalWrite(MultiTreeRight, HIGH);
delay(100.0 * lagMod);
digitalWrite(MultiTreeLeft, LOW);
delay(0.0 * lagMod);
digitalWrite(MultiTreeRight, LOW);
delay(100.0 * lagMod);
digitalWrite(MultiTreeLeft, HIGH);
delay(0.0 * lagMod);
digitalWrite(MultiTreeRight, HIGH);
delay(100.0 * lagMod);
digitalWrite(MultiTreeLeft, LOW);
delay(0.0 * lagMod);
digitalWrite(MultiTreeRight, LOW);
delay(100.0 * lagMod);
digitalWrite(MultiTreeLeft, HIGH);
delay(0.0 * lagMod);
digitalWrite(MultiTreeRight, HIGH);
delay(0.0 * lagMod);
Serial.println("Cue: 182.6");
digitalWrite(MultiTreeLeft, LOW);
digitalWrite(BlueTreeLeft, LOW);
digitalWrite(Snowflakes, LOW);
digitalWrite(Whites, LOW);
digitalWrite(IciclesBack, LOW);
digitalWrite(MultiTreeRight, LOW);
digitalWrite(RedTreeRight, LOW);
delay(800.0 * lagMod);
Serial.println("Cue: 183.4");
digitalWrite(Whites, HIGH);
digitalWrite(MultiTreeRight, HIGH);
digitalWrite(MultiTreeLeft, HIGH);
digitalWrite(Snowflakes, HIGH);
digitalWrite(IciclesBack, HIGH);
digitalWrite(IciclesFront, HIGH);
delay(3350.0 * lagMod);
Serial.println("Cue: 186.75");
digitalWrite(MultiTreeLeft, LOW);
digitalWrite(MultiTreeRight, LOW);
delay(1240.0 * lagMod);
Serial.println("Cue: 187.99");
digitalWrite(MultiTreeLeft, HIGH);
digitalWrite(MultiTreeRight, HIGH);
delay(2060.0 * lagMod);
Serial.println("Cue: 190.05");
digitalWrite(MultiTreeLeft, LOW);
delay(70.0 * lagMod);
Serial.println("Cue: 190.12");
digitalWrite(BlueTreeLeft, HIGH);
digitalWrite(Snowflakes, HIGH);
digitalWrite(MultiTreeRight, LOW);
digitalWrite(RedTreeRight, HIGH);
digitalWrite(IciclesBack, LOW);
digitalWrite(IciclesFront, LOW);
delay(3990.0 * lagMod);
Serial.println("Cue: 194.11");
digitalWrite(MultiTreeLeft, HIGH);
digitalWrite(BlueTreeLeft, LOW);
digitalWrite(RedTreeRight, LOW);
digitalWrite(MultiTreeRight, HIGH);
delay(1880.0 * lagMod);
Serial.println("Cue: 195.99");
digitalWrite(Whites, LOW);
delay(1190.0 * lagMod);
Serial.println("Cue: 197.18");
digitalWrite(MultiTreeLeft, LOW);
digitalWrite(BlueTreeLeft, HIGH);
digitalWrite(IciclesBack, HIGH);
digitalWrite(Snowflakes, LOW);
digitalWrite(MultiTreeRight, LOW);
digitalWrite(RedTreeRight, HIGH);
digitalWrite(IciclesFront, HIGH);
delay(1020.0 * lagMod);
Serial.println("Cue: 198.2");
digitalWrite(IciclesBack, LOW);
digitalWrite(IciclesFront, LOW);
delay(450.0 * lagMod);
Serial.println("Cue: 198.65");
digitalWrite(MultiTreeLeft, HIGH);
digitalWrite(BlueTreeLeft, LOW);
digitalWrite(IciclesBack, LOW);
digitalWrite(IciclesFront, LOW);
digitalWrite(MultiTreeRight, HIGH);
digitalWrite(RedTreeRight, LOW);
delay(2180.0 * lagMod);
Serial.println("Cue: 200.83");
digitalWrite(MultiTreeLeft, LOW);
digitalWrite(BlueTreeLeft, HIGH);
digitalWrite(Whites, HIGH);
digitalWrite(RedTreeRight, HIGH);
digitalWrite(MultiTreeRight, LOW);
digitalWrite(IciclesBack, HIGH);
digitalWrite(IciclesFront, HIGH);
delay(1000.0 * lagMod);
Serial.println("Cue: 201.83");
digitalWrite(IciclesBack, LOW);
digitalWrite(IciclesFront, LOW);
delay(630.0 * lagMod);
Serial.println("Cue: 202.46");
digitalWrite(MultiTreeLeft, HIGH);
digitalWrite(BlueTreeLeft, LOW);
digitalWrite(IciclesBack, LOW);
digitalWrite(IciclesFront, LOW);
digitalWrite(RedTreeRight, LOW);
digitalWrite(MultiTreeRight, HIGH);
delay(1800.0 * lagMod);
Serial.println("Cue: 204.26");
digitalWrite(MultiTreeLeft, LOW);
digitalWrite(BlueTreeLeft, HIGH);
digitalWrite(IciclesBack, HIGH);
digitalWrite(MultiTreeRight, HIGH);
delay(760.0 * lagMod);
Serial.println("Cue: 205.02");
digitalWrite(MultiTreeLeft, HIGH);
digitalWrite(BlueTreeLeft, LOW);
digitalWrite(IciclesFront, HIGH);
digitalWrite(RedTreeRight, HIGH);
digitalWrite(MultiTreeRight, LOW);
delay(580.0 * lagMod);
Serial.println("Cue: 205.6");
digitalWrite(BlueTreeLeft, HIGH);
delay(450.0 * lagMod);
Serial.println("Cue: 206.05");
digitalWrite(MultiTreeLeft, HIGH);
digitalWrite(Whites, HIGH);
digitalWrite(MultiTreeRight, HIGH);
digitalWrite(Snowflakes, HIGH);
delay(270.0 * lagMod);
digitalWrite(MultiTreeLeft, LOW);
delay(0.0 * lagMod);
digitalWrite(MultiTreeRight, LOW);
delay(270.0 * lagMod);
digitalWrite(MultiTreeLeft, HIGH);
delay(0.0 * lagMod);
digitalWrite(MultiTreeRight, HIGH);
delay(270.0 * lagMod);
digitalWrite(MultiTreeLeft, LOW);
delay(0.0 * lagMod);
digitalWrite(MultiTreeRight, LOW);
delay(270.0 * lagMod);
digitalWrite(MultiTreeLeft, HIGH);
delay(0.0 * lagMod);
digitalWrite(MultiTreeRight, HIGH);
delay(270.0 * lagMod);
digitalWrite(MultiTreeLeft, LOW);
delay(0.0 * lagMod);
digitalWrite(MultiTreeRight, LOW);
delay(270.0 * lagMod);
digitalWrite(MultiTreeLeft, HIGH);
delay(0.0 * lagMod);
digitalWrite(MultiTreeRight, HIGH);
delay(270.0 * lagMod);
digitalWrite(MultiTreeLeft, LOW);
delay(0.0 * lagMod);
digitalWrite(MultiTreeRight, LOW);
delay(270.0 * lagMod);
digitalWrite(MultiTreeLeft, HIGH);
delay(0.0 * lagMod);
digitalWrite(MultiTreeRight, HIGH);
delay(270.0 * lagMod);
digitalWrite(MultiTreeLeft, LOW);
delay(0.0 * lagMod);
digitalWrite(MultiTreeRight, LOW);
delay(270.0 * lagMod);
digitalWrite(MultiTreeLeft, HIGH);
delay(0.0 * lagMod);
digitalWrite(MultiTreeRight, HIGH);
delay(270.0 * lagMod);
digitalWrite(MultiTreeLeft, LOW);
delay(0.0 * lagMod);
digitalWrite(MultiTreeRight, LOW);
delay(270.0 * lagMod);
digitalWrite(MultiTreeLeft, HIGH);
delay(0.0 * lagMod);
digitalWrite(MultiTreeRight, HIGH);
delay(270.0 * lagMod);
digitalWrite(MultiTreeLeft, LOW);
delay(0.0 * lagMod);
digitalWrite(MultiTreeRight, LOW);
delay(210.0 * lagMod);
Serial.println("Cue: 209.77");
digitalWrite(Whites, HIGH);
digitalWrite(RedTreeRight, HIGH);
digitalWrite(BlueTreeLeft, HIGH);
digitalWrite(MultiTreeRight, HIGH);
digitalWrite(Snowflakes, HIGH);
digitalWrite(MultiTreeLeft, HIGH);
digitalWrite(IciclesBack, LOW);
digitalWrite(IciclesFront, LOW);
delay(100.0 * lagMod);
digitalWrite(Whites, LOW);
delay(0.0 * lagMod);
digitalWrite(MultiTreeRight, LOW);
delay(0.0 * lagMod);
digitalWrite(MultiTreeLeft, LOW);
delay(100.0 * lagMod);
digitalWrite(Whites, HIGH);
delay(0.0 * lagMod);
digitalWrite(MultiTreeRight, HIGH);
delay(0.0 * lagMod);
digitalWrite(MultiTreeLeft, HIGH);
delay(100.0 * lagMod);
digitalWrite(Whites, LOW);
delay(0.0 * lagMod);
digitalWrite(MultiTreeRight, LOW);
delay(0.0 * lagMod);
digitalWrite(MultiTreeLeft, LOW);
delay(100.0 * lagMod);
digitalWrite(Whites, HIGH);
delay(0.0 * lagMod);
digitalWrite(MultiTreeRight, HIGH);
delay(0.0 * lagMod);
digitalWrite(MultiTreeLeft, HIGH);
delay(100.0 * lagMod);
digitalWrite(Whites, LOW);
delay(0.0 * lagMod);
digitalWrite(MultiTreeRight, LOW);
delay(0.0 * lagMod);
digitalWrite(MultiTreeLeft, LOW);
delay(100.0 * lagMod);
digitalWrite(Whites, HIGH);
delay(0.0 * lagMod);
digitalWrite(MultiTreeRight, HIGH);
delay(0.0 * lagMod);
digitalWrite(MultiTreeLeft, HIGH);
delay(100.0 * lagMod);
digitalWrite(Whites, LOW);
delay(0.0 * lagMod);
digitalWrite(MultiTreeRight, LOW);
delay(0.0 * lagMod);
digitalWrite(MultiTreeLeft, LOW);
delay(100.0 * lagMod);
digitalWrite(Whites, HIGH);
delay(0.0 * lagMod);
digitalWrite(MultiTreeRight, HIGH);
delay(0.0 * lagMod);
digitalWrite(MultiTreeLeft, HIGH);
delay(100.0 * lagMod);
digitalWrite(Whites, LOW);
delay(0.0 * lagMod);
digitalWrite(MultiTreeRight, LOW);
delay(0.0 * lagMod);
digitalWrite(MultiTreeLeft, LOW);
delay(100.0 * lagMod);
digitalWrite(Whites, HIGH);
delay(0.0 * lagMod);
digitalWrite(MultiTreeRight, HIGH);
delay(0.0 * lagMod);
digitalWrite(MultiTreeLeft, HIGH);
delay(100.0 * lagMod);
digitalWrite(Whites, LOW);
delay(0.0 * lagMod);
digitalWrite(MultiTreeRight, LOW);
delay(0.0 * lagMod);
digitalWrite(MultiTreeLeft, LOW);
delay(100.0 * lagMod);
digitalWrite(Whites, HIGH);
delay(0.0 * lagMod);
digitalWrite(MultiTreeRight, HIGH);
delay(0.0 * lagMod);
digitalWrite(MultiTreeLeft, HIGH);
delay(100.0 * lagMod);
digitalWrite(Whites, LOW);
delay(0.0 * lagMod);
digitalWrite(MultiTreeRight, LOW);
delay(0.0 * lagMod);
digitalWrite(MultiTreeLeft, LOW);
delay(100.0 * lagMod);
digitalWrite(Whites, HIGH);
delay(0.0 * lagMod);
digitalWrite(MultiTreeRight, HIGH);
delay(0.0 * lagMod);
digitalWrite(MultiTreeLeft, HIGH);
delay(100.0 * lagMod);
digitalWrite(Whites, LOW);
delay(0.0 * lagMod);
digitalWrite(MultiTreeRight, LOW);
delay(0.0 * lagMod);
digitalWrite(MultiTreeLeft, LOW);
delay(70.0 * lagMod);
Serial.println("Cue: 211.34");
digitalWrite(Snowflakes, HIGH);
digitalWrite(IciclesBack, HIGH);
digitalWrite(Whites, LOW);
digitalWrite(RedTreeRight, LOW);
digitalWrite(MultiTreeRight, LOW);
digitalWrite(IciclesFront, HIGH);
digitalWrite(BlueTreeLeft, LOW);
digitalWrite(MultiTreeLeft, LOW);
delay(3780.0 * lagMod);
Serial.println("Cue: 215.12");
digitalWrite(MultiTreeLeft, LOW);
digitalWrite(BlueTreeLeft, LOW);
digitalWrite(Snowflakes, LOW);
digitalWrite(Whites, LOW);
delay(3880.0 * lagMod);
Serial.println("Cue: 219.0");
digitalWrite(IciclesBack, LOW);
digitalWrite(IciclesFront, LOW);
}
