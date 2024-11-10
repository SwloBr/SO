class Process {
    private int pid;
    private int totalExecutionTime;
    private int tp = 0;
    private int cp = 1;
    private String state = "PRONTO";
    private int ioCount = 0;
    private int cpuCount = 0;

    public Process(int pid, int totalExecutionTime) {
        this.pid = pid;
        this.totalExecutionTime = totalExecutionTime;
    }

    @Override
    public String toString() {
        return String.format("PID %-2d | Estado: %-10s | TP: %-5d | CP: %-5d | NES: %-3d | N_CPU: %-3d",
                pid, state, tp, cp, ioCount, cpuCount);
    }

    public void updateState(String newState) {
        System.out.printf("PID %d: %s >>> %s | TP: %d, CP: %d, NES: %d, N_CPU: %d%n",
                pid, state, newState, tp, cp, ioCount, cpuCount);
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        state = newState;
        if (newState.equals("EXECUTANDO")) {
            cpuCount++;
        }
    }

    public void performIoOperation() {
        ioCount++;
        updateState("BLOQUEADO");
    }

    public int getPid() {
        return pid;
    }

    public int getTp() {
        return tp;
    }

    public void incrementTp() {
        tp++;
        cp = tp + 1;
    }

    public int getTotalExecutionTime() {
        return totalExecutionTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
