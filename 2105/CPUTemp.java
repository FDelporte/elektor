import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;

public class CPUTemp {
 
    private static final String FILE = "/sys/class/thermal/thermal_zone0/temp";
    private static final List<Integer> values = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {
    	while(true) {
        	checkTemp();
		Thread.sleep(1000);
        }
       
    }

    private static void checkTemp() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            int value = Integer.valueOf(br.readLine());
            values.add(value);
            int total = values.stream().mapToInt(Integer::valueOf).sum();
            System.out.println("Now: " + value 
                + " - Average: " + (total / values.size())
                + " - Number of measurements: " + values.size());
        } catch (Exception ex) {
            System.err.println("Error during temperature check: " + ex.getMessage());
        }
    }
}
