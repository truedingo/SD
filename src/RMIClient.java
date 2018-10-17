import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;


public class RMIClient {
    private static RMI rmiInterface;
    private static Configurations configurations;


    public static void main(String args[]) throws InterruptedException {

        //This might be necessary if you ever need to download classes:
        //System.getProperties().put("java.security.policy", "policy.all");
        //System.setSecurityManager(new RMISecurityManager());
        //configurations = new Configurations(("/Users/iroseiro/IdeaProjects/Project/src/com/company/RMI_configs.cfg"));
        configurations = new Configurations(("/Users/dingo/Desktop/SD/DropMusicMerged/src/RMI_configs.cfg"));

        try {
            RMI rmiInterface = (RMI) Naming.lookup("rmi://localhost:7000/DropMusic");
            rmiInterface.sayHello();

        } catch (Exception e) {
            System.out.println("Exception in main: " + e);
            e.printStackTrace();
        }

    }
}