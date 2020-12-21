import java.io.IOException;
import java.util.Scanner;

public class RaspiGpio {
 
    private static final String LED_1 = "14";
    private static final String LED_2 = "15";
    private static final String BUTTON = "18";

    private enum Action {
        OUTPUT_PIN("op"),
        INPUT_PIN("ip"),
        DIGITAL_HIGH("dh"),
        DIGITAL_LOW("dl");
        private final String action;
        Action(String action) {
            this.action = action;
        }
        String getAction() {
            return action;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // Configure the two LEDs as output pins
        doAction(LED_1, Action.OUTPUT_PIN);
        doAction(LED_2, Action.OUTPUT_PIN);

        // Configure the button as input pin
        doAction(BUTTON, Action.INPUT_PIN);

        // Blink the LEDs with different interval
        for (int i = 0; i < 20; i++) {
            System.out.println("Blink loop: " + i);
            if (i % 2 == 0) {
                doAction(LED_1, Action.DIGITAL_HIGH);
            } else {
                doAction(LED_1, Action.DIGITAL_LOW);
            }
            if (i % 3 == 0) {
                doAction(LED_2, Action.DIGITAL_HIGH);
            } else {
                doAction(LED_2, Action.DIGITAL_LOW);
            }
            Thread.sleep(500);
        }       
        doAction(LED_1, Action.DIGITAL_LOW);
        doAction(LED_2, Action.DIGITAL_LOW);

        // Read the button state 10 times
        for (int j = 0; j < 10; j++) {
            String result = runCommand("raspi-gpio get " + BUTTON);
            System.out.println("Button check " + j
                + ": " + result
                + " - PUSHED: " + (result.contains("level=1")));
            Thread.sleep(1000);
        }
    }

    private static void doAction(String pin, Action action) {
        runCommand("raspi-gpio set " + pin + " " + action.getAction().toLowerCase());
    }

    private static String runCommand(String cmd) {
        System.out.println("Executing: " + cmd);
        Scanner s = null;
        try {
            s = new Scanner(Runtime.getRuntime().exec(cmd).getInputStream())
                .useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        } catch (Exception ex) {
            System.err.println("Error during command: " + ex.getMessage());
            return "";
        } finally {
            if (s != null) {
                s.close();
            }
        }
    }
}
