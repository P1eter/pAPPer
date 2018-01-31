'''
    Simple socket server using threads
    Based on 'http://www.binarytides.com/python-socket-server-code-example/'
'''
 
import socket
import sys
from naoqi import ALProxy

HOST = ''   # Symbolic name meaning all available interfaces
PORT = 1717 # Arbitrary non-privileged port

tts = ALProxy("ALTextToSpeech", "localhost", 9559)
move = ALProxy("ALMotion", "localhost", 9559)
autl = ALProxy("ALAutonomousLife", "localhost", 9559)
ap = ALProxy("ALAudioPlayer", "localhost", 9559)
ansp = ALProxy("ALAnimatedSpeech", "localhost", 9559)

sys.path.append('dances')
 
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.setsockopt(socket.SOL_SOCKET, socket.SO_SNDBUF, 9999)
s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
print 'Socket created'
 
#Bind socket to local host and port
try:
    s.bind((HOST, PORT))
except socket.error as msg:
    print 'Bind failed. Error Code : ' + str(msg[0]) + ' Message ' + msg[1]
    sys.exit()
     
print 'Socket bind complete'
 
#Start listening on socket
s.listen(0)
print 'Socket now listening'

def dance(dance):
    print dance
    try :
        variables = {}
        execfile("dances/" + dance + ".py", variables)

        if not move.robotIsWakeUp():
            print "waking pepper up"
            move.wakeUp()

        if dance == "saxophone":
            ap.playFile("/home/nao/sounds/epicsax.ogg")

        move.angleInterpolationBezier(variables["names"], variables["times"], variables["keys"])
    except Exception, e:
        print e

    # try:
    #   # uncomment the following line and modify the IP if you use this script outside Choregraphe.
    #   motion = ALProxy("ALMotion", "localhost", 9559)
    #   # motion = ALProxy("ALMotion")
    #   motion.angleInterpolationBezier(names, times, keys)
    # except BaseException, err:
    #   print err

def handleData(data_string):
    if data_string[:4] == "talk":
        volume, animated_speech = data_string[5:].split(" ")[:2]
        volume, animated_speech = int(volume), bool(int(animated_speech))
        # data_string[6+len(str(volume)):]
        text = data_string[8+len(str(volume)):]
        
        print "Setting volume to", volume
        tts.setVolume(round(volume / 10.0, 1))

        print "animated speech:", animated_speech

        if animated_speech:
            ansp.say(text)
        else:
            tts.say(text)
    elif data_string[:4] == "move":
        x, y, theta = data_string[5:].split(" ")
        x, y, theta = float(x), float(y), float(theta)
        # print(split_move_command)
        if not move.robotIsWakeUp():
            print "waking Pepper up"
            move.wakeUp()
        if x <= 1 and x >= -1 and y <= 1 and y >= -1 and theta <= 1 and theta > -1:
            move.moveToward(float(x), float(y), float(theta))
            print "moving:", data_string[5:]
    elif data_string[:4] == "wake":
        move.wakeUp() if data_string[5] == "1" else move.rest()
    elif data_string[:4] == "danc":
        dance(data_string[5:])
    elif data_string[:4] == "autl":
        if data_string[5] == "1":
            print "entering autonomous life state"
            autl.setState("interactive")
        elif data_string[5] == "0":
            print "exiting autonomous life state"
            autl.setState("disabled")

def preprocessData(data_string):
    # only execute the first command that was sent
    return data_string.split('\n')[0]

def sendAlive():
    conn.sendall("alive")

#Function for handling connections. This will be used to create threads
def handleClient(conn):
    #Sending message to connected client
    conn.send('Welcome to the server. Type something and hit enter\n') #send only takes string

    #infinite loop so that function do not terminate and thread do not end.
    while True:
        #Receiving from client
        try:
            print "receiving..."
            data_string = conn.recv(1024)
        except Exception as e:
            print e
            break

        if data_string == "":
            break

        print "I received: \"", data_string, "\""

        data_string = preprocessData(data_string)
        handleData(data_string)

        # sendAlive();
     
        # conn.sendall(reply)
     
    #came out of loop
    conn.close()

#now keep talking with the client
while 1:
    #wait to accept a connection - blocking call
    conn, addr = s.accept()
    print 'Connected with ' + addr[0] + ':' + str(addr[1])

    handleClient(conn)

    print "listening for connections again"     
    # #start new thread takes 1st argument as a function name to be run, second is the tuple of arguments to the function.
    # start_new_thread(clientthread ,(conn,))

s.close()
