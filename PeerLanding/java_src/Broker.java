import org.zeromq.ZMQ;

public class Broker {

    public static void main(String[] args) {
        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket xpub = context.socket(ZMQ.XSUB);
        ZMQ.Socket xsub = context.socket(ZMQ.XPUB);
        System.out.println(xpub.bind("tcp://*:2000"));
        System.out.println(xsub.bind("tcp://*:2001"));
        ZMQ.proxy(xpub, xsub, null);
    }
}