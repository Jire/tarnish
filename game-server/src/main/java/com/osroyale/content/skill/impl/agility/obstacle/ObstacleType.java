package com.osroyale.content.skill.impl.agility.obstacle;

import com.osroyale.content.skill.impl.agility.obstacle.impl.*;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.position.Position;

public enum ObstacleType {
    WILDERNESS_COURSE(new WildernessDoorInteraction() {
        @Override
        public int getAnimation() {
            return 0;
        }
        @Override
        public String getPreMessage() {
            return null;
        }
        @Override
        public String getPostMessage() {
            return null;
        }
    }),
    PLANK(new WalkInteraction() {
        @Override
        public int getAnimation() {
            return 762;
        }
        @Override
        public String getPreMessage() {
            return null;
        }
        @Override
        public String getPostMessage() {
            return null;
        }
    }),
    TIGHT_ROPE(new WalkInteraction() {
        @Override
        public int getAnimation() {
            return 762;
        }
        @Override
        public String getPreMessage() {
            return "You carefully cross the tightrope...";
        }
        @Override
        public String getPostMessage() {
            return null;
        }
    }),
    LEDGE(new WalkInteraction() {
        @Override
        public int getAnimation() {
            return 756;
        }
        @Override
        public String getPreMessage() {
            return "You put your foot on the ledge and try to edge across...";
        }
        @Override
        public String getPostMessage() {
            return "You skillfully ege across the gap.";
        }
    }),
    STEEP_LEDGE(new WalkInteraction() {
        @Override
        public int getAnimation() {
            return 756;
        }
        @Override
        public String getPreMessage() {
            return null;
        }
        @Override
        public String getPostMessage() {
            return null;
        }
    }),
    PIPE(new WalkInteraction() {
        @Override
        public int getAnimation() {
            return 844;
        }
        @Override
        public String getPreMessage() {
            return "You squeeze into the pipe...";
        }
        @Override
        public String getPostMessage() {
            return null;
        }
    }),
    NETTING(new ClimbInteraction() {
        @Override
        public int getAnimation() {
            return 3063;
        }
        @Override
        public String getPreMessage() {
            return "You climb the netting...";
        }
        @Override
        public String getPostMessage() {
            return null;
        }
    }),
    CLIMB_UP(new ClimbInteraction() {
        @Override
        public int getAnimation() {
            return 828;
        }
        @Override
        public String getPreMessage() {
            return null;
        }
        @Override
        public String getPostMessage() {
            return "You climb up safely.";
        }
    }),
    CLIMB_DOWN(new ClimbInteraction() {
        @Override
        public int getAnimation() {
            return 828;
        }
        @Override
        public String getPreMessage() {
            return null;
        }
        @Override
        public String getPostMessage() {
            return "You climb down safely.";
        }
    }),
    TREE_BRANCH_UP(new ClimbInteraction() {
        @Override
        public int getAnimation() {
            return 828;
        }
        @Override
        public String getPreMessage() {
            return "You climb the tree...";
        }
        @Override
        public String getPostMessage() {
            return "...To the platform above.";
        }
    }),
    TREE_BRANCH_DOWN(new ClimbInteraction() {
        @Override
        public int getAnimation() {
            return 828;
        }
        @Override
        public String getPreMessage() {
            return "You climb the tree...";
        }
        @Override
        public String getPostMessage() {
            return "You land on the ground.";
        }
    }),
    LADDER(new ClimbInteraction() {
        @Override
        public int getAnimation() {
            return 828;
        }
        @Override
        public String getPreMessage() {
            return "You climb down the ladder.";
        }
        @Override
        public String getPostMessage() {
            return null;
        }
    }),
    LOW_WALL(new ClimbOverInteraction() {
        @Override
        public int getAnimation() {
            return 839;
        }
        @Override
        public String getPreMessage() {
            return "You climb the low wall...";
        }
        @Override
        public String getPostMessage() {
            return null;
        }
    }),
    ROCKS(new WalkInteraction() {
        @Override
        public int getAnimation() {
            return 839;
        }
        @Override
        public String getPreMessage() {
            return "You climb over the rocks...";
        }
        @Override
        public String getPostMessage() {
            return null;
        }
    }),
    ROPE_SWING(new RopeSwingInteraction() {
        @Override
        public int getAnimation() {
            return 751;
        }
        @Override
        public String getPreMessage() {
            return "You skillfully swing across.";
        }
        @Override
        public String getPostMessage() {
            return null;
        }
    }),
    STEPPING_STONES(new SteppingStonesInteraction() {
        @Override
        public int getAnimation() {
            return 741;
        }
        @Override
        public String getPreMessage() {
            return "You jump across the stepping stones...";
        }
        @Override
        public String getPostMessage() {
            return "...You make it safely to the other side.";
        }
    }),
    JUMP_OVER(new WalkInteraction() {
        @Override
        public int getAnimation() {
            return 3067;
        }
        @Override
        public String getPreMessage() {
            return null;
        }
        @Override
        public String getPostMessage() {
            return null;
        }
    }),
    SEERS_CLIMB(new SeersClimbInteraction() {
        @Override
        public int getAnimation() {
            return 737;
        }
        @Override
        public String getPreMessage() {
            return "You climb up the wall...";
        }
        @Override
        public String getPostMessage() {
            return null;
        }
    }),
    ROOFTOP_CLIMB_1(new ClimbInteraction() {
        @Override
        public int getAnimation() {
            return 737;
        }
        @Override
        public String getPreMessage() {
            return "You climb up the wall...";
        }
        @Override
        public String getPostMessage() {
            return null;
        }
    }),
    ROOFTOP_CLIMB_2(new ClimbInteraction() {
        @Override
        public int getAnimation() {
            return 1118;
        }
        @Override
        public String getPreMessage() {
            return null;
        }
        @Override
        public String getPostMessage() {
            return null;
        }
    }),
    JUMP_SEERS_GAP(new SeersJumpGapInteraction() {
        @Override
        public int getAnimation() {
            return 0;
        }
        @Override
        public String getPreMessage() {
            return null;
        }
        @Override
        public String getPostMessage() {
            return null;
        }
    }),
    JUMP_ARDOUGNE_ROOF(new ArdougneRoofJumpInteraction() {
        @Override
        public int getAnimation() {
            return 0;
        }
        @Override
        public String getPreMessage() {
            return null;
        }
        @Override
        public String getPostMessage() {
            return null;
        }
    }),
    JUMP_ARDOUGNE_GAP(new ArdougneJumpGapInteraction() {
        @Override
        public int getAnimation() {
            return 0;
        }
        @Override
        public String getPreMessage() {
            return null;
        }
        @Override
        public String getPostMessage() {
            return null;
        }
    }),
    JUMP_SEERS_GAP_2(new SeersJumpGapInteraction2() {
        @Override
        public int getAnimation() {
            return 0;
        }
        @Override
        public String getPreMessage() {
            return null;
        }
        @Override
        public String getPostMessage() {
            return null;
        }
    }),
    JUMP_ARDOUGNE_ROOF_2(new ArdougneRoofJumpInteraction2() {
        @Override
        public int getAnimation() {
            return 0;
        }
        @Override
        public String getPreMessage() {
            return null;
        }
        @Override
        public String getPostMessage() {
            return null;
        }
    }),
    ARDOUGNE_WALL_CLIMB(new ArdougneWallClimbInteraction() {
        @Override
        public int getAnimation() {
            return 0;
        }
        @Override
        public String getPreMessage() {
            return "You climb up the wall...";
        }
        @Override
        public String getPostMessage() {
            return "...jump, and grab hold of the sign!";
        }
    }),
    RUNNING_START(new RunningStartInteraction() {
        @Override
        public int getAnimation() {
            return 1995;
        }
        @Override
        public String getPreMessage() {
            return "You being an almighty run-up...";
        }
        @Override
        public String getPostMessage() {
            return null;
        }
    }),
    SWING_ACROSS(new SwingAcrossCableInteraction() {
        @Override
        public int getAnimation() {
            return 751;
        }
        @Override
        public String getPreMessage() {
            return null;
        }
        @Override
        public String getPostMessage() {
            return "You gained enough momentum to swing to the other side!";
        }
    });

    private final ObstacleInteraction interaction;

    ObstacleType(ObstacleInteraction interaction) {
        this.interaction = interaction;
    }

    public ObstacleInteraction getInteraction() {
        return interaction;
    }

    public void execute(Player player, Obstacle next, Position start, Position end, int level, float experience, int ordinal) {
        interaction.execute(player, next, start, end, level, experience, ordinal);
    }
}
