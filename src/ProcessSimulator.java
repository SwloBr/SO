import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProcessSimulator {
    public static void saveProcessTable(List<Process> processes) {
        try (FileWriter writer = new FileWriter("process_table.txt")) {
            for (Process process : processes) {
                writer.write(process.toString() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void executeProcess(Process process, int quantum, Random random, List<Process> processes) {
        process.updateState("EXECUTANDO");
        saveProcessTable(processes);
        int remainingCycles = quantum;

        while (remainingCycles > 0 && process.getTp() < process.getTotalExecutionTime()) {
            if (random.nextDouble() < 0.01) {
                process.performIoOperation();
                saveProcessTable(processes);
                return;
            }

            process.incrementTp();
            remainingCycles--;
        }

        if (process.getTp() >= process.getTotalExecutionTime()) {
            finalizeProcess(process);
        } else {
            contextSwitch(process, processes);
        }
    }

    public static void finalizeProcess(Process process) {
        System.out.printf("Processo %d FINALIZADO. em: %s%n", process.getPid(), process);
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        process.setState("FINALIZADO");
    }

    public static void contextSwitch(Process process, List<Process> processes) {
        if (process.getState().equals("EXECUTANDO")) {
            process.updateState("PRONTO");
            saveProcessTable(processes);
        }
    }

    public static void checkBLOQUEADOProcess(Process process, Random random, List<Process> processes) {
        if (process.getState().equals("BLOQUEADO") && random.nextDouble() < 0.3) {
            process.updateState("PRONTO");
            saveProcessTable(processes);
        }
    }

    public static void simulate() {
        List<Process> processes = initializeProcesses();
        int FINALIZADOProcesses = 0;
        int quantum = 1000;
        Random random = new Random();

        while (FINALIZADOProcesses < processes.size()) {
            for (Process process : processes) {
                if (process.getState().equals("PRONTO")) {
                    executeProcess(process, quantum, random, processes);
                    if (process.getState().equals("FINALIZADO")) {
                        FINALIZADOProcesses++;
                    }
                } else if (process.getState().equals("BLOQUEADO")) {
                    checkBLOQUEADOProcess(process, random, processes);
                }
            }
        }
    }

    public static List<Process> initializeProcesses() {
        List<Process> processes = new ArrayList<>();
        processes.add(new Process(0, 10000));
        processes.add(new Process(1, 5000));
        processes.add(new Process(2, 7000));
        processes.add(new Process(3, 3000));
        processes.add(new Process(4, 3000));
        processes.add(new Process(5, 8000));
        processes.add(new Process(6, 2000));
        processes.add(new Process(7, 5000));
        processes.add(new Process(8, 4000));
        processes.add(new Process(9, 10000));
        return processes;
    }

    public static void main(String[] args) {
        simulate();
    }
}
