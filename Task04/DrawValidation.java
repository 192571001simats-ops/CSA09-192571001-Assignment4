import java.awt.*;
import java.awt.event.*;
import java.nio.file.*;
import javax.swing.*;
import javax.tools.*;

public class DrawValidation {
  public static void main(String[] args) throws Exception {
    Class.forName("java.awt.event.MouseEvent");
    DrawPad[] box = new DrawPad[1];
    EventQueue.invokeAndWait(
        () -> {
          box[0] = new DrawPad();
          box[0].setVisible(true);
          for (int i = 0; i < 3; i++)
            box[0].dispatchEvent(
                new MouseEvent(
                    box[0],
                    MouseEvent.MOUSE_CLICKED,
                    System.currentTimeMillis(),
                    0,
                    80 + i * 60,
                    120,
                    1,
                    false));
        });
    Thread.sleep(200);
    EventQueue.invokeAndWait(
        () -> {
          Bench.require(
              box[0].pointCount() == 3 && box[0].countLabel().equals("Clicks: 3"),
              "mouse handler and label");
          GuiEvidence.capture(box[0], "dots-before-minimize.png");
          box[0].setState(Frame.ICONIFIED);
        });
    Thread.sleep(150);
    int iconified = box[0].getState();
    EventQueue.invokeAndWait(
        () -> {
          box[0].setState(Frame.NORMAL);
          box[0].toFront();
          box[0].repaint();
        });
    Thread.sleep(800);
    EventQueue.invokeAndWait(
        () -> {
          Bench.require(
              box[0].getState() == Frame.NORMAL && box[0].pointCount() == 3,
              "stored dots survive restoration and repaint");
          GuiEvidence.capture(box[0], "dots-after-restore.png");
          GuiEvidence.finish();
        });
    System.out.println("Observed iconified state=" + iconified);
    Path src = Path.of("TypoListener.java");
    String typo =
        "import java.awt.event.*; class TypoListener extends MouseAdapter { public void"
            + " mouseclicked(MouseEvent e) {} }";
    Files.writeString(src, typo);
    int silent = ToolProvider.getSystemJavaCompiler().run(null, null, null, src.toString());
    Bench.require(silent == 0, "without Override the typo compiles silently");
    Files.writeString(src, typo.replace("public void", "@Override public void"));
    java.io.ByteArrayOutputStream error = new java.io.ByteArrayOutputStream();
    int rejected = ToolProvider.getSystemJavaCompiler().run(null, null, error, src.toString());
    System.out.println(error.toString());
    Bench.require(rejected != 0, "Override exposes misspelled callback");
    Files.delete(src);
    Files.deleteIfExists(Path.of("TypoListener.class"));
  }
}
