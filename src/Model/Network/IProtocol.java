package Model.Network;

/**
 * IProtocol is the interface for the Qwirkle Protocol implementation. This interface
 * contains definitions for all commands used in the protocol. An implementation has
 * to be made by the groups themselves.
 *
 *
 *
 * @author Erik Gaal
 * @version 0.1
 * @since 0.1-w01 */
public interface IProtocol {

    /**
     * <p>Enumeration of the features supported by the protocol.</p>
     */
    enum Feature {
        CHAT, CHALLENGE, LEADERBOARD, LOBBY
    }

    /**
     * <p>Enumeration of the error codes.</p>
     */
    enum Error {
        COMMAND_NOT_FOUND, INVALID_PARAMETER,
        NAME_INVALID, NAME_USED,
        QUEUE_INVALID,
        MOVE_TILES_UNOWNED, MOVE_INVALID,
        DECK_EMPTY, TRADE_FIRST_TURN,
        INVALID_CHANNEL,
        CHALLENGE_SELF, NOT_CHALLENGED
    }

    /**
     * <p>Sent by the client when connecting to a server to identify itself.</p>
     * <p>The player name must match regex <code>^[a-zA-Z0-9-_]$</code> <code>{@link IProtocol.Error#NAME_INVALID }</code></p>
     * <p>The player name must be unique. <code>{@link IProtocol.Error#NAME_USED }</code></p>
     *
     * <dl>
     *     <dt>Parameters:</dt>
     *     <dd><code>name</code> - name of the player</dd>
     *     <dd><code>features</code> - list of features supported by the client <em>(see {@link IProtocol.Feature})</em></dd>
     * </dl>
     * <dl>
     *     <dt>Example:</dt>
     *     <dd><code><strong>CONNECT</strong> PlayerA CHAT,LEADERBOARD</code></dd>
     * </dl>
     */
    String CLIENT_IDENTIFY = "IDENTIFY";

    /**
     * <p>Sent by the client when gracefully quitting a game.</p>
     *
     * <dl>
     *     <dt>Parameters:</dt>
     * </dl>
     * <dl>
     *     <dt>Example:</dt>
     *     <dd><code><strong>QUIT</strong></code></dd>
     * </dl>
     */
    String CLIENT_QUIT = "QUIT";

    /**
     * <p>Sent by the client to enter a queue for a n-player game.</p>
     * <p>The player can queue for 2, 3 or 4 player games. <code>{@link IProtocol.Error#QUEUE_INVALID }</code></p>
     *
     * <dl>
     *     <dt>Parameters:</dt>
     *     <dd><code>numbers</code> - a list of numbers of players</dd>
     * </dl>
     * <dl>
     *     <dt>Example:</dt>
     *     <dd><code><strong>QUEUE</strong> 2,4</code></dd>
     * </dl>
     */
    String CLIENT_QUEUE = "QUEUE";

    /**
     * <p>Sent by the client to put tiles on the board as a move.</p>
     * <p>The player must own the tiles.<code>{@link IProtocol.Error#MOVE_TILES_UNOWNED }</code></p>
     * <p>The move must be valid. <code>{@link IProtocol.Error#MOVE_INVALID }</code></p>
     *
     * <dl>
     *     <dt>Parameters:</dt>
     *     <dd><code>tile@x,y</code> - list of tilecode at coordinate</dd>
     * </dl>
     * <dl>
     *     <dt>Example:</dt>
     *     <dd><code><strong>MOVE_PUT</strong> 0@0,0 6@0,1 12@0,2</code></dd>
     * </dl>
     */
    String CLIENT_MOVE_PUT = "MOVE_PUT";

    /**
     * <p>Sent by the client to trade tiles as a move.</p>
     * <p>The player must own the tiles. <code>{@link IProtocol.Error#MOVE_TILES_UNOWNED }</code></p>
     * <p>The deck contain at least as many tiles as traded. <code>{@link IProtocol.Error#DECK_EMPTY }</code></p>
     * <p>The player cannot trade if the board is empty. <code>{@link IProtocol.Error#TRADE_FIRST_TURN }</code></p>
     *
     * <dl>
     *     <dt>Parameters:</dt>
     *     <dd><code>tiles</code> - list of tiles</dd>
     * </dl>
     * <dl>
     *     <dt>Example:</dt>
     *     <dd><code><strong>MOVE_TRADE</strong> 23 18 7</code></dd>
     * </dl>
     */
    String CLIENT_MOVE_TRADE = "MOVE_TRADE";

