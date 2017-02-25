package com.ylinor.client.physics;

import com.artemis.Component;

public class Size extends Component {
	public float width = 0.6f, height = 1.8f;
	
	public void setSize(float w, float h) {
		width = w;
		height = h;
	}
}
