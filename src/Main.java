import manager.TaskManager;
import manager.Managers;
import task.Epic;
import task.Subtask;
import task.Task;


public class Main {
    public static void main(String[] args) {
      test();
    }

    public static void test(){
        TaskManager taskManager = Managers.getDefault();
        Task task1 = taskManager.createTask("Купить помидоры", "Выбрать в самокате");
        Task task2 = taskManager.createTask("Купить огурцы", "Зайти на рынок");
        Epic epic1 = taskManager.createEpic("Приготовить обед", "Купить продукты");
        Epic epic2 = taskManager.createEpic("Убраться в квартире", "Успеть сделать все до четверга");
        Subtask subtask1 = taskManager.createSubtask(epic2.getId(), "Помыть полы", "Использовать средство");
        Subtask subtask2 = taskManager.createSubtask(epic2.getId(), "Протереть пыль", "Тряпка в шкафу");
        Subtask subtask3 = taskManager.createSubtask(epic2.getId(), "Заказать рис", "Рис Басмати");

        taskManager.getHistory();

        taskManager.getTaskById(task2.getId());
        taskManager.getTaskById(task1.getId());
        System.out.println("1_______");
        System.out.println(taskManager.getHistory());


        taskManager.getEpicById(epic1.getId());
        taskManager.getTaskById(task2.getId());
        System.out.println("2_______");
        System.out.println(taskManager.getHistory());


        taskManager.getTaskById(task1.getId());
        taskManager.getEpicById(epic1.getId());
        taskManager.getEpicById(epic2.getId());

        System.out.println("3________");
        System.out.println(taskManager.getHistory());
        taskManager.removeTaskById(task1.getId());

        System.out.println("4________");
        System.out.println(taskManager.getHistory());

        taskManager.removeEpicById(epic2.getId());
        System.out.println("5________");
        System.out.println(taskManager.getHistory());

    }
}
