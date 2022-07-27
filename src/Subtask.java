public class Subtask extends Task{

    private int epicId;                                        // id эпика к которому относится подзадача

    public Subtask(String name, String description, String status, int id, int epicId) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
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
