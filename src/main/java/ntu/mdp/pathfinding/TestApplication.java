package ntu.mdp.pathfinding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class TestApplication {
    private final static String RPI = "192.168.17.17";
    private final static int RPI_PORT = 4444;

    public static void main(String[] args) throws IOException {
        Socket socket1 = new Socket(RPI, RPI_PORT);
        PrintWriter out = new PrintWriter(socket1.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
        Scanner sc = new Scanner(System.in);

        while (true) {
            out.println("STM:01010");
            System.out.println("Pending read");
            System.out.println(in.readLine());
            //sc.nextLine();
        }

    }
}
