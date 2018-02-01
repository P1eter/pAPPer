# pAPPer, an app to control the Pepper robot

By Pieter Kronemeijer

This is an app that makes it easy for the user to control the basic functionalities of the Pepper robot, like movements and talking, without needing any coding experience.

TODO: INCLUDE SCREENSHOT

## Technical Design
The app has in total five screens. The basic structure is a tabbed activity, containing three tabs. Tab one is for talking, tab two is for moving and tab three is for dancing. The actionbar has an icon that opens the fourth screen, allowing for connection to the Pepper robot. The fifth screen opens when a button in the second tab is pushed, which opens a fullscreen activity with two joysticks that allow the user to move the robot.

### MainActivity
The `MainActivity` takes care of the switching between different tabs with the use of a `ViewPager`. Once a different tab is requested, the `TabPagerAdapter` instance will receive a callback, after which it will return a new fragment. The `MainActivity` also handles the `MenuItem`. When this icon is clicked, this activity will start a dialog fragment, the `RobotSelectFragment`, where the user can connect to a robot. When this fragment is started, a list of available robots will be retrieved from the `DiscoveryListener` that was started in this activity and passed to the `RobotSelectFragment`. The `MainActivity` also implements an interface with a callback method `onConnectionChanged`, which is activated when a connection is made or lost. When this callback is activated, the color of the connect icon in the menu bar will be set to either red or green, for connection lost or gained respectively. 


### TalkFragment
This fragment is the first tab of the application. It has an `EditText` where the user can type a sentence that the robot will speak once the corresponding button is pressed. The user can set two options with this feature; the volume and whether or not the robot should make hand gestures during the talking ("`Animated Speech`"). When the `talk` button is pressed, the data is sent to the `NetworkSender`, which will send it to the server on the robot, where the command will be executed.

### MoveFragment
This fragment handles the movement of the robot. There are three options in this fragment; two switches and a button. The first switch will toggle the stiffness of the robot. When the stiffness of the robot is turned off, the motors in the joints will not persist their state. When activated, the robot will initialize to a standing position. The second switch toggles `Autonomous Life`, which is a function of the robot where the robot will act as humanly as possible, like responding to basic questions ("Pepper, what time is it?"), following faces and moving the arms very slightly. The button in this fragment brings the user to another activity, where there are two joysticks, one for moving the robot (forward, backward, left and right) and one for the orientation (turning left or right).

### DanceFragment
This fragment (currently) contains six buttons. Each one of them activates a different behaviour of the robot. A behaviour here is a short animation where the robot will imitate something a human can do. The options are: vacuum, headbang, take a picture, play the saxophone, play the guitar and throw an American Football ball. On button press, a message will be sent to the `NetworkSender`, indicating what motion should be executed.

### RobotSelectFragment
This fragment allows the user to select a robot from a spinner, and connect to it (if possible). When the fragment is created, a `Bundle` is passed as an extra, containing a list of all possible connections (filtered by `service type = '_naoqi'`). The entries in this list are set as entries in the dropdown menu. Beneath the dropdown menu, there are two buttons; `connect` and `disconnect`, which try to connect to or disconnect from the robot, respectively. In both cases, a message will be passed to the `NetworkSender`, notifying it of the intentions of the user.

### MoveFullscreenActivity
This activity is started (and kept) in a fullscreen landscape mode, and contains two joysticks and a back button. The joysticks are managed by their `JoystickChangedListeners`. The back button will finish this activity and return to the `MoveFragment` where it was started. When the focus of the window is changed, and consequently the full screen flags are cleared, these flags are automatically reset.

### NetworkSender
This singleton class handles all communication with the robot server. It creates a socket with the host set by the `RobotSelectFragment`, on port 1717 (always). After that, it opens a read and write stream for sending data to the server. Then it can send over commands. It has a few package-private functions for sending over specific commands, like `talk`, `move`, `wakeUp`, `dance` and `autonomousLife`. 

### DiscoveryListener
This class will start an `NsdManager`, a Network Service Discovery Manager, on startup. The class implements callback methods from the `DiscoveryListener` from the `NsdManager` class, which are notified when a new service is available. The requested `service type` is `_naoqi`, which indicates that the service that was found is a robot running on `Naoqi` (the Pepper robot). A public list will be kept of all available connections.

### TabPagerAdapter
This adapter class handles which tab is placed in which position. The `getItem` method returns a fragment based on the `position` argument. The first tab is the `TalkFragment`, the second tab is the `MoveFragment` and the third tab is the `DanceFragment`.

### JoystickCombinator
This singleton class combines the input of both joysticks. When either of the joysticks changes, they send new values for either `x` (forward) and `y` (left or right), or for `theta` (turning). This class then overwrites the previous value for these parameters, and sends them all to the `NetworkSender`. This is neccessary to be able to move and turn at the same time.

### MoveJoystickChangedListener
This is a listener class with a callback `onMove`, which is activated when the move joystick changes. It receives an `angle` and a `strength`. The angle is used for the directional movement and the strength is used for the speed with which to execute the movement. After conversion to `x` (forward) and `y` (sideways, left positive) values, these values are sent to the `JoystickCombinator`.

### OrientationJoystickChangedListener
This listener class listens to changes in the orientation joystick, by making use of the `onMove` callback of the orientation joystick. This joystick can only move horizontally. When the joystick moves right, the angle is `0` and otherwise the angle is `180`. Based on this information, the `theta` parameter is set to respectively `-1` and `1`. The `strength` is again used for determining the speed at which to execute the turn.

### SeekBarChangedListener
This listener class listens to changes in the volume `SeekBar`, and sets the text of the `TextView` to the new volume percentage.

### OnConnectionChangedListener
This is an interface that defines a callback method that is activated when the state of the connection is changed to either `connected` or `disconnected`. This callback is called in the `NetworkSender` class, and is implemented by the `MainActivity`. The `MainActivity` will set the correct color of the connection icon when this callback is activated. 

## Challenges
My first goal was to make an app for the tablet of Pepper as well, which I would use for communication and showing for example the current camera view of the robot. However, during the first days of the project it became clear that there were some issues with the tablet. For starters, it didn't respond to anything. I talked with students of the `UvA@home` team, who use the Pepper robot regularly, about this issue, and they told me that they had the same problem during the RoboCup in Japan. They have talked to the developers of the robot there, and they were told that this should not happen and is a bug. There were more people there with the same problem.

After this I decided to just use the Wificard of the robot itself for wifi-direct connection. This would still allow the user for quick connection to the robot without much trouble of setting up things. However, Wifi-direct appeared to be much more trouble getting to work than I anticipated, so I switched to using a socket server in python on the robot, and a socket client for the app. This meant that the user first has to start a server on the robot and connect the robot to the same wifi-network as the app is on in order to be able to use the app. This was a setback, but in order to get it to work in time I had to make this decision. After some hard-to-solve bugs, this approached worked, so I could continue with the app.

The malfunctioning of the tablet on the chest of Pepper has as a consequence that I can not show the video feed of the camera to the user, so that idea was scrapped until the tablet works again.

Another change is the use of two joysticks for movement instead of buttons. The Pepper robot allows for relatively smooth movement, so flexibility of joysticks is a big improvement over the determinedness of a few buttons.

In a perfect world I would have implemented a wifi-direct connection with the robot, to greatly increase the usability of the app (it is currently still way better than coding your movements, but still requires the user to set up the server), since it would allow for immediate connection. However, given the time I had to make this work, using a socket server was the better option.

 