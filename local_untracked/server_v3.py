import socket
import sys
from naoqi import ALProxy

class Server():
    def __init__(self):
        self.HOST = ''   # Symbolic name meaning all available interfaces
        self.PORT = 1717

        self.tts = ALProxy("ALTextToSpeech", "localhost", 9559)
        self.move = ALProxy("ALMotion", "localhost", 9559)
        self.autl = ALProxy("ALAutonomousLife", "localhost", 9559)
        self.ap = ALProxy("ALAudioPlayer", "localhost", 9559)
        self.ansp = ALProxy("ALAnimatedSpeech", "localhost", 9559)

        sys.path.append('dances')

        self.s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.s.setsockopt(socket.SOL_SOCKET, socket.SO_SNDBUF, 9999)
        self.s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        print 'Socket created'
 
        #Bind socket to local host and port
        try:
            self.s.bind((self.HOST, self.PORT))
        except socket.error as msg:
            print 'Bind failed. Error Code : ' + str(msg[0]) + ' Message ' + msg[1]
            sys.exit()

        print 'Socket bind complete'

        #Start listening on socket
        self.s.listen(0)
        print 'Socket now listening'

    def dance(self, dance):
        print dance
        try :
            variables = {}
            execfile("dances/" + dance + ".py", variables)

            if not self.move.robotIsWakeUp():
                print "waking pepper up"
                self.move.wakeUp()

            if dance == "saxophone":
                self.ap.playFile("/home/nao/sounds/epicsax.ogg")

            self.move.angleInterpolationBezier(variables["names"], variables["times"], variables["keys"])
        except Exception, e:
            print e

    def handleData(self, data_string):
        if data_string[:4] == "talk":
            volume, animated_speech = data_string[5:].split(" ")[:2]
            volume, animated_speech = int(volume), bool(int(animated_speech))
            # data_string[6+len(str(volume)):]
            text = data_string[8+len(str(volume)):]
            
            print "Setting volume to", volume
            self.tts.setVolume(round(volume / 10.0, 1))

            print "animated speech:", animated_speech

            if animated_speech:
                self.ansp.say(text)
            else:
                self.tts.say(text)
        elif data_string[:4] == "move":
            x, y, theta = data_string[5:].split(" ")
            x, y, theta = float(x), float(y), float(theta)
            # print(split_move_command)
            if not self.move.robotIsWakeUp():
                print "waking Pepper up"
                self.move.wakeUp()
            if x <= 1 and x >= -1 and y <= 1 and y >= -1 and theta <= 1 and theta > -1:
                self.move.moveToward(float(x), float(y), float(theta))
                print "moving:", data_string[5:]
        elif data_string[:4] == "wake":
            self.move.wakeUp() if data_string[5] == "1" else self.move.rest()
        elif data_string[:4] == "danc":
            self.dance(data_string[5:])
        elif data_string[:4] == "autl":
            if data_string[5] == "1":
                print "entering autonomous life state"
                self.autl.setState("interactive")
            elif data_string[5] == "0":
                print "exiting autonomous life state"
                self.autl.setState("disabled")

    def preprocessData(self, data_string):
        # only execute the first command that was sent
        return data_string.split('\n')[0]

    def sendAlive(self):
        self.conn.sendall("alive")

    #Function for handling connections. This will be used to create threads
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
         
        #came out of loop
        self.conn.close()
        self.tts.say("Disconnected")

    def run(self):
        #now keep talking with the client
        while 1:
            #wait to accept a connection - blocking call
            self.conn, self.addr = self.s.accept()
            print 'Connected with ' + self.addr[0] + ':' + str(self.addr[1])

            self.handleClient(self.conn)

            print "listening for connections again"     

        self.s.close()



if __name__ == "__main__":
    server = Server()
    server.run()
