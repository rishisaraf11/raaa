package com.vmware.scheduler.domain;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.springframework.data.annotation.Id;

import java.io.InputStream;
import java.util.Map;

/**
 * Created by ppushkar on 10/23/2015.
 */
public class Command  {

    @Id
    public String id;
    Map<String,String> payload;

    public Command() {}
    public Command(Map<String,String> payload)
    {
        this.payload=(Map) payload;
    }

    public String getId () { return id;  }
    public void setId (String id) { this.id=id;  }
    public Map getPayload(){ return payload;}
    public void setPayload(Map payload){ this.payload=payload;}
    public void execute()
    {
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        JSch jsch = new JSch();
        String user = payload.get("user");
        String host_name=payload.get("host_name");
        String pwd = payload.get("pwd");
        String cmd = payload.get("cmd");
        try {
        Session session=jsch.getSession(user, host_name, 22);
        session.setPassword(pwd);
        session.setConfig(config);
        session.connect();
        System.out.println("Connected");
        Channel channel=session.openChannel("exec");
        ((ChannelExec)channel).setCommand(cmd);
        channel.setInputStream(null);
        ((ChannelExec)channel).setErrStream(System.err);
        channel.setOutputStream(System.out);
        InputStream in=channel.getInputStream();
        channel.connect();
        byte[] tmp=new byte[1024];
        while(true){
            while(in.available()>0){
                int i=in.read(tmp, 0, 1024);
                if(i<0)break;
                System.out.print(new String(tmp, 0, i));
            }
            if(channel.isClosed()){
                System.out.println("exit-status: "+channel.getExitStatus());
                break;
            }
            try{Thread.sleep(1000);}catch(Exception ee){}
        }
        channel.disconnect();
        session.disconnect();
        System.out.println("DONE");
    }catch(Exception e){
        e.printStackTrace();
    }
    }
}
