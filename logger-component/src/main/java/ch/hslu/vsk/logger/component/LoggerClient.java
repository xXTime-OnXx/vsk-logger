package ch.hslu.vsk.logger.component;

import ch.hslu.vsk.logger.common.ClientRequestCodes;
import ch.hslu.vsk.logger.common.ServerResponseCodes;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import java.net.URI;
import java.rmi.ConnectException;

class LoggerClient {
    private final ZContext context = new ZContext();
    private ZMQ.Socket socket;
    private final URI address;

    public LoggerClient(URI address) {
        this.address = address;

        reconnect(); // Initial connection setup
    }

    public void reconnect() {

        if (socket != null) {
            context.destroySocket(socket);
        }
        socket = context.createSocket(SocketType.REQ);
        socket.connect(address.toString());
        socket.setReceiveTimeOut(1000);
    }

    /**
     *  Tests Connection to Server
     * @return true if connection succeeded, false if connection failed
     */
    public boolean testConnection() {
        try {
            socket.send(ClientRequestCodes.HEARTBEAT.toString());
            String response = socket.recvStr();

            if (response != null && response.equals(ServerResponseCodes.ALIVE.toString())) {
                System.out.println("Connection is alive");
                return true;
            }
        } catch (Exception e) {
            System.out.println("Connection failed due to exception: " + e.getMessage());
        }
        return false;
    }
    public void sendLogMessage(String logMessage) throws ConnectException {
        System.out.println("Trying to send message: " + logMessage);
        socket.send(logMessage);

        String response = socket.recvStr();
        System.out.println("Received response: " + response);

        if (response == null || !response.equals(ServerResponseCodes.RECEIVED.toString())) {
            throw new ConnectException("Failed to send message: " + logMessage);
        }
    }
}