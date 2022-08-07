public class Subtask extends Task{

    private final int epicId;                                        // id эпика к которому относится подзадача

    public Subtask(String name, String description, Status status, int id, int epicId) {
        super(name, description, status, id);
        this.epicId = epicId;
    }

    public int getEpicId(){                                     //Возвращаем id эпика
        return epicId;
    }

    @Override
    public String toString() {
        return "Подзадача{" + name +
                ", \nописание='" + description + '\'' +
                ", \nid='" + id + '\'' +
                ", \nстатус=" + status +
                ", \nid эпика=" + epicId +
                '}';
    }
}
