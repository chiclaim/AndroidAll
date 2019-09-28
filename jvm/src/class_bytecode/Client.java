package class_bytecode;

import java.io.Serializable;

@Deprecated
public class Client implements Serializable {

    @Deprecated
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.setUsername("Chiclaim");
        System.out.println(client.getUsername());
    }
}
