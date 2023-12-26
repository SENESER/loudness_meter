import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.util.Dictionary;
import java.util.Enumeration;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.plaf.basic.BasicSliderUI;

public class MeterUI extends BasicSliderUI {
    public MeterUI(JSlider slider) {
        super(slider);
    }

    private final Color trackColor0 = new Color(0, 0, 0);
    private final Color trackColor1 = new Color(100, 200, 100);

    @Override
    public void paintTrack(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(trackColor0);

        var trackBounds = trackRect;

        int cx = (trackBounds.width / 2) - 2;
        int ch = trackBounds.height;
        g.translate(trackBounds.x + cx, trackBounds.y);
        g.fillRect(0, 0, cx, ch);

        // Part of track to fill in range [0; 1]
        var fillness = ((float) slider.getValue() - slider.getMinimum()) / (slider.getMaximum() - slider.getMinimum());
        var height = Math.round((ch - 2) * fillness);
        var y = ch - height - 1;
        g.setColor(trackColor1);
        g.fillRect(1, y, cx - 2, height);

        g.translate(-(trackBounds.x + cx), -trackBounds.y);
    }

    // Do not paint thumb
    @Override
    public void paintThumb(Graphics graphics) {
    }

    // Same code, except labels are always enabled
    // (commented `boolean enabled = slider.isEnabled();` and `label.setEnabled(enabled)`)
    // Also replaced `BasicGraphicsUtils.isLeftToRight(slider)`
    // with `slider.getComponentOrientation().isLeftToRight()`
    // because BasicGraphicsUtils.isLeftToRight is not available from here
    @Override
    public void paintLabels( Graphics g ) {
        Rectangle labelBounds = labelRect;

        @SuppressWarnings("rawtypes")
        Dictionary dictionary = slider.getLabelTable();
        if ( dictionary != null ) {
            Enumeration<?> keys = dictionary.keys();
            int minValue = slider.getMinimum();
            int maxValue = slider.getMaximum();
            // boolean enabled = slider.isEnabled();
            while ( keys.hasMoreElements() ) {
                Integer key = (Integer)keys.nextElement();
                int value = key.intValue();
                if (value >= minValue && value <= maxValue) {
                    JComponent label = (JComponent) dictionary.get(key);
                    // label.setEnabled(enabled);

                    if (label instanceof JLabel) {
                        Icon icon = label.isEnabled() ? ((JLabel) label).getIcon() : ((JLabel) label).getDisabledIcon();

                        if (icon instanceof ImageIcon) {
                            // Register Slider as an image observer. It allows to catch notifications about
                            // image changes (e.g. gif animation)
                            Toolkit.getDefaultToolkit().checkImage(((ImageIcon) icon).getImage(), -1, -1, slider);
                        }
                    }

                    if ( slider.getOrientation() == JSlider.HORIZONTAL ) {
                        g.translate( 0, labelBounds.y );
                        paintHorizontalLabel( g, value, label );
                        g.translate( 0, -labelBounds.y );
                    }
                    else {
                        int offset = 0;
                        // 
                        if (!slider.getComponentOrientation().isLeftToRight()) {
                            offset = labelBounds.width -
                                label.getPreferredSize().width;
                        }
                        g.translate( labelBounds.x + offset, 0 );
                        paintVerticalLabel( g, value, label );
                        g.translate( -labelBounds.x - offset, 0 );
                    }
                }
            }
        }

    }
}
