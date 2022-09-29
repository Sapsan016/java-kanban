package Managers;

import Tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final CustomLinkedList<Task> customHistoryList = new CustomLinkedList<>(); //Список просмотренных задач
    private final Map<Integer, Node<Task>> historyMap = new HashMap<>();

    @Override

    public void add(Task task) {
        if (task != null) {                                               //Проверяем, что задача ранее не удалена
            if (historyMap.containsKey(task.id)) {                        //Если задача уже есть в истории,
                remove(task.id);                                          //то сначала удаляем задачу из истории,
                historyMap.put(task.id, customHistoryList.linkLast(task));//потом снова добавляем в мапу и конец списка

            } else {
                historyMap.put(task.id, customHistoryList.linkLast(task));//Добавляем новую задачу в мапу
                                                                          // и в конец списока истории
            }
        }
    }

    @Override
    public List<Task> getHistory() {                        //Возвращаем список истории просмотров
        return customHistoryList.getTasks();
    }

    @Override
    public void remove(int id) {                            //Удаляем задачу из истории
        if (historyMap.containsKey(id)) {                   //Проверяем наличие записи задачи в мапе

            customHistoryList.removeNode(historyMap.get(id));
        }
    }
}

class CustomLinkedList<T> {                          //Создаем двусвязный список
    public Node<Task> head;

    public Node<Task> tail;

    private int size = 0;

    public Node<Task> getHead() {
        return head;
    }

    public Node<Task> linkLast(Task task) {                           //Добавляем элемент в конец списка

        Node<Task> oldTail = tail;                                    //Запоминаем "хвост"
        Node<Task> newNode = new Node<>(oldTail, task, null);   // Создаем новый последний узел
        tail = newNode;                                               //Присваиваем новому узлу значение "хвост"
        if (oldTail == null)                                         //Если запомненый "хвост" пустой то,
            head = newNode;                                          // присваиваем новому узлу значение "голова"
        else
            oldTail.next = newNode;                                //Иначе новый узел встает последним
        size++;
        return newNode;
    }

    public ArrayList<Task> getTasks() {                      //Перекладываем просмотренные задачи в ArrayList
        ArrayList<Task> historyList = new ArrayList<>();
        Node<Task> temp = getHead();                         //Создаем временную переменную
        while (temp != null) {                               //Если в ней есть данные,
            historyList.add(temp.getData());                 //Добавляем в Arraylist
            temp = temp.getNext();                           //Изменяем значение переменной
        }
        return historyList;
    }

    public void removeNode(Node<Task> node) {            //Удаляем узел из списка
        Node<Task> prev = node.prev;
        Node<Task> next = node.next;

        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
            node.prev = null;
        }
        if (next == null) {
            tail = prev;
        } else {
            next.prev = prev;
            node.next = null;
        }
        size--;
    }
}