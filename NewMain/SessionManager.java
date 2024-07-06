public class SessionManager {
    private static SessionManager instance;
    private Member currentUser;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void login(Member user) {
        this.currentUser = user;
    }

    public void logout() {
        this.currentUser = null;
    }

    public Member getCurrentUser() {
        return currentUser;
    }
	
	public boolean isLoggedIn() {
        return currentUser != null;
    }
}
