import org.zeromq.ZMQ;

public class Broker {

    public static void main(String[] args) {
        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket xpub = context.socket(ZMQ.XPUB);
        ZMQ.Socket xsub = context.socket(ZMQ.XSUB);
        xpub.bind("tcp://*:2000");
        xsub.bind("tcp://*:2001");
        ZMQ.proxy(xpub, xsub, null);
    }
}