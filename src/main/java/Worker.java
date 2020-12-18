import java.util.Arrays;
import java.util.Collections;

public class Worker {
    /*
    Two-dimensional array to keep a record of the status of the workers. If we have n rows of workers and m slots on the belt
    we have an array of workers[n][m]. So if we need to know what a worker has on row 2, slot 3, we get the value of workers[1][2].
     */
    private String[][] workers;
    // Conveyor belt
    private Belt belt;
    // Time the worker needs to create the product
    private static final int TIME_TO_CREATE_PRODUCT = 4;
    // Row of workers
    public static final int ROW_OF_WORKERS = 2;

    Worker(Belt belt) {
        this.workers = new String[ROW_OF_WORKERS][Belt.BELT_SLOT];
        // Initializing the workers status to "" meaning they hold no product at the beginning and they are free
        for (int i = 0; i < ROW_OF_WORKERS; i++) {
            for (int j = 0; j < Belt.BELT_SLOT; j++) {
                workers[i][j] = "";
            }
        }
        this.belt = belt;
    }

    // Used for getting workers status and printing it
    public String[][] getWorkers() {
        return workers;
    }

    public void checkBelt() {
        Integer[] randomNumbers;
        for (int i = 0; i < Belt.BELT_SLOT; i++) {
            randomNumbers = makeRandom();
            boolean didTake = false;
            // We loop over all the workers on slot i, If for example worker on row 1 cannot take product from slot 2 we still give chance to other workers
            // on the slot 2 to take the product
            for (int j = 0; j < ROW_OF_WORKERS; j++) {
                if (workerNeedsToWait(i, randomNumbers[j])) { // Selected worker is busy making product select other workers
                    continue;
                }
                /* Selected worker takes a component from belt so we set the boolean to true that other workers on the same slot
                cannot put products on the belt any more but we iterate over other workers on the same slot so that if they are creating product,
                we decrease the the time by one unit. Please note that when the worker takes the component we empty the belt.*/
                if (workerTookProductFromBelt(i, randomNumbers[j])) {
                    didTake = true;
                    continue;
                }
                if (workerPutProduct(i, randomNumbers[j]) && !didTake) { // selected worker put the product on the belt
                    break;
                }
            }
        }

    }

    private boolean workerTookProductFromBelt(int slotIndex, int workerRow) {
        String workerHands = workers[workerRow][slotIndex];
        // If there is product on the belt or the belt is empty, do nothing
        if (belt.getBeltStatus(slotIndex) == BeltStatus.Product || belt.getBeltStatus(slotIndex) == BeltStatus.Empty) {
            return false;
        }
        // If worker has product on one hand can have only one component
        if (workerHands.contains("P") && (workerHands.contains("A") || workerHands.contains("B"))) {
            return false;
        }
        // If worker has already the component on the belt in it's hand we don't take it
        if (!workerHands.contains(belt.getBeltStatus(slotIndex).name().substring(0, 1))) {
            // Workers add first letter of the component to it's status as it is in it's hands
            workers[workerRow][slotIndex] = workerHands + (belt.getBeltStatus(slotIndex).name().substring(0, 1));
            // We empty the belt
            belt.emptyBeltSlot(slotIndex);
            return true;
        }
        return false;
    }

    private boolean workerNeedsToWait(int slotIndex, int workerRow) {
        String workerHands = workers[workerRow][slotIndex];
        // If worker has nothing in it's hands we don't need to work
        if (workerHands.equals("")) {
            return false;
        }

        // If worker has only product in it' hands, it means product is ready don't need to wait
        if (workerHands.equals("P") && workerHands.length() == 1) {
            return false;
        }

        // We check if the worker has all the components in it's hand if yes we set hasAllComponents to true
        boolean hasAllComponents = false;
        for (Products product : Products.values()) {
            hasAllComponents = workerHands.contains(product.name().substring(0, 1));
            if (!hasAllComponents) break;
        }

        // We check if the worker has all the components in it's hand and timer is 1 which means time to make the product slot is finished
        if (hasAllComponents && workerHands.contains("1")) {
            workers[workerRow][slotIndex] = "P";
            return true;
        }
        // We check if the worker has all the components in it's hand and we need to start the timer to make the product after n slots
        if (hasAllComponents && workerHands.length() == Products.values().length) {
            workers[workerRow][slotIndex] = workerHands + (TIME_TO_CREATE_PRODUCT - 1);
            return true;
        }
        // worker is in the process of making the product, so we decrease one time unit
        if (hasAllComponents && workerHands.length() == Products.values().length + 1) {
            int timeSpent = Integer.parseInt(workerHands.substring(workerHands.length() - 1));
            workers[workerRow][slotIndex] = workerHands.substring(0, workerHands.length() - 1) + (timeSpent - 1);
            return true;
        }
        return false;
    }

    private boolean workerPutProduct(int slotIndex, int workerRow) {
        // If belt is empty and we have the product put the product on the belt
        if (belt.getBeltStatus(slotIndex) == BeltStatus.Empty && workers[workerRow][slotIndex].contains("P")) {
            belt.putProductOnTheBelt(slotIndex);
            // Remove product from workers hand
            workers[workerRow][slotIndex] = workers[workerRow][slotIndex].replace("P", "");
            return true;
        }
        return false;
    }

    /*
    In order to make the selection of workers on each slot fair, we make an array with size of "n" which "n" is rows of workers and
    fill it with numbers from 0 to n. Then we shuffle it to have an array of random numbers from 0 to n with no duplication.
    So for example [4,1,2,5,3] means first worker on row 4 then row 1 then row 2 and so on ...
    */
    private Integer[] makeRandom() {
        Integer[] arr = new Integer[ROW_OF_WORKERS];
        for (int i = 0; i < ROW_OF_WORKERS; i++) {
            arr[i] = i;
        }
        Collections.shuffle(Arrays.asList(arr));
        return arr;
    }

}
