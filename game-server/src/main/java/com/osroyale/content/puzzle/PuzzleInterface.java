package com.osroyale.content.puzzle;

public interface PuzzleInterface<T> {
    void onSuccess(final T player);

    void onFailure(final T player);
}
