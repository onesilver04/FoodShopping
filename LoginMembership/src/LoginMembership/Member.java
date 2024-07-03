package LoginMembership;


class Member {
    private String name;
    private String id;
    private String password;
    private String address;
    private String birthdate;
    private String phonenumber;

    public Member(String name, String id, String password,String address,String birthdate,String phonenumber) {
        this.name = name;
        this.id = id;
        this.password = password;
        this.address = address;
        this.birthdate = birthdate;
        this.phonenumber = phonenumber;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }
    
    public String getAdress() {
        return address;
    }
    
    public String getBirth() {
        return birthdate;
    }
    
    public String getPhonenumber() {
        return phonenumber;
    }
    
    public String toCsvString() {
    	return name + "," + id + "," + password + "," + address + "," + birthdate + "," + phonenumber;
    }
    
}