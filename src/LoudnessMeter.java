import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

public class LoudnessMeter extends JPanel {
    private final JSlider inputSlider = new JSlider(JSlider.VERTICAL, 0, 100, 1);
    private final Meter meter = new Meter();
    private final double v0;

    private int value;

    public int getValue() {
        return value;
    }

    protected transient ChangeEvent changeEvent = null;
    protected EventListenerList listenerList = new EventListenerList();

    private int min;
    private int max;

    public LoudnessMeter(int v0, int min, int max, int minorTickSpacing, int majorTickSpacing) {
        this.v0 = v0;
        this.min = min;
        this.max = max;

        inputSlider.setEnabled(true);
        inputSlider.setPaintTrack(true);
        inputSlider.setPaintTicks(true);
        inputSlider.setPaintLabels(true);
        inputSlider.setMajorTickSpacing(50);
        inputSlider.setMinorTickSpacing(5);

        inputSlider.addChangeListener(e -> recalculate());
        add(inputSlider);

        meter.setMinimum(min);
        meter.setMaximum(max);
        meter.setPaintTicks(true);
        meter.setPaintLabels(true);
        meter.setMinorTickSpacing(minorTickSpacing);
        meter.setMajorTickSpacing(majorTickSpacing);
        add(meter);

        recalculate(false);
    }

    private void recalculate() {
        recalculate(true);
    }

    private void recalculate(boolean fireIfChanged) {
        var v = (double) inputSlider.getValue();
        var newValue = (20 * Math.log10(v / v0));
        newValue = Math.clamp(newValue, min, max);
        var newValueInt = (int) newValue;
        meter.setValue(newValueInt);
        if (!fireIfChanged || value == newValueInt) {
            return;
        }
        value = newValueInt;
        fireStateChanged();
    }

    protected void fireStateChanged() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                if (changeEvent == null) {
                    changeEvent = new ChangeEvent(this);
                }
                ((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
            }
        }
    }

    public void addChangeListener(ChangeListener l) {
        listenerList.add(ChangeListener.class, l);
    }

    public void removeChangeListener(ChangeListener l) {
        listenerList.remove(ChangeListener.class, l);
    }
}
