package com.ylinor.library.api.protocol;

public enum ConnectionState {

    /**
     * When the client just connected to the server. It is waiting for the login
     * confirm (auth token, valid protocol version, ...).
     */
    HANDSHAKE,

    /**
     * When the client logged in, but is not yet in game: the player selects (or
     * manages) a character.
     */
    SELECTING_CHARACTER,
    
    /**
     * The user selected a character and clicked 'Play'.
     */
    IN_GAME,

    /**
     * When the client was kicked/disconnected but the connection is not yet closed.
     */
    SHOULD_DISCONNECT
}
