import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    protected String name;
    protected String description;

    protected int id;

    protected Status status;
    protected int duration;
    protected LocalDateTime startTime;

    protected LocalDateTime endTime;

    protected DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.HH:mm"); //Шаблон для форматирования времени начала

    public Task(String name, String description, Status status, int id, int duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Type getType(){
        return Type.TASK;
    }


    public LocalDateTime getEndTime(){
        return endTime;
    }

    public void setEndTime() {
        this.endTime = startTime.plusMinutes(duration);;
    }

    public LocalDateTime getStartTime(){
        return startTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return id + "," + Type.TASK + "," + name + "," + status + "," + description + "," + duration + ","
                + startTime.format(formatter);
    }

    @Override
    public boolean equals(Object obj) {    //Переопределяем equals для сравнения
        if (this == obj) return true;      // проверяем адреса объектов
        if (obj == null) return false; // проверяем ссылку на null
        if (this.getClass() != obj.getClass()) return false; // сравниваем классы
        Task otherTask = (Task) obj; // открываем доступ к полям другого объекта
        return Objects.equals(id, otherTask.id) && // проверяем все поля
                Objects.equals(name, otherTask.name) &&
                Objects.equals(description, otherTask.description) &&
                Objects.equals(duration, otherTask.duration);

    }

    @Override
    public int hashCode() {

        return Objects.hash(id ,Type.TASK,name, status ,description ,duration ,startTime.format(formatter));
    }



    public void setStatus(Status newStatus) {
        status = newStatus;

    }
}
