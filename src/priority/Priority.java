package priority;
import java.util.*;


public class Priority {
    ArrayList<Integer> arrival_Time = new ArrayList<>();
    ArrayList<Integer> burstTime = new ArrayList<>();
    ArrayList<Integer> completionTime = new ArrayList<>();
    ArrayList<Integer> waitingTime = new ArrayList<>();
    ArrayList<Integer> turnaroundTime = new ArrayList<>();
    ArrayList<Integer> responseTime = new ArrayList<>();
    ArrayList<Integer> priority = new ArrayList<>();
    ArrayList<Boolean> isCompleted = new ArrayList<>();
    ArrayList<Integer> startTime = new ArrayList<>(); // 👈 Added for Gantt Chart

    int currenttime = 0;
    public int numofprocess;

    public Priority(ArrayList<Integer> arrival_Time, ArrayList<Integer> burstTime, ArrayList<Integer> priority) {
        if (arrival_Time.size() != burstTime.size() || burstTime.size() != priority.size()) {
            throw new IllegalArgumentException("All input lists must have the same size.");
        }

        this.arrival_Time = new ArrayList<>(arrival_Time);
        this.burstTime = new ArrayList<>(burstTime);
        this.priority = new ArrayList<>(priority);
        this.numofprocess = arrival_Time.size();

        // Initialize all time lists
        for (int i = 0; i < numofprocess; i++) {
            completionTime.add(0);
            waitingTime.add(0);
            turnaroundTime.add(0);
            responseTime.add(0);
            startTime.add(0);  // 👈 Also initialize startTime
            isCompleted.add(false);
        }
    }

    public void schedule() {
        currenttime = 0;
        int completed = 0;

        while (completed < numofprocess) {
            int next = -1;
            int highestPrio = Integer.MAX_VALUE;

            for (int i = 0; i < numofprocess; i++) {
                if (!isCompleted.get(i) && arrival_Time.get(i) <= currenttime) {
                    if (priority.get(i) < highestPrio) {
                        highestPrio = priority.get(i);
                        next = i;
                    }
                }
            }

            if (next == -1) {
                currenttime++;
                continue;
            }

            startTime.set(next, currenttime); // 👈 Record the start time
            responseTime.set(next, currenttime - arrival_Time.get(next));
            currenttime += burstTime.get(next);
            completionTime.set(next, currenttime);
            turnaroundTime.set(next, currenttime - arrival_Time.get(next));
            waitingTime.set(next, turnaroundTime.get(next) - burstTime.get(next));
            isCompleted.set(next, true);

            completed++;
        }
    }

    public double getAverageWaitingTime() {
        if (numofprocess == 0) return 0;
        double total = 0;
        for (int time : waitingTime) {
            total += time;
        }
        return total / numofprocess;
    }

    public double getAverageTurnaroundTime() {
        if (numofprocess == 0) return 0;
        double total = 0;
        for (int time : turnaroundTime) {
            total += time;
        }
        return total / numofprocess;
    }

    public double getAverageResponseTime() {
        if (numofprocess == 0) return 0;
        double total = 0;
        for (int time : responseTime) {
            total += time;
        }
        return total / numofprocess;
    }

    public double getTotalCompletionTime() {
        int max = 0;
        for (int time : completionTime) {
            if (time > max) {
                max = time;
            }
        }
        return max;
    }

    // 👉 NEW FUNCTION TO GET START AND END TIMES FOR GANTT CHART
  public ArrayList<int[]> getGanttChartData() {
    ArrayList<int[]> ganttData = new ArrayList<>();
    
    // نضيف start و end في متغيرات منفصلة
    ArrayList<Integer> startTimes = new ArrayList<>();
    ArrayList<Integer> endTimes = new ArrayList<>();
    
    for (int i = 0; i < numofprocess; i++) {
        if (isCompleted.get(i)) {
            startTimes.add(startTime.get(i));
            endTimes.add(completionTime.get(i));
        }
    }

    // ترتيب بيانات البداية والنهاية حسب البداية
    ArrayList<int[]> sortedGanttData = new ArrayList<>();
    for (int i = 0; i < startTimes.size(); i++) {
        sortedGanttData.add(new int[]{startTimes.get(i)/*, endTimes.get(i)*/});
    }

    // فرز الـ sortedGanttData بناءً على وقت البداية
    sortedGanttData.sort(new Comparator<int[]>() {
        @Override
        public int compare(int[] process1, int[] process2) {
            return Integer.compare(process1[0], process2[0]);  // مقارنة على حسب البداية
        }
    });

    // إضافة العناصر المرتبة إلى ganttData
    ganttData.addAll(sortedGanttData);
    
    return ganttData;
}
}
