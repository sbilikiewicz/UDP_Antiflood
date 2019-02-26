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
    
    public int intOfIpV4(String ip) {
        int result = 0;
        byte[] bytes = IPAddressUtil.textToNumericFormatV4(ip);
        if (bytes == null) {
            return result;
        }
        for (byte b : bytes) {
            result = result << 8 | (b & 0xFF);
        }
        return result;
    }
    
    private void listen() throws Exception
    {
        System.out.println("[AntiFlood] : Creating listener at " + InetAddress.getLocalHost() + ":" + InetAddress.getLocalPort());
        String msg;
        //test
        int [] floodMatrix = new int [12];
        while (true)
        {
            byte[] buf = new byte[256];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            //Test
            String IPAdd = packet.getAddress().getHostAddress();
            int Matrix = floodMatrix[intOfIpV4(IPAdd)];
            floodMatrix[intOfIpV4(IPAdd)] = Matrix + 1;
            if (Matrix > 100) //100 packets per second
            {
                System.out.println("Packet Limit exceeded " + Matrix);
                Matrix = 0;
                //block those packets
            }
            // wait until a packet is received
            udpSocket.receive(packet);
            // For debug only
            msg = new String(packet.getData()).trim();
            System.out.println("New message " + IPAdd + ": " + msg);
        }
    }
    
    public static void main(String[] args) throws Exception {
        UDPServer client = new UDPServer(50000); //server port
        client.listen();
    }
}