// include SPI, MP3 and SD libraries
#include <SPI.h>
#include <Adafruit_VS1053.h>
#include <SD.h>
// These are the pins used for the music maker shield
#define SHIELD_RESET  -1      // VS1053 reset pin (unused!)
#define SHIELD_CS     7      // VS1053 chip select pin (output)
#define SHIELD_DCS    6      // VS1053 Data/command select pin (output)
// These are common pins between breakout and shield
#define CARDCS 4     // Card chip select pin
// DREQ should be an Int pin, see http://arduino.cc/en/Reference/attachInterrupt
#define DREQ 3       // VS1053 Data request, ideally an Interrupt pin
Adafruit_VS1053_FilePlayer musicPlayer = Adafruit_VS1053_FilePlayer(SHIELD_RESET, SHIELD_CS, SHIELD_DCS, DREQ, CARDCS);

char* track = "track_4.mp3";
String inputString = "";         // a string to hold incoming data
boolean stringComplete = false;  // whether the string is complete
boolean startTrack = false;
int numTracks = 3;

void setup() {
  Serial.begin(9600);
//    while(!Serial);
  Serial1.begin(9600);
  while (!Serial1); // needed only for Leonardo
  
  // reserve 200 bytes for the inputString:
  inputString.reserve(200);
  
  
  // initialise the music player
  if (! musicPlayer.begin()) { // initialise the music player
    Serial.println(F("Couldn't find VS1053, do you have the right pins defined?"));
    while (1);
  }
  Serial.println(F("VS1053 found"));
  if (!SD.begin(CARDCS)) {
  Serial.println(F("SD failed, or not present"));
  while (1);  // don't do anything more
  }
  Serial.println("SD OK!");
  
  // Set volume for left, right channels. lower numbers == louder volume!
  musicPlayer.setVolume(40,40);
  if (! musicPlayer.useInterrupt(VS1053_FILEPLAYER_PIN_INT)) {
    Serial.println(F("DREQ pin is not an interrupt pin"));
   }
   Serial.println("Starting...");
 }
 
void loop() {
   // if there's any serial available, read it:
  while (!musicPlayer.playingMusic && Serial1.available() > 0) {
    // look for the next valid integer in the incoming serial stream:
    int input = Serial1.parseInt();

    // look for the newline. That's the end of your
    // sentence:
    
    if(input){
      Serial.println("Input: ");
      Serial.print(input);
      String trackName = "track_" + String(input) + ".mp3";

       Serial.println(trackName);
       trackName.toCharArray(track, 12);
       startTrack = true;
    }
  }
      
  if(!musicPlayer.playingMusic && startTrack){
    Serial.println("Starting track: ");
    Serial.print(track);
    Serial.print("\n");
    // Start playing a file, then we can do stuff while waiting for it to finish
    if (! musicPlayer.startPlayingFile(track)) {
    Serial.println("Could not open file track");
          Serial1.print(0);
    }
    else {
      Serial1.print(1);
    }
    startTrack = false;
  }
}
