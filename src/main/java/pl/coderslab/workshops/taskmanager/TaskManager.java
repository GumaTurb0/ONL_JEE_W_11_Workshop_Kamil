package pl.coderslab.workshops.taskmanager;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import pl.coderslab.ConsoleColors;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
//import java.sql.ResultSet;
import java.util.Scanner;

public class TaskManager {
    private static final String[] OPTIONS = {"add", "remove", "list", "exit"};
    private static final String EXIT_OPTION = "exit";
    private static final String TASKS_FILE_NAME = "tasks.csv";

    //przechowywanie danych
    private static String[][] tasks = new String[0][];

    // główna metoda
    public static void main(String[] args) {
        run();
    }

    public static void run() {
        printWelcomeMessage();
        loadTasks();
        while (true) {
            printMenu();
            String option = selectOption();
            if (!validOption(option)) {
                printErrorMessage(option);
                continue;
            }
            executeOption(option);
            if (isExitOption(option)) {
                break;
            }
        }
        printExitMessage();
        saveTasks();
    }

    private static void loadTasks() {
        StringBuilder dataBulder = new StringBuilder();
        try (Scanner scanner = new Scanner(new File(TASKS_FILE_NAME))) {
            while (scanner.hasNextLine()) {
                dataBulder.append(scanner.nextLine()).append(";");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] taskLines = dataBulder.toString().split(";");
        for (String taskLine : taskLines) {
            String[] task = taskLine.split(",");
            tasks = ArrayUtils.add(tasks, task);
        }
        System.out.println(ConsoleColors.BLUE + tasks.length + " task has been read" + ConsoleColors.RESET);
    }

    private static void saveTasks() {
        try (PrintWriter writer = new PrintWriter(new File(TASKS_FILE_NAME))) {
            for (String[] task : tasks) {
                String taskLine = StringUtils.join(task, ",");
                writer.println(taskLine);
            }
            System.out.println(ConsoleColors.PURPLE + tasks.length + " tasks has been written" + ConsoleColors.RESET);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void printWelcomeMessage() {
        String userName = System.getProperty("user.name");
        System.out.println("Hello " + userName);
    }

//    public static void printOptions () {
//        System.out.println(ConsoleColors.BLUE + ConsoleColors.WHITE_UNDERLINED);
//        System.out.println("What you want to do:" + ConsoleColors.RESET);
//
//        String[] menu = {"add", "remove", " list", "exit"};
//        for (String option : menu) {
//            System.out.println(option);
//        }
    private static void printExitMessage() {
        System.out.println(ConsoleColors.BLUE + "Bye, bye" + ConsoleColors.RESET);
    }
    private static void executeOption(String option) {
        switch ( option){
            case "add": {
                addTask();
                break;
            }
            case "remove": {
                removeTask();
                break;
            }
            case "list": {
                listTasks();
                break;
            }
        }
    }
    private static void listTasks () {
        for (int i = 0; i< tasks.length; i++) {
            String[] task = tasks[i];
            System.out.printf("%d : %s %s%n", i, task[0], task[1], task[2]);
        }
    }
    private static void removeTask() {
        if (tasks.length == 0) {
            System.out.println(ConsoleColors.RED + "No tasks to remove" + ConsoleColors.RESET);
        }
        System.out.println(ConsoleColors.BLUE + "Please select number to remove: " + ConsoleColors.RESET);
        Scanner scanner = new Scanner(System.in);
        int index;
        while (true) {
            while (!scanner.hasNextLine()) {
                scanner.nextLine();
                System.out.println(ConsoleColors.RED + "Invalid argument passed. Please give number between 0 and " + (tasks.length - 1) + ": " + ConsoleColors.RESET);
            }
            index = scanner.nextInt();
            if (index >= 0 && index < tasks.length) {
                break;
            } else {
                System.out.println(ConsoleColors.RED + "Invalid argument passed. Please give number between 0 and " + (tasks.length -1) + ": " + ConsoleColors.RESET);
            }
        }
        tasks = ArrayUtils.remove(tasks, index);
        System.out.println("Task was successfully deleted");
    }
    private static void addTask(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please add task description: ");
        String description = scanner.nextLine();
        System.out.println("Please add task due date: ");
        String dueDate = scanner.nextLine();
        String important = null;
        do {
            System.out.println("Is task is important (true/false)? ");
            important = scanner.nextLine();
        }while (!("false".equals(important) || "true".equals(important)));
        tasks = ArrayUtils.add(tasks, new String[] {description, dueDate, important});
        System.out.println("Task was successfully added");
    }
    private static boolean isExitOption(String option) {
        return EXIT_OPTION.equalsIgnoreCase(option);
    }
    private static void printErrorMessage(String option) {
        System.out.println(ConsoleColors.RED + "Invalid menu options: '" + option + "'" + ConsoleColors.RESET);
    }
    private static boolean validOption(String option) {
        return ArrayUtils.contains(OPTIONS, option);
    }
    private static String selectOption() {
        Scanner scnner = new Scanner(System.in);
        return scnner.nextLine();
    }
    private static void printMenu() {
        System.out.println("Available options: ");
        for (String option : OPTIONS) {
            System.out.println(" " + option);
        }
        System.out.println(ConsoleColors.BLUE + "Please select an option: " + ConsoleColors.RESET);
    }
}
