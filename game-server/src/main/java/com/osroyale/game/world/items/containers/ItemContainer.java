package com.osroyale.game.world.items.containers;

import com.google.common.base.Preconditions;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.ItemDefinition;
import com.osroyale.game.world.items.containers.pricechecker.PriceType;
import com.osroyale.net.packet.out.SendItemOnInterface;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;


/**
 * An abstraction game representing a group of {@link Item}s.
 *
 * @author lare96 <http://github.com/lare96>
 */
public class ItemContainer implements Iterable<Item> {

    // TODO: Unit tests for various functions

    /** An {@link Iterator} implementation for this container. */
    private static final class ItemContainerIterator implements Iterator<Item> {

        /** The container instance to iterate over. */
        private final ItemContainer container;

        /** The current index being iterated over. */
        private int index;

        /** The last index that was iterated over. */
        private int lastIndex = -1;

        /** Creates a new {@link ItemContainerIterator}. */
        ItemContainerIterator(ItemContainer container) {
            this.container = container;
        }

        @Override
        public boolean hasNext() {
            return (index + 1) <= container.capacity;
        }

        @Override
        public Item next() {
            checkState(index < container.capacity, "no more elements left to iterate");

            lastIndex = index;
            index++;
            return container.items[lastIndex];
        }

        @Override
        public void remove() {
            checkState(lastIndex != -1, "can only be called once after 'next'");

            Item oldItem = container.items[lastIndex];
            container.items[lastIndex] = null;
            container.fireItemUpdatedEvent(oldItem, null, lastIndex, true);

            index = lastIndex;
            lastIndex = -1;
        }
    }

    /** An enumerated type defining policies for stackable {@link Item}s. */
    public enum StackPolicy {

        /**
         * The {@code STANDARD} policy, items are only stacked if they are
         * defined as stackable in their {@link ItemDefinition} table.
         */
        STANDARD,

        /**
         * The {@code ALWAYS} policy, items are always stacked regardless of
         * their {@link ItemDefinition} table.
         */
        ALWAYS,

        /**
         * The {@code NEVER} policy, items are never stacked regardless of their
         * {@link ItemDefinition} table.
         */
        NEVER
    }

    /**
     * An {@link ArrayList} of {@link ItemContainerListener}s listening for
     * various events.
     */
    private final List<ItemContainerListener> listeners = new ArrayList<>();

    /** The capacity of this container. */
    private final int capacity;

    /** The policy of this container. */
    private final StackPolicy policy;

    /** The {@link Item}s within this container. */
    private Item[] items;

    /** If events are currently being fired. */
    private boolean firingEvents = true;

    /** Creates a new {@link ItemContainer}. */
    public ItemContainer(int capacity, StackPolicy policy, Item[] items) {
        this.capacity = capacity;
        this.policy = policy;
        this.items = items;
    }

    /** Creates a new {@link ItemContainer}. */
    public ItemContainer(int capacity, StackPolicy policy) {
        this(capacity, policy, new Item[capacity]);
    }

    /**
     * Iterates through all of the {@link Item}s within this container and
     * performs {@code action} on them, skipping empty indexes ({@code null}
     * values) as they are encountered.
     */
    @Override
    public final void forEach(Consumer<? super Item> action) {
        Objects.requireNonNull(action);
        for (Item item : items) {
            if (item != null) {
                action.accept(item);
            }
        }
    }

    @Override
    public final Spliterator<Item> spliterator() {
        return Spliterators.spliterator(items, Spliterator.ORDERED);
    }

    @Override
    public final Iterator<Item> iterator() {
        return new ItemContainerIterator(this);
    }

    /**
     * @return A stream associated with the elements in this container, built
     * using the {@code spliterator()} implementation.
     */
    public final Stream<Item> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    /**
     * Attempts to deposit {@code item} into this container.
     *
     * @param item The {@link Item} to deposit.
     * @return {@code true} the {@code Item} was added, {@code false} if there
     * was not enough space left.
     */
    public boolean add(Item item) {
        return add(item, -1, true);
    }

    /**
     * Attempts to deposit {@code item} into this container.
     *
     * @param item The {@link Item} to deposit.
     * @param slot The slot to deposit the item too.
     * @return {@code true} the {@code Item} was added, {@code false} if there
     * was not enough space left.
     */
    public boolean add(Item item, int slot) {
        return add(item, slot, true);
    }

