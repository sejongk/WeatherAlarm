package test.helloworld22;

public class ContactItem {
    String user_phNumber, user_Name, user_Email;
    long photo_id=0, person_id=0;
    int id;
    public ContactItem(){}

    public String toString(){
        return this.user_phNumber;
    }
    public int hashCode(){
        return getPhNumberChanged().hashCode();
    }
    public String getPhNumberChanged(){
        return user_phNumber.replace("-","");
    }
    @Override
    public boolean equals(Object o){
        if(o instanceof ContactItem){
            return getPhNumberChanged().equals(((ContactItem)o).getPhNumberChanged());
        }
        return false;
    }

    public String getUser_phNumber() {
        return user_phNumber;
    }

    public void setUser_phNumber(String user_phNumber) {
        this.user_phNumber = user_phNumber;
    }

    public String getUser_Name() {
        return user_Name;
    }

    public void setUser_Name(String user_Name) {
        this.user_Name = user_Name;
    }

    public String getUser_Email() {
        return user_Email;
    }

    public void setUser_Email(String user_Email) {
        this.user_Email = user_Email;
    }

    public long getPhoto_id() {
        return photo_id;
    }

    public void setPhoto_id(long photo_id) {
        this.photo_id = photo_id;
    }

    public long getPerson_id() {
        return person_id;
    }

    public void setPerson_id(long person_id) {
        this.person_id = person_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



}
