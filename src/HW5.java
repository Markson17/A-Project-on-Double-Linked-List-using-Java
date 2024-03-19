import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class HW5 {

    public static void main(String[] args) {
        // Create an instance of the SkipListMap to store activities
        FakeRandHeight random = new FakeRandHeight();
        SkipListMap<Integer, String> activityTracker = new SkipListMap<>(random);
        
        // Read input commands from the specified file
        String inputFile = args[0]; // Assuming the input file is provided as a command-line argument

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Parse and execute each input command
                executeCommand(line, activityTracker);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void executeCommand(String command, SkipListMap<Integer, String> activityTracker) {
        // Split the command into tokens
        String[] tokens = command.split("\\s+");
        
        // Determine the type of command and execute the corresponding operation
        switch (tokens[0]) {
            case "AddActivity":
                int time = Integer.parseInt(tokens[1]);
                String activity = tokens[2];
                activityTracker.put(time, activity);
                System.out.println(command);                
                break;
            case "RemoveActivity":
                time = Integer.parseInt(tokens[1]);
                activityTracker.remove(time);
                System.out.println(command);
                
                break;
            case "GetActivity":
                time = Integer.parseInt(tokens[1]);
                String result = activityTracker.get(time);
                if (result != null) {
                    System.out.println(command + " " + result);
                } else {
                    System.out.println(command + " none");
                }
                break;
            case "GetActivitiesBetweenTimes":
                int startTime = Integer.parseInt(tokens[1]);
                int endTime = Integer.parseInt(tokens[2]);
                SkipListMap<Integer, String> activitiesBetweenTimes = activityTracker.subMap(startTime, endTime);
                if (!activitiesBetweenTimes.isEmpty()) {
                    StringBuilder output = new StringBuilder(command);
                    activitiesBetweenTimes.forEach((k, v) -> output.append(" ").append(k).append(":").append(v));
                    System.out.println(output);
                } else {
                    System.out.println(command + " none");
                }
                break;
            case "GetActivitiesForOneDay":
                int date = Integer.parseInt(tokens[1]);
                int startOfDay = date * 10000; // Start of the day
                int endOfDay = (date + 1) * 10000 - 1; // End of the day
                SkipListMap<Integer, String> activitiesForOneDay = activityTracker.subMap(startOfDay, endOfDay);
                if (!activitiesForOneDay.isEmpty()) {
                    StringBuilder output = new StringBuilder(command);
                    activitiesForOneDay.forEach((k, v) -> output.append(" ").append(k).append(":").append(v));
                    System.out.println(output);
                } else {
                    System.out.println(command + " none");
                }
                break;
            case "GetActivitiesFromEarlierInTheDay":
                int currentTime = Integer.parseInt(tokens[1]);
                int startOfCurrentDay = currentTime / 10000 * 10000; // Start of the current day
                SkipListMap<Integer, String> activitiesEarlierInTheDay = activityTracker.subMap(startOfCurrentDay, currentTime);
                if (!activitiesEarlierInTheDay.isEmpty()) {
                    StringBuilder output = new StringBuilder(command);
                    activitiesEarlierInTheDay.forEach((k, v) -> output.append(" ").append(k).append(":").append(v));
                    System.out.println(output);
                } else {
                    System.out.println(command + " none");
                }
                break;
            case "PrintSkipList":
                System.out.println(command);
                activityTracker.printSkipList();
                break;
            default:
                // Unsupported command
                System.err.println("Unsupported command: " + command);
        }
    }
}