    /**
     * Attempts to deposit {@code item} into this container.
     *
     * @param id     the id of the item.
     * @param amount the amount of the item.
     * @return {@code true} the item was added, {@code false} if there was not
     * enough space left.
     */
    public boolean add(int id, int amount) {
        return add(new Item(id, amount));
    }

    public boolean add(Item item, boolean refresh, boolean stack) {
        return add(item, -1, refresh, stack);
    }

    public boolean add(Item item, int preferredIndex, boolean refresh) {
        return add(item, preferredIndex, refresh, false);
    }

    /**
     * Attempts to deposit {@code item} into this container, preferably at
     * {@code preferredIndex}.
     *
     * @param item           The {@link Item} to deposit.
     * @param preferredIndex The preferable index to deposit {@code item} to.
     * @param refresh        The condition if we will be refreshing our
     *                       container.
     * @return {@code true} if the {@code Item} was added, {@code false} if
     * there was not enough space left.
     */
    public boolean add(Item item, int preferredIndex, boolean refresh, boolean stack) {
        checkArgument(preferredIndex >= -1, "invalid index identifier");

        item = item.copy();

        boolean stackable = stack || (policy.equals(StackPolicy.STANDARD) && item.isStackable()) || policy.equals(StackPolicy.ALWAYS);

        if (stackable && !stack) {
            preferredIndex = computeIndexForId(item.getId());
        } else if (preferredIndex != -1) {
            preferredIndex = items[preferredIndex] != null ? -1 : preferredIndex;
        }

        preferredIndex = preferredIndex == -1 ? computeFreeIndex() : preferredIndex;

        if (preferredIndex == -1) { // Not enough space in container.
            fireCapacityExceededEvent();
            return false;
        }

        if (stackable) {
            Item current = items[preferredIndex];
            items[preferredIndex] = (current == null) ? item : current.createAndIncrement(item.getAmount());
            fireItemUpdatedEvent(current, items[preferredIndex], preferredIndex, refresh);
        } else {
            int remaining = remaining();
            int until = (remaining > item.getAmount()) ? item.getAmount() : remaining;

            for (int index = 0; index < until; index++) {
                preferredIndex = (preferredIndex > capacity || preferredIndex < 0 || items[preferredIndex] == null) ? preferredIndex : computeFreeIndex();
                if (preferredIndex == -1) {//Couldn't find an empty spot.
                    fireCapacityExceededEvent();
                    return false;
                }
                item.setAmount(1);
                items[preferredIndex] = item;

                fireItemUpdatedEvent(null, item, preferredIndex++, refresh);
            }
        }
        return true;
    }

    /**
     * Attempts to deposit {@code items} in bulk into this container.
     *
     * @param items The {@link Item}s to deposit.
     * @return {@code true} if at least {@code 1} of the {@code Item}s were
     * added, {@code false} if none could be added.
     */
    public boolean addAll(Collection<? extends Item> items) {
        if (items.size() == 1) { // Bulk operation on singleton list? No thanks..
            Optional<? extends Item> item = items.stream().
                    filter(Objects::nonNull).
                    findFirst();
            return item.isPresent() && add(item.get());
        }

        firingEvents = false;

        boolean added = false;
        try {
            for (Item item : items) {
                if (item == null) {
                    continue;
                }
                if (add(item, -1, false)) {
                    added = true;
                }
            }
        } finally {
            firingEvents = true;
        }
        fireBulkItemsUpdatedEvent();
        return added;
    }

    /**
     * Attempts to deposit {@code items} in bulk into this container.
     *
     * @param items The {@link Item}s to deposit.
     * @return {@code true} if at least {@code 1} of the {@code Item}s were
     * added, {@code false} if none could be added.
     */
    public boolean addAll(Item... items) {
        return addAll(Arrays.asList(items));
    }

    /**
     * Attempts to deposit {@code items} in bulk into this container.
     *
     * @param items The {@link Item}s to deposit.
     * @return {@code true} if at least {@code 1} of the {@code Item}s were
     * added, {@code false} if none could be added.
     */
    public boolean addAll(ItemContainer items) {
        return addAll(items.items);
    }

    /**
     * Attempts to withdraw {@code item} from this container.
     *
     * @param item The {@link Item} to withdraw.
     * @return {@code true} if the {@code Item} was removed, {@code false} if it
     * isn't present in this container.
     */
    public boolean remove(Item item) {
        return remove(item, -1, true);
    }

