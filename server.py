'''
    Simple socket server using threads
    Based on 'http://www.binarytides.com/python-socket-server-code-example/'
'''
 
import socket
import sys
from thread import *
from naoqi import ALProxy
 
HOST = ''   # Symbolic name meaning all available interfaces
PORT = 1717 # Arbitrary non-privileged port

tts = ALProxy("ALTextToSpeech", "localhost", 9559)
 
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
s.listen(1)
print 'Socket now listening'
 
def handleData(data_string):
    if data_string[:4] == "talk":
        tts.say(data_string[6:])

def preprocessData(data_string):
    # only execute the first command that was sent
    return data_string.split('\n')[0]

def sendBit():
    s.send(b'1')

#Function for handling connections. This will be used to create threads
def clientthread(conn):
    #Sending message to connected client
    conn.send('Welcome to the server. Type something and hit enter\n') #send only takes string
     
    #infinite loop so that function do not terminate and thread do not end.
    while True:
        #Receiving from client
        try:
            data_string = conn.recv(1024)
        except Exception as e:
            print e
            break

        print "I received: ", data_string

        data_string = preprocessData(data_string)
        handleData(data_string)

        sendBit();
     
        # conn.sendall(reply)
     
    #came out of loop
    conn.close()
 
#now keep talking with the client
while 1:
    #wait to accept a connection - blocking call
    conn, addr = s.accept()
    print 'Connected with ' + addr[0] + ':' + str(addr[1])
     
    #start new thread takes 1st argument as a function name to be run, second is the tuple of arguments to the function.
    start_new_thread(clientthread ,(conn,))
 
s.close()