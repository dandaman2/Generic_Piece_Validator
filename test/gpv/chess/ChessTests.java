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

import static gpv.chess.ChessPieceDescriptor.*;
//import static org.junit.Assert.*;
import static gpv.util.Coordinate.makeCoordinate;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import gpv.util.Board;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

/**
 * Tests to ensure that pieces are created correctly and that all pieces
 * get created.
 * @version Feb 23, 2020
 */
class ChessPieceTests
{
	private static ChessPieceFactory factory = null;
	private Board board;
	
	@BeforeAll
	public static void setupBeforeTests()
	{
		factory = new ChessPieceFactory();
	}
	
	@BeforeEach
	public void setupTest()
	{
		board = new Board(8, 8);
	}
	
	@Test
	void makePiece()
	{
		ChessPiece pawn = factory.makePiece(WHITEPAWN);
		assertNotNull(pawn);
	}
	
	/**
	 * This type of test loops through each value in the Enum and
	 * one by one feeds it as an argument to the test method.
	 * It's worth looking at the different types of parameterized
	 * tests in JUnit: 
	 * https://junit.org/junit5/docs/current/user-guide/#writing-tests-parameterized-tests
	 * @param d the Enum value
	 */
	@ParameterizedTest
	@EnumSource(ChessPieceDescriptor.class)
	void makeOneOfEach(ChessPieceDescriptor d)
	{
		ChessPiece p = factory.makePiece(d);
		assertNotNull(p);
		assertEquals(d.getColor(), p.getColor());
		assertEquals(d.getName(), p.getName());
	}

	@Test
	void placeOnePiece()
	{
		ChessPiece p = factory.makePiece(BLACKPAWN);
		board.putPieceAt(p, makeCoordinate(2, 2));
		assertEquals(p, board.getPieceAt(makeCoordinate(2, 2)));
	}

	@Test
	void placeTwoPieces()
	{
		ChessPiece bn = factory.makePiece(BLACKKNIGHT);
		ChessPiece wb = factory.makePiece(WHITEBISHOP);
		board.putPieceAt(bn, makeCoordinate(3, 5));
		board.putPieceAt(wb, makeCoordinate(2, 6));
		assertEquals(bn, board.getPieceAt(makeCoordinate(3, 5)));
		assertEquals(wb, board.getPieceAt(makeCoordinate(2, 6)));
	}
	
	@Test
	void checkForPieceHasMoved()
	{
		ChessPiece bq = factory.makePiece(BLACKQUEEN);
		assertFalse(bq.hasMoved());
		bq.setHasMoved();
		assertTrue(bq.hasMoved());
	}
	