    /**
     * Attempts to withdraw {@code item} from this container, preferably from
     * {@code preferredIndex}.
     *
     * @param item           The {@link Item} to withdraw.
     * @param preferredIndex The preferable index to withdraw {@code item}
     *                       from.
     * @return {@code true} if the {@code Item} was removed, {@code false} if it
     * isn't present in this container.
     */
    public boolean remove(Item item, int preferredIndex) {
        return remove(item, preferredIndex, true);
    }

    public boolean remove(int id) {
        return remove(new Item(id, 1));
    }

    public boolean remove(int id, int amount) {
        return remove(new Item(id, amount));
    }

    public boolean remove(Item item, int preferredIndex, boolean refresh) {
        return remove(item, preferredIndex, refresh, true);
    }

    /**
     * Attempts to withdraw {@code item} from this container, preferably from
     * {@code preferredIndex}.
     *
     * @param item           The {@link Item} to withdraw.
     * @param preferredIndex The preferable index to withdraw {@code item}
     *                       from.
     * @param refresh        The condition if we will be refreshing our
     *                       container.
     * @param removeAll      Determines if the items should be removed from all
     *                       slots or only the specified s
     * @return {@code true} if the {@code Item} was removed, {@code false} if it
     * isn't present in this container.
     */
    public boolean remove(Item item, int preferredIndex, boolean refresh, boolean removeAll) {
        checkArgument(preferredIndex >= -1, "invalid index identifier");

        item = item.copy();

        boolean stackable = (policy.equals(StackPolicy.STANDARD) && item.isStackable()) || policy.equals(StackPolicy.ALWAYS);

        if (stackable) {
            //todo fix dupe
            /*
            because stackable is true (due to the coins being stackable). it will compute the first index.
            being the 1 coin, so when you drop. amount is 260. removes 1coin but drops 260k.
             */
            preferredIndex = computeIndexForId(item.getId());
        } else {
            preferredIndex = preferredIndex == -1 ? computeIndexForId(item.getId()) : preferredIndex;

            if (preferredIndex != -1 && items[preferredIndex] == null) {
                preferredIndex = -1;
            }

        }

        if (preferredIndex == -1) { // Item isn't present within this container.
            return false;
        }

        if (stackable) {
            Item current = items[preferredIndex];
            if (current.getAmount() > item.getAmount()) {
                items[preferredIndex] = current.createAndDecrement(item.getAmount());
            } else {
                if (allowZero()) {
                    items[preferredIndex] = current.createWithAmount(0);
                } else {
                    items[preferredIndex] = null;
                }
            }

            fireItemUpdatedEvent(current, items[preferredIndex], preferredIndex, refresh);
        } else {
            int until = removeAll ? computeAmountForId(item.getId()) : items[preferredIndex].getAmount();
            until = (item.getAmount() > until) ? until : item.getAmount();

            for (int index = 0; index < until && index < capacity; index++) {
                if (removeAll) {
                    if (preferredIndex < 0 || preferredIndex >= capacity) {
                        preferredIndex = computeIndexForId(item.getId());
                    } else if (items[preferredIndex] == null) {
                        preferredIndex = computeIndexForId(item.getId());
                    } else if (items[preferredIndex].getId() != item.getId()) {
                        preferredIndex = computeIndexForId(item.getId());
                    }
                    if (preferredIndex == -1) {
                        break;
                    }
                }
                Item oldItem = items[preferredIndex];
                if (allowZero()) {
                    items[preferredIndex] = oldItem.createWithAmount(0);
                } else {
                    items[preferredIndex] = null;
                }
                fireItemUpdatedEvent(oldItem, null, preferredIndex++, refresh);
            }
        }
        return true;
    }

    /**
     * Attempts to withdraw {@code items} in bulk from this container.
     *
     * @param items The {@link Item}s to withdraw.
     * @return {@code true} if at least {@code 1} of the {@code Item}s were
     * withdraw, {@code false} if none could be removed.
     */
    public boolean removeAll(Collection<? extends Item> items) {
        if (items.size() == 1) { // Bulk operation on singleton list? No thanks..
            Optional<? extends Item> item = items.stream().
                    filter(Objects::nonNull).
                    findFirst();
            return item.isPresent() && remove(item.get());
        }

        firingEvents = false;
        boolean removed = false;
        try {
            for (Item item : items) {
                if (item == null) {
                    continue;
                }
                if (remove(item, -1, false)) {
                    removed = true;
                }
            }
        } finally {
            firingEvents = true;
        }
        fireBulkItemsUpdatedEvent();
        return removed;
    }

