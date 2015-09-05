/*
Copyright 2010-2013 Michael Shick

This file is part of 'Lock Pattern Generator'.

'Lock Pattern Generator' is free software: you can redistribute it and/or
modify it under the terms of the GNU General Public License as published by the
Free Software Foundation, either version 3 of the License, or (at your option)
any later version.

'Lock Pattern Generator' is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details.

You should have received a copy of the GNU General Public License along with
'Lock Pattern Generator'.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.example.haotian.haotianalp;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PatternGenerator
{
    protected int mGridLength;
    protected int mMinNodes;
    protected int mMaxNodes;
    protected Random mRng;
    protected List<Point> mAllNodes;

    public PatternGenerator()
    {
        mRng = new Random();
        setGridLength(0);
        setMinNodes(0);
        setMaxNodes(0);
    }

    public List<Point> getPattern()
    {
        List<Point> pattern = new ArrayList<Point>();
        for(int passwordIndex = 0; passwordIndex < 4; passwordIndex++ ) {

            //create nodes and add them to list of possibilities
            List<Point> availablePoints = new ArrayList<>();
            for (int i = 0; i < 2; i++){
                for (int ii = 0; i < 2; i++){
                    availablePoints.add(new Point(i, ii));
                }
            }

            //remove the points that are already within the pattern
            availablePoints.removeAll(pattern);

            /* gets a random number within the available list range, then adds that point to
            /the pattern list, then removes that from the available points */
            int newPick = mRng.nextInt(availablePoints.size());
            Log.i("addedPoint", availablePoints.get(newPick).toString());
            pattern.add(availablePoints.get(newPick));
            availablePoints.remove(newPick);

            // the last point
            Point lastPoint = availablePoints.get(availablePoints.size() - 1);
            // use the last point to clean the available Points
            for (int i = 0; i < availablePoints.size(); i++) {
                // 2 away, left or right
                if (Math.abs(lastPoint.x - availablePoints.get(i).x) == 2) {
                    availablePoints.remove(i);
                    i--; // keep index same since this is a list
                }
                // 2 away, up or down
                else if (Math.abs(lastPoint.y - availablePoints.get(i).y) == 2) {
                    availablePoints.remove(i);
                    i--; // keep index same since this is a list
                }
            }
        }

        return pattern;
    }

    //
    // Accessors / Mutators
    //

    public void setGridLength(int length)
    {
        // build the prototype set to copy from later
        List<Point> allNodes = new ArrayList<Point>();
        for(int y = 0; y < length; y++)
        {
            for(int x = 0; x < length; x++)
            {
                allNodes.add(new Point(x,y));
            }
        }
        mAllNodes = allNodes;

        mGridLength = length;
    }
    public int getGridLength()
    {
        return mGridLength;
    }

    public void setMinNodes(int nodes)
    {
        mMinNodes = nodes;
    }
    public int getMinNodes()
    {
        return mMinNodes;
    }

    public void setMaxNodes(int nodes)
    {
        mMaxNodes = nodes;
    }
    public int getMaxNodes()
    {
        return mMaxNodes;
    }

    //
    // Helper methods
    //

    public static int computeGcd(int a, int b)
    /* Implementation taken from
     * http://en.literateprograms.org/Euclidean_algorithm_(Java)
     * Accessed on 12/28/10
     */
    {
        if(b > a)
        {
            int temp = a;
            a = b;
            b = temp;
        }

        while(b != 0)
        {
            int m = a % b;
            a = b;
            b = m;
        }

        return a;
    }
}
