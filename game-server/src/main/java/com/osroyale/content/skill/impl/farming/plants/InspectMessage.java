package com.osroyale.content.skill.impl.farming.plants;

/**
 * Inspection messages for all plants that grow in farming patches.
 *
 * @author Michael | Chex
 */
public enum InspectMessage {
    POTATOES(new String[][] {
        { "The potato seeds have only just been planted." },
        { "The potato plants have grown to double their", "previous height." },
        { "The potato plants now are the same height as the", "surrounding weeds." },
        { "The potato plants now spread their branches wider,", "not growing as much as before." },
        { "The potato plants are ready to harvest. A white", "flower at the top of each plant opens up." }
    }),
    ONIONS(new String[][] {
        { "The onion seeds have only just been planted." },
        { "The onions are partially visible and the stems", "have grown." },
        { "The top of the onion of the onion plant is clear", "above the ground and the onion is white." },
        { "The onion plant is slightly larger than before and", "the onion is cream coloured." },
        { "The onion stalks are larger than before and the", "onion is now light and brown coloured." }
    }),
    CABBAGES(new String[][] {
        { "The cabbage seeds have only just been planted,", "the cabbages are small and bright green." },
        { "The cabbages are much larger, with more leaves", "surrounding the head." },
        { "The cabbages are larger than before, and textures", "of leaves are now easily observable." },
        { "The cabbage head has swollen larger, and the", "surrounding leaves are more close to the ground." },
        { "The cabbage plants are ready to harvest. The", "centre of each cabbage head is light green coloured." }
    }),
    TOMATOES(new String[][] {
        { "The tomato seeds have only just been planted." },
        { "The tomato plants grow twice as large as before." },
        { "The tomato plants grow larger, and small green", "tomatoes are now observable." },
        { "The tomato plants grow thicker to hold up the", "weight of the tomatoes. The tomatoes are now light", "orange and slightly larger on the plant." },
        { "The tomato plants are ready to harvest. The tomato", "plants leaves are larger and the tomatoes are", "ripe red." }
    }),
    SWEETCORNS(new String[][] {
        { "The sweetcorn plants have only just been planted." },
        { "The sweetcorn plants are waist tall now and are", "leafy." },
        { "The sweetcorn plants are slightly taller than", "before and slightly thicker." },
        { "The sweetcorn leaves are larger at the base, and", "the plants are slightly taller." },
        { "Closed corn cobs are now observable on the", "sweetcorn plants." },
        { "The sweetcorn plants are ready to harvest. The", "corn cobs are open and visibly yellow." }
    }),
    STRAWBERRIES(new String[][] {
        { "The strawberry seeds have only just been planted." },
        { "The strawberry plants have more leaves than before." },
        { "The strawberry plants have even more leaves and is", "slightly taller than before." },
        { "Each strawberry plant has opened one white", "flower each." },
        { "The strawberry plants are slightly larger, and", "have small strawberries visible at their bases." },
        { "The strawberry plants are slightly larger, opened", "a second flower each, and have more strawberries." },
        { "The strawberry plants are ready to harvest. The", "strawberries are almost as large as the flowers." }
    }),
    WATERMELONS(new String[][] {
        { "The watermelon seeds have only just been planted." },
        { "The watermelon plants have more leaves than before." },
        { "The watermelon plants have even more leaves and is", "slightly taller than before." },
        { "Each watermelon plant has opened one white", "flower each." },
        { "The watermelon plants are slightly larger, and", "have small watermelons visible at their bases." },
        { "The watermelon plants are slightly larger, opened", "a second flower each, and have more watermelons." },
        { "The watermelon plants are ready to harvest. The", "watermelons are almost as large as the flowers." }
    }),
    MARIGOLD(new String[][] {
        { "The seeds have only just been planted." },
        { "The marigold plants have developed leaves." },
        { "The marigold plants have begun to grow their", "flowers. The new flowers are orange and small at", "first." },
        { "The marigold plants are larger, and more", "developed in their petals." },
        { "The marigold plants are ready to harvest. Their", "flowers are fully matured." }
    }),
    ROSEMARY(new String[][] {
        { "The seeds have only just been planted." },
        { "The rosemary plant is taller than before." },
        { "The rosemary plant is bushier and taller than", "before." },
        { "The rosemary plant is developing a flower bud at", "its top." },
        { "The plant is ready to harvest. The rosemary", "plant's flower has opened." }
    }),
    NASTURTIUM(new String[][] {
        { "The nasturtium seed has only just been planted." },
        { "The nasturtium plants have started to develop", "leaves." },
        { "The nasturtium plants have grown more leaves,", "and nine flower buds." },
        { "The nasturtium plants open their flower buds." },
        { "The plants are ready to harvest. The nasturtium", "plants grow larger than before and the flowers", "fully open." }
    }),
    WOAD(new String[][] {
        { "The woad seed has only just been planted." },
        { "The woad plant produces more stalks, that split", "in tow near the top." },
        { "The woad plant grows more segments from its", "intitial stalks." },
        { "The woad plant develops flower buds on the end", "of each of its stalks." },
        { "The woad plant is ready to harvest. The plant has", "all of its stalks pointing directly up, with", "all flowers open." }
    }),
    LIMPWURT(new String[][] {
        { "The seed has only just been planted." },
        { "The limpwurt plant produces more roots." },
        { "The limpwurt plant produces an unopened pink", "flower bud and continues to grow larger." },
        { "The limpwurt plant grows larger, with more loops", "in its roots. The flower bud is still unopened." },
        { "The limpwurt plant is ready to harvest. The", "flower finally opens wide, with a spike in the", "middle." }
    });

    /** The inspection messages. */
    private final String[][] messages;

    /** Constructs a new inspection message for a plant. */
    InspectMessage(String[][] messages) {
        this.messages = messages;
    }

    /** @return the inspection messages. */
    public String[][] getMessages() {
        return messages;
    }

}
