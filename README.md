# Connect4Logic

Four in a row project, was created for a MindSwap project developed by Carla Pereira, João Santos and Luís.
The project was build using Java language in IntelliJ IDE.
The main objective was to build a piece of software where you could run a multiplayer game using multithreading and a TCP protocol.

## Motivation

Four in a row project, was created for a MindSwap project developed by Carla, João and Luís.

## Build Status

Bugs to be solved:
You save the opponents play, when is not your go, if for mistake you insert the collum and save it for the your turn it self;
If someone plays before his turn, the game saves that move and accept it, right after the current player makes his move.


## Code Style
There were applied a Client server pattern were you could run the game itself and interact between the players.

## Screenshots



## How to Use?

Four in a row the game is played with two players. The game starts when you run the server and two players arrive.
The game objective it is to make a line of four(R or Y) in line vertical, horizontal or diagnonal.
When the board it is completed with the 42 plays and there aren't a line that it is draw.

## Packages

## Classes:
### Game
### PlayerHandler
### Server
Initialize the server socket
Wait for a player to connect
Accept the player connection
Create a thread to support the player
Go back to step 2.( Wait for a player to connect)
# Player

