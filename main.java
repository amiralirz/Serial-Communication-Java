import java.io.InputStream;
import java.util.Scanner;

import com.fazecast.jSerialComm.SerialPort; // importing JSerialCom

public class main {
    public static void main(String[] args) {

        //---------------------------------------Setting up com port---------------------------------------

        SerialPort ports[] = SerialPort.getCommPorts(); // List of available ports
        System.out.println("Select Port");
        int i = 1;
        for (SerialPort port : ports) {
            System.out.println(i++ + "." + port.getSystemPortName());
        }

        //---------------------------------------choosing the com port---------------------------------------

        Scanner s = new Scanner(System.in);
        int chosenPort = s.nextInt();

        SerialPort port = ports[chosenPort - 1];
        if (port.openPort()) { // opening the selected port
            System.out.println("Port opened Successfully");
        } else {
            System.out.println("Unable to open the port");
            return;
        }

        port.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0); // this timeout setting is used when working with a scanner


        //---------------------------------------setting up the input stream and processing---------------------------------------

        InputStream input_scanner = port.getInputStream(); // for handling the received data byte by byte
        //input_scanner.useDelimiter("");
        Thread printer = new Thread() {
            @Override
            public void run() {
                try {
                    int[] bytes = new int[256]; // the received bytes are stored in this array. array size can be anything
                    int counter = 0;
                    while (true) {
                        bytes[counter] = (int) input_scanner.read(); // reading the received data byte by byte
                        //the data should be processed here
                        //the data is received byte by byte. I can't find a way to receive a whole package at once :(
                    }
                } catch (Exception e) {

                }
            }
        };
        printer.start();

        //---------------------------------------Setting up the output stream---------------------------------------

        while (true) {
            byte out[] = {0}; // the data the yoy want to send must be stored in a byte array and then passed to the function
            out[0] = s.nextByte();
            port.writeBytes(out, 1); // this function is used to send data through the com port
            System.out.println("data sent");
        }
    }
}
