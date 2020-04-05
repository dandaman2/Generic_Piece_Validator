/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright ©2020 Gary F. Pollice
 *******************************************************************************/

package gpv.chess;

import gpv.Piece;
import gpv.util.*;

import static gpv.chess.PieceName.ROOK;
import static gpv.chess.PlayerColor.WHITE;

/**
 * The chess piece is a piece with some special properties that are used for
 * determining whether a piece can move. It implements the Piece interface
 * and adds properties and methods that are necessary for the chess-specific
 * behavior.
 * @version Feb 21, 2020
 */
public class ChessPiece implements Piece<ChessPieceDescriptor>
{
	private final ChessPieceDescriptor descriptor;
	private boolean hasMoved;	// true if this piece has moved
	
	/**
	 * The only constructor for a ChessPiece instance. Requires a descriptor.
	 * @param descriptor
	 */
	public ChessPiece(ChessPieceDescriptor descriptor)
	{
		this.descriptor = descriptor;
		hasMoved = false;
	}

	/*
	 * @see gpv.Piece#getDescriptor()
	 */
	@Override
	public ChessPieceDescriptor getDescriptor()
	{
		return descriptor;
	}
	
	/**
	 * @return the color
	 */
	public PlayerColor getColor()
	{
		return descriptor.getColor();
	}

	/**
	 * @return the name
	 */
	public PieceName getName()
	{
		return descriptor.getName();
	}

	/*
	 * @see gpv.Piece#canMove(gpv.util.Coordinate, gpv.util.Coordinate, gpv.util.Board)
	 */
	@Override
	public boolean canMove(Coordinate from, Coordinate to, Board b)
	{
		if(!to.isInBounds(b) || !from.differentLocation(to) || this.sameColorAt(to, b))
			return false;

		switch(this.getName()){

			case ROOK:
				return from.validHorizMove(to, b);

			case KING:
				//check if castling attempt
				if(from.validHorizMove(to, b) && Math.abs(from.getColumn()-to.getColumn()) == 2){
					//get the column location of where the rook should be based on the direction of castling
					int rookCol = (to.getColumn() < from.getColumn())? 1 : b.getnColumns();
					ChessPiece closeRook = (ChessPiece)b.getPieceAt(Coordinate.makeCoordinate(to.getRow(), rookCol));
					//if space is empty, return false
					if(closeRook == null){
						return false;
					}
					//check if Rook or king has moved
					return(closeRook.getName() == ROOK && !closeRook.hasMoved() && !this.hasMoved());

				}
				return Math.abs(to.getColumn() - from.getColumn()) < 2 && Math.abs(to.getRow() - from.getRow()) < 2;

			case PAWN:
				int difference = (this.getColor() == WHITE)? 1 : -1;

				//If not capturing
				if(to.getColumn() == from.getColumn()) {

					//Cannot go forwards one if piece is blocking it
					if(b.getPieceAt(to) != null)
						return false;

					//Move forwards one
					if (to.getRow() - from.getRow() == difference) {
						return true;
					}
					//Move forwards two if not previously moved
					return !this.hasMoved() && (to.getRow() - from.getRow() == 2*difference);

				//capturing logic
				}
				else{
					return(Math.abs(to.getColumn() - from.getColumn()) == 1 &&
						(this.diffColorAt(to, b))&&
							(to.getRow() - from.getRow() == difference));
				}


			default:
				return false;
		}
	}

	/**
	 * @return the hasMoved
	 */
	public boolean hasMoved()
	{
		return hasMoved;
	}

	/**
	 * Once it moves, you can't change it.
	 */
	public void setHasMoved()
	{
		hasMoved = true;
	}

	/**
	 * Checks to see if a coordinate has the same color as the current piece
	 * @param coordinate the coordinate to check for an ally piece
	 * @param board the game board state
	 * @return boolean as to whether there is a same-color piece at teh given coordinate
	 */
	public boolean sameColorAt(Coordinate coordinate, Board board){
		PlayerColor curColor = this.getColor();
		if(board.getPieceAt(coordinate) == null){
			return false;
		}
		ChessPieceDescriptor desc = (ChessPieceDescriptor)board.getPieceAt(coordinate).getDescriptor();
		if(desc.getColor() == curColor){
			return true;
		}
		return false;
	}

	/**
	 * Checks to see if a coordinate has a different-color (enemy) piece
	 * @param coordinate the coordinate to check for an enemy piece
	 * @param board the game board state
	 * @return boolean as to whether there is an enemy piece at the given coordinate
	 */
	public boolean diffColorAt(Coordinate coordinate, Board board){
		return !(board.getPieceAt(coordinate) == null || sameColorAt(coordinate, board));
	}
}
