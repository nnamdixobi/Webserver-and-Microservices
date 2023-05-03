import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Array;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class ServiceRegistry {

    public static HashMap<Integer, List<ServiceInfoModel>> registry = new HashMap<>();

    public static void main(String[] args) throws Exception {

        System.out.println("Hello world!");

        Class.forName("org.sqlite.JDBC");

        // server is listening on port 5000
        ServerSocket ss = new ServerSocket(5000);



        // running infinite loop for getting
        // client request

        System.out.println("Starting server program: Service Registry!!!");

        int nClients = 0;

        while (true)
        {
            Socket s = null;
            // socket object to receive incoming client requests
            s = ss.accept();

            nClients++;
            System.out.println("A new client is connected : " + s + " client number: " + nClients);
            serve(s, nClients);

        }
    }

    private static void serve(Socket socket, int clientID) throws IOException {
        DataInputStream reader = new DataInputStream(socket.getInputStream());
        String msg = reader.readUTF();
        Gson gson = new Gson();
        ServiceMessageModel req = gson.fromJson(msg, ServiceMessageModel.class);
        ServiceMessageModel res = new ServiceMessageModel();

        if (req.code == ServiceMessageModel.SERVICE_PUBLISH_REQUEST) {
            ServiceInfoModel info = gson.fromJson(req.data, ServiceInfoModel.class);

            System.out.println("Service info from client " + clientID + ": " + req.data);

            List<ServiceInfoModel> list = registry.get(info.serviceCode);
            if (list == null) {
                list = new ArrayList<ServiceInfoModel>();
                registry.put(info.serviceCode, list);
            }

            list.add(info);

            res.code = ServiceMessageModel.SERVICE_PUBLISH_OK;
            res.data = "Service Published";
        }

        // need for SERVICE_UNPUBLISH_REQUEST: remove a microservice from the registry!!!


        // discovery request!

        if (req.code == ServiceMessageModel.SERVICE_DISCOVER_REQUEST) {

            int serviceCode = Integer.parseInt(req.data);

            System.out.println("Client asks for service " + clientID + ": " + req.data);

            List<ServiceInfoModel> list = registry.get(serviceCode);

            if (list == null) {
                res.code = ServiceMessageModel.SERVICE_DISCOVER_NOT_FOUND;
                res.data = "";
            }
            else {
                res.code = ServiceMessageModel.SERVICE_DISCOVER_OK;
                Random rand = new Random();
                int id = rand.nextInt(list.size());
                res.data = gson.toJson(list.get(id));
            }

        }

        DataOutputStream printer = new DataOutputStream(socket.getOutputStream());
        String json = gson.toJson(res);
        System.out.println("Response to client  " + clientID + " json = "  + json);
        printer.writeUTF(json);
        printer.flush();
        printer.close();
        reader.close();
        socket.close();
    }
}
