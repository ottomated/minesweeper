import java.awt.*;
import java.awt.event.MouseListener;
import java.util.*;

public class Board {
   private Square[][] squares;
   private Game parent;
   public int h;
   public int w;
   private int mines;
   public Square selected;
   
   public Board(int w, int h, Game p) {
      parent = p;
      this.w = w;
      this.h = h;
      
      squares = new Square[h][w];
      for (int y = 0; y < h; y++) {
         for (int x = 0; x < w; x++) {
            squares[y][x] = new Square(x, y, p);
         }
      }
   }
   public void addSquaresToPane(Container pane) {
      for (int y = 0; y < h; y++) {
         for (int x = 0; x < w; x++) {
            pane.add(squares[y][x]);
         }
      }
   }
   public void addMouseListeners(MouseListener ml) {
      for (int y = 0; y < h; y++) {
         for (int x = 0; x < w; x++) {
            //squares[y][x].setRevealed(true);
            squares[y][x].addMouseListener(ml);
         }
      }
   }
   public void placeMines(int count) {
      mines = count;
      Random rand = new Random();
      for (int i = 0; i < count; i++) {
         int x = rand.nextInt(w);
         int y = rand.nextInt(h);
         if (squares[y][x].getCount() == -1) i--;
         else squares[y][x].setCount(-1);
      }
   }
   public void updateNeighbors() {
      for (int y = 0; y < h; y++) {
         for (int x = 0; x < w; x++) {
            Square s = squares[y][x];
            if (s.getCount() == -1) {
               for (Square n : getNeighbors(s)) {
                  if (n.getCount() >= 0)
                     n.setCount(n.getCount() + 1);
               }
            }
         }
      }
      toString();
   }
   private ArrayList<Square> getNeighbors(Square s) {
      ArrayList<Square> list = new ArrayList<Square>();
      for (int dx = -1; dx <= 1; dx++) {
         for (int dy = -1; dy <= 1; dy++) {
            if (s.x + dx < w && s.x + dx >= 0
             && s.y + dy < h && s.y + dy >= 0)
               list.add(squares[s.y + dy][s.x + dx]);
         }
      }
      return list;
   }
   
   public String toString() {
   
      for (int y = 0; y < h; y++) {
         for (int x = 0; x < w; x++) {
            System.out.print(squares[y][x].getCount());
            System.out.print(" ");
         }
         System.out.println();
      }
      return "";
   }
   public void repaint() {
      for (int y = 0; y < h; y++) {
         for (int x = 0; x < w; x++) {
            squares[y][x].repaint();
         }
      }
   }
   public void open(Square s) {
      if (s.getRevealed()) return;
      s.setRevealed(true);
      if (s.getCount() == -1) {
         parent.kill();
         for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
               Square m = squares[y][x];
               if (m.getCount() == -1) {
                  if (!m.getFlagged())
                     m.setRevealed(true);
               } else if (m.getFlagged()) {
                  m.setNotMine(true);
               }
            }
         }
         s.setRedMine(true);
      }
      if (s.getCount() == 0 ) {
         for (Square n : getNeighbors(s)) {
            open(n);
         }
      }
      if (checkWin()) {
         parent.win();
         for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
               squares[y][x].setRevealed(true);
            }
         }
      }
   }
   
   public boolean checkWin() {
      int flagCount = 0;
      int revealedCount = 0;
      for (int y = 0; y < h; y++) {
         for (int x = 0; x < w; x++) {
            if (squares[y][x].getFlagged()) {
               if (squares[y][x].getCount() == -1) 
                  flagCount++;
               else
                  return false;
            } else if (squares[y][x].getRevealed()) {
               if (squares[y][x].getCount() == -1)
                  return false;
               else
                  revealedCount++;
            }
         }
      }
      if (flagCount == mines) return true;
      if (revealedCount == w * h - mines) return true;
      return false;
   }
   
   public void specialOpen(Square s) {
      ArrayList<Square> ns = getNeighbors(s);
      int flagCount = 0;
      for (Square n : ns) {
         if (n.getFlagged())
            flagCount++;
      }
      if (flagCount == s.getCount()) {
         for (Square n : ns) {
            if (!n.getFlagged() && !n.getRevealed())
               open(n);
         }
      }
   }
}