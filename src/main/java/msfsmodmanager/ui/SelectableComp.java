package msfsmodmanager.ui;

public interface SelectableComp {
    public String getName();
    public boolean isSelected();
    public default boolean isCity() { return false; }
}
