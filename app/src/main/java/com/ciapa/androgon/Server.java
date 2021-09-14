package com.ciapa.androgon;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.content.ContentValues.TAG;

/**
 * Created by Allen on 2017/2/28.
 */

public class Server implements Runnable {
    private InetAddress CLIENT_IP;
    private int CLIENT_PORT;
    private DatagramPacket packetRec, packetSend;
    private DatagramSocket socket;
    private boolean isLife = true;
    private boolean isLifeOver = false;
    private ExecutorService singleExecutor;

    public Server() {
        singleExecutor = Executors.newSingleThreadExecutor();
    }

    private void setSoTime(int ms) {
        try {
            socket.setSoTimeout(ms);
        } catch (SocketException e) {
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

    public void sendMsg(final String content) {
        singleExecutor.execute(new Runnable() {
            @Override
            public void run() {
                if (content.length() != 0 && CLIENT_PORT != -1) {
                    packetSend = new DatagramPacket(content.getBytes(), content.getBytes().length,
                            CLIENT_IP, CLIENT_PORT);
                    try {
                        socket.send(packetSend);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void initSocket() {
        if (socket == null) {
            try {
                socket = new DatagramSocket(18765);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        initSocket();
        while (isLife) {
            try {
                byte[] msg = new byte[1024];
                packetRec = new DatagramPacket(msg, msg.length);
                socket.receive(packetRec);
                msg = packetRec.getData();
                CLIENT_IP = packetRec.getAddress();
                CLIENT_PORT = packetRec.getPort();
                listener.onMsgReceived(this, msg);
            } catch (IOException e) {
                e.printStackTrace();
                isLife = false;
            }
        }
        socket.close();
        isLifeOver = true;
    }

    private OnMsgReceivedListener listener;

    public void setOnMsgReceivedListener(OnMsgReceivedListener listener) {
        this.listener = listener;
    }

    public interface OnMsgReceivedListener {
        void onMsgReceived(Runnable runnable, byte[] data);
    }

}
