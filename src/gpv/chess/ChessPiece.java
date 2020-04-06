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
		//Make sure input is valid (piece at 'from' is the same as the current piece)
		if(((ChessPiece)b.getPieceAt(from))== null ||
				((ChessPiece)b.getPieceAt(from)).getName() != this.getName() ||
				((ChessPiece)b.getPieceAt(from)).getColor() != this.getColor())
			return false;

		//Make sure locations are valid
		if(!to.isInBounds(b) || !from.differentLocation(to) || this.sameColorAt(to, b))
			return false;

		return this.getName().isValid(this, from, to, b);
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

	/**
	 * Returns a boolean as to whether a valid horizontal move can
	 * be made between the two input coordinates
	 * @param from the first input coordinate to check
	 * @param to the second input coordinate to check
	 * @param board the game board state
	 * @return True if a valid move can be made, False if the coordinates are not on the same horizontal plane,
	 * or there are other pieces in the way.
	 */
	public static boolean validHorizMove(Coordinate from, Coordinate to, Board board){
		if(from.getRow() != to.getRow()){
			return false;
		}
		int start, end;
		if(from.getColumn() < to.getColumn()){
			start = from.getColumn() + 1;
			end = to.getColumn();
		}else{
			start = to.getColumn() + 1;
			end = from.getColumn();
		}
		for(int i = start; i<end; i++){
			if(board.getPieceAt(Coordinate.makeCoordinate(from.getRow(), i)) != null){
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns a boolean as to whether a valid vertical move can
	 * be made between the two input coordinates
	 * @param from the first input coordinate to check
	 * @param to the second input coordinate to check
	 * @param board the game board state
	 * @return True if a valid move can be made, False if the coordinates are not on the same vertical plane,
	 * or there are other pieces in the way.
	 */
	public static boolean validVertMove(Coordinate from, Coordinate to, Board board){
		if(from.getColumn() != to.getColumn()){
			return false;
		}
		int start, end;
		if(from.getRow() < to.getRow()){
			start = from.getRow() + 1;
			end = to.getRow();
		}else{
			start = to.getRow() + 1;
			end = from.getRow();
		}
		for(int i = start; i<end; i++){
			if(board.getPieceAt(Coordinate.makeCoordinate(i, from.getColumn())) != null){
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns a boolean as to whether a valid diagonal move can
	 * be made between the two input coordinates
	 * @param from the first input coordinate to check
	 * @param to the second input coordinate to check
	 * @param board the game board state
	 * @return True if a valid move can be made, False if the coordinates are not on the same diagonal,
	 * or there are other pieces in the way.
	 */
	public static boolean validDiagMove(Coordinate from, Coordinate to, Board board){
		if(Math.abs(from.getRow() - to.getRow()) == Math.abs(from.getColumn() - to.getColumn())){
			int dr = (from.getRow() < to.getRow())? 1 : -1;
			int dc = (from.getColumn() < to.getColumn())? 1 : -1;
			int diff = Math.abs(from.getRow() - to.getRow());
			int loops = 1;
			while(loops < diff){
				if(board.getPieceAt(Coordinate.makeCoordinate(from.getRow() + loops*dr, from.getColumn() + loops*dc)) != null){
					return false;
				}
				loops++;
			}
			return true;
		}
		return false;
	}
}