    /**
     * Attempts to withdraw {@code items} in bulk from this container.
     *
     * @param items The {@link Item}s to withdraw.
     * @return {@code true} if at least {@code 1} of the {@code Item}s were
     * withdraw, {@code false} if none could be removed.
     */
    public boolean removeAll(Item... items) {
        return removeAll(Arrays.asList(items));
    }

    /**
     * Attempts to withdraw {@code items} in bulk from this container.
     *
     * @param items The {@link Item}s to withdraw.
     * @return {@code true} if at least {@code 1} of the {@code Item}s were
     * withdraw, {@code false} if none could be removed.
     */
    public boolean removeAll(ItemContainer items) {
        return removeAll(items.items);
    }

    /**
     * Gets the total worth of the container using the item's values.
     *
     * @return The total container worth.
     */
    public long containerValue(PriceType type) {
        long value = 0;

        for (final Item item : items) {
            if (item == null) {
                continue;
            }

            int price = item.getValue(type);

            if (value >= Long.MAX_VALUE - price * item.getAmount()) {
                return Long.MAX_VALUE;
            }

            value += price * item.getAmount();
        }

        return value;
    }

    /**
     * Computes the next free ({@code null}) index in this container.
     *
     * @return The free index, {@code -1} if no free indexes could be found.
     */
    public final int computeFreeIndex() {
        for (int index = 0; index < capacity; index++) {
            if (items[index] == null) {
                return index;
            }
        }
        return -1;
    }

    /**
     * Computes the first index found that {@code id} is in.
     *
     * @param id The identifier to compute for.
     * @return The first index found, {@code -1} if no {@link Item} with {@code
     * id} is in this container.
     */
    public final int computeIndexForId(int id) {
        for (int index = 0; index < capacity; index++) {
            if (items[index] != null && items[index].getId() == id) {
                return index;
            }
        }
        return -1;
    }

    /**
     * Computes the total quantity of the {@link Item}s in this container with
     * {@code id}.
     *
     * @param id The identifier of the {@code Item} to determine the total
     *           quantity of.
     * @return The total quantity.
     */
    public final int computeAmountForId(int id) {
        int amount = 0;
        for (Item item : items) {
            if (item == null || item.getId() != id)
                continue;
            amount += item.getAmount();
        }
        return amount;
    }

    /**
     * Computes the identifier of the {@link Item} on {@code index}.
     *
     * @param index The index to compute the identifier for.
     * @return The identifier wrapped in an optional.
     */
    public final Optional<Integer> computeIdForIndex(int index) {
        return retrieve(index).map(Item::getId);
    }

    /**
     * Replaces the first occurrence of the {@link Item} having the identifier
     * {@code oldId} with {@code newId}.
     *
     * @param oldId   The old identifier to replace.
     * @param newId   The new identifier to replace.
     * @param refresh The condition if the coontainer will be refreshed.
     * @return {@code true} if the replace operation was successful, {@code
     * false otherwise}.
     */
    public final boolean replace(int oldId, int newId, int index, boolean refresh) {
        Item oldItem = items[index];

        if (oldItem == null || !oldItem.matchesId(oldId))
            return false;

        Item newItem = oldItem.createWithId(newId);
        return remove(oldItem, index, refresh) && add(newItem, index, refresh);
    }

    /**
     * Replaces the first occurrence of the {@link Item} having the identifier
     * {@code oldId} with {@code newId}.
     *
     * @param oldId   The old identifier to replace.
     * @param newId   The new identifier to replace.
     * @param refresh The condition if the coontainer will be refreshed.
     * @return {@code true} if the replace operation was successful, {@code
     * false otherwise}.
     */
    public final boolean replace(int oldId, int newId, boolean refresh) {
        int index = computeIndexForId(oldId);
        if (index == -1) {
            return false;
        }

        Item oldItem = items[index];

        if (oldItem == null || !oldItem.matchesId(oldId))
            return false;

        Item newItem = oldItem.createWithId(newId);
        return remove(oldItem, index, refresh) && add(newItem, index, refresh);
    }

