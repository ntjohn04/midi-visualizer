# midi-visualizer

## DESCRIPTION

A JavaFX program that allows the user to select and play a MIDI file from disk. The program features a
piano roll of notes represented by boxes sorted by pitch along the y-axis, and by time along the x-axis.
When the notes strike the action line about a quarter from the left of the program screen, the note will turn from blue to red, and play the note.
Played notes beyond the action line will turn grey. The timing of notes on screen and sounds are coded independently, so
the boxes themselves do not cause the sounds to play. Rather, the timing of all sounds and boxes is determined as
soon as the MIDI file is chosen. Program works best for solo keyboard music.

## CLASSES

### BoxList
An array of Rectangles, including a timer which calls moveBoxes() and detectBounds() every 25 milliseconds.
Constructor takes a single integer parameter, trackSize, which is taken from the MIDI file.
moveBoxes() shifts all non-null Rectangles to the left slightly, giving a smooth rolling appearance.
detectBounds() tests all non-null Rectangles for their position being either before, on, or after the action line.
This function controls the color of each box at all times, as well as setting off-screen played notes to null for garbage collection.

### MidiPlayer
Constructor takes a MIDI file as parameter, and immediatley plays a note upon selection of file. This is because it makes the
playback less buggy at the start, probably due to the audio system needing a moment to allocate memory/processing, although using
a silent audio file here may work just as well. A for loop iterates through each track of MIDI, which can vary for single instruemnt and
multi instrument tracks. The total size is obtained from adding up each track size. The function playFile() is called, which creates the
timings for both the sounds and rectangles. The y-axis position of each note is given by the key, as integers from 0 - 76. The file names
of the sounds are given as 'c4', 'as6', etc, so the note name is obtained by taking mod 12 on the key, then converted to string using an 
enumerated String type with 12 variations ( C - B ). The program then uses a timer to schedule TimerTasks, which create boxes and play sounds
such that they line up in the intended manner. It is set to schedule these tasks at two times the MIDI tick value of each note, although this is
planned to be changeable to allow custom BPM/tempo. The sounds are scheduled to play at the same rate as the creation of boxes, 
however the sounds are given an offset/intercept of 6150 milliseconds to allow the first boxes to reach the action line as soon as the sounds
begin playing. The timer scheduling will also ignore notes with velocity of 0, as in MIDI this is the same as NOTE_OFF, 
which is uneeded because the piano sounds I use decay at the same rate every time. If an instrument is added such as organ, this
will have to be changed. The function playSound() takes a String as a parameter, which is passed by playFile() as 'c4.wav', 'as6.wav', etc,
which gets concatenated with the folder location of the sound files to the beginning of the String. A Clip is used to open an AudioInputStream
for each note, then played with Clip.Start().

### MidiPlayerDriver
Creates the GUI elements and the file chooser dialogue. Once a file has been chosen it will construct a MidiPlayer. The only GUI element
is a stop button, which is needed because just exiting the program does not yet stop the scheduled sound tasks (be warned!). If this is accidentally done, 
the sounds will have to be stopped by closing the java task in Task Manager, or by waiting.

## Video Demonstration
<video src=https://www.youtube.com/watch?v=hNJa1hOPtPI width=180/>

## E-MAIL

ntjohn04@louisville.edu