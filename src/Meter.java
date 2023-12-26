import javax.swing.JSlider;

public class Meter extends JSlider {
    public Meter() {
        setOpaque(false);
        setOrientation(JSlider.VERTICAL);
        setPaintTicks(true);
        setPaintLabels(true);
        setEnabled(false);
        setUI(new MeterUI(this));
    }
    public Meter(int min, int max) {
        this();
        setMinimum(min);
        setMaximum(max);
        setPaintTicks(true);
        setPaintLabels(true);
    }
}