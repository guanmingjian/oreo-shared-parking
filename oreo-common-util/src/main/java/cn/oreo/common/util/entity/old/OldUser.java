package cn.oreo.common.util.entity.old;

public class OldUser {
    private Integer id;

    private String name;

    private String password;

    private String sex;

    private Integer age;

    private String phoneNumber;

    private String email;

    private String birth;

    private String type;

    private String label;

    private String registerTime;

    private String headImage;

    private String status;

    public OldUser() {
    }

    public OldUser(Integer id, String name, String password, String sex, Integer age, String phoneNumber, String email, String birth, String type, String label, String registerTime, String headImage, String status) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.sex = sex;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.birth = birth;
        this.type = type;
        this.label = label;
        this.registerTime = registerTime;
        this.headImage = headImage;
        this.status = status;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", sex='" + sex + '\'' +
                ", age=" + age +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", birth='" + birth + '\'' +
                ", type='" + type + '\'' +
                ", label='" + label + '\'' +
                ", registerTime='" + registerTime + '\'' +
                ", headImage='" + headImage + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber == null ? null : phoneNumber.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth == null ? null : birth.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label == null ? null : label.trim();
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime == null ? null : registerTime.trim();
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage == null ? null : headImage.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }
}