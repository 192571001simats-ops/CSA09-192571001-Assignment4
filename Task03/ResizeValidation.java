import java.nio.file.*;
import javax.swing.*;

public class ResizeValidation {
  public static void main(String[] args) throws Exception {
    final LibrarySearch[] box = new LibrarySearch[1];
    SwingUtilities.invokeAndWait(
        () -> {
          box[0] = new LibrarySearch();
          box[0].setVisible(true);
        });
    StringBuilder sizes =
        new StringBuilder("width,height,search_height,results_height,bottom_height\n");
    int originalSearch = -1, originalBottom = -1, lastCenter = 0;
    for (int[] size : new int[][] {{640, 400}, {900, 600}, {1200, 760}}) {
      SwingUtilities.invokeAndWait(
          () -> {
            box[0].setSize(size[0], size[1]);
            box[0].validate();
          });
      Thread.sleep(150);
      LibrarySearch ui = box[0];
      int sh = ui.top.getHeight(), bh = ui.bottom.getHeight(), ch = ui.center.getHeight();
      if (originalSearch < 0) {
        originalSearch = sh;
        originalBottom = bh;
      }
      Bench.require(
          sh == originalSearch && bh == originalBottom && ch > lastCenter,
          "resizing preserves controls and grows results " + size[0]);
      lastCenter = ch;
      sizes.append(size[0] + "," + size[1] + "," + sh + "," + ch + "," + bh + "\n");
      SwingUtilities.invokeAndWait(
          () -> GuiEvidence.capture(ui, "library-" + size[0] + "x" + size[1] + ".png"));
    }
    Files.writeString(Path.of("layout-measurements.csv"), sizes);
    SwingUtilities.invokeAndWait(GuiEvidence::finish);
  }
}