    /**
     * Replaces the first occurrence of the {@link Item} having the identifier
     * {@code oldId} with {@code newId}.
     *
     * @param refresh The condition if the coontainer will be refreshed.
     * @return {@code true} if the replace operation was successful, {@code
     * false otherwise}.
     */
    public final boolean replace(Item first, Item second, boolean refresh) {
        int index = computeIndexForId(first.getId());

        if (index == -1) {
            return false;
        }

        Item oldItem = items[index];

        if (oldItem == null || !oldItem.matchesId(first.getId()))
            return false;

        return remove(oldItem, index, refresh) && add(second, index, refresh);
    }

    /**
     * Replaces all occurrences of {@link Item}s having the identifier {@code
     * oldId} with {@code newId}.
     *
     * @param oldId The old identifier to replace.
     * @param newId The new identifier to replace.
     * @return {@code true} if the replace operation was successful at least
     * once, {@code false otherwise}.
     */
    public final boolean replaceAll(int oldId, int newId) {
        boolean replaced = false;

        firingEvents = false;
        try {
            while (replace(oldId, newId, false)) {
                replaced = true;
            }
        } finally {
            firingEvents = true;
        }
        fireBulkItemsUpdatedEvent();
        return replaced;
    }

    /**
     * Computes the amount of indexes required to hold {@code items} in this
     * container.
     *
     * @param forItems The items to compute the index count for.
     * @return The index count.
     */
    public final int computeIndexCount(Item... forItems) {
        int indexCount = 0;
        for (Item item : forItems) {
            if (item == null)
                continue;
            boolean stackable = (policy.equals(StackPolicy.STANDARD) && item.isStackable()) || policy.equals(StackPolicy.ALWAYS);

            if (stackable) {
                int index = computeIndexForId(item.getId());
                if (index == -1) {
                    indexCount++;
                    continue;
                }

                Item existing = items[index];
                if ((existing.getAmount() + item.getAmount()) <= 0) {
                    indexCount++;
                }
            } else {
                indexCount += item.getAmount();
            }
        }
        return indexCount;
    }

    /**
     * Determines if this container has the capacity for {@code item}.
     *
     * @param item The {@link Item} to determine this for.
     * @return {@code true} if {@code item} can be added, {@code false}
     * otherwise.
     */
    public final boolean hasCapacityFor(Item... item) {
        int indexCount = computeIndexCount(item);
        return remaining() >= indexCount;
    }

    /**
     * Creates a copy of the underlying container and removes the items
     * specified from it and after tries to deposit the specified items to it.
     *
     * @param add    the items to deposit to this container.
     * @param remove the items that should be removed before adding.
     * @return {@code true} if {@code item} can be added, {@code false}
     * otherwise.
     */
    public final boolean hasCapacityAfter(Item[] add, Item... remove) {
        ItemContainer container = new ItemContainer(capacity, policy, toArray());
        container.removeAll(Arrays.copyOf(remove, remove.length));
        return container.hasCapacityFor(add);
    }

