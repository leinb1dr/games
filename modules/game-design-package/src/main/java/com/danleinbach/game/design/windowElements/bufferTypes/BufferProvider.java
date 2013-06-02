package com.danleinbach.game.design.windowElements.bufferTypes;

import java.awt.*;

public interface BufferProvider {

	Graphics2D getGraphics();

	void draw();

	void resize(int width, int height);
}
