package msfsmodmanager.ui.main;

public interface SelectableComp {
    public String getName();
    public boolean isSelected();
    public default boolean isCity() { return false; }
}
