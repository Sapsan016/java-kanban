public class Task {
    protected String name;
    protected String description;
    protected int id;
    protected Status status;

    public Task(String name, String description, Status status, int id) {
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
        return id + "," + Type.TASK + "," + name + "," + status + "," + description;
    }


    public void setStatus(Status newStatus) {
        status = newStatus;

    }
}
