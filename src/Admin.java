public class Admin {
    private String username;
    private String password;

    public Admin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean autenticar(String usuario, String clave) {
        return this.username.equals(usuario) && this.password.equals(clave);
    }
}