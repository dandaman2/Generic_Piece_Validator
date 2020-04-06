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
import static gpv.chess.ChessPiece.*;
import static gpv.chess.ChessPieceDescriptor.*;
import static gpv.util.Coordinate.makeCoordinate;
import static gpv.util.SquareInitializer.makeSquareInitializer;
//import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.Stream;

import gpv.util.Board;
import org.junit.jupiter.api.*;
import gpv.chess.*;

class NonPieceTests {

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


    //Valid Horizontal Movement Tests//////////////////////////////////////////////////////////////////

    //Test to make sure horizontal movement is working for a given direction
    @Test
    void validHorizMoveWay1(){
        assertTrue(validHorizMove(makeCoordinate(3, 1), makeCoordinate(3, 8), board));
    }

    //Test to make sure horizontal movement is working for the opposite direction
    @Test
    void validHorizMoveWay2(){
        assertTrue(validHorizMove(makeCoordinate(3, 8), makeCoordinate(3, 1), board));
    }

    //Test to make sure horizontal movement does not pass for changes in row
    @Test
    void invalidHorizMove(){
        assertFalse(validHorizMove(makeCoordinate(3, 1), makeCoordinate(4, 1), board));
    }

    //Test to make sure horizontal movement does not pass if a piece is in the way
    @Test
    void inTheWayHorizMoveWay1(){
        ChessPiece bp = factory.makePiece(BLACKPAWN);
        board.putPieceAt(bp, makeCoordinate(5, 7));
        assertFalse(validHorizMove(makeCoordinate(5, 5), makeCoordinate(5, 8), board));
    }

    //Test to make sure horizontal movement does not pass if a piece is in the way going the opposite direction
    @Test
    void inTheWayHorizMoveWay2(){
        ChessPiece bp = factory.makePiece(BLACKPAWN);
        board.putPieceAt(bp, makeCoordinate(5, 7));
        assertFalse(validHorizMove(makeCoordinate(5, 8), makeCoordinate(5, 5), board));
    }

    //Valid Vertical Movement Tests//////////////////////////////////////////////////////////////////

    //Test to make sure vertical movement is working for a given direction
    @Test
    void validVertMoveWay1(){
        assertTrue(validVertMove(makeCoordinate(3, 1), makeCoordinate(5, 1), board));
    }

    //Test to make sure vertical movement is working for the opposite direction
    @Test
    void validVertMoveWay2(){
        assertTrue(validVertMove(makeCoordinate(5, 1), makeCoordinate(3, 1), board));
    }

    //Test to make sure horizontal movement does not pass for changes in row
    @Test
    void invalidVertMove(){
        assertFalse(validVertMove(makeCoordinate(3, 1), makeCoordinate(3, 2), board));
    }

    //Test to make sure horizontal movement does not pass if a piece is in the way
    @Test
    void inTheWayVertMoveWay1(){
        ChessPiece bp = factory.makePiece(BLACKPAWN);
        board.putPieceAt(bp, makeCoordinate(7, 5));
        assertFalse(validVertMove(makeCoordinate(5, 5), makeCoordinate(8, 5), board));
    }

    //Test to make sure horizontal movement does not pass if a piece is in the way going the opposite direction
    @Test
    void inTheWayVertMoveWay2(){
        ChessPiece bp = factory.makePiece(BLACKPAWN);
        board.putPieceAt(bp, makeCoordinate(7, 5));
        assertFalse(validVertMove(makeCoordinate(8, 5), makeCoordinate(5, 5), board));
    }

    //Valid Diagonal Movement Tests//////////////////////////////////////////////////////////////////

    //Test to make sure diagonal movement is working for a given direction
    @Test
    void validDiagMoveWay1(){
        assertTrue(validDiagMove(makeCoordinate(1, 1), makeCoordinate(2, 2), board));
    }

    //Test to make sure diagonal movement is working for the opposite direction
    @Test
    void validDiagMoveWay2(){
        assertTrue(validDiagMove(makeCoordinate(3, 4), makeCoordinate(1, 2), board));
    }

    //Test to make sure diagonal moves stay along a diagonal
    @Test
    void invalidDiagMove(){
        assertFalse(validDiagMove(makeCoordinate(5, 5), makeCoordinate(6, 7), board));
    }

    //Test to make sure diagonal movement does not pass if a piece is in the way
    @Test
    void inTheWayDiagMove1(){
        ChessPiece bp = factory.makePiece(BLACKPAWN);
        board.putPieceAt(bp, makeCoordinate(3, 3));
        assertFalse(validDiagMove(makeCoordinate(1, 1), makeCoordinate(4, 4), board));
    }

    //Test to make sure diagonal movement does not pass if a piece is in the way (different direction)
    @Test
    void inTheWayDiagMove2(){
        ChessPiece bp = factory.makePiece(BLACKPAWN);
        board.putPieceAt(bp, makeCoordinate(3, 3));
        assertFalse(validDiagMove(makeCoordinate(5, 1), makeCoordinate(1, 5), board));
    }

}

