import manager.TaskManager;
import manager.Managers;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;


public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();
        Task task1 = taskManager.createTask("Купить помидоры", "Выбрать в самокате");
        Task task2 = taskManager.createTask("Купить огурцы", "Зайти на рынок");
        Epic epic1 = taskManager.createEpic("Приготовить обед", "Купить продукты");
        Subtask subtask = taskManager.createSubtask(epic1.getId(), "Заказать рис", "Рис Басмати");
        Epic epic2 = taskManager.createEpic("Убраться в квартире", "Успеть сделать все до четверга");
        Subtask subtask1 = taskManager.createSubtask(epic2.getId(), "Помыть полы", "Использовать средство");
        Subtask subtask2 = taskManager.createSubtask(epic2.getId(), "Протереть пыль", "Тряпка в шкафу");

        System.out.println(task1.toString());
        System.out.println(task2.toString());
        System.out.println(epic1.toString());
        System.out.println(subtask.toString());
        System.out.println(epic2.toString());
        System.out.println(subtask1.toString());
        System.out.println(subtask2.toString());

        task1.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(task1);
        System.out.println(task1.toString());

        task2.setStatus(Status.DONE);
        taskManager.updateTask(task2);
        System.out.println(task2.toString());

        subtask.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(epic1.getId(), subtask);
        System.out.println(epic1.toString());

        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getTasks());

        System.out.println(taskManager.getTaskById(task1.getId()));
        System.out.println(taskManager.getEpicById(epic1.getId()));
        System.out.println(taskManager.getEpicById(epic2.getId()));

        System.out.println(taskManager.getHistory());
    }
}
