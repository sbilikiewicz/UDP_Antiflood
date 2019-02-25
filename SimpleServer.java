//**********************************************************
//
// Simple UDP anti flood firewall
//   by Sbilikiewicz https://github.com/sbilikiewicz
//
//**********************************************************
package com.pgx.java.socket;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
public class UDPServer {
    private DatagramSocket udpSocket;
    private int port;
 
    public UDPServer(int port) throws SocketException, IOException {
        this.port = port;
        this.udpSocket = new DatagramSocket(this.port);
    }
    private void listen() throws Exception
    {
        System.out.println("[AntiFlood] : Creating listener at " + InetAddress.getLocalHost());
        String msg;
        
        while (true)
        {
            byte[] buf = new byte[256];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            
            // wait until a packet is received
            udpSocket.receive(packet);
            // For debug only
            msg = new String(packet.getData()).trim();
            System.out.println("New message " + packet.getAddress().getHostAddress() + ": " + msg);
        }
    }
    
    public static void main(String[] args) throws Exception {
        UDPServer client = new UDPServer(Integer.parseInt(args[0]));
        client.listen();
    }
}