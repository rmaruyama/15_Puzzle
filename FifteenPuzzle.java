import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

/*************************************************************************
 * 15 Puzzle Applet
 * @author Maruyama,Ryoji.
 *************************************************************************/
@SuppressWarnings("serial")
public class FifteenPuzzle extends JApplet {
	static final int DIFFICULTY_1 = 11;
	static final int DIFFICULTY_2 = 21;
	static final int DIFFICULTY_3 = 41;
	static final int DIFFICULTY_4 = 61;
	static final int DIFFICULTY_5 = 101;
	static final int DIFFICULTY_6 = 201;
	
	// difficulty comboBox
	private String[] difficultyLabels = {"DIFFICULTY_1",
										"DIFFICULTY_2",
										"DIFFICULTY_3",
										"DIFFICULTY_4",
										"DIFFICULTY_5",
										"DIFFICULTY_6"};
	private JComboBox<String> jcbDifficulty = new JComboBox<String>(difficultyLabels);
	
	// Reset button
	private JButton jbtReset = new JButton("Reset");
	
	// Puzzle board panel
	private PuzzleBoard puzzleBoard = new PuzzleBoard();
	
	// Game mode
	private boolean isWon = false;

	/**
	 * Constructor
	 */
	public FifteenPuzzle() {
		// Add Puzzle board
		add(puzzleBoard, BorderLayout.CENTER);

		// Make ComboBox and Button part
		JPanel jpNorth = new JPanel();
		jpNorth.add(jcbDifficulty);
		jpNorth.add(jbtReset);
		add(jpNorth, BorderLayout.NORTH);
		
		// Register listener
		jbtReset.addActionListener(new ActionListener() { // Reset Button
			// When Reset button is clicked
			public void actionPerformed(ActionEvent e) {
				if(jcbDifficulty.getSelectedItem().toString().equals("DIFFICULTY_1")) {
					puzzleBoard.shuffleTiles(DIFFICULTY_1);
				} else if(jcbDifficulty.getSelectedItem().toString().equals("DIFFICULTY_2")) {
					puzzleBoard.shuffleTiles(DIFFICULTY_2);
				} else if(jcbDifficulty.getSelectedItem().toString().equals("DIFFICULTY_3")) {
					puzzleBoard.shuffleTiles(DIFFICULTY_3);
				} else if(jcbDifficulty.getSelectedItem().toString().equals("DIFFICULTY_4")) {
					puzzleBoard.shuffleTiles(DIFFICULTY_4);
				} else if(jcbDifficulty.getSelectedItem().toString().equals("DIFFICULTY_5")) {
					puzzleBoard.shuffleTiles(DIFFICULTY_5);
				} else if(jcbDifficulty.getSelectedItem().toString().equals("DIFFICULTY_6")) {
					puzzleBoard.shuffleTiles(DIFFICULTY_6);
				}
				
				isWon = false;
				repaint(); // Show new board
			}
		});
	}
	
	public void parentRepaint() {
		repaint();
	}
	
	/**
	 * MAIN
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame("15 Puzzle");
		FifteenPuzzle applet = new FifteenPuzzle();
		frame.add(applet, BorderLayout.CENTER);
		applet.init();
		applet.start();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(420, 470);
		frame.setLocationRelativeTo(null); // Center the frame
		frame.setVisible(true);
	}
	
	
	/***************************************************************
	 * Puzzle board panel
	 **************************************************************/
	class PuzzleBoard extends JPanel  implements MouseListener{
		int tiles[][] = new int[4][4]; // tiles[x][y]
	
		/**
		 * Constructor
		 */
		PuzzleBoard() {	
			addMouseListener(this);
			shuffleTiles(DIFFICULTY_1);
		}
		
		/**
		 * Reset the Puzzle Board
		 */
		public void resetPuzzleBoard() {
			// Make tiles
			for(int y = 0; y < 4; y++) {
				for(int x = 0; x < 4; x++) {
					tiles[x][y] = (y * 4 + x) + 1; 
				}
			}
			// Change into 0, which means empty 
			tiles[3][3] = 0;
		}
		
		/**
		 * Get color for tiles
		 */
		public Color getColor(int num) {
			Color color = null;
			switch(num) {
			case 0: color = new Color(255, 255, 255);
				break;
			case 1:
			case 2:
			case 3:
			case 4: color = new Color(255, 153, 153);
				break;
			case 5:
			case 6:
			case 7:
			case 8: color = new Color(245, 245, 153);
				break;
			case 9:
			case 10:
			case 11:
			case 12: color = new Color(153, 255, 153);
				break;
			case 13:
			case 14:
			case 15: color = new Color(153, 153, 255);
				break;
			}
			return color;
		}
	
