package bgu.spl.net.impl.stomp;

import bgu.spl.net.impl.echo.LineMessageEncoderDecoder;
import bgu.spl.net.impl.rci.ObjectEncoderDecoder;
import bgu.spl.net.srv.Server;

import java.io.Serializable;

public class StompServer {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: StompServer <port> <server type>");
            return;
        }
        if (args[1].equals("tpc"))
            Server.threadPerClient(Integer.parseInt(args[0]), StompMessagingProtocolImpl::new, LineMessageEncoderDecoder::new).serve();
        else if (args[1].equals("reactor"))
            Server.reactor(Runtime.getRuntime().availableProcessors(), Integer.parseInt(args[0]), StompMessagingProtocolImpl::new, LineMessageEncoderDecoder::new).serve();
    }
}
