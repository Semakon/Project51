package Model.Game;

import Model.Game.Enumerations.Identity;
import Model.Game.Enumerations.Positioning;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Martijn on 11-1-2016.
 */
public class Board {

    private Map<Location, Tile> field;

    public Board() {
        field = new HashMap<>();
    }

    public Map<Location, Tile> getField() {
        return field;
    }

    public boolean fieldIsEmpty() {
        return field.isEmpty();
    }

    public boolean validMove(Move move) {
        boolean valid;
        if (move instanceof PutMove) {
            valid = validPut((PutMove) move);
        } else
            valid = move instanceof TradeMove && validTrade((TradeMove) move);
        return valid;
    }

    private boolean validPut(PutMove move) { //TODO: add exceptions
        Map<Location, Tile> m = move.getMove();

        boolean valid = false;
        boolean verticalValid = false;
        boolean horizontalValid = false;

        for (Location loc : move.getMove().keySet()) {
            for (Location loc2 : field.keySet()) {
                if (loc.isEqualTo(loc2)) {
                    //TODO: throw exception: location already in use
                    return false;
                }
            }
        }

        // vertical
        outerLoop:
        if (move.getPositioning() == Positioning.vertical) {
            int testedBlocks = 0;
            int validatedBlocks = 0;
            for (Location loc : m.keySet()) {
                Tile tileBelow = null;
                Tile tileAbove = null;
                boolean inMove = false;
                for (Location loc2 : m.keySet()) {
                    if (loc2.isEqualTo(loc.getX(), loc.getY() - 1)) {
                        // (loc.x, loc.y - 1) is not empty.
                        inMove = true;
                        break;
                    }
                }
                if (inMove) {
                    inMove = false;
                } else {
                    for (Location fieldLoc : field.keySet()) {
                        if (field.get(fieldLoc).isEqualTo(m.get(loc))) {
                            valid = false;
                            break outerLoop;
                        }
                        if (fieldLoc.isEqualTo(loc.getX(), loc.getY() - 1)) {
                            // (loc.x, loc.y - 1) is present on the board.
                            tileBelow = field.get(fieldLoc);
                            testedBlocks++;
                            break;
                        }
                    }
                }
                for (Location loc2 : m.keySet()) {
                    if (loc2.isEqualTo(loc.getX(), loc.getY() + 1)) {
                        // (loc.x, loc.y + 1) is not empty.
                        inMove = true;
                        break;
                    }
                }
                if (!inMove) {
                    for (Location fieldLoc : field.keySet()) {
                        if (field.get(fieldLoc).isEqualTo(m.get(loc))) {
                            valid = false;
                            break outerLoop;
                        }
                        if (fieldLoc.isEqualTo(loc.getX(), loc.getY() + 1)) {
                            // (loc.x, loc.y + 1) is present on the board.
                            tileAbove = field.get(fieldLoc);
                            testedBlocks++;
                            break;
                        }
                    }
                }
                if (move.getIdentity() == Identity.color) {
                    if (tileBelow != null && tileBelow.getColor() == m.get(loc).getColor()) {
                        validatedBlocks++;
                    }
                    if (tileAbove != null && tileAbove.getColor() == m.get(loc).getColor()) {
                        validatedBlocks++;
                    }
                } else if (move.getIdentity() == Identity.shape) {
                    if (tileBelow != null && tileBelow.getShape() == m.get(loc).getShape()) {
                        validatedBlocks++;
                    }
                    if (tileAbove != null && tileAbove.getShape() == m.get(loc).getShape()) {
                        validatedBlocks++;
                    }
                } else if (move.getIdentity() == Identity.unspecified) {
                    if (tileBelow != null && (tileBelow.getColor() == m.get(loc).getColor() || tileBelow.getShape() == m.get(loc).getShape())) {
                        validatedBlocks++;
                    }
                    if (tileAbove != null && (tileAbove.getColor() == m.get(loc).getColor() || tileAbove.getShape() == m.get(loc).getShape())) {
                        validatedBlocks++;
                    }
                } else {
                    //TODO: throw runtimeException: invalid identity.
                }
            }
            if (testedBlocks - validatedBlocks == 0) {
                verticalValid = true;
            }

        } else if (move.getPositioning() == Positioning.horizontal) {
            //TODO: implement
        } else if (move.getPositioning() == Positioning.unspecified) {
            //TODO: implement
        } else {
            //TODO: throw runtimeException: invalid positioning
        }


        return valid;
    }

    public boolean validLine(PutMove move, Location location, int step) { //TODO: add choice for X or Y

        //TODO: FIX EVERYTHING BECAUSE I FAILED TO UNDERSTAND THE SIMPLEST OF RULES... FUCK THIS GAME
        boolean valid = false;
        boolean inMove = false;
        for (Location loc : move.getMove().keySet()) {
            if (loc.isEqualTo(location)) {
                valid = validLine(move, new Location(loc.getX(), loc.getY() + step), step);             // recursive
                inMove = true;
                break;
            }
        }
        if (!inMove) {
            for (Location loc : field.keySet()) {
                if (loc.isEqualTo(location)) {

                    if (move.getIdentity() == Identity.color) {
                        for (Location loc2 : move.getMove().keySet()) {
                            if (field.get(loc).getColor() == move.getMove().get(loc2).getColor()) {
                                //TODO: throw exception: color already in row/column
                                return false;
                            }
                        }
                        valid = validLine(move, new Location(loc.getX(), loc.getY() + step), step);     // recursive

                    } else if (move.getIdentity() == Identity.shape) {
                        for (Location loc2 : move.getMove().keySet()) {
                            if (field.get(loc).getShape() == move.getMove().get(loc2).getShape()) {
                                //TODO: throw exception: shape already in row/column
                                return false;
                            }
                        }
                        valid = validLine(move, new Location(loc.getX(), loc.getY() + step), step);     // recursive

                    } else if (move.getIdentity() == Identity.unspecified) {
                        for (Location loc2 : move.getMove().keySet()) {
                            if (field.get(loc).getColor() == move.getMove().get(loc2).getColor() ||
                                    field.get(loc).getShape() == move.getMove().get(loc2).getShape()) {
                                //TODO: throw exception: shape/color already in row/column
                                return false;
                            }
                        }
                    } else {
                        //TODO: throw runtimeException: invalid identity
                    }

                    break; // right location is found, no need to continue loop

                }
            }
        }
        return valid;
    }

    private boolean validTrade(TradeMove move) {
        //TODO: implement
        return false;
    }

}