		/**
		 * Shuffle tiles
		 * Point(x, y) means the place of tiles[x][y]
		 */
		public void shuffleTiles(int times) {
			resetPuzzleBoard();
			Point currentP = new Point(3, 3); // point of the place of 0 (empty place)
			Point oldP = new Point(currentP); // to avoid going back to the old place
			
			for(int i = 0; i < times; i++) {
				randomMove(currentP, oldP);
			}
		}
	
		/**
		 * Move a tile randomly
		 * Point(x, y) means the place of tiles[x][y]
		 */
		public void randomMove(Point currentP, Point oldP) {
			ArrayList<Point> candidates = new ArrayList<Point>();
			
			// Check candidates
			// Check Up place
			Point upP = new Point(currentP);
			upP.translate(0, -1); // Up point
			if(currentP.y > 0 && !(upP.equals(oldP))) {
				candidates.add(upP);
			}
			// Check Down place
			Point downP = new Point(currentP);
			downP.translate(0, 1); // Down point
			if(currentP.y < 3 && !(downP.equals(oldP))) {
				candidates.add(downP);
			}
			// Check Left place
			Point leftP = new Point(currentP);
			leftP.translate(-1, 0); // Left point
			if(currentP.x > 0 && !(leftP.equals(oldP))) {
				candidates.add(leftP);
			}
			// Check Right place
			Point rightP = new Point(currentP);
			rightP.translate(1, 0); // Right point
			if(currentP.x < 3 && !(rightP.equals(oldP))) {
				candidates.add(rightP);
			}
			
			// Choose one from candidates
			int randomIndex = (int)(Math.random() * candidates.size());
			Point newP = candidates.get(randomIndex);
			// Move tile
			tiles[currentP.x][currentP.y] = tiles[newP.x][newP.y];
			tiles[newP.x][newP.y] = 0;
			
			// Update information
			oldP.setLocation(currentP);
			currentP.setLocation(newP);
		}
	
		/**
		 * Check the end of game
		 */
		public boolean isEnd() {
			for(int y = 0; y < 4; y++) {
				for(int x = 0; x < 4; x++) {
					int num = (y * 4 + x) + 1;
					if( !(tiles[x][y] == num) && (num < 16)) {
						return false;
					}
				}
			}
			return true;
		}
	
		/**
		 * Paint the Puzzle Board
		 */
		public void paint(Graphics g) {
			int tileWidth = getWidth() / 4;   // define tile width
			int tileHieght = getHeight() / 4; // define tile height
			
			for(int y = 0; y < 4; y++) {
				for(int x = 0; x < 4; x++) {
					// draw tiles
					g.setColor(getColor(tiles[x][y]));
					g.fillRoundRect(tileWidth * x, tileHieght * y,
							        tileWidth - 1, tileHieght - 1, 20, 20);
					// draw tile numbers
					g.setColor(new Color(255, 255, 255));
		    		g.setFont(new Font("", Font.BOLD, 40));
		    		g.drawString(String.valueOf(tiles[x][y]),
		    				     tileWidth * x + tileWidth / 3,
		    				     tileHieght * y + tileHieght * 2 /3);
				}
			}
			
			if(isWon){
				g.setColor(new Color(204, 204, 204));
				g.fillRoundRect(tileWidth / 2, tileHieght / 2,
						        tileWidth * 3, tileHieght * 3, 20, 20);
				g.setColor(new Color(255, 255, 255));
	    		g.drawString("YOU WON!", tileWidth, tileHieght * 2);
			}
		}
	
		/**
		 * When Mouse Released
		 */
		public void mouseReleased(MouseEvent e) {
			// Check game mode
			if(isWon) {
				return; // ignore mouse event
			}
			
			int xClicked = e.getX();
			int yClicked = e.getY();
			
			// Figure out the x and y
			int y = (yClicked * 4) / getHeight();
			int x = (xClicked * 4) / getWidth();
			
			// Move the clicked tile
			if(y > 0 && tiles[x][y - 1] == 0){ // Go Up
				tiles[x][y - 1] = tiles[x][y];
				tiles[x][y] = 0;
			} else if(y < 3 && tiles[x][y + 1] == 0) { // Go Down
				tiles[x][y + 1] = tiles[x][y];
				tiles[x][y] = 0;
			} else if(x > 0 && tiles[x - 1][y] == 0) { // Go Left
				tiles[x - 1][y] = tiles[x][y];
				tiles[x][y] = 0;
			} else if(x < 3 && tiles[x + 1][y] == 0) { // Go Right
				tiles[x + 1][y] = tiles[x][y];
				tiles[x][y] = 0;
			}
			
			// Check won or not
			if(isEnd()){
				isWon = true;
			}
			
			parentRepaint();
		}
	
		public void mousePressed(MouseEvent e) {
		}
	
		public void mouseClicked(MouseEvent e) {
		}
	
		public void mouseEntered(MouseEvent e) {
		}
	
		public void mouseExited(MouseEvent e) {
		}
	}
}