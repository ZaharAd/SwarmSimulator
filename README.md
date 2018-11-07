# Swarm Simulator


This project uses Java Swing to create a visual swarm of drones.<br />
Forked from https://github.com/renatoathaydes/SwarmSimulator for adding screen for each drone <br />
Each screen simulates IR points that the dorne supposed to follow in order to remain in the formation of a swarm<br />
 

Swarms leader drone moves following key presses, and behind it 2 InfraRed points. <br />
The following drone detect using WII sensoe and respond to the movement of 2 IR points identified by its WII camera <br />

*The keys with which the leader moves*<br />
![keys](https://github.com/ZaharAd/SwarmSimulator/blob/master/src/gui/leaderDirection/keyboard.jpg)<br />
* arrow keys: 
  * up - pitch forward 
  * down - pitch backward
  * left - roll left
  * right - roll right <br />
* w -throttle up
* s - throttle down
* a - yaw left
* d - yaw right
* space - stop

Explanations and pictures for illustration can be found in the presentation
https://www.slideshare.net/ZaharAdiniaev/final-presentation-116554137
