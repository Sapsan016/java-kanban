import java.util.ArrayList;

class Epic extends Task{   //Наследует от Task

private final ArrayList<Integer> subtasksIds; //Список id подзадач

    public Epic(String name, String description, Status status, int id) {
        super(name, description, status, id);
        subtasksIds = new ArrayList<>();
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
    public void removeSubtask(int id){                     //Удаляем подзадачи одного эпика
        subtasksIds.remove(id);
    }

    @Override
    public String toString() {
        return "Эпик{" + name +
                ", \nописание='" + description + '\'' +
                ", \nid='" + id + '\'' +
                ", \nстатус=" + status +
                ", \nid_позадач=" + subtasksIds +
                '}';
    }
}

