package com.osroyale.content.puzzle;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.out.SendInterfaceWidget;
import com.osroyale.util.Utility;

/**
 * Handles displaying the puzzle.
 *
 * @author Daniel
 */
public class PuzzleDisplay {
    /** The player instance. */
    private final Player player;

    /** The puzzle data. */
    private PuzzleData puzzle;

    /** The options array. */
    private int[] options;

    /** The puzzle type. */
    private PuzzleType type;

    /** The success count. */
    int successCount;

    /** Constructs a new <code>PuzzleDisplay</code>. */
    public PuzzleDisplay(Player player) {
        this.player = player;
    }

    /** Randomize the puzzle. */
    private void randomize() {
        this.puzzle = PuzzleData.PUZZLES[(int) (Math.random() * PuzzleData.PUZZLES.length)];
        this.options = Utility.shuffleArray(puzzle.getOptions());
    }

    /** Opens the puzzle interface. */
    public void open(PuzzleType puzzleType) {
        randomize();

        for (int index = 0; index < 3; index++) {
            int sequenceModel = puzzle.getSequenceModel(index);
            int optionModel = options[index];
            player.send(new SendInterfaceWidget(4545 + index, sequenceModel));
            player.send(new SendInterfaceWidget(4550 + index, optionModel));
        }

        type = puzzleType;
        player.interfaceManager.open(4543);
    }

    /** Checks the puzzle answer. */
    private boolean checkAnswer(int button) {
        int index = button - 4550;
        int model = options[index];
        return model == puzzle.getAnswer();
    }

    /** Handles clicking on the puzzle interface. */
    public boolean click(int button) {
        switch (button) {
            case 4550:
            case 4551:
            case 4552:
                if (checkAnswer(button)) {
                    successCount++;
                    type.onSuccess(player);
                    return true;
                }
                successCount = 0;
                type.onFailure(player);
                return true;
        }
        return false;
    }
}
