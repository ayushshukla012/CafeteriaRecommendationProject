package cafemanagement.model;

public class PollItem {
    private int pollItemId;
    private int pollId;
    private int menuItemId;
    private String itemName;
    private Menu menuItem;

    public PollItem(int pollItemId, int pollId, int menuItemId, String itemName) {
        this.pollItemId = pollItemId;
        this.pollId = pollId;
        this.menuItemId = menuItemId;
        this.itemName = itemName;
    }

    public int getPollItemId() {
        return pollItemId;
    }

    public int getPollId() {
        return pollId;
    }

    public int getMenuItemId() {
        return menuItemId;
    }

    public String getItemName() {
        return itemName;
    }

    public Menu getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(Menu menuItem) {
        this.menuItem = menuItem;
    }
    
    @Override
    public String toString() {
        return "PollItem{" +
                "pollItemId=" + pollItemId +
                ", pollId=" + pollId +
                ", menuItemId=" + menuItemId +
                ", itemName='" + itemName + '\'' +
                '}';
    }
}
