package Tasks;

import Tasks.Status;
import Tasks.Task;
import Tasks.Type;

import java.time.LocalDateTime;

public class Subtask extends Task {

    private int epicId;                                        // id эпика к которому относится подзадача

    public Subtask(String name, String description, Status status, int id, int epicId, int duration, LocalDateTime startTime) {
        super(name, description,status, id, duration, startTime);

        this.epicId = epicId;
    }
    @Override
    public Type getType(){
        return Type.SUBTASK;
    }

    public int getEpicId(){                                     //Возвращаем id эпика
        return epicId;
    }

    @Override
    public String toString() {
        return id + "," + Type.SUBTASK + "," + name + "," + status + "," + description + "," + duration + "," +
                startTime.format(formatter) + "," + epicId;
    }
}
