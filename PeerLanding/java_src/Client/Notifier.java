package Client;

import org.zeromq.ZMQ;
import java.util.Vector;

public class Notifier implements Runnable{
    private ZMQ.Socket sub;
    private ZMQ.Context context;
    private Vector<String> mailbox;

    public Notifier()
    {
        this.context = ZMQ.context(1);
        this.sub = context.socket(ZMQ.SUB);
        this.sub.connect("tcp://localhost:2000");
        mailbox = new Vector<>();
    }

    public String mailbox(){
        StringBuilder str = new StringBuilder();
        for(String mail:mailbox)
            str.append(mail).append('\n');
        return str.toString();
    }

    @Override
    public void run() {
        while(true)
        {
            byte[] notification = this.sub.recv();
            mailbox.add(new String(notification));
        }
    }

    public void subscricao(String tipo, String empresas){
        String[] array = empresas.split(" ");
        if (array.length == 0)
            sub.subscribe(tipo);
        for (int i = 0; i < 10 && i < array.length ;i++)
            sub.subscribe(tipo+"-"+array[i]+"-");
    }
}
