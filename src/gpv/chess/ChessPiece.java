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

	/**
	 * Checks to see if the given piece can legally move from one coordinate to another
	 * @param from The coordinate the piece is moving from
	 * @param to the coordinate the piece is moving to
	 * @param b the board state of the game
	 * @return a boolean indicating if the move is valid for a given piece
	 */
	@Override
	public boolean canMove(Coordinate from, Coordinate to, Board b)
	{
		//Make sure input is valid (piece at 'from' is the same as the current piece)
		if(((ChessPiece)b.getPieceAt(from))== null ||
				((ChessPiece)b.getPieceAt(from)).getName() != this.getName() ||
				((ChessPiece)b.getPieceAt(from)).getColor() != this.getColor())
			return false;

		//Make sure locations are valid
		if(!to.isInBounds(b) || !from.differentLocation(to) || this.sameColorAt(to, b))
			return false;


		//Iterate through MoveSets to find proper one
		for(MoveSet r: MoveSet.values()){
			if(r.name().equals(this.getName().name())){
				return r.isValid(this, from, to , b);
			}
		}
		return false;
		//return this.getName().isValid(this, from, to, b);
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