    /**
     * <p>Sent by the server to confirm a player connecting.</p>
     *
     * <dl>
     *     <dt>Parameters:</dt>
     *     <dd><code>features</code> - list of features</dd>
     * </dl>
     * <dl>
     *     <dt>Example:</dt>
     *     <dd><code><strong>CONNECTOK</strong> CHAT,LOBBY</code></dd>
     * </dl>
     */
    String SERVER_IDENITFY = "IDENTIFYOK";

    /**
     * <p>Sent by the server to announce a game starting.</p>
     *
     * <dl>
     *     <dt>Parameters:</dt>
     *     <dd><code>players</code> - list of players in the game</dd>
     * </dl>
     * <dl>
     *     <dt>Example:</dt>
     *     <dd><code><strong>GAMESTART</strong> Alice Bob Carol</code></dd>
     * </dl>
     */
    String SERVER_GAMESTART = "GAMESTART";

    /**
     * <p>Sent by the server to announce a game ending.</p>
     *
     * <dl>
     *     <dt>Parameters:</dt>
     *     <dd><code>result</code> - WIN or ERROR</dd>
     *     <dd><code>player</code> - list of player with their score</dd>
     * </dl>
     * <dl>
     *     <dt>Example:</dt>
     *     <dd><code><strong>GAMEEND</strong> WIN 11,Alice 13,Bob 17,Carol</code></dd>
     * </dl>
     */
    String SERVER_GAMEEND = "GAMEEND";

    /**
     * <p>Sent by the server to announce the current turn.</p>
     *
     * <dl>
     *     <dt>Parameters:</dt>
     *     <dd><code>player</code> - the player</dd>
     * </dl>
     * <dl>
     *     <dt>Example:</dt>
     *     <dd><code><strong>TURN</strong> Alice</code></dd>
     * </dl>
     */
    String SERVER_TURN = "TURN";

    /**
     * <p>Sent by the server to draw a player a new tile.</p>
     * <p>The server must send this to one player.</p>
     *
     * <dl>
     *     <dt>Parameters:</dt>
     *     <dd><code>tiles</code> - list of tiles</dd>
     * </dl>
     * <dl>
     *     <dt>Example:</dt>
     *     <dd><code><strong>DRAWTILE</strong> 23 7 19</code></dd>
     * </dl>
     */
    String SERVER_DRAWTILE = "DRAWTILE";

    /**
     * <p>Sent by the server to confirm putting tiles as a move.</p>
     *
     * <dl>
     *     <dt>Parameters:</dt>
     *     <dd><code>tile@x,y</code> - list of tile at coordinate</dd>
     * </dl>
     * <dl>
     *     <dt>Example:</dt>
     *     <dd><code><strong>MOVEOK_PUT</strong> 0@0,0 6@0,1 12@0,2</code></dd>
     * </dl>
     */
    String SERVER_MOVE_PUT = "MOVEOK_PUT";

    /**
     * <p>Sent by the server to confirm trading tiles as a move.</p>
     *
     * <dl>
     *     <dt>Parameters:</dt>
     *     <dd><code>amount</code> - amount of tiles traded</dd>
     * </dl>
     * <dl>
     *     <dt>Example:</dt>
     *     <dd><code><strong>MOVEOK_TRADE</strong> 3</code></dd>
     * </dl>
     */
    String SERVER_MOVE_TRADE = "MOVEOK_TRADE";

    /**
     * <p>Sent by the server to indicate an error.</p>
     *
     * <dl>
     *     <dt>Parameters:</dt>
     *     <dd><code>code</code> - the error</dd>
     *     <dd><code>[message]</code> - a human readable message</dd>
     * </dl>
     * <dl>
     *     <dt>Example:</dt>
     *     <dd><code><strong>ERROR</strong> 1 Invalid move</code></dd>
     * </dl>
     */
    String SERVER_ERROR = "ERROR";

    /**
     * <p>Sent by the client when chatting.</p>
     * <p>The channel must be <code>global</code> or <code>@playername</code>. <code>{@link IProtocol.Error#INVALID_CHANNEL }</code></p>
     *
     * <dl>
     *     <dt>Parameters:</dt>
     *     <dd><code>channel</code> - the channel</dd>
     *     <dd><code>message</code> - the message</dd>
     * </dl>
     * <dl>
     *     <dt>Example:</dt>
     *     <dd><code><strong>CHAT</strong> global Hello world!</code></dd>
     *     <dd><code><strong>CHAT</strong> @Bob Hello Bob!</code></dd>
     * </dl>
     */
    String CLIENT_CHAT = "CHAT";

