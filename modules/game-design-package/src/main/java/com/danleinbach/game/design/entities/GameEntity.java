package com.danleinbach.game.design.entities;

import java.awt.*;
import java.util.Observable;

/**
 * Game Entity is an object representing the players and elements in a game.
 * The object handels its own updateing and drawing. It also extends observable
 * so it can notify its observers of any changes in its state.
 *
 * @param <E> - This is the return type of its update methods, sometimes an entity
 *            doesn't need to return anything, other times it does.
 * @author Daniel
 */
public abstract class GameEntity<E> extends Observable {

	/**
	 * This is the width of the area that the entity can be drawn in
	 */
	protected int drawWidth;
	/**
	 * This is the height of the area that the entity can be drawn in
	 */
	protected int drawHeight;

	/**
	 * Construct a new game entity with two parameters. This
	 * is so the entitiy knows its bounds.
	 *
	 * @param pWidth  - Width of the game area
	 * @param pHeight - Height of the game area
	 */
	public GameEntity(int pWidth, int pHeight) {
		super();
		this.drawWidth = pWidth;
		this.drawHeight = pHeight;
	}

	/**
	 * Standard update for the entity. This should be called to perform routine
	 * updates to the object.
	 *
	 * @return A value appropriate to the entity
	 */
	public abstract E update();

	/**
	 * Draw the entity.<br/>
	 * With the given graphics object draw itself and add it to the
	 * rest of the elements.
	 *
	 * @param g - Graphics object that holds all objects to be drawn.
	 */
	public abstract void draw(Graphics2D g);

	/**
	 * Update the entity at a given location. This can be interpreted many ways,
	 * so it is left up to the implementor.
	 *
	 * @param x - the x location on the game panel
	 * @param y - the y location on the game panel
	 * @return A value appropriate to the entity
	 */
	public abstract E update(int x, int y);

}