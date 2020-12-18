import java.util.Arrays;

public class Main {
    private static final int BELT_SIZE = 100;
    public static void main(String[] args) {
       Belt belt = new Belt();
        Worker worker = new Worker(belt);
        for (int i = 0; i <= BELT_SIZE; i++) {

            System.out.println(Arrays.toString(belt.getBeltStatus()) + " Status of the belt before workers taking the products");

            worker.checkBelt();
            printStatus(worker, belt);

        }
        // Print number of missed items and products that has been made
        belt.printResults();
    }

    // Printing status of the belt and workers
    private static void printStatus(Worker worker, Belt belt) {
        for (int l = 0; l < Worker.ROW_OF_WORKERS; l++) {
            for (int j = 0; j < Belt.BELT_SLOT; j++) {
                System.out.print(worker.getWorkers()[l][j]);
                System.out.print("  -------  ");
            }
            System.out.println();
        }
        System.out.println(Arrays.toString(belt.getBeltStatus()) + " Status of the belt after workers taking the products");
        System.out.println("--------------------------------------------------");
        belt.moveBelt();
    }
}
