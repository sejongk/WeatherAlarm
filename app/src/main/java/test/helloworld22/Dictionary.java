package test.helloworld22;

public class Dictionary {
    private String id;
    private String name;
    private String phone;

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public Dictionary(String id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }
}
