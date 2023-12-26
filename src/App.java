import javax.swing.JFrame;

public class App {
    public static void main(String[] args) throws Exception {
        var frame = new JFrame("Loudness Meter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(220, 280);

        var loudnessMeter = new LoudnessMeter(100, -45, 5, 1, 5);
        loudnessMeter.addChangeListener(e -> {
            var source = (LoudnessMeter) e.getSource();
            System.out.println(source.getValue());
        });
        frame.add(loudnessMeter);
        frame.setVisible(true);
    }
}
