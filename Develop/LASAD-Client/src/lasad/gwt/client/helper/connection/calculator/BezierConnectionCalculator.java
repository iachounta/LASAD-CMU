/*
 * Copyright 2007 Michał Baliński
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package lasad.gwt.client.helper.connection.calculator;

import java.util.List;

import lasad.gwt.client.helper.connection.data.BezierConnectionData;
import lasad.gwt.client.helper.connection.data.ConnectionData;
import lasad.gwt.client.helper.connection.data.Point;
import lasad.gwt.client.helper.connector.Connector;
import lasad.gwt.client.helper.connector.Direction;

/**
 * Calculates ConnectionData for bezier connections.
 * 
 * @author Michał Baliński (michal.balinski@gmail.com)
 */
public class BezierConnectionCalculator implements ConnectionDataCalculator {

	/**
	 * @see lasad.gwt.client.helper.connection.calculator.ConnectionDataCalculator#calculateConnectionData(java.util.List)
	 */
	public ConnectionData calculateConnectionData(List<Connector> connectors) {
		if (connectors.size() != 2) {
			throw new IllegalArgumentException("Unsupported connectors count");
		}

		Connector c1 = (Connector) connectors.get(0);
		Connector c2 = (Connector) connectors.get(1);

		Direction[] d = Direction.computeDirections(c1, c2);

		Point p1 = c1.pointOnBorder(d[0]);
		Point p2 = c2.pointOnBorder(d[1]);

		Point b1 = p1.move(d[0], 50); // TODO Use generic parameters
		Point b2 = p2.move(d[1], 50); // TODO Use generic parameters

		// Point b1 = p1.move(d[0], 3*(d[0].isHorizontal() ? c1.getHeight() :
		// c1.getWidth()));
		// Point b2 = p2.move(d[1], 3*(d[1].isHorizontal() ? c2.getHeight() :
		// c2.getWidth()));

		BezierConnectionData data = new BezierConnectionData(new Point[] { p1, p2 }, new Point[] { b1, b2 });

		return data;
	}
}