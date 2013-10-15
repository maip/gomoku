gomoku
======
Gomoku is a game played on a 15x15 grid with two players. The goal is to get 5 pieces in a row in any direction.

This is a GUI game implemented in Java.
Click the mouse to place a piece on the grid.
Press the Enter key to reset the game.

To play, type the following commands in command line:
javac *.java
java Gomoku

Or to create a jar file:
javac *.java
echo Main-Class: Gomoku >manifest.txt
jar cvfm Gomoku.jar manifest.txt *.class