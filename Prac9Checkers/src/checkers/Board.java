package checkers;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Board extends JPanel implements MouseListener {
	private static final int numRowCol = 8;
	private Square[][] squares = new Square[numRowCol][numRowCol];
	private Square firstSelected = null;
	private Square secondSelected = null;
	private boolean isBlackTurn = true;

	public Board() {
		// TODO create squares and add pieces to each square to match the default
		// configuration of the
		// board from the image
		this.addMouseListener(this);
		setPreferredSize(new Dimension(Square.SIZE * numRowCol, Square.SIZE * numRowCol));
		setLayout(new FlowLayout());
		
		for (int i = 0; i < numRowCol; i++) {
			for (int j = 0; j < numRowCol; j++) {
				squares[j][i] = new Square(j, i, (i+j) % 2 == 0);
				if ((i+j) % 2 == 0 && j < 3) {
					squares[j][i].addPiece(new Piece(0));
				}
				
				if ((i+j) % 2 == 0 && j > 4) {
					squares[j][i].addPiece(new Piece(1));
				}
				
			}
		}
		
	}

	public void paintComponent(Graphics g) {
		// TODO draw each square
		for (int i = 0; i < numRowCol; i++) {
			for (int j = 0; j < numRowCol; j++) {
				squares[j][i].draw(g);
			}
		}
		
/*
		for (int i = 0; i < numRowCol; i++) {
			for (int j = 0; j < numRowCol; j++) {
				
				System.out.println(String.valueOf(i)+String.valueOf(j)+squares[i][j].getPiece());
			}
			
		}*/
	}

	// TODO Select the first Square and then select the second square. Execute the
	// move IF IT IS VALID
	@Override
	public void mousePressed(MouseEvent e) {
	    if (firstSelected == null) {
	        firstSelected = squares[e.getY()/Square.SIZE][e.getX()/Square.SIZE];
	    } else {
	        if ((isBlackTurn && firstSelected.getPiece().getColor() == firstSelected.getPiece().BLACK) 
	            || (!isBlackTurn && firstSelected.getPiece().getColor() == firstSelected.getPiece().RED)) {
	            if (secondSelected == null) {
	                secondSelected = squares[e.getY()/Square.SIZE][e.getX()/Square.SIZE];
	                if (secondSelected.getPiece() == null) {
	                    if (isAnyCapturePossible()) {
	                        if (isCaptureMove(firstSelected, secondSelected)) {
	                            capture();
	                        } else {
	                            JOptionPane.showMessageDialog(this, "A capture is possible. You must capture the opponent's piece.");
	                            firstSelected = null;
	                            secondSelected = null;
	                        }
	                    } else {
	                        move();
	                    }
	                } else {
	                    JOptionPane.showMessageDialog(this, "Position ("+(secondSelected.getX()/Square.SIZE+1)+", "+(secondSelected.getY()/Square.SIZE+1)+") is Blocked");
	                    firstSelected = null;
	                    secondSelected = null;
	                }
	            }
	        } else {
	            JOptionPane.showMessageDialog(this, "Not Your Turn");
	            firstSelected = null;
	            secondSelected = null;
	        }
	    }
	}




	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	public void capture() {
		// TODO execute capture 
		// Hint: to reuse code, you can call move within this method
		int direction = (secondSelected.getY() - firstSelected.getY()) / Square.SIZE;

	    // Check if the piece is a normal piece trying to capture in a reverse direction
	    if (!firstSelected.getPiece().isCrown() && firstSelected.getPiece().getColor() == firstSelected.getPiece().RED && direction > 0) {
	        JOptionPane.showMessageDialog(this, "Normal pieces cannot capture in a reverse direction. Choose another move");
	        firstSelected = null;
	        secondSelected = null;
	        return;
	    }

	    if (!firstSelected.getPiece().isCrown() && firstSelected.getPiece().getColor() == firstSelected.getPiece().BLACK && direction < 0) {
	        JOptionPane.showMessageDialog(this, "Normal pieces cannot capture in a reverse direction. Choose another move");
	        firstSelected = null;
	        secondSelected = null;
	        return;
	    }
		if (squares[secondSelected.getY()/Square.SIZE][secondSelected.getX()/Square.SIZE].getPiece() == null && squares[(secondSelected.getY()/Square.SIZE+firstSelected.getY()/Square.SIZE)/2][(secondSelected.getX()/Square.SIZE+firstSelected.getX()/Square.SIZE)/2].getPiece() != null && squares[(secondSelected.getY()/Square.SIZE+firstSelected.getY()/Square.SIZE)/2][(secondSelected.getX()/Square.SIZE+firstSelected.getX()/Square.SIZE)/2].getPiece().getColor() != firstSelected.getPiece().getColor()) {
			squares[(secondSelected.getY()/Square.SIZE+firstSelected.getY()/Square.SIZE)/2][(secondSelected.getX()/Square.SIZE+firstSelected.getX()/Square.SIZE)/2].removePiece();
			Square thirdSelected = squares[secondSelected.getY()/Square.SIZE][secondSelected.getX()/Square.SIZE];
			Square mid = squares[(secondSelected.getY()/Square.SIZE+firstSelected.getY()/Square.SIZE)/2][(secondSelected.getX()/Square.SIZE+firstSelected.getX()/Square.SIZE)/2];			
			
			secondSelected = mid;
			move();
			firstSelected = mid;
			secondSelected = thirdSelected;
			move();
			if (isBlackTurn) {
				isBlackTurn = false;
			}else {
				isBlackTurn = true;
			}
			/*if (secondSelected.getPiece() != null) {
		        if (isCapturePossible(secondSelected)) {
		            // If an additional capture is possible, make the captured square the new selected square
		            firstSelected = secondSelected;
		            secondSelected = null;
		        } else {
		            // If no additional capture is possible, switch turns
		            if (isBlackTurn) {
		                isBlackTurn = false;
		            } else {
		                isBlackTurn = true;
		            }
		            // Reset the selected squares
		            firstSelected = null;
		            secondSelected = null;
		        }
		    } */
			
			
			}else {
				JOptionPane.showMessageDialog(this, "Position ("+(secondSelected.getX()/Square.SIZE+1)+", "+(secondSelected.getY()/Square.SIZE+1)+") is blocked. Choose another move");
				firstSelected = null;
				secondSelected = null;
			}
		
		
		
		
	}

	public void move() {
		// TODO execute move Square.SIZE
		if (firstSelected.getPiece() != null) {
			int x = firstSelected.getX()/Square.SIZE;
			int y = firstSelected.getY()/Square.SIZE;
			int x1 = secondSelected.getX()/Square.SIZE;
			int y1 = secondSelected.getY()/Square.SIZE;
			if (firstSelected.getPiece().isCrown()) {
				System.out.println(""+x1+y1+x+y);
				if ((x1+y1)%2 == 0 && -1 < x1 && x1 < 8 && x-2 < x1 && x1 < x+2 && -1 < y1 && y1 < 8 && (y1 == y-1 || y1 == y+1)) {
					System.out.println(1);
					secondSelected.addPiece(firstSelected.getPiece());
					firstSelected.removePiece();
					if (isBlackTurn) {
						isBlackTurn = false;
					}else {
						isBlackTurn = true;
					}
					
				}
			}else{
				if (firstSelected.getPiece().getColor() == firstSelected.getPiece().RED) {
					if ((x1+y1)%2 == 0 && -1 < x1 && x1 < 8 && x-2 < x1 && x1 < x+2 && -1 < y1 && y1 < 8 && y1 == y-1) {
						System.out.println(2);
						secondSelected.addPiece(firstSelected.getPiece());
						firstSelected.removePiece();
						if (isBlackTurn) {
							isBlackTurn = false;
						}else {
							isBlackTurn = true;
						}
						
					}
				}else if (firstSelected.getPiece().getColor() == firstSelected.getPiece().BLACK) {
					if ((x1+y1)%2 == 0 && -1 < x1 && x1 < 8 && x-2 < x1 && x1 < x+2 && -1 < y1 && y1 < 8 && y1 == y+1) {
						System.out.println(3);
						secondSelected.addPiece(firstSelected.getPiece());
						firstSelected.removePiece();
						if (isBlackTurn) {
							isBlackTurn = false;
						}else {
							isBlackTurn = true;
						}
						
					}
				}
				
			}
			squares[secondSelected.getY()/Square.SIZE][secondSelected.getX()/Square.SIZE] = secondSelected;
			squares[y][x] = firstSelected;
			try {
			if (secondSelected.getPiece().getColor() == secondSelected.getPiece().RED && secondSelected.getY()/Square.SIZE == 0) {
				System.out.println("Crown");
				squares[secondSelected.getY()/Square.SIZE][secondSelected.getX()/Square.SIZE].getPiece().makeCrown();	
			}
			if (secondSelected.getPiece().getColor() == secondSelected.getPiece().BLACK && secondSelected.getY()/Square.SIZE == 7) {
				squares[secondSelected.getY()/Square.SIZE][secondSelected.getX()/Square.SIZE].getPiece().makeCrown();	
				System.out.println("Crown");
			}
			}catch (Exception e) {}
			
			repaint();
		}else {
			JOptionPane.showMessageDialog(this, "No Piece Selected");
		}

		firstSelected = null;
		secondSelected = null;
	
		checkWin();
		
	}
	
	public void checkWin() {
		// TODO check if one of the players has won the game after a move is executed
		boolean win = true;
			for (int i = 0; i < numRowCol; i++) {
				for (int j = 0; j < numRowCol; j++) {
					try {
					if (isBlackTurn) {
						if (squares[j][i].getPiece().getColor() == squares[j][i].getPiece().BLACK) {
							win = false;
						}
					}else {
						if (squares[j][i].getPiece().getColor() == squares[j][i].getPiece().RED) {
							win = false;
						}
					}
					}catch(Exception e) {}
				}
			}
			if (win) {
			if (isBlackTurn) {
				JOptionPane.showMessageDialog(this, "RED WINS!");
			}else {
				JOptionPane.showMessageDialog(this, "BLACK WINS!");
			}
			}
	}
	
	public boolean isAnyCapturePossible() {
	    for (int y = 0; y < 8; y++) {
	        for (int x = 0; x < 8; x++) {
	            Square square = squares[y][x];
	            Piece piece = square.getPiece();

	            // Check if the square contains a piece of the current player
	            if (piece != null && ((isBlackTurn && piece.getColor() == piece.BLACK) || (!isBlackTurn && piece.getColor() == piece.RED))) {
	                // Check if a capture is possible from this square
	                if (isCapturePossible(square)) {
	                    return true;
	                }
	            }
	        }
	    }

	    return false;
	}


	public boolean isCaptureMove(Square firstSelected, Square secondSelected) {
	    Piece piece = firstSelected.getPiece();
	    int x = firstSelected.getX() / Square.SIZE;
	    int y = firstSelected.getY() / Square.SIZE;
	    int dx = secondSelected.getX() / Square.SIZE - x;
	    int dy = secondSelected.getY() / Square.SIZE - y;

	    // Check if the move is a capture move
	    if (Math.abs(dx) == 2 && Math.abs(dy) == 2) {
	        int nx = x + dx / 2;
	        int ny = y + dy / 2;

	        // Check if the jumped over square contains an opponent's piece
	        if (squares[ny][nx].getPiece() != null && squares[ny][nx].getPiece().getColor() != piece.getColor()) {
	            return true;
	        }
	    }

	    return false;
	}
	public boolean isCapturePossible(Square square) {
	    Piece piece = square.getPiece();
	    int x = square.getX() / Square.SIZE;
	    int y = square.getY() / Square.SIZE;

	    // Check all four possible capture directions
	    for (int dx = -1; dx <= 1; dx += 2) {
	        for (int dy = -1; dy <= 1; dy += 2) {
	            int nx = x + dx;
	            int ny = y + dy;

	            // Check if the next square is inside the board and contains an opponent's piece
	            if (nx >= 0 && nx < 8 && ny >= 0 && ny < 8 && squares[ny][nx].getPiece() != null && squares[ny][nx].getPiece().getColor() != piece.getColor()) {
	                int nnx = nx + dx;
	                int nny = ny + dy;

	                // Check if the next next square is inside the board and is empty
	                if (nnx >= 0 && nnx < 8 && nny >= 0 && nny < 8 && squares[nny][nnx].getPiece() == null) {
	                    // For normal pieces, check the direction of capture
	                    if (!piece.isCrown()) {
	                        if ((piece.getColor() == piece.RED && dy < 0) || (piece.getColor() == piece.BLACK && dy > 0)) {
	                            return true;
	                        }
	                    } else {
	                        // For king pieces, capture in both directions is allowed
	                        return true;
	                    }
	                }
	            }
	        }
	    }

	    return false;
	}

	
	




}
