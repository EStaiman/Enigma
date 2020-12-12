# Enigma
A working enigma machine in Java in command line.

This was developed 3 years ago as my first independent project in Java. I recently found it on an old drive, and I decided to clean the code up and implement a switchboard/plugboard. This has been tested with enigma simulators online and works. There are 5 possible rings, 3 possible reflectors, and a fully configurable switchboard. It can encrypt whole words at a time and will output to a text file, called enigma.txt by default. Since enigma is bidirectional, you can also decrypt with it. Keep in mind that every letter entered changes the wiring, so to decode, you will have to restart the program and use the same settings as before.
