package Login;

public enum options {
    Admin, Customer;

    private options(){}

    public String value(){
        return name();
    }

    public static options fromValue(String value){
        return valueOf(value);
    }
}
