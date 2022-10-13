package Tasks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static Tasks.Type.EPIC;

public class Epic extends Task {   //Наследует от Tasks.Task

private final ArrayList<Integer> subtasksIds; //Список id подзадач

      public Epic(String name, String description, Status status, int id, int duration, LocalDateTime startTime) {
        super(name, description, status, id, duration, startTime);
        subtasksIds = new ArrayList<>();
    }

    public void setEndTime(LocalDateTime endTime) {                //Устанавливаем время окончания эпика
        this.endTime = endTime;
    }

    public void setStartTime(LocalDateTime startTime) {              //Устанавливаем время эпика
                  this.startTime = startTime;
                   }

    public void setDuration(int duration) {                         //Устанавливаем продолжительность эпика
        this.duration = duration;

    }
    @Override
    public Type getType(){
        return EPIC;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setSubtasksIds(int id){                     //Добавляем id подзадач в поле эпика
        subtasksIds.add(id);
    }
    public ArrayList<Integer> getSubtasksIds(){            //Возвращем список id подзадач
       return subtasksIds;
    }

    public void setStatus(Status newStatus){                //Изменяем статус в зависимости от статуса подзадач
        status = newStatus;

    }
    public void removeAllSubtasksIds(){                        //Удаляем все id подзадач
        subtasksIds.clear();
    }
    public void removeSubtask(Subtask id){                     //Удаляем подзадачи одного эпика
        subtasksIds.remove((Integer) id.getId());
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.HH:mm"); //Шаблон для форматирования времени начала
        return id + "," + EPIC + "," + name + "," + status + "," + description + "," + duration + "," +
                startTime.format(formatter) + "," +
                 subtasksIds.toString().replace("[", "").replace("]",
                         "").replaceAll("\\s+","");

    }
}

