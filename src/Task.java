public class Task {
    protected String name;
    protected String description;
    protected int id;
    protected String status;

    public Task() {

    }

    public Task(String name, String description, String status, int id) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Задача{" + name +
                ", \nописание='" + description + '\'' +
                ", \nid='" + id + '\'' +
                ", \nстатус=" + status +
                "}";
    }
}
