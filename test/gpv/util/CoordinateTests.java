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


package gpv.util;
import static gpv.chess.ChessPieceDescriptor.*;
import static gpv.util.Coordinate.makeCoordinate;
import static gpv.util.SquareInitializer.makeSquareInitializer;
//import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.Stream;
import org.junit.jupiter.api.*;
import gpv.chess.*;

class CoordinateTests {

    private static ChessPieceFactory factory = null;
    private Board board;

    @BeforeAll
    public static void setupBeforeTests()
    {
        factory = new ChessPieceFactory();
    }

    @BeforeEach
    public void setup()
    {
        board = new Board(8, 8);
    }


    //Tests to make sure coordinate is in-bounds//////////////////////////////////////////

    @Test
    void InBoundsCoordinate1() {
        assertTrue(makeCoordinate(2, 1).isInBounds(board));
    }
    @Test
    void InBoundsCoordinate2() {
        assertTrue(makeCoordinate(1, 8).isInBounds(board));
    }

    //Tests to make sure that row <1 is out of bounds
    @Test
    void OutOfBoundsBottomCoordinate() {
        assertFalse(makeCoordinate(0, 2).isInBounds(board));
    }

    //Tests to make sure that column <1 is out of bounds
    @Test
    void OutOfBoundsLeftCoordinate() {
        assertFalse(makeCoordinate(3, 0).isInBounds(board));
    }

    //Tests to make sure that row >8 is out of bounds
    @Test
    void OutOfBoundsTopCoordinate() {
        assertFalse(makeCoordinate(9, 1).isInBounds(board));
    }

    //Tests to make sure that column >8 is out of bounds
    @Test
    void OutOfBoundsRightCoordinate() {
        assertFalse(makeCoordinate(5, 11).isInBounds(board));
    }


    //differentLocation Tests/////////////////////////////////////////////////////////////////////////

    //Tests to make sure that the two coordinates are different
    @Test
    void differentCoordinate() {
        assertTrue(makeCoordinate(2, 1).differentLocation(makeCoordinate(3, 1)));
    }

    //Tests to see if the two coordinates are the same
    @Test
    void sameCoordinate() {
        assertFalse(makeCoordinate(2, 1).differentLocation(makeCoordinate(2, 1)));
    }


    //Valid Horizontal Movement Tests

    //Test to make sure horizontal movement is working for a given direction
    @Test
    void validHorizMoveWay1(){
        assertTrue(makeCoordinate(3, 1).validHorizMove(makeCoordinate(3, 8), board));
    }

    //Test to make sure horizontal movement is working for the opposite direction
    @Test
    void validHorizMoveWay2(){
        assertTrue(makeCoordinate(3, 8).validHorizMove(makeCoordinate(3, 1), board));
    }

    //Test to make sure horizontal movement does not pass for changes in row
    @Test
    void invalidHorizMove(){
        assertFalse(makeCoordinate(3, 1).validHorizMove(makeCoordinate(4, 1), board));
    }

    //Test to make sure horizontal movement does not pass if a piece is in the way
    @Test
    void inTheWayHorizMoveWay1(){
        ChessPiece bp = factory.makePiece(BLACKPAWN);
        board.putPieceAt(bp, makeCoordinate(5, 7));
        assertFalse(makeCoordinate(5, 5).validHorizMove(makeCoordinate(5, 8), board));
    }

    //Test to make sure horizontal movement does not pass if a piece is in the way going the opposite direction
    @Test
    void inTheWayHorizMoveWay2(){
        ChessPiece bp = factory.makePiece(BLACKPAWN);
        board.putPieceAt(bp, makeCoordinate(5, 7));
        assertFalse(makeCoordinate(5, 8).validHorizMove(makeCoordinate(5, 5), board));
    }
}
