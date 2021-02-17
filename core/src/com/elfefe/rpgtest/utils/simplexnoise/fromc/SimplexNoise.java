package com.elfefe.rpgtest.utils.simplexnoise.fromc;//====================================================================================================
//The Free Edition of C++ to Java Converter limits conversion output to 100 lines per file.

//To purchase the Premium Edition, visit our website:
//https://www.tangiblesoftwaresolutions.com/order/order-cplus-to-java.html
//====================================================================================================

/**
 * @file    SimplexNoise.cpp
 * @brief   A Perlin Simplex Noise C++ Implementation (1D, 2D, 3D).
 *
 * Copyright (c) 2014-2018 Sebastien Rombauts (sebastien.rombauts@gmail.com)
 *
 * This C++ implementation is based on the speed-improved Java version 2012-03-09
 * by Stefan Gustavson (original Java source code in the public domain).
 * http://webstaff.itn.liu.se/~stegu/simplexnoise/SimplexNoise.java:
 * - Based on example code by Stefan Gustavson (stegu@itn.liu.se).
 * - Optimisations by Peter Eastman (peastman@drizzle.stanford.edu).
 * - Better rank ordering method by Stefan Gustavson in 2012.
 *
 * This implementation is "Simplex Noise" as presented by
 * Ken Perlin at a relatively obscure and not often cited course
 * session "Real-Time Shading" at Siggraph 2001 (before real
 * time shading actually took on), under the title "hardware noise".
 * The 3D function is numerically equivalent to his Java reference
 * code available in the PDF course notes, although I re-implemented
 * it from scratch to get more readable code. The 1D, 2D and 4D cases
 * were implemented from scratch by me from Ken Perlin's text.
 *
 * Distributed under the MIT License (MIT) (See accompanying file LICENSE.txt
 * or copy at http://opensource.org/licenses/MIT)
 */

/**
 * @file    SimplexNoise.h
 * @brief   A Perlin Simplex Noise C++ Implementation (1D, 2D, 3D).
 *
 * Copyright (c) 2014-2018 Sebastien Rombauts (sebastien.rombauts@gmail.com)
 *
 * Distributed under the MIT License (MIT) (See accompanying file LICENSE.txt
 * or copy at http://opensource.org/licenses/MIT)
 */


/**
 * @brief A Perlin Simplex Noise C++ Implementation (1D, 2D, 3D, 4D).
 */
public class SimplexNoise
{
	// 1D Perlin simplex noise

	/**
	 * 1D Perlin simplex noise
	 *
	 *  Takes around 74ns on an AMD APU.
	 *
	 * @param[in] x float coordinate
	 *
	 * @return Noise value in the range[-1; 1], value of 0 on all integer coordinates.
	 */
	public static float noise(float x)
	{
		float n0; // Noise contributions from the two "corners"
		float n1;

		// No need to skew the input space in 1D

		// Corners coordinates (nearest integer values):
		int i0 = GlobalMembers.fastfloor(x);
		int i1 = i0 + 1;
		// Distances to corners (between 0 and 1):
		float x0 = x - i0;
		float x1 = x0 - 1.0f;

		// Calculate the contribution from the first corner
		float t0 = 1.0f - x0 * x0;
	//  if(t0 < 0.0f) t0 = 0.0f; // not possible
		t0 *= t0;
		n0 = t0 * t0 * GlobalMembers.grad(GlobalMembers.hash(0), x0);

		// Calculate the contribution from the second corner
		float t1 = 1.0f - x1 * x1;
	//  if(t1 < 0.0f) t1 = 0.0f; // not possible
		t1 *= t1;
		n1 = t1 * t1 * GlobalMembers.grad(GlobalMembers.hash(i1), x1);

		// The maximum value of this noise is 8*(3/4)^4 = 2.53125
		// A factor of 0.395 scales to fit exactly within [-1,1]
		return 0.395f * (n0 + n1);
	}

