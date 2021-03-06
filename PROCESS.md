# day 2
Today I followed several guides to try to implement peer2peer connection between the tablet and the phone. This was quite a challenge, but in the end I managed to get a list of available devices.

# day 3
Today I tried to make an app that runs on the tablet of Pepper. I 'robotified' a new application (meaning it is compatible with the robot SDK's), but the tablet of the robot was not connectable. I searched all fora on this issue, but I couldn't fix it. I'm in the process of posting a question on the softbank forums, but my account still has to be approved. In the meantime I will create a direct connection to the Pepper robot, without use of the tablet.

# day 4
Instead of making two apps that communicate (one phone and one tablet on Pepper), I decided to connect to the Pepper robot directly instead. This is still possible with WiFiDirect (as far as I know at this point). Today I followed tutorials on how sending stuff over the network works, and got a python script that sets up a basic server with a socket that can listen to a specific port. I don't think it can be found by ServiceDiscovery yet, so I have to add that still. The android side can already discover peers, and connect when the right one is found.

# day 5
Eureka day! Today I managed to get ServiceDiscovery working. With that, I can create a correct socket that binds to the right host and port. With this connection I can now send strings from phone (android) to the server, running in python on the robot. Sadly I had to take the "easy" way out (for now), and drop the idea of WiFiDirect. This connection goes through a hotspot, which takes away the part of connecting the devices in code. I will implement this after the core functionality is added. Furthermore, I can interpret the sent strings on the robot, and let pepper speak. 

[INSERT PICTURES FROM PHONE]

# weekend
In the weekend I fixed the icon of my app and added some very basic design. I also fixed numerous bugs in the connection.

# day 6
Today I connected the talk fragment in the app to the back-end. The user can now type anything and let the robot say it. I also improved connection stability, and fixed bugs regarding the different cycle frequencies of the server and the client.

# day 7
Today I connected the movement and connection tabs to the back-end, made an attempt (WIP) to make the connection between the phone and the robot more stable and made a changed version of the server, so I can test things without a robot. I had trouble figuring out what the socket protocol is when the server runs on my laptop, since I could not set it on the server and there was barely any information on this specific topic. To solve this, I posted a question on stackoverflow, but nobody responded yet. 

# day 8
Today I found some major issues with the connection. The app would crash on a reconnect with the robot, and the server would receive a lot of messages from unknown source. Little debug information was available, but I think the issues are fixed now. Furthermore, I connected the move fragment to the backend, so now the robot can move. I also added a toggle button to wake up the robot and let it go to sleep.

# day 9
I implemented a disconnect button, connected dances to the backend, created files of joint interpolation data that the robot can execute, created different colored icons for showing the connection and I implemented an interface for a callback from the network thread to the mainactivity to change the color of the icon. This last bit is a bit of a WIP, since apparently a callback function in the mainactivity throws a `CalledFromWrongThreadException`.

# day 10
Today I tried to use Peppers tablet to show a picture that the robot takes, but apparently there is a bug in the software (recognized by Softbank developers) which makes the tablet unusable. There is no known fix for this (as far as I could find). Besides that, I upgraded the overall usability of the app. I made the connect fragment disappear on connect/disconnect, added another dance, put a class in its on file, added a seekBar for volume setting, set the drop down menu to display available connections and updated some design things. 

# weekend
In the weekend I changed some basic design and I found and added a joystick library.

# day 11
I implemented the full screen activity for the movement joysticks. I also connected the volume bar for talking and fixed some bugs.

# day 12
Today I did a lot of bugfixing, design improvements, and I added an autonomous life button.

# day 13
Today I added the option to do animated speech instead of normal speech. I also moved every nested class to its own file, removed unused (commented) code and imports, fixed some style issues, removed excess use of logging statements and fixed some bugs.

