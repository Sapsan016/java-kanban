public class Managers {
    public static TaskManager getDefault(){
        return new InMemoryTaskManager();
    }
    public static FileBackedTasksManager getNewManager(){
        return new FileBackedTasksManager("testFile.csv");
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
