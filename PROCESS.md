# day 2
Today I followed several guides to try to implement peer2peer connection between the tablet and the phone. This was quite a challenge, but in the end I managed to get a list of available devices.

# day 3
Today I tried to make an app that runs on the tablet of Pepper. I 'robotified' a new application (meaning it is compatible with the robot SDK's), but the tablet of the robot was not connectable. I searched all fora on this issue, but I couldn't fix it. I'm in the process of posting a question on the softbank forums, but my account still has to be approved. In the meantime I will create a direct connection to the Pepper robot, without use of the tablet.

# day 4
Instead of making two apps that communicate (one phone and one tablet on Pepper), I decided to connect to the Pepper robot directly instead. This is still possible with WiFiDirect (as far as I know at this point). Today I followed tutorials on how sending stuff over the network works, and got a python script that sets up a basic server with a socket that can listen to a specific port. I don't think it can be found by ServiceDiscovery yet, so I have to add that still. The android side can already discover peers, and connect when the right one is found.



