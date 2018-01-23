'''
    Simple socket server using threads
    Based on 'http://www.binarytides.com/python-socket-server-code-example/'
'''
 
import socket
import sys
from thread import *
 
HOST = ''   # Symbolic name meaning all available interfaces
PORT = 1717 # Arbitrary non-privileged port

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
s.listen(10)
print 'Socket now listening'
 
def handleData(data):
    if data[:4] == "talk":
        print data
    if data[:4] == "move":
        print data

#Function for handling connections. This will be used to create threads
def clientthread(conn):
    #Sending message to connected client
    conn.send('Welcome to the server.\n') #send only takes string
     
    #infinite loop so that function do not terminate and thread do not end.
    while True:
         
        #Receiving from client
        try:
            print "receiving..."
            data = conn.recv(1024)
            print "received"
        except Exception as e:
            print e
            break

        # reply = 'Thanks for the message!: "' + data + '"'
        if not data:
            break

        print "I received: ", data

        handleData(data)
     
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