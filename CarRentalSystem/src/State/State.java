package State;

import DataBase.Client.Client;
import DataBase.Employee.Employee;
import Util.Config;

public class State {
    private boolean isLogin;
    private Config.Identity identity;

    private static Client client = null;
    private static Employee employee = null;

    private static State INSTANCE;

    private State() {
        isLogin = false;
        identity = null;
    }

    public static State getINSTANCE() {
        if (INSTANCE == null)
            INSTANCE = new State();
        return INSTANCE;
    }

    public Client getClient(){
        return client;
    }

    public Employee getEmployee(){
        return employee;
    }

    public Config.Identity getIdentity(){
        return identity;
    }

    public void login(Config.Identity identity, Client client, Employee employee){
        isLogin = true;
        this.identity = identity;
        switch (identity){
            case Client:
                State.client = client.clone();
                break;
            case Employee:
                State.employee = employee.clone();
                break;
            default:
                break;
        }
    }

    public void logout(){
        isLogin = false;
        identity = null;
        State.employee = null;
        State.client = null;
    }
}
