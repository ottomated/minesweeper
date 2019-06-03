import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import java.util.Vector;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Game extends JFrame {
   public static final int MARGIN_SIDES = 20;
   public static final int MARGIN_TOP = 100;
   
   private boolean playing;
   private Board board;
   private Container pane;
   private MouseListener mouseListener;
    
   public Game() {
      super("Minesweeper");
      try {
         UIManager.setLookAndFeel(
            UIManager.getSystemLookAndFeelClassName());
      } catch(Exception e) {}
      loadImages();
      initGUI();
      initBoard(16, 16);
      playing = true;
   }
   
   private void initBoard(int w, int h) {
      board = new Board(w, h, this);
      pane.setPreferredSize(new Dimension(w * Square.SIZE + MARGIN_SIDES * 2,
                                          h * Square.SIZE + MARGIN_TOP + MARGIN_SIDES));
      this.pack();
      this.setLocationRelativeTo(null); // Center the window position
      board.addSquaresToPane(pane);
      board.addMouseListeners(mouseListener);
      board.placeMines(40);
      board.updateNeighbors();
      board.repaint();
   }
   
   public BufferedImage blank;
   public BufferedImage flag;
   public BufferedImage mine;
   public BufferedImage redMine;
   public BufferedImage notMine;
   public BufferedImage[] counts;
   private void loadImages() {
      try {
         blank = ImageIO.read(new File("img/blank.png"));
         flag = ImageIO.read(new File("img/flag.png"));
         mine = ImageIO.read(new File("img/mine.png"));
         redMine = ImageIO.read(new File("img/redmine.png"));
         notMine = ImageIO.read(new File("img/notmine.png"));
         counts = new BufferedImage[9];
         for (int i = 0; i < 9; i++) {
            counts[i] = ImageIO.read(new File("img/" + i + ".png"));         }
      } catch (IOException e) {
         System.out.println("Failed during image loading");
         System.out.println(e);
      }
   }
   
   private void initGUI() {
      mouseListener = 
         new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}
         
            @Override
            public void mousePressed(MouseEvent e) {
               if (!playing) 
                  return;
               Square s = (Square) e.getSource();
               if (SwingUtilities.isLeftMouseButton(e)) {
                  s.setMouseDown(true);
                  board.selected = s;
               } else if (SwingUtilities.isRightMouseButton(e) && !s.getRevealed()) {
                  s.setFlagged(!s.getFlagged());
               }
            }
         
            @Override
            public void mouseReleased(MouseEvent e) {
               if (!playing) 
                  return;
               Square s = board.selected;
               s.setMouseDown(false);
               if (!s.getRevealed()) {
                  if (SwingUtilities.isLeftMouseButton(e) && !s.getFlagged())
                     board.open(s);
               } else if (SwingUtilities.isLeftMouseButton(e) && !s.getFlagged()) {
                  board.specialOpen(s);
               }
            }
         
            @Override
            public void mouseEntered(MouseEvent e) {
               if (!playing) 
                  return;
               if (SwingUtilities.isLeftMouseButton(e)) {
                  Square s = (Square) e.getSource();
                  if (SwingUtilities.isLeftMouseButton(e))
                     s.setMouseDown(true);
                  board.selected = s;
               }
            }
         
            @Override
            public void mouseExited(MouseEvent e) {
               if (!playing) 
                  return;
               Square s = (Square) e.getSource();
               s.setMouseDown(false);
            }
         };
      //frame = new JFrame("Minesweeper");
      this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      pane = this.getContentPane();
      pane.setLayout(null);
   
      this.pack();
      this.setResizable(false);
      this.setVisible(true);
   }
   
   public void kill() {
      playing = false;
   }
   
   public void win() {
      playing = false;
   }
   
   @Override
   public void paint(Graphics g){
      super.paint(g);
      getContentPane().setBackground(new Color(189, 189, 189));
      
      //g.setColor(new Color(123, 123, 123));
      //g.fillRect(16, 16, board.w * Square.SIZE, 4);
   }
   
}