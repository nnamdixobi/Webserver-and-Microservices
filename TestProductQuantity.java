import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ThreadLocalRandom;

public class TestProductQuantity {

    public static void main(String[] args) throws IOException {
        ServiceInfoModel service = discoverService(ServiceInfoModel.PRODUCT_QUANTITY_UPDATE_SERVICE);
        testService(service.serviceHostAddress, service.serviceHostPort);
    }

    public static ServiceInfoModel discoverService(int serviceCode) throws IOException {
        // ask for service from Registry

        Socket socket = new Socket("localhost", 5000);

        ServiceMessageModel req = new ServiceMessageModel();
        req.code = ServiceMessageModel.SERVICE_DISCOVER_REQUEST;
        req.data = String.valueOf(serviceCode);

        Gson gson = new Gson();

        DataOutputStream printer = new DataOutputStream(socket.getOutputStream());
        printer.writeUTF(gson.toJson(req));
        printer.flush();


        DataInputStream reader = new DataInputStream(socket.getInputStream());
        String msg = reader.readUTF();

        System.out.println("Message from server: " + msg);

        printer.close();
        reader.close();
        socket.close();

        ServiceMessageModel res = gson.fromJson(msg, ServiceMessageModel.class);

        if (res.code == ServiceMessageModel.SERVICE_DISCOVER_OK) {
            System.out.println(res.data);
        }
        else {
            System.out.println("Service not found");
            return null;
        }

        ServiceInfoModel info = gson.fromJson(res.data, ServiceInfoModel.class);


        System.out.println(info);
        return info;
    }

    public static void testService(String hostName, int port) throws IOException  {
        Socket socket = new Socket(hostName, port);
        DataOutputStream printer = new DataOutputStream(socket.getOutputStream());

        Gson gson = new Gson();

        int randomNumber = ThreadLocalRandom.current().nextInt(1, 10 + 1);

        String request = "";
        jsonModel msg = new jsonModel();
        msg.int1 = 1; // id
        msg.int2 = randomNumber; // quantity
        request = gson.toJson(msg);
        printer.writeUTF(request);
        printer.flush();

        DataInputStream reader = new DataInputStream(socket.getInputStream());
        String rsp = reader.readUTF();

        ServiceMessageModel response = gson.fromJson(rsp, ServiceMessageModel.class);

        if (response.code == ServiceMessageModel.SERVICE_TEST_OK) {
            System.out.println(response.data);
        } else {
            System.out.println("Testing Failed");
        }

        printer.close();
        socket.close();
    }

}