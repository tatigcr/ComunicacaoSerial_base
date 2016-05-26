package br.com.plamat.comunicacao;


import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Tatiane
 */
public class SerialComm implements SerialPortEventListener{
    
      InputStream inputStream;
      
 
    public void execute() {        
 
        String portName = getPortNameByOS();
 
        CommPortIdentifier portId = getPortIdentifier(portName);
        if(portId != null) {
 
            try {
        SerialPort serialPort = (SerialPort) portId.open(this.getClass().getName(), 2000);
 
                inputStream = serialPort.getInputStream();
 
                serialPort.addEventListener(this);
 
                serialPort.notifyOnDataAvailable(true);
 
                serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8,
                           SerialPort.STOPBITS_1,
                           SerialPort.PARITY_NONE);
 
            }
            catch (PortInUseException e) {}
 
            catch (IOException e) {}
 
            catch (UnsupportedCommOperationException e) {
                e.printStackTrace();
            }
            catch (TooManyListenersException e) {}
 
        } else {
            System.out.println("Porta Serial não disponível");
        }
    }
 
    /**
     * Get The port name
     **/
    private String getPortNameByOS() {
        
        String osname = System.getProperty("os.name","").toLowerCase();
        if ( osname.startsWith("windows") ) {
                // windows
                return "COM4";
        } else if (osname.startsWith("linux")) {
                // linux
                return "/dev/ttyS0";
        } else if ( osname.startsWith("mac") ) {
                // mac
                return "???";
        } else {
                System.out.println("Desculpe, o sistema operacional não é suportado.");
                System.exit(1);
                return null;
        }
 
    }
    /**
     *Get the Port Identifier
     **/
    private CommPortIdentifier getPortIdentifier(String portName) {
        Enumeration portList = CommPortIdentifier.getPortIdentifiers();
    Boolean portFound = false;
        while (portList.hasMoreElements()) {
                CommPortIdentifier portId = (CommPortIdentifier) portList.nextElement();
                if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                        System.out.println("Porta disponível: " + portId.getName());
                        if (portId.getName().equals(portName)) {
                                System.out.println("Found port: "+portName);
                                portFound = true;
                                return portId;
                        }
                } 
 
        } 
 
        return null;
 
    }      
 
//    public void serialEvent(SerialPortEvent event) {
// 
//        switch (event.getEventType()) {
//        case SerialPortEvent.BI:
//        case SerialPortEvent.OE:
//        case SerialPortEvent.FE:
//        case SerialPortEvent.PE:
//        case SerialPortEvent.CD:
//        case SerialPortEvent.CTS:
//        case SerialPortEvent.DSR:
//        case SerialPortEvent.RI:
//        case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
//            break;
//        case SerialPortEvent.DATA_AVAILABLE:
//            byte[] readBuffer = new byte[20];
//            try {
//               int numBytes = 0;
//                while (inputStream.available() > 0) {
//                    numBytes = inputStream.read(readBuffer);
//                }
//                String result  = new String(readBuffer);
//                                result = result.substring(1, numBytes);
//                System.out.println("Read: "+result);                                                
// 
//            } catch (IOException e) {}
// 
//            break;
//        }
//    }
    
    
    public void serialEvent(SerialPortEvent ev) {
        switch (ev.getEventType()) {
            case SerialPortEvent.BI:
            case SerialPortEvent.OE:
            case SerialPortEvent.FE:
            case SerialPortEvent.PE:
            case SerialPortEvent.CD:
            case SerialPortEvent.CTS:
            case SerialPortEvent.DSR:
            case SerialPortEvent.RI:
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                break;
            case SerialPortEvent.DATA_AVAILABLE:
                byte[] bufferLeitura = new byte[30];
                int numBytes = 0;
                try {
                    //try {
//                 while (inputStream.available() > 0) {
//                    numBytes = inputStream.read(bufferLeitura);
//                }                
//                    String Dadoslidos = new String(bufferLeitura);
//                    System.out.print("\nDados: "+Dadoslidos);
//
//                } catch (Exception e) {
//                    System.out.println("Erro durante a leitura: " + e);
//                }
//               //System.out.println("bytes lidos : " + numBytes);
//                //break;
                    while (inputStream.available() > 0) {
                        numBytes = inputStream.read(bufferLeitura);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(SerialComm.class.getName()).log(Level.SEVERE, null, ex);
                }
                String Dados = new String(bufferLeitura);
                Dados = Dados.substring(0, numBytes);
                if (bufferLeitura.length == 0) {
                    System.out.println("Sem Retorno!!");
                } else {
                    System.out.println("Dados: " + Dados);
                }

        }
    }

}
