package tasks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static tasks.Type.SUBTASK;

public class Subtask extends Task {

    private int epicId;                                        // id эпика к которому относится подзадача

    public Subtask(String name, String description, Status status, int id, int epicId, int duration, LocalDateTime startTime) {
        super(name, description,status, id, duration, startTime);

        this.epicId = epicId;
    }
    @Override
    public Type getType(){
        return SUBTASK;
    }

    public int getEpicId(){                                     //Возвращаем id эпика
        return epicId;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.HH:mm"); //Шаблон для форматирования времени начала
        return id + "," + SUBTASK + "," + name + "," + status + "," + description + "," + duration + "," +
                startTime.format(formatter) + "," + epicId;
    }
}
