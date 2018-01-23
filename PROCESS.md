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

