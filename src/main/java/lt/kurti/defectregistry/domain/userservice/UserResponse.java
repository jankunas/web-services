package lt.kurti.defectregistry.domain.userservice;

public class UserResponse {

    private Data data;
    private String message;

    public Data getData() {
        return data;
    }

    public void setData(final Data data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }
}
