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

import gpv.util.Board;
import gpv.util.Coordinate;

import static gpv.chess.ChessPiece.*;

/**
 * A functional interface which hold the method declaration for the isValid function
 * Used for assigning lambda functions to MoveRules instance variables within the PieceName enums
 */
@FunctionalInterface
interface MoveRules{
	boolean isValid(ChessPiece piece, Coordinate from, Coordinate to, Board b);
}

/**
 * A simple enumeration of chess piece names. Mainly used in the ChessPieceDescriptor
 * @version Feb 23, 2020
 */
public enum PieceName implements MoveRules {
	PAWN((piece, from, to, b) -> {
		int difference = (piece.getColor() == PlayerColor.WHITE)? 1 : -1;

		//If not capturing
		if(to.getColumn() == from.getColumn()) {

			//Cannot go forwards one if piece is blocking it (ally or enemy)
			if(b.getPieceAt(to) != null)
				return false;

			//Move forwards one
			if (to.getRow() - from.getRow() == difference) {
				return true;
			}
			//Move forwards two if not previously moved
			return !piece.hasMoved() && (to.getRow() - from.getRow() == 2*difference);


		}
		else{
			//capturing logic
			return(Math.abs(to.getColumn() - from.getColumn()) == 1 &&
					(piece.diffColorAt(to, b))&&
					(to.getRow() - from.getRow() == difference));
		}
	}),

	ROOK((piece, from, to, b) -> {
		//can move horizontally or vertically
		return validVertMove(from, to, b) || validHorizMove(from, to, b);
	}),

	KNIGHT((piece, from, to, b) -> {
		//Can move with one column difference and a two row difference or vice versa.
		return (Math.abs(from.getColumn() - to.getColumn()) == 1 && Math.abs(from.getRow() - to.getRow())== 2)||
				(Math.abs(from.getColumn() - to.getColumn()) == 2 && Math.abs(from.getRow() - to.getRow()) == 1);
	}),

	BISHOP((piece, from, to, b) -> {
		//can move diagonally
		return validDiagMove(from, to, b);
	}),

	QUEEN((piece, from, to, b) -> {
		//can move diagonally, vertically or horizontally
		return validDiagMove(from, to, b) || validVertMove(from, to, b) || validHorizMove(from, to, b);
	}),

	KING((piece, from, to, b) -> {
		//check if castling attempt
		if(validHorizMove(from, to, b) && Math.abs(from.getColumn()-to.getColumn()) == 2){
			//get the column location of where the rook should be based on the direction of castling
			int rookCol = (to.getColumn() < from.getColumn())? 1 : b.getnColumns(); //Get rook on first or last row
			ChessPiece closeRook = (ChessPiece)b.getPieceAt(Coordinate.makeCoordinate(to.getRow(), rookCol));
			//if space is empty or no clear line of sight, return false
			if(closeRook == null || !validHorizMove(from, Coordinate.makeCoordinate(to.getRow(), rookCol), b)){
				return false;
			}
			//check if Rook or king has moved
			return(closeRook.getName() == ROOK && !closeRook.hasMoved() && !piece.hasMoved());

		}
		return Math.abs(to.getColumn() - from.getColumn()) < 2 && Math.abs(to.getRow() - from.getRow()) < 2;
	});

	private MoveRules ruleset;

	/**
	 * A private costructor for assigning move rules to a given PieceName
	 * @param rules the runes to be assigned to the give PieceName (in lambda function format)
	 */
	private PieceName(MoveRules rules) {
		this.ruleset = rules;
	}

	/**
	 * A Method which checks if a given chess move is valid
	 * @param piece The piece that is being moved
	 * @param from the location that the piece was moved from
	 * @param to the location that the piece is being moved to
	 * @param board the state of the game board
	 * @return a boolean as to whether a given move is valid or not: True -> Valid, False -> Invalid
	 */
	@Override
	public boolean isValid(ChessPiece piece, Coordinate from, Coordinate to, Board board) {
		return ruleset.isValid(piece, from, to, board);
	}
}
