public class Managers {
    public static TaskManager getDefault(){

        return new InMemoryTaskManager();
    }
    public static TaskManager getNewManager(){
        return new FileBackedTasksManager("testFile.csv");
    }



    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }



}
