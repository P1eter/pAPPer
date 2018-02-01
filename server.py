import socket
import sys
from naoqi import ALProxy

class Server():
    def __init__(self):
        self.HOST = ''   # symbolic name meaning all available interfaces
        self.PORT = 1717 # always 1717

        # make proxies to naoqi
        self.tts = ALProxy("ALTextToSpeech", "localhost", 9559)
        self.motion = ALProxy("ALMotion", "localhost", 9559)
        self.autl = ALProxy("ALAutonomousLife", "localhost", 9559)
        self.ap = ALProxy("ALAudioPlayer", "localhost", 9559)
        self.ansp = ALProxy("ALAnimatedSpeech", "localhost", 9559)

        sys.path.append('dances')

        # create socket
        self.s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.s.setsockopt(socket.SOL_SOCKET, socket.SO_SNDBUF, 9999)
        self.s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        print 'Socket created'
 
    def startServer(self):
        # bind socket to host and port
        try:
            self.s.bind((self.HOST, self.PORT))
        except socket.error as msg:
            print 'Bind failed. Error Code : ' + str(msg[0]) + ' Message ' + msg[1]
            sys.exit()
        print 'Socket bind complete'

        # start listening on socket
        self.s.listen(0)
        print 'Socket now listening'

    def talk(self, data):
        volume, animated_speech = data.split(" ")[:2]
        volume, animated_speech = int(volume), bool(int(animated_speech))
        # data_string[6+len(str(volume)):]
        text = data[3+len(str(volume)):]
        
        print "Setting volume to", volume
        self.tts.setVolume(round(volume / 10.0, 1))

        print "animated speech:", animated_speech

        if animated_speech:
            self.ansp.say(text)
        else:
            self.tts.say(text)

    def move(self, data):
        x, y, theta = data.split(" ")
        x, y, theta = float(x), float(y), float(theta)

        if not self.motion.robotIsWakeUp():
            print "waking Pepper up"
            self.motion.wakeUp()

        # check for valid parameters
        if x <= 1 and x >= -1 and y <= 1 and y >= -1 and theta <= 1 and theta > -1:
            self.motion.moveToward(float(x), float(y), float(theta))
            print "moving:", data

    def wakeUp(self, data):
        self.motion.wakeUp() if data == "1" else self.motion.rest()

    def dance(self, dance):
        try :
            # get interpolation data from dance files
            variables = {}
            execfile("dances/" + dance + ".py", variables)

            if not self.motion.robotIsWakeUp():
                print "waking pepper up"
                self.motion.wakeUp()

            # execute interpolation
            self.motion.angleInterpolationBezier(variables["names"], variables["times"], variables["keys"])
        except Exception, e:
            print e

    def setAutonomousLife(self, data):
            if data == "1":
                print "entering autonomous life state"
                self.autl.setState("interactive")
            elif data == "0":
                print "exiting autonomous life state"
                self.autl.setState("disabled")

    def handleData(self, data_string):
        if data_string[:4] == "talk":
            self.talk(data_string[5:])
        elif data_string[:4] == "move":
            self.move(data_string[5:])
        elif data_string[:4] == "wake":
            self.wakeUp(data_string[5])
        elif data_string[:4] == "danc":
            self.dance(data_string[5:])
        elif data_string[:4] == "autl":
            self.setAutonomousLife(data_string[5])

    def preprocessData(self, data_string):
        # only execute the first command that was sent
        return data_string.split('\n')[0]

    # function for handling connections
    def handleClient(self, conn):
        #Sending message to connected client
        # self.conn.send('Welcome to the server. Type something and hit enter\n') #send only takes string

        self.tts.say("Connected")

        #infinite loop so that function do not terminate and thread do not end.
        while True:
            #Receiving from client
            try:
                print "receiving..."
                data_string = self.conn.recv(1024)
            except Exception as e:
                print e
                break

            if data_string == "":
                break

            print "I received: \"", data_string, "\""

            data_string = self.preprocessData(data_string)
            self.handleData(data_string)
         
        # came out of loop
        self.conn.close()
        self.tts.say("Disconnected")

    def run(self):
        self.startServer()

        # now keep talking with the client
        while 1:
            # wait to accept a connection - blocking call
            self.conn, self.addr = self.s.accept()
            print 'Connected with ' + self.addr[0] + ':' + str(self.addr[1])

            self.handleClient(self.conn)

            print "listening for connections again"     

        self.s.close()


if __name__ == "__main__":
    server = Server()
    server.run()
