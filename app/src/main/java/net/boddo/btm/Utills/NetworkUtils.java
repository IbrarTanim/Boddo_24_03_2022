package net.boddo.btm.Utills;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class NetworkUtils {
    /**
     * https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out
     * @return
     */
    private static boolean isConnected() {
        try {
            int timeoutMs = 1500;
            Socket sock = new Socket();
            SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);
            sock.connect(sockaddr, timeoutMs);
            sock.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Checking two times to ensure reliable connectivity
     *
     * @return
     */
    public static boolean isOnline() {
        int count = 0;
        for (int i = 0; i < 2; i++) {
            count += isConnected() ? 1 : 0;
        }
        return count > 0;
    }
}