    /**
     * Determines if this container contains {@code id}.
     *
     * @param id The identifier to check this container for.
     * @return {@code true} if this container has {@code id}, {@code false}
     * otherwise.
     */
    public boolean contains(int id) {
        for (Item item : items) {
            if (item != null && id == item.getId()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if this container contains all {@code identifiers}.
     *
     * @param identifiers The identifiers to check this container for.
     * @return {@code true} if this container has all {@code identifiers},
     * {@code false} otherwise.
     */
    public final boolean containsAll(int... identifiers) {
        for (int id : identifiers) {
            if (!contains(id)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determines if this container contains any {@code identifiers}.
     *
     * @param identifiers The identifiers to check this container for.
     * @return {@code true} if this container has any {@code identifiers},
     * {@code false} otherwise.
     */
    public final boolean containsAny(int... identifiers) {
        for (int id : identifiers) {
            if (contains(id)) {
                return true;
            }
        }
        return false;
    }

    /** @return {@code true} if this container has the {@code item} */
    public final boolean contains(Item item) {
        return item != null && contains(item.getId(), item.getAmount());
    }

    /** @return {@code true} if this container has id with amount */
    public final boolean contains(int id, int amount) {
        for (Item item : items) {
            if (item != null && id == item.getId()) {
                amount -= item.getAmount();
                if (amount <= 0) return true;
            }
        }
        return false;
    }

    /** @return {@code true} if this container has all {@code items} */
    public final boolean containsAll(Item... items) {
        for (Item item : items) {
            if (!contains(item)) {
                return false;
            }
        }
        return true;
    }

    /** @return {@code true} if this container has all {@code items} */
    public final boolean containsAll(Collection<Item> items) {
        for (Item item : items) {
            if (!contains(item)) {
                return false;
            }
        }
        return true;
    }

    /** @return {@code true} if this container has all {@code items} */
    public final boolean containsAny(Item... items) {
        for (Item item : items) {
            if (contains(item)) {
                return true;
            }
        }
        return false;
    }

    /** @return {@code true} if this container has all {@code items} */
    public final boolean containsAny(Collection<Item> items) {
        for (Item item : items) {
            if (contains(item)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sends the items on the itemcontainer.
     *
     * @param widget The widget to send the {@code Item}s on.
     */
    public void refresh(Player player, int widget) {
        player.send(new SendItemOnInterface(widget, items));
        onRefresh();
    }

    /** Any functionality that should occur when refreshed. */
    public void onRefresh() {
        /* can be overriden */
    }

    /**
     * Swaps the {@link Item}s on {@code oldIndex} and {@code newIndex}.
     *
     * @param oldIndex The old index.
     * @param newIndex The new index.
     */
    public final void swap(int oldIndex, int newIndex) {
        swap(false, oldIndex, newIndex, true);
    }

    /**
     * Swaps the {@link Item}s on {@code oldIndex} and {@code newIndex}.
     *
     * @param insert   If the {@code Item} should be inserted.
     * @param oldIndex The old index.
     * @param newIndex The new index.
     * @param refresh  The condition that determines if we will refresh the
     *                 container.
     */
    public final void swap(boolean insert, int oldIndex, int newIndex, boolean refresh) {
        if (insert) {
            insert(oldIndex, newIndex, refresh);
        } else {
            swap(oldIndex, newIndex, refresh);
        }
    }

    public final void swap(int oldIndex, int newIndex, boolean refresh) {
        checkArgument(oldIndex >= 0 && oldIndex < capacity, "[swap] oldIndex out of range - [old=" + oldIndex + ", new=" + newIndex + ", refresh=" + refresh + ", size=" + size() + ", capacity=" + capacity + "]");
        checkArgument(newIndex >= 0 && newIndex < capacity, "[swap] newIndex out of range - [old=" + oldIndex + ", new=" + newIndex + ", refresh=" + refresh + ", size=" + size() + ", capacity=" + capacity + "]");

        Item itemOld = items[oldIndex];
        Item itemNew = items[newIndex];

        items[oldIndex] = itemNew;
        items[newIndex] = itemOld;

        fireItemUpdatedEvent(itemOld, items[oldIndex], oldIndex, refresh);
        fireItemUpdatedEvent(itemNew, items[newIndex], newIndex, refresh);
    }

    public final void insert(int oldIndex, int newIndex, boolean refresh) {
        checkArgument(oldIndex >= 0 && oldIndex < capacity, "[insert] oldIndex out of range - [old=" + oldIndex + ", new=" + newIndex + ", refresh=" + refresh + ", size=" + size() + ", capacity=" + capacity + "]");
        checkArgument(newIndex >= 0 && newIndex < capacity, "[insert] newIndex out of range - [old=" + oldIndex + ", new=" + newIndex + ", refresh=" + refresh + ", size=" + size() + ", capacity=" + capacity + "]");

        if (newIndex > oldIndex) {
            for (int index = oldIndex; index < newIndex; index++) {
                swap(index, index + 1, refresh);
            }
        } else if (oldIndex > newIndex) {
            for (int index = oldIndex; index > newIndex; index--) {
                swap(index, index - 1, refresh);
            }
        }
    }

    public final void transfer(int firstIndex, int secondIndex, ItemContainer other, boolean refresh) {
        checkArgument(firstIndex >= 0 && firstIndex < capacity, "[transfer] firstIndex out of range - [first=" + firstIndex + ", second=" + secondIndex + ", refresh=" + refresh + "]");
        checkArgument(secondIndex >= 0 && secondIndex < other.capacity, "[transfer] secondIndex out of range - [first=" + firstIndex + ", second=" + secondIndex + ", refresh=" + refresh + "]");
        Item first = get(firstIndex);
        Item second = other.get(secondIndex);
        set(firstIndex, second, true);
        other.set(secondIndex, first, true);
    }

    /**
     * Percolates the null indices to the end of the stack.
     */
    public void shift() {
        Item[] newItems = new Item[capacity];
        int newIndex = 0;

        for (Item item : items) {
            if (item == null) {
                continue;
            }
            newItems[newIndex++] = item;
        }

        setItems(newItems);
    }

    /**
     * Sets the container of items to {@code items}. The container will not hold
     * any references to the array, nor the item instances in the array.
     *
     * @param items the new array of items, the capacities of this must be equal
     *              to or lesser than the container.
     */
    public final void setItems(Item[] items, boolean copy) {
        Preconditions.checkArgument(items.length <= capacity);
        clear();
        for (int i = 0; i < items.length; i++) {
            this.items[i] = items[i] == null ? null : copy ? items[i].copy() : items[i];
        }
        fireBulkItemsUpdatedEvent();
    }

    public final void setItems(Item[] items) {
        setItems(items, true);
    }

    public final void set(Item[] toSet) {
        items = toSet;
    }

    /**
     * Returns a <strong>shallow copy</strong> of the backing array. Changes
     * made to the returned array will not be reflected on the backing array.
     *
     * @return A shallow copy of the backing array.
     */
    public final Item[] toArray() {
        return Arrays.copyOf(items, items.length);
    }

    public final Item[] toNonNullArray() {
        if (size() == 0) {
            return new Item[0];
        }

        final List<Item> items = new ArrayList<>(size());

        for (Item item : getItems()) {
            if (item == null) {
                continue;
            }

            items.add(item);
        }

        return items.toArray(new Item[items.size()]);
    }

    /**
     * Gets the weight of the container.
     *
     * @return The weight of the container.
     */
    public double getWeight() {
        double weight = 0;
        for (Item item : toArray()) {
            if (item == null)
                continue;
            weight += item.getWeight();
        }
        return weight;
    }

    /**
     * Sets the {@code index} to {@code item}.
     *
     * @param index   The index to set.
     * @param item    The {@link Item} to set on the index.
     * @param refresh The condition if the container must be refreshed.
     */
    public void set(int index, Item item, boolean refresh) {
        Item oldItem = items[index];
        items[index] = item;
        fireItemUpdatedEvent(oldItem, items[index], index, refresh);
    }

    /**
     * Retrieves the item located on {@code index}.
     *
     * @param index the index to get the item on.
     * @return the item on the index, or {@code null} if no item exists on the
     * index.
     */
    public final Optional<Item> retrieve(int index) {
        if (index >= 0 && index < items.length)
            return Optional.ofNullable(items[index]);
        return Optional.empty();
    }

    /**
     * Consumes an action if the {@code index} is a valid item index in this
     * container.
     *
     * @param index the index to get the item on.
     */
    public final void ifPresent(int index, Consumer<Item> action) {
        if (index >= 0 && index < items.length)
            action.accept(items[index]);
    }

    /**
     * Gets the {@link Item} located on {@code index}.
     *
     * @param index The index to get the {@code Item} on.
     * @return The {@code Item} instance, {@code null} if the index is empty.
     */
    public final Item get(int index) {
        if (index >= 0 && index < items.length)
            return items[index];
        return null;
    }

    /**
     * Gets the item id located on {@code index}.
     *
     * @param index The index to get the {@code Item} on.
     * @return The {@code Item} instance, {@code null} if the index is empty.
     */
    public final int getId(int index) {
        if (items[index] == null) {
            return -1;
        }
        return items[index].getId();
    }

    /**
     * Searches and returns the first item found with {@code id}.
     *
     * @param id the identifier to search this container for.
     * @return the item wrapped within an optional, or an empty optional if no
     * item was found.
     */
    public Optional<Item> search(int id) {
        return stream().filter(i -> i != null && id == i.getId()).findFirst();
    }

    /**
     * Searches and returns the first item found with {@code id} and {@code
     * amount}.
     *
     * @param item the item to search this container for.
     * @return the item wrapped within an optional, or an empty optional if no
     * item was found.
     */
    public Optional<Item> search(Item item) {
        return stream().filter(i -> i != null && item.getId() == i.getId() && item.getAmount() == i.getAmount()).findFirst();
    }

    /**
     * Returns {@code true} if {@code index} is occupied (non-{@code null}).
     */
    public final boolean indexOccupied(int index) {
        return retrieve(index).isPresent();
    }

    /**
     * Returns {@code true} if {@code index} is not occupied ({@code null}).
     */
    public final boolean indexFree(int index) {
        return !indexOccupied(index);
    }

    /**
     * Creates a copy of the underlying item container.
     *
     * @return a copy of the unterlying item container.
     */
    public final ItemContainer copy() {
        ItemContainer container = new ItemContainer(this.capacity, this.policy, this.toArray());
        this.listeners.forEach(container::addListener);
        return container;
    }

    /** Removes all of the items from this container. */
    public void clear() {
        clear(true);
    }

    /** Removes all of the items from this container. */
    public final void clear(boolean refresh) {
        Arrays.fill(items, null);
        if (refresh)
            fireBulkItemsUpdatedEvent();
    }

    /** @return {@code true} if this container is empty */
    public final boolean isEmpty() {
        return size() == 0;
    }

    public final boolean isFull() {
        return getFreeSlots() == 0;
    }

    /**
     * Adds an {@link ItemContainerListener} to this container.
     *
     * @param listener The listener to deposit to this container.
     * @return {@code true} if the listener was added, {@code false} otherwise.
     */
    public final boolean addListener(ItemContainerListener listener) {
        return listeners.add(listener);
    }

    /**
     * Removes an {@link ItemContainerListener} from this container.
     *
     * @param listener The listener to withdraw from this container.
     * @return {@code true} if the listener was removed, {@code false}
     * otherwise.
     */
    public final boolean removeListener(ItemContainerListener listener) {
        return listeners.remove(listener);
    }

    /**
     * Fires the {@code ItemContainerListener.itemUpdated(ItemContainer, int)}
     * event.
     */

    public final void fireItemUpdatedEvent(Item oldItem, Item newItem, int index, boolean refresh) {
        fireItemUpdatedEvent(oldItem, newItem, index, refresh, false);
    }

    public final void fireItemUpdatedEvent(Item oldItem, Item newItem, int index, boolean refresh, boolean login) {
        if (firingEvents) {
            listeners.forEach(evt -> evt.itemUpdated(this, Optional.ofNullable(oldItem), Optional.ofNullable(newItem), index, refresh, login));
        }
    }

    /**
     * Fires the {@code ItemContainerListener.bulkItemsUpdated(ItemContainer)}
     * event.
     */
    public final void fireBulkItemsUpdatedEvent() {
        if (firingEvents) {
            listeners.forEach(evt -> evt.bulkItemsUpdated(this));
        }
    }

    /**
     * Fires the {@code ItemContainerListener.capacityExceeded(ItemContainer)}
     * event.
     */
    public final void fireCapacityExceededEvent() {
        if (firingEvents) {
            listeners.forEach(evt -> evt.capacityExceeded(this));
        }
    }

    /** @return The item array in this container. */
    public Item[] getItems() {
        return items;
    }

    /** Sets the value for {@link #firingEvents}. */
    public void setFiringEvents(boolean firingEvents) {
        this.firingEvents = firingEvents;
    }

    /** @return the amount of remaining free indices */
    public final int remaining() {
        return capacity - size();
    }

    /** @return the amount of free slots available in the container */
    public int getFreeSlots() {
        return capacity() - size();
    }

    /** @return the amount of used indices */
    public final int size() {
        return (int) Arrays.stream(items).filter(Objects::nonNull).count();
    }

    /** @return the total amount of used and free indices */
    public final int capacity() {
        return capacity;
    }

    /** @return the policy this container follows */
    public final StackPolicy policy() {
        return policy;
    }

    /** @return this policy checks if the container will allow empty items */
    public boolean allowZero() {
        return false;
    }

    /**
     * Gets a slot by id.
     *
     * @param id
     *            The id.
     * @return The slot, or <code>-1</code> if it could not be found.
     */
    public int getSlotById(int id) {
        for (int i = 0; i < items.length; i++) {
            if (items[i] == null) {
                continue;
            }
            if (items[i].getId() == id) {
                return i;
            }
        }
        return -1;
    }

//    public boolean isFull() {
//        return size() == capacity();
//    }
}