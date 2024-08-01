package com.osroyale.game.world.entity.combat.attack;

import java.util.function.Function;

public class Modifier {
	private Function<Integer, Integer> attackModifier = Function.identity();
	private Function<Integer, Integer> magicModifier = Function.identity();
	private Function<Integer, Integer> rangedModifier = Function.identity();
	private Function<Integer, Integer> aggressiveModifier = Function.identity();
	private Function<Integer, Integer> defensiveModifier = Function.identity();

	private Function<Integer, Integer> maxHitModifier = Function.identity();
	private Function<Integer, Integer> damageModifier = Function.identity();

	/* ************************************************************ */

	protected final Modifier attackPercent(double percentage) {
		attackModifier = link(attackModifier, percentage(percentage));
		return this;
	}

	protected final Modifier ranged(double percentage) {
		rangedModifier = link(rangedModifier, percentage(percentage));
		return this;
	}

	protected final Modifier magic(double percentage) {
		magicModifier = link(magicModifier, percentage(percentage));
		return this;
	}

	protected final Modifier aggressive(double percentage) {
		aggressiveModifier = link(aggressiveModifier, percentage(percentage));
		return this;
	}

	protected final Modifier defensive(double percentage) {
		defensiveModifier = link(defensiveModifier, percentage(percentage));
		return this;
	}

	protected final Modifier maxHit(double percentage) {
		maxHitModifier = link(maxHitModifier, percentage(percentage));
		return this;
	}

	/* ************************************************************ */

	protected final Modifier attack(int level) {
		attackModifier = link(attackModifier, addition(level));
		return this;
	}

	protected final Modifier ranged(int level) {
		rangedModifier = rangedModifier.andThen(addition(level));
		return this;
	}

	protected final Modifier magic(int level) {
		magicModifier = magicModifier.andThen(addition(level));
		return this;
	}

	protected final Modifier aggressive(int level) {
		aggressiveModifier = aggressiveModifier.andThen(addition(level));
		return this;
	}

	protected final Modifier defensive(int level) {
		defensiveModifier = defensiveModifier.andThen(addition(level));
		return this;
	}

	protected final Modifier maxHit(int level) {
		maxHitModifier = maxHitModifier.andThen(addition(level));
		return this;
	}

	/* ************************************************************ */

	public static Modifier link(Modifier first, Modifier second) {
		Modifier modifier = new Modifier();
		modifier.attackModifier = first.attackModifier.andThen(second.attackModifier);
		modifier.magicModifier = first.magicModifier.andThen(second.magicModifier);
		modifier.rangedModifier = first.rangedModifier.andThen(second.rangedModifier);
		modifier.aggressiveModifier = first.aggressiveModifier.andThen(second.aggressiveModifier);
		modifier.defensiveModifier = first.defensiveModifier.andThen(second.defensiveModifier);
		modifier.maxHitModifier = first.maxHitModifier.andThen(second.maxHitModifier);
		modifier.damageModifier = first.damageModifier.andThen(second.damageModifier);
		return modifier;
	}

	public int modifyAttack(int level) {
		return attackModifier.apply(level);
	}

	public int modifyRanged(int level) {
		return rangedModifier.apply(level);
	}

	public int modifyMagic(int level) {
		return magicModifier.apply(level);
	}

	public int modifyAggressive(int level) {
		return aggressiveModifier.apply(level);
	}

	public int modifyDefensive(int level) {
		return defensiveModifier.apply(level);
	}

	public int modifyMaxHit(int max) {
		return maxHitModifier.apply(max);
	}

	public int modifyDamage(int damage) {
		return damageModifier.apply(damage);
	}

	private static Function<Integer, Integer> link(Function<Integer, Integer> modifier, Function<Integer, Integer> next) {
		return modifier.andThen(next);
	}

	private static Function<Integer, Integer> addition(int amount) {
		return modifier -> modifier + amount;
	}

	private static Function<Integer, Integer> percentage(double percent) {
		return modifier -> (int) (modifier * (1 + percent));
	}

	private static Modifier attack(double percent) {
		return new Modifier() {{ attackPercent(percent); }};
	}

	public static void main(String[] args) {
		Modifier godsword = attack(0.25);
		Modifier agsMaxHit = attack(0.10);

		Modifier chain = link(godsword, agsMaxHit);
		Modifier linked = link(attack(0.25), attack(0.10));

		double total;

		total = link(chain, linked).modifyAttack(10000) / 10000.0;
		System.out.println(total);

		total = link(chain, linked).modifyAttack(10000) / 10000.0;
		System.out.println(total);
	}


}
