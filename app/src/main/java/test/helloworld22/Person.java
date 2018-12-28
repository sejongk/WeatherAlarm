package test.helloworld22;


public class Person {
    String no;
    String name ;
    String phone;
    public Person(String no, String name, String phone){
        this.name = name;
        this.no = no;
        this.phone = phone;
    }
    public String getName() {
        return name;
    }

    public String getNo() {
        return no;
    }

    public String getPhone() {
        return phone;
    }


}
