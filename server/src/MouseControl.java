import java.awt.*;

public class MouseControl implements Control {

    private static final double MAX_SCREEN_W;
    private static final double MAX_SCREEN_H;

    static {
        // https://stackoverflow.com/questions/3680221/how-can-i-get-screen-resolution-in-java/3680236#3680236
        // Might look into dealing with multi-monitor configurations.
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        MAX_SCREEN_W = screenSize.getWidth();
        MAX_SCREEN_H = screenSize.getHeight();

        System.out.println(MAX_SCREEN_H);
    }

    @Override
    public void process(Instructions instructions) {

    }
}
