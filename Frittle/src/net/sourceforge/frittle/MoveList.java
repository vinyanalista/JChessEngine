/*
Frittle - Chess Engine for WinBoard/XBoard [http://frittle.sourceforge.net]
Copyright (C) 2009 Rohan Padhye <verminox@gmail.com>

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package net.sourceforge.frittle;

/**
 * A set of moves generated by the move generator is implemented as a
 * singly linked list. Operations are available to select only certain moves and
 * re-order the moves in best-first fashion.
 *
 * This class does not make use of the Java Collections interface for
 * performance purposes.
 */
public class MoveList implements Iterable<Move>
{
    /** The head of the linked list */
    private MoveNode first;

    /** The tail of the linked list */
    private MoveNode last;

    /**
     * Add a move to the list. Illegal moves are not added
     * @param   move    the Move to add to the list
     * @param   state   the GameState before the move is attempted
     */
    public void add(Move move)
    {
        if(move.illegal)
            return;
        MoveNode node = new MoveNode(move);
        if(first == null)  // List is empty
            first = node;  // Set first node
        else
            last.next = node; // Otherwise chain the node at the end

        last = node;  // Last points to the last added node
    }

    /**
     * Concatenates two move lists together.
     * @param   list    the operand list to concatenate
     * @return  the concatenated list
     */
    public MoveList addAll(MoveList list)
    {
        if(first == null) // If this is empty, result is 'list'
            first = list.first;    // set first node
        else
            last.next = list.first; // chain the two lists together

        if(list.last != null)      // second list is not empty
            last = list.last;      // new last node
        return this;
    }

    /**
     * Deletes all elements in the list
     */
    public void clear()
    {
        first = null;
        last = null;
    }

    /**
     * Gets a reference to the first element in the list
     * @return  the first  Move
     */
    public Move first()
    {
        return first.move;
    }

    /**
     * Gets a reference to the last added element in the list
     * @return  the last added Move
     */
    public Move last()
    {
        return last.move;
    }

    /**
     * Whether the move list is empty
     * @return  <code>true</code> if no moves are present in the list
     */
    public boolean isEmpty()
    {
        return (first==null);
    }

    /**
     * Whether the move list has any items
     * @return  <code>true</code> if the list contains at least one move .
     */
    public boolean hasMoves()
    {
        return (first!=null);
    }

    /**
     * Returns an iterator over the moves in the list
     * @return the Move iterator.
     */
    public java.util.Iterator<Move> iterator()
    {
        return new MoveIterator(first);
    }

}

/**
 * A singly linked list node.
 */
class MoveNode
{
    /** Move stored in the node */
    Move move;

    /** Reference to next node in the list, or null if the node is terminal */
    MoveNode next;

    /**
     * Construct a node from a Move object
     * @param   move    the Move object to store in the node
     */
    MoveNode(Move move)
    {
        this.move = move;
    }
}

/**
 * A Move iterator
 */
class MoveIterator implements java.util.Iterator<Move>
{
    MoveNode node;

    MoveIterator(MoveNode node)
    {
        this.node = node;
    }

    public boolean hasNext()
    {
        return (node != null);
    }

    public Move next()
    {
        Move move = node.move;
        node = node.next;
        return move;
    }

    public void remove()
    {
        throw new UnsupportedOperationException();
    }
}