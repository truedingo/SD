import java.io.Serializable;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private boolean status;     //0->offline 1->online
    private boolean privilege;  //0->regular 1->editor
    private boolean checkPrivilegeNotification; //0->delivered 1->not delivered
    private boolean checkEditNofication; //0-> delivered 1-> not delivered

    User(){
    };

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isPrivilege() {
        return privilege;
    }

    public void setPrivilege(boolean privilege) {
        this.privilege = privilege;
    }

    public boolean isCheckPrivilegeNotification() {
        return checkPrivilegeNotification;
    }

    public void setCheckPrivilegeNotification(boolean checkPrivilegeNotification) {
        this.checkPrivilegeNotification = checkPrivilegeNotification;
    }

    public boolean isCheckEditNofication() {
        return checkEditNofication;
    }

    public void setCheckEditNofication(boolean checkEditNofication) {
        this.checkEditNofication = checkEditNofication;
    }

    @Override
    public String toString() {
        return "Username:" + username + "\nPassword:" + password + "\nStatus:" + status + "\nPrivilege:" + privilege + "\nPrivNotif:" + checkPrivilegeNotification + "\nEdtNotif:" + checkEditNofication;
    }
}