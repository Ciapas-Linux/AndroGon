package com.ciapa.androgon;

import android.util.Log;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.content.ContentValues.TAG;
import static java.lang.System.in;

/**
 * Created by Allen on 2017/2/28.
 */

public class Client implements Runnable
{
    private int SERVER_PORT = 18765;
    private DatagramPacket packetRec, packetSend;
    private DatagramSocket socket;
    private boolean isLife = true;
    private boolean isLifeOver = false;
    private ExecutorService singleExecutor;
    String server_ip;
    String message;
    boolean NewMessage = false;
    boolean connected = false;


    public Client()
    {
        singleExecutor = Executors.newSingleThreadExecutor();
    }

    public void close()
    {
        socket.close();
        isLifeOver = false;
    }

    private void setSoTime(int ms)
    {
        try
        {
            socket.setSoTimeout(ms);
        } catch (SocketException e)
        {
            e.printStackTrace();
        }
    }

    public boolean isLife() {
        return isLife;
    }

    public void setLife(boolean b) {
        this.isLife = b;
    }

    public boolean getLifeOver() {
        return isLifeOver;
    }

    public void sendMsg(final String content)
    {
        singleExecutor.execute(new Runnable()
        {
            @Override
            public void run()
            {
                if (content.length() != 0)
                {
                    InetAddress SERVER_IP = null;
                    try
                    {
                        SERVER_IP = InetAddress.getByName(server_ip.trim());
                    } catch (UnknownHostException e)
                    {
                        e.printStackTrace();
                    }
                    packetSend = new DatagramPacket(content.getBytes(), content.getBytes().length,
                            SERVER_IP, SERVER_PORT);
                    try
                    {
                        socket.send(packetSend);
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private void initSocket()
    {
        if (socket == null)
        {
            try
            {
                socket = new DatagramSocket(SERVER_PORT);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run()
    {
        initSocket();
        while (isLife)
        {
            try
            {
                byte[] msg = new byte[2048];
                packetRec = new DatagramPacket(msg, msg.length);
                socket.receive(packetRec);
                message =  packetRec.toString();
                NewMessage = true;
                message.trim();
                if(message.contains("*%$@:") == true)
                {
                    server_ip = message.substring(5);
                    //listener.onMsgReceived(this, server_ip.getBytes());
                }else
                {
                    //listener.onMsgReceived(this, packetRec.getData());
                }

            } catch (IOException e)
            {
                e.printStackTrace();
                isLife = false;
            }
        }
        socket.close();
        isLifeOver = true;
    }


}
