import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Member {
    private String name;
    private String id;
    private String password;
    private String address;
    private String birthdate;
    private String phoneNumber;
    private List<String> cartItems;

    public Member(String name, String id, String password, String address, String birthdate, String phoneNumber) {
        this.name = name;
        this.id = id;
        this.password = password;
        this.address = address;
        this.birthdate = birthdate;
        this.phoneNumber = phoneNumber;
        this.cartItems = new ArrayList<>();
        loadCartFromFile();  // Member 객체 생성 시 장바구니 파일을 읽어옴
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<String> getCartItems() {
        return cartItems;
    }

    public void addToCart(String productName, int quantity) {
        cartItems.add(productName + "," + quantity);
        saveCartToFile();
    }

    public void saveCartToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(id + "_cart.txt", true))) {
            for (String item : cartItems) {
                bw.write(item);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadCartFromFile() {
        cartItems.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(id + "_cart.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                cartItems.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String toCsvString() {
        return name + "," + id + "," + password + "," + address + "," + birthdate + "," + phoneNumber;
    }

    public static Member fromCsvString(String csv) {
        String[] parts = csv.split(",");
        return new Member(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
    }
}
