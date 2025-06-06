package view;

public class WandComponents {
    private final int coreId;
    private final int woodId;

    public WandComponents(int coreId, int woodId) {
        this.coreId = coreId;
        this.woodId = woodId;
    }

    public int getCoreId() {
        return coreId;
    }

    public int getWoodId() {
        return woodId;
    }

    @Override
    public String toString() {
        return "WandComponents{" +
                "coreId=" + coreId +
                ", woodId=" + woodId +
                '}';
    }
}