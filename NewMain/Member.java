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
    private String birthdate;  // 생일 필드 추가
    private String phoneNumber; // 폰 번호 필드 추가
    private List<String> cartItems; // 장바구니 항목 리스트

    // 생성자: Member 객체를 초기화합니다.
    public Member(String name,String id, String password, String address, String birthdate, String phoneNumber) {
        this.name = name;
		this.id = id;
        this.password = password;
        this.address = address;
        this.birthdate = birthdate;
        this.phoneNumber = phoneNumber;
        this.cartItems = new ArrayList<>();
        loadCartFromFile(); // 파일에서 장바구니 항목을 로드합니다.
    }

    // Getter와 Setter 메서드
	
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
        return id;
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

    // 장바구니 항목 리스트를 반환합니다.
    public List<String> getCartItems() {
        return cartItems;
    }

    // 장바구니에 항목 추가
    public void addToCart(String productName, int quantity) {
        cartItems.add(productName + "," + quantity);
        saveCartToFile(); // 장바구니를 파일에 저장합니다.
    }

    // 장바구니를 파일에 저장
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

    // 파일에서 장바구니 항목 로드
    public void loadCartFromFile() {
        cartItems.clear(); // 기존 장바구니 항목을 비웁니다.
        try (BufferedReader br = new BufferedReader(new FileReader(id + "_cart.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                cartItems.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 회원 정보를 CSV 형식으로 반환
    public String toCsvString() {
        return name + "," + id + "," + password + "," + address + "," + birthdate + "," + phoneNumber;
    }

    // CSV 형식에서 Member 객체를 생성
    public static Member fromCsvString(String csv) {
        String[] parts = csv.split(",");
        return new Member(parts[1], parts[2], parts[0], parts[3], parts[4], parts[5]);
    }
}
