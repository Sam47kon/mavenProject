package GrandTasks;

import java.util.Scanner;

// Среднее арифметическое введеных нескольких чисел чисел
public class Task4 {
    public static void main(String[] args) {
        double average = new Task4().getArithmeticMeanOfSeveralNumbers();
        System.out.println("Среднее арифметическое чисел: " + average);
    }

    public double getArithmeticMeanOfSeveralNumbers() {
        Scanner reader = new Scanner(System.in);
        System.out.printf("Введите количество чисел:");
        int[] numbers = new int[reader.nextInt()];
        double sum = 0;
        for (int i = 0; i < numbers.length; i++) {
            System.out.println("Enter " + ++i + " number");
            i--;
            numbers[i] = reader.nextInt();
            sum += numbers[i];
        }
        double average = sum / numbers.length;
        return average;
    }
}