	@Test
	void thisUsedToFailOnDelivery()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		board.putPieceAt(wk, makeCoordinate(1,5));
		assertTrue(wk.canMove(makeCoordinate(1,5), makeCoordinate(2, 5), board));
	}

	//Tests for diffColorAt////////////////////////////////////////////////////////////

	//Check to make sure that the enemy of a black pawn is a white piece
	@Test
	void checkEnemyEnemyPiece() {
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		board.putPieceAt(bp, makeCoordinate(5, 5));
		board.putPieceAt(factory.makePiece(WHITEPAWN), makeCoordinate(4, 5));
		assertTrue(bp.diffColorAt(makeCoordinate(4, 5), board));
	}


	//Check to make sure a black piece does not recognize another black piece as an enemy
	@Test
	void checkEnemyNonEnemyPiece() {
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		board.putPieceAt(bp, makeCoordinate(5, 5));
		board.putPieceAt(factory.makePiece(BLACKPAWN), makeCoordinate(4, 5));
		assertFalse(bp.diffColorAt(makeCoordinate(4, 5), board));
	}

	//Check to make sure a black piece does not recognize an empty space as an enemy
	@Test
	void checkEnemyEmptySpace() {
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		board.putPieceAt(bp, makeCoordinate(5, 5));
		assertFalse(bp.diffColorAt(makeCoordinate(4, 5), board));
	}

	//Tests for sameColorAt////////////////////////////////////////////////////////////

	//Check to make sure that the ally of a black pawn is not a white piece
	@Test
	void checkAllyEnemyPiece() {
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		board.putPieceAt(bp, makeCoordinate(5, 5));
		board.putPieceAt(factory.makePiece(WHITEPAWN), makeCoordinate(4, 5));
		assertFalse(bp.sameColorAt(makeCoordinate(4, 5), board));
	}


	//Check to make sure a black piece recognizes another black piece as an ally
	@Test
	void checkAllyAllyPiece() {
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		board.putPieceAt(bp, makeCoordinate(5, 5));
		board.putPieceAt(factory.makePiece(BLACKPAWN), makeCoordinate(4, 5));
		assertTrue(bp.sameColorAt(makeCoordinate(4, 5), board));
	}

	//Check to make sure a black piece does not recognize an empty space as an enemy
	@Test
	void checkAllyEmptySpace() {
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		board.putPieceAt(bp, makeCoordinate(5, 5));
		assertFalse(bp.sameColorAt(makeCoordinate(4, 5), board));
	}



	//INDIVIDUAL PIECE TESTS////////////////////////////////////////////////////////////

	//Tests for Pawns///////////////////////////////////////////////////////////////////

	//Fail move if something is in front of the pawn
	@Test
	void inFrontOfPawn() {
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		board.putPieceAt(wp, makeCoordinate(2, 1));
		board.putPieceAt(bp, makeCoordinate(3, 1));
		assertFalse(wp.canMove(makeCoordinate(2, 1), makeCoordinate(3, 1), board));
	}

	//Pass if next space is empty
	@Test
	void nothingInFrontOfPawn() {
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wp, makeCoordinate(2, 1));
		assertTrue(wp.canMove(makeCoordinate(2, 1), makeCoordinate(3, 1), board));
	}

	//Check to make sure white pawn goes forward one row
	@Test
	void whitePawnForwards1() {
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wp, makeCoordinate(5, 5));
		assertTrue(wp.canMove(makeCoordinate(5, 5), makeCoordinate(6, 5), board));
	}

	//Check to make sure white pawn doesnt move sideways
	@Test
	void IllegalWhitePawnSideways() {
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wp, makeCoordinate(5, 5));
		assertFalse(wp.canMove(makeCoordinate(5, 5), makeCoordinate(5, 6), board));
	}

	//Check to make sure white pawn goes forward one row
	@Test
	void IllegalWhitePawnDiagonal() {
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wp, makeCoordinate(5, 5));
		assertFalse(wp.canMove(makeCoordinate(5, 5), makeCoordinate(6, 6), board));
	}

	//Check to make sure white pawn cannot go forwards 2 spots if not in starting position
	@Test
	void IllegalWhitePawnForwards2() {
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		wp.setHasMoved(); // Pawn has moved previously
		board.putPieceAt(wp, makeCoordinate(5, 5));
		assertFalse(wp.canMove(makeCoordinate(5, 5), makeCoordinate(7, 5), board));
	}

	//Check to make sure white pawn cannot go forwards 2 spots if not in starting position
	@Test
	void whitePawnForwards2() {
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wp, makeCoordinate(2, 1));
		assertTrue(wp.canMove(makeCoordinate(2, 1), makeCoordinate(4, 1), board));
	}

	//Check to make sure white pawn cannot go forwards 2 spots if not in starting position
	@Test
	void whitePawnForwards3() {
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wp, makeCoordinate(2, 1));
		assertFalse(wp.canMove(makeCoordinate(2, 1), makeCoordinate(5, 1), board));
	}

	//Check to make sure black pawn can only move in proper direction
	@Test
	void blackPawnForwards1() {
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		board.putPieceAt(bp, makeCoordinate(7, 1));
		assertTrue(bp.canMove(makeCoordinate(7, 1), makeCoordinate(6, 1), board));
	}

	//Check to make sure white pawn can only move in proper direction
	@Test
	void IllegalWhitePawnForwards1() {
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wp, makeCoordinate(7, 1));
		assertFalse(wp.canMove(makeCoordinate(7, 1), makeCoordinate(6, 1), board));
	}

	//Check to make sure black pawn can only move twice if pawn hasnt moved
	@Test
	void blackPawnForwards2() {
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		board.putPieceAt(bp, makeCoordinate(7, 1));
		assertTrue(bp.canMove(makeCoordinate(7, 1), makeCoordinate(5, 1), board));
	}

	//Check to make sure black pawn can only move twice if pawn hasnt moved
	@Test
	void movedBlackPawnForwards2() {
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		bp.setHasMoved();
		board.putPieceAt(bp, makeCoordinate(7, 1));
		assertFalse(bp.canMove(makeCoordinate(7, 1), makeCoordinate(5, 1), board));
	}

	//Check to make sure black pawn can only move in proper direction
	@Test
	void IllegalBlackPawnForwards1() {
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		board.putPieceAt(bp, makeCoordinate(6, 1));
		assertFalse(bp.canMove(makeCoordinate(6, 1), makeCoordinate(7, 1), board));
	}

	//Check to make sure black pawn can only move in proper direction when in the starting location
	@Test
	void IllegalBlackPawnForwards2() {
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		board.putPieceAt(bp, makeCoordinate(6, 1));
		assertFalse(bp.canMove(makeCoordinate(6, 1), makeCoordinate(8, 1), board));
	}


	//Check to make sure black pawn can capture properly to the right
	@Test
	void properBlackPawnCaptureRight() {
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		board.putPieceAt(bp, makeCoordinate(5, 5));
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wp, makeCoordinate(4, 4));
		assertTrue(bp.canMove(makeCoordinate(5, 5), makeCoordinate(4, 4), board));
	}

	//Check to make sure black pawn can capture properly to the left
	@Test
	void properBlackPawnCaptureLeft() {
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		board.putPieceAt(bp, makeCoordinate(5, 5));
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wp, makeCoordinate(4, 6));
		assertTrue(bp.canMove(makeCoordinate(5, 5), makeCoordinate(4, 6), board));
	}

	//Check to make sure white pawn can capture properly to the left
	@Test
	void properWhitePawnCaptureLeft() {
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		board.putPieceAt(bp, makeCoordinate(5, 5));
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wp, makeCoordinate(4, 4));
		assertTrue(wp.canMove(makeCoordinate(4, 4), makeCoordinate(5, 5), board));
	}

	//Check to make sure white pawn can capture properly to the right
	@Test
	void properWhitePawnCaptureRight() {
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		board.putPieceAt(bp, makeCoordinate(5, 5));
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wp, makeCoordinate(4, 6));
		assertTrue(wp.canMove(makeCoordinate(4, 6), makeCoordinate(5, 5), board));
	}

	//Check to make sure white pawn cannot capture backwards
	@Test
	void backwardsWhitePawnCapture() {
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wp, makeCoordinate(5, 5));
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		board.putPieceAt(bp, makeCoordinate(4, 4));
		assertFalse(wp.canMove(makeCoordinate(5, 5), makeCoordinate(4, 4), board));
	}

	//Check to make sure black pawn cannot capture backwards
	@Test
	void backwardsBlackPawnCapture() {
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		board.putPieceAt(bp, makeCoordinate(4, 4));
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wp, makeCoordinate(5, 5));
		assertFalse(wp.canMove(makeCoordinate(4, 4), makeCoordinate(5, 5), board));
	}

	//Check to make sure black pawn can capture properly to the right
	@Test
	void samePawnIllgalCapture() {
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		board.putPieceAt(bp, makeCoordinate(5, 5));
		ChessPiece bp2 = factory.makePiece(BLACKPAWN);
		board.putPieceAt(bp2, makeCoordinate(4, 4));
		assertFalse(bp.canMove(makeCoordinate(5, 5), makeCoordinate(4, 4), board));
	}


	//Tests for Kings//////////////////////////////////////////////////////////////////////

	//Helper method to provide a stream of possible moves for a king that can move in any direction
	static Stream<Arguments> helperKingValues(){
		return Stream.of(
				Arguments.of(5, 5, 6, 5), //top
				Arguments.of(5, 5, 6, 6), //top-r
				Arguments.of(5, 5, 5, 6), //right
				Arguments.of(5, 5, 4, 6), //bot-r
				Arguments.of(5, 5, 4, 5), //bot
				Arguments.of(5, 5, 4, 4), //bot-l
				Arguments.of(5, 5, 5, 4), //left
				Arguments.of(5, 5, 6, 4) //top-l
		);
	}

	//Check to make sure King can move one space upwards
	@Test
	void kingMovementUp() {
		ChessPiece wk = factory.makePiece(WHITEKING);
		board.putPieceAt(wk, makeCoordinate(1, 5));
		assertTrue(wk.canMove(makeCoordinate(1, 5), makeCoordinate(2, 5), board));
	}

	//Check to make sure a king can move in any direction so long as it is on the board
	@ParameterizedTest
	@MethodSource("helperKingValues")
	void kingMovementAnyDirection(int x1, int y1, int x2, int y2) {
		ChessPiece wk = factory.makePiece(WHITEKING);
		board.putPieceAt(wk, makeCoordinate(x1, y1));
		assertTrue(wk.canMove(makeCoordinate(x1, y1), makeCoordinate(x2, y2), board));
	}

	//Castling Tests

	//Castling towards the higher-rank rook
	@Test
	void castlingWay1() {
		ChessPiece wk = factory.makePiece(WHITEKING);
		board.putPieceAt(wk, makeCoordinate(1, 5));
		ChessPiece wr = factory.makePiece(WHITEROOK);
		board.putPieceAt(wr, makeCoordinate(1, 8));
		assertTrue(wk.canMove(makeCoordinate(1, 5), makeCoordinate(1, 7), board));
	}

	//Castling towards the lower-rank rook
	@Test
	void castlingWay2() {
		ChessPiece wk = factory.makePiece(WHITEKING);
		board.putPieceAt(wk, makeCoordinate(1, 5));
		ChessPiece wr = factory.makePiece(WHITEROOK);
		board.putPieceAt(wr, makeCoordinate(1, 1));
		assertTrue(wk.canMove(makeCoordinate(1, 5), makeCoordinate(1, 3), board));
	}

	//Castling towards the higher-rank rook without one existing
	@Test
	void castlingNoRookWay1() {
		ChessPiece wk = factory.makePiece(WHITEKING);
		board.putPieceAt(wk, makeCoordinate(1, 5));
		assertFalse(wk.canMove(makeCoordinate(1, 5), makeCoordinate(1, 7), board));
	}

	//Castling towards the lower-rank rook without one existing
	@Test
	void castlingNoRookWay2() {
		ChessPiece wk = factory.makePiece(WHITEKING);
		board.putPieceAt(wk, makeCoordinate(1, 5));
		assertFalse(wk.canMove(makeCoordinate(1, 5), makeCoordinate(1, 3), board));
	}

	//Castling attempt after the king has been moved
	@Test
	void castlingKingMoved() {
		ChessPiece wk = factory.makePiece(WHITEKING);
		board.putPieceAt(wk, makeCoordinate(2, 5));
		wk.setHasMoved();
		ChessPiece wr = factory.makePiece(WHITEROOK);
		board.putPieceAt(wr, makeCoordinate(1, 8));
		assertFalse(wk.canMove(makeCoordinate(2, 5), makeCoordinate(2, 3), board));
	}

	//Castling attempt after the rook has moved

	@Test
	void castlingRookMoved() {
		ChessPiece wk = factory.makePiece(WHITEKING);
		board.putPieceAt(wk, makeCoordinate(1, 5));
		ChessPiece wr = factory.makePiece(WHITEROOK);
		wr.setHasMoved();
		board.putPieceAt(wr, makeCoordinate(1, 8));
		assertFalse(wk.canMove(makeCoordinate(1, 5), makeCoordinate(1, 7), board));
	}

	//Castling towards the higher-rank rook
	@Test
	void blackCastlingWay1() {
		ChessPiece bk = factory.makePiece(BLACKKING);
		board.putPieceAt(bk, makeCoordinate(8, 5));
		ChessPiece br = factory.makePiece(BLACKROOK);
		board.putPieceAt(br, makeCoordinate(8, 8));
		assertTrue(bk.canMove(makeCoordinate(8, 5), makeCoordinate(8, 7), board));
	}

	//Castling towards the lower-rank rook
	@Test
	void blackCastlingWay2() {
		ChessPiece bk = factory.makePiece(BLACKKING);
		board.putPieceAt(bk, makeCoordinate(8, 5));
		ChessPiece br = factory.makePiece(BLACKROOK);
		board.putPieceAt(br, makeCoordinate(8, 1));
		assertTrue(bk.canMove(makeCoordinate(8, 5), makeCoordinate(8, 3), board));
	}


	//Tests for Rook//////////////////////////////////////////////////////////////////////

	//Valid horizontal rook movement
	@Test
	void validHorizRook1() {
		ChessPiece wr= factory.makePiece(WHITEROOK);
		board.putPieceAt (wr, makeCoordinate(1, 1));
		assertTrue(wr.canMove(makeCoordinate(1, 1), makeCoordinate(1, 7), board));
	}

	//Valid horizontal rook movement in different direction
	@Test
	void validHorizRook2() {
		ChessPiece wr= factory.makePiece(WHITEROOK);
		board.putPieceAt (wr, makeCoordinate(1, 7));
		assertTrue(wr.canMove(makeCoordinate(1, 7), makeCoordinate(1, 1), board));
	}

	//Invalid horizontal rook movement because of piece in the way
	@Test
	void pieceInWayHorizRook1() {
		ChessPiece wr= factory.makePiece(WHITEROOK);
		board.putPieceAt (wr, makeCoordinate(2, 1));
		ChessPiece wp= factory.makePiece(WHITEPAWN);
		board.putPieceAt (wp, makeCoordinate(2, 4));
		assertFalse(wr.canMove(makeCoordinate(2, 1), makeCoordinate(2, 7), board));
	}

	//Invalid horizontal rook movement because of piece in the way (opposite direction)
	@Test
	void pieceInWayHorizRook2() {
		ChessPiece wr= factory.makePiece(WHITEROOK);
		board.putPieceAt (wr, makeCoordinate(2, 7));
		ChessPiece wp= factory.makePiece(WHITEPAWN);
		board.putPieceAt (wp, makeCoordinate(2, 4));
		assertFalse(wr.canMove(makeCoordinate(2, 7), makeCoordinate(2, 1), board));
	}

	//invalid horizontal movement to a different row
	@Test
	void invalidMovementRook() {
		ChessPiece wr= factory.makePiece(WHITEROOK);
		board.putPieceAt (wr, makeCoordinate(2, 7));
		assertFalse(wr.canMove(makeCoordinate(2, 7), makeCoordinate(3, 3), board));
	}


	//Valid vertical rook movement
	@Test
	void validVertRook1() {
		ChessPiece wr= factory.makePiece(WHITEROOK);
		board.putPieceAt (wr, makeCoordinate(1, 1));
		assertTrue(wr.canMove(makeCoordinate(1, 1), makeCoordinate(7, 1), board));
	}

	//Valid vertical rook movement in different direction
	@Test
	void validVertRook2() {
		ChessPiece wr= factory.makePiece(WHITEROOK);
		board.putPieceAt (wr, makeCoordinate(7, 7));
		assertTrue(wr.canMove(makeCoordinate(7, 1), makeCoordinate(1, 1), board));
	}

	//Invalid vertical rook movement because of piece in the way
	@Test
	void pieceInWayVertRook1() {
		ChessPiece wr= factory.makePiece(WHITEROOK);
		board.putPieceAt (wr, makeCoordinate(1, 4));
		ChessPiece wp= factory.makePiece(WHITEPAWN);
		board.putPieceAt (wp, makeCoordinate(2, 4));
		assertFalse(wr.canMove(makeCoordinate(1, 4), makeCoordinate(7, 4), board));
	}

	//Invalid vertical rook movement because of piece in the way (opposite direction)
	@Test
	void pieceInWayVertRook2() {
		ChessPiece wr= factory.makePiece(WHITEROOK);
		board.putPieceAt (wr, makeCoordinate(7, 4));
		ChessPiece wp= factory.makePiece(WHITEPAWN);
		board.putPieceAt (wp, makeCoordinate(2, 4));
		assertFalse(wr.canMove(makeCoordinate(7, 4), makeCoordinate(1, 4), board));
	}


}
