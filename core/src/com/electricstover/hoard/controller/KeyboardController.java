package com.electricstover.hoard.controller;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

public class KeyboardController implements InputProcessor {
	public boolean left,right,up,down;
	public boolean isMouse1Down, isMouse2Down,isMouse3Down;
	public boolean isDragged;
	public Vector2 mouseLocation;

	@Override
	public boolean keyDown(int keycode) {
		boolean keyProcessed = false;
		switch (keycode) // switch code base on the variable keycode
		{
		case Keys.LEFT:  	// if keycode is the same as Keys.LEFT a.k.a 21
		case Keys.A:		// or if it is "A"
			left = true;	// do this
			keyProcessed = true;// we have reacted to a keypress 
			break;
		case Keys.RIGHT: 	// if keycode is the same as Keys.LEFT a.k.a 22
		case Keys.D:
			right = true;	// do this
			keyProcessed = true;// we have reacted to a keypress 
			break;
		case Keys.UP: 		// if keycode is the same as Keys.LEFT a.k.a 19
		case Keys.W:
			up = true;		// do this
			keyProcessed = true;// we have reacted to a keypress 
			break;
		case Keys.DOWN: 	// if keycode is the same as Keys.LEFT a.k.a 20
		case Keys.S:
			down = true;	// do this
			keyProcessed = true;// we have reacted to a keypress
		}
		return keyProcessed;	//  return our peyProcessed flag
	}

	@Override
	public boolean keyUp(int keycode) {
		boolean keyProcessed = false;
		switch (keycode) // switch code base on the variable keycode
		{
		case Keys.LEFT:  	// if keycode is the same as Keys.LEFT a.k.a 21
		case Keys.A:		// or if it is "A"
			left = false;	// do this
			keyProcessed = true;	// we have reacted to a keypress 
			break;
		case Keys.RIGHT: 	// if keycode is the same as Keys.LEFT a.k.a 22
		case Keys.D:
			right = false;	// do this
			keyProcessed = true;	// we have reacted to a keypress 
			break;
		case Keys.UP: 		// if keycode is the same as Keys.LEFT a.k.a 19
		case Keys.W:
			up = false;		// do this
			keyProcessed = true;	// we have reacted to a keypress 
			break;
		case Keys.DOWN: 	// if keycode is the same as Keys.LEFT a.k.a 20
		case Keys.S:
			down = false;	// do this
			keyProcessed = true;	// we have reacted to a keypress
		}
		return keyProcessed;	//  return our peyProcessed flag
	}
	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if(button == 0){
			isMouse1Down = true;
		}else if(button == 1){
			isMouse2Down = true;
		}else if(button == 2){
			isMouse3Down = true;
		}
		mouseLocation = new Vector2(screenX, screenY);
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		isDragged = false;
		//System.out.println(button);
		if(button == 0){
			isMouse1Down = false;
		}else if(button == 1){
			isMouse2Down = false;
		}else if(button == 2){
			isMouse3Down = false;
		}
		mouseLocation = new Vector2(screenX, screenY);
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		isDragged = true;
		mouseLocation = new Vector2(screenX, screenY);
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		mouseLocation = new Vector2(screenX, screenY);
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}