    /**
     * <p>Sent by the server to confirm a chat message.</p>
     *
     * <dl>
     *     <dt>Parameters:</dt>
     *     <dd><code>channel</code> - the channel</dd>
     *     <dd><code>sender</code> - the sender</dd>
     *     <dd><code>message</code> - the message</dd>
     * </dl>
     * <dl>
     *     <dt>Example:</dt>
     *     <dd><code><strong>CHATOK</strong> global Alice Hello world!</code></dd>
     *     <dd><code><strong>CHATOK</strong> @Bob Alice Hello Bob!</code></dd>
     * </dl>
     */
    String SERVER_CHAT = "CHATOK";

    /* Challenge */
    /**
     * <p>Sent by the client to challenge another player.</p>
     * <p>The player cannot challenge itself. <code>{@link IProtocol.Error#CHALLENGE_SELF }</code></p>
     *
     * <dl>
     *     <dt>Parameters:</dt>
     *     <dd><code>player</code> - the player</dd>
     * </dl>
     * <dl>
     *     <dt>Example:</dt>
     *     <dd><code><strong>CHALLENGE</strong> Bob</code></dd>
     * </dl>
     */
    String CLIENT_CHALLENGE = "CHALLENGE";

    /**
     * <p>Sent by the client to accept a challenge.</p>
     * <p>The player must be challenged by the other player beforehand. <code>{@link IProtocol.Error#NOT_CHALLENGED }</code></p>
     *
     * <dl>
     *     <dt>Parameters:</dt>
     *     <dd><code>player</code> - the player</dd>
     * </dl>
     * <dl>
     *     <dt>Example:</dt>
     *     <dd><code><strong>CHALLENGE_ACCEPT</strong> Alice</code></dd>
     * </dl>
     */
    String CLIENT_CHALLENGE_ACCEPT = "CHALLENGE_ACCEPT";

    /**
     * <p>Sent by the client to decline a challenge.</p>
     * <p>The player must be challenged by the other player beforehand. <code>{@link IProtocol.Error#NOT_CHALLENGED }</code></p>
     *
     * <dl>
     *     <dt>Parameters:</dt>
     *     <dd><code>player</code> - the player</dd>
     * </dl>
     * <dl>
     *     <dt>Example:</dt>
     *     <dd><code><strong>CHALLENGE_DECLINE</strong> Alice</code></dd>
     * </dl>
     */
    String CLIENT_CHALLENGE_DECLINE = "CHALLENGE_DECLINE";

    /* Leaderboard */
    /**
     * <p>Sent by the client to request the leaderboard.</p>
     *
     * <dl>
     *     <dt>Parameters:</dt>
     * </dl>
     * <dl>
     *     <dt>Example:</dt>
     *     <dd><code><strong>LEADERBOARD</strong></code></dd>
     * </dl>
     */
    String CLIENT_LEADERBOARD = "LEADERBOARD";

    /**
     * <p>Sent by the server to transfer the leaderboard.</p>
     * <p>How the leaderboard is stored may be decided by the groups themselves. One way is to store the amount of wins and losses.</p>
     *
     * <dl>
     *     <dt>Parameters:</dt>
     *     <dd><code>player,wins,losses</code> - list of players with their scores</dd>
     * </dl>
     * <dl>
     *     <dt>Example:</dt>
     *     <dd><code><strong>LEADERBOARDOK</strong> Alice,2,1 Bob,0,3 Carol,1,2</code></dd>
     * </dl>
     */
    String SERVER_LEADERBOARD = "LEADERBOARDOK";

    /**
     * <p>Sent by the client to request the lobby.</p>
     *
     * <dl>
     *     <dt>Parameters:</dt>
     * </dl>
     * <dl>
     *     <dt>Example:</dt>
     *     <dd><code><strong>LOBBY</strong></code></dd>
     * </dl>
     */
    String CLIENT_LOBBY = "LOBBY";

    /**
     * <p>Sent by the server to transfer the lobby.</p>
     *
     * <dl>
     *     <dt>Parameters:</dt>
     *     <dd><code>players</code> - list of players</dd>
     * </dl>
     * <dl>
     *     <dt>Example:</dt>
     *     <dd><code><strong>LOBBYOK</strong> Alice Bob Claire Dave</code></dd>
     * </dl>
     */
    String SERVER_LOBBY = "LOBBYOK";
}