import java.util.Random;

enum BeltStatus{
    Empty,
    A,
    B,
    Product;

    // In order to make put random items on the belt
    public static BeltStatus generateRandomBeltStatus() {
        BeltStatus[] values = BeltStatus.values();
        int length = values.length -1;
        int randIndex = new Random().nextInt(length);
        return values[randIndex];
    }
}

enum Products {
    A,
    B
}

public class Belt {
    static final int BELT_SLOT = 3;
    private int[] sums;
    private BeltStatus[] beltStatus;

    Belt() {
        // set initial state of the belt
        this.beltStatus = new BeltStatus[BELT_SLOT];
        for (int i = 0; i < BELT_SLOT; i++) {
            beltStatus[i] = BeltStatus.generateRandomBeltStatus();
        }

        this.sums = new int[BeltStatus.values().length];
    }

    public BeltStatus[] getBeltStatus() {
        return beltStatus;
    }

    public void putProductOnTheBelt(int index) {
        beltStatus[index] = BeltStatus.Product;
    }

    public void emptyBeltSlot(int index) {
        beltStatus[index] = BeltStatus.Empty;
    }

    public BeltStatus getBeltStatus(int index) {
        return beltStatus[index];
    }

    // Move belt one slot and put random item on it
    public void moveBelt() {
        countItems(beltStatus[beltStatus.length-1]);
        System.arraycopy(beltStatus, 0, beltStatus, 1, beltStatus.length-1);
        beltStatus[0] = BeltStatus.generateRandomBeltStatus();
    }

    // We count number of each item that pass the belt
    private void countItems(BeltStatus beltStatus) {
        for (BeltStatus items : BeltStatus.values()) {
            if (beltStatus.name().equals(items.name())) {
                sums[beltStatus.ordinal()]++;
            }
        }
    }

    public void printResults() {
        for (int i = 0; i < sums.length; i++) {
            System.out.println(sums[i] + " " + BeltStatus.values()[i]);
        }
    }
}
