import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.*;

public class Square extends JButton {
   public static final int SIZE = 32;
   
   public int x;
   public int y;
   private int count; // -1 = mine, 0-8 = number nearby
   private boolean isRevealed;
   private boolean isFlagged;
   private boolean isMouseDown;
   private boolean isRedMine;
   private boolean isNotMine;
   private BufferedImage img;
   private Game parent;
   
   public Square(int x, int y, Game p) {
      super();
      this.parent = p;
      this.x = x;
      this.y = y;
      setBounds(x * SIZE + Game.MARGIN_SIDES, y * SIZE + Game.MARGIN_TOP, SIZE, SIZE);
      isRevealed = false;
      isFlagged = false;
      isMouseDown = false;
      count = 0;
      setState();
   }
   public void setRedMine(boolean b) {
      isRedMine = b;
      setState();
   }
   public void setNotMine(boolean b) {
      isNotMine = b;
      setState();
   }
      
   public int getCount() {
      return count;
   }
   public boolean getRevealed() {
      return isRevealed;
   }
   public boolean getFlagged() {
      return isFlagged;
   }
   public boolean getMouseDown() {
      return isMouseDown;
   }
   public void setCount(int c) {
      count = c;
      setState();
   }
   public void setRevealed(boolean r) {
      isRevealed = r;
      setState();
   }
   public void setFlagged(boolean f) {
      isFlagged = f;
      setState();
   }
   public void setMouseDown(boolean m) {
      isMouseDown = m;
      setState();
   }
   public void setState() {
      if (isNotMine) {
         img = parent.notMine;
      } else if (isRedMine) {
         img = parent.redMine;
      } else if (isRevealed) {
         if (count == -1)
            img = parent.mine;
         else
            img = parent.counts[count];
      } else if (isFlagged) {
         img = parent.flag;
      } else if (isMouseDown) {
         img = parent.counts[0];
      } else {
         img = parent.blank;
      }
      repaint();
   }
   
   public void paintComponent(Graphics g) {
      g.drawImage(img, 0, 0, SIZE, SIZE, null);
   }
}