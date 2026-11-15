package org.firstinspires.ftc.teamcode.Bambusa.Helpers;

import org.firstinspires.ftc.teamcode.Bambusa.Config.Settings;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class USBConnection {
    // Port
    public final int port;

    // Status (accessible)
    public String status = "Uninitialized";
    public boolean connected = false;

    // Sockets
    private ServerSocket serverSocket;
    private Socket clientSocket;

    // IO Channels
    private PrintWriter out;
    private BufferedReader in;

    public USBConnection(int port) {
        this.port = port;

        try {
            serverSocket = new ServerSocket();
            serverSocket.setReuseAddress(true);
            serverSocket.bind(new InetSocketAddress("127.0.0.1", port));
            serverSocket.setSoTimeout(Settings.USB.DATA.TIMEOUT);

            status = "Listening on port " + port;
        } catch (Exception e) {
            status = "Init Failed on port " + port + ": " + e.getMessage();
        }
    }

    // Actively polls for incoming connections
    public void update() {
        if (serverSocket == null) return;

        // If already connected and socket is active, do nothing
        if (connected && clientSocket != null && !clientSocket.isClosed()) return;

        try {
            // Attempt to accept incoming client
            clientSocket = serverSocket.accept();
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            connected = true;
            status = "Client connected on port " + port;

        } catch (SocketTimeoutException ignored) {
            // Not error; timeout
            if (!connected) status = "Waiting for client on port " + port + "...";
        } catch (Exception e) {
            disconnect();
            status = "Accept error: " + e.getMessage();
        }
    }

    // Input channel
    public String read() {
        if (!connected || in == null) return "";

        try {
            if (in.ready()) {
                String line = in.readLine();
                if (line != null) {
                    return line;
                } else {
                    // Remote client closed connection cleanly
                    disconnect();
                }
            }
        } catch (Exception e) {
            disconnect();
        }

        return "";
    }

    // Sends data as a String
    public void write(String data) {
        if (!connected || out == null) return;

        out.println(data);

        // Checks if the remote end disconnected
        if (out.checkError()) disconnect();
    }

    // Closes active connection while keeping server alive (to reconnect)
    private void disconnect() {
        connected = false;
        status = "Client disconnected from port " + port;

        // Closing port
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (clientSocket != null) clientSocket.close();
        } catch (Exception ignored) {}

        // Setting port to null
        in = null;
        out = null;
        clientSocket = null;
    }

    // Shuts down server and client
    public void stop(){
        disconnect();
        
        try {
            if (serverSocket != null) serverSocket.close();
        } catch (Exception ignored) {
            // Ignore when closing
        }
        
        status = "Port " + port + " closed";
    }
}