	// 2D Perlin simplex noise

	/**
	 * 2D Perlin simplex noise
	 *
	 *  Takes around 150ns on an AMD APU.
	 *
	 * @param[in] x float coordinate
	 * @param[in] y float coordinate
	 *
	 * @return Noise value in the range[-1; 1], value of 0 on all integer coordinates.
	 */
	public static float noise(float x, float y)
	{
		float n0; // Noise contributions from the three corners
		float n1;
		float n2;

		// Skewing/Unskewing factors for 2D
		final float F2 = 0.366025403f; // F2 = (sqrt(3) - 1) / 2
		final float G2 = 0.211324865f; // G2 = (3 - sqrt(3)) / 6   = F2 / (1 + 2 * K)

		// Skew the input space to determine which simplex cell we're in
		final float s = (x + y) * F2; // Hairy factor for 2D
		final float xs = x + s;
		final float ys = y + s;
		final int i = GlobalMembers.fastfloor(xs);
		final int j = GlobalMembers.fastfloor(ys);

		// Unskew the cell origin back to (x,y) space
		final float t = (float)(i + j) * G2;
		final float X0 = i - t;
		final float Y0 = j - t;
		final float x0 = x - X0; // The x,y distances from the cell origin
		final float y0 = y - Y0;

		// For the 2D case, the simplex shape is an equilateral triangle.
		// Determine which simplex we are in.
		int i1 = 0; // Offsets for second (middle) corner of simplex in (i,j) coords
		int j1 = 0;
		if (x0 > y0)
		{ // lower triangle, XY order: (0,0)->(1,0)->(1,1)
			i1 = 1;
			j1 = 0;
		}
		else
		{ // upper triangle, YX order: (0,0)->(0,1)->(1,1)
			i1 = 0;
			j1 = 1;
		}

		// A step of (1,0) in (i,j) means a step of (1-c,-c) in (x,y), and
		// a step of (0,1) in (i,j) means a step of (-c,1-c) in (x,y), where
		// c = (3-sqrt(3))/6

		final float x1 = x0 - i1 + G2; // Offsets for middle corner in (x,y) unskewed coords
		final float y1 = y0 - j1 + G2;
		final float x2 = x0 - 1.0f + 2.0f * G2; // Offsets for last corner in (x,y) unskewed coords
		final float y2 = y0 - 1.0f + 2.0f * G2;

		// Work out the hashed gradient indices of the three simplex corners
		final int gi0 = GlobalMembers.hash(i + GlobalMembers.hash(j));
		final int gi1 = GlobalMembers.hash(i + i1 + GlobalMembers.hash(j + j1));
		final int gi2 = GlobalMembers.hash(i + 1 + GlobalMembers.hash(j + 1));

		// Calculate the contribution from the first corner
		float t0 = 0.5f - x0 * x0 - y0 * y0;
		if (t0 < 0.0f)
		{
			n0 = 0.0f;
		}
		else
		{
			t0 *= t0;
			n0 = t0 * t0 * GlobalMembers.grad(gi0, x0, y0);
		}

		// Calculate the contribution from the second corner
		float t1 = 0.5f - x1 * x1 - y1 * y1;
		if (t1 < 0.0f)
		{
			n1 = 0.0f;
		}
		else
		{
			t1 *= t1;
			n1 = t1 * t1 * GlobalMembers.grad(gi1, x1, y1);
		}

		// Calculate the contribution from the third corner
		float t2 = 0.5f - x2 * x2 - y2 * y2;
		if (t2 < 0.0f)
		{
			n2 = 0.0f;
		}
		else
		{
			t2 *= t2;
			n2 = t2 * t2 * GlobalMembers.grad(gi2, x2, y2);
		}

		// Add contributions from each corner to get the final noise value.
		// The result is scaled to return values in the interval [-1,1].
		return 45.23065f * (n0 + n1 + n2);
	}
